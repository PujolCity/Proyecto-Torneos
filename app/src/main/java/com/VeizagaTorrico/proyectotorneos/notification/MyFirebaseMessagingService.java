package com.VeizagaTorrico.proyectotorneos.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.HomeActivity;
import com.VeizagaTorrico.proyectotorneos.MisSolicitudesActivity;
import com.VeizagaTorrico.proyectotorneos.NavigationMainActivity;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.ResetPassActivity;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.SinginActivity;
import com.VeizagaTorrico.proyectotorneos.UserRegisterActivity;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CompetidoresListFragment;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.NotificationSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_TOKEN_FIREBASE;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_TOKEN;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;

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
        showNotificationPush(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), idCompetencia, typeView);

//        if (remoteMessage.getData().size() > 0) {
//            Log.d("MSG_NOTIF", "Los datos: " + remoteMessage.getData());
//
//            showNotificationPush(remoteMessage.getData().get("message"));
//
//            if (true) {
//                squeduleJob();
//            } else {
//                handleNow();
//            }
//
//            if (remoteMessage.getNotification() != null) {
//                Log.d("MSG_NOTIF", "body de la notificacion:" + remoteMessage.getNotification());
//            }
//        }
    }

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        String channelId = "Default";
//        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(channel);
//        }
//        manager.notify(1, builder.build());
//    }

    private void squeduleJob() {
    }

    private void handleNow() {
    }

    private void showNotificationPush(String title, String body, String idCompetencia, String typeView) {
        // el activity que vamos a abrir al recibir la notificacion
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetencia);
        i.putExtra(Constants.EXTRA_KEY_VIEW, typeView);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, this.getResources().getString(R.string.notification_id_channel))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Tutorial test")
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Inscripcion")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent);

        NotificationManager managerNotif = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        managerNotif.notify(1, builder.build());
    }


    @Override
    public void onNewToken(@NonNull String newToken) {
//        Log.d("MANAGER_SHARED newToken", newToken);
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN, newToken);
//        Log.d("MANAGER_SHARED_PREF", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN));
    }

}
