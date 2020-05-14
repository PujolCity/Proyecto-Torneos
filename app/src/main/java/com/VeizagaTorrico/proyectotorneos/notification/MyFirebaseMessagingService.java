package com.VeizagaTorrico.proyectotorneos.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.HomeActivity;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.Map;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_TOKEN_FIREBASE;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_TOKEN;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("MSGNotifi", "Llega la notif: ");
        //super.onMessageReceived(remoteMessage);
//        Log.d("MSG_NOTIF", "Datos recibidos: "+remoteMessage.getData().toString());

        if (remoteMessage.getNotification() != null) {
            Log.d("MSG_NOTIF", "Notificacion: " +remoteMessage.getNotification().getBody());
        }

        String idCompetencia = null;
        String typeView = null;
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> dataNotification = remoteMessage.getData();
            idCompetencia = dataNotification.get(Constants.EXTRA_KEY_ID_COMPETENCIA);
            typeView = dataNotification.get(Constants.EXTRA_KEY_VIEW);
//            Log.d("MSG_NOTIF", "IdCompetencia de la notif: " + idCompetencia);
//            Log.d("MSG_NOTIF", "Pantalla de la notif: " + typeView);
        }

        // mostramos la notificacion en el dispositivo
        showNotificationPush(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), idCompetencia, typeView);
    }

    private void squeduleJob() {
    }

    private void handleNow() {
    }

    private void showNotificationPush(String title, String body, String idCompetencia, String typeView) {
        // el activity que vamos a abrir al recibir la notificacion
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Constants.EXTRA_KEY_VIEW, typeView);

        if(typeView != null){
            if(typeView.equals(Constants.NOTIF_VIEW_SOLICITUD)){
                i.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetencia);
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, this.getResources().getString(R.string.notification_id_channel))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setTicker("Tutorial test")
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Proyecto Torneos")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent);

        NotificationManager managerNotif = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        managerNotif.notify(1, builder.build());
    }


    @Override
    public void onNewToken(@NonNull String newToken) {
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN, newToken);
//        Log.d("MANAGER_SHARED_PREF", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN));
    }

}
