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

import com.VeizagaTorrico.proyectotorneos.HomeActivity;
import com.VeizagaTorrico.proyectotorneos.NavigationMainActivity;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
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
        // TODO: poner afuera el showNotif
        if (remoteMessage.getNotification() != null) {
            Log.d("MSGNotifi", "Notificacion: :" +remoteMessage.getNotification().getBody());
            //showNotificationPush(remoteMessage.getData().get("message"));
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d("MSGNotifi", "Los datos: " + remoteMessage.getData());

            showNotificationPush(remoteMessage.getData().get("message"));
            //remoteMessage.getNotification();
//            showNotificationPush(remoteMessage.getNotification().getTitle());

            if (true) {
                squeduleJob();
            } else {
                handleNow();
            }

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "body de la notificacion:" + remoteMessage.getNotification());
            }
        }
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

    private void showNotificationPush(String message) {
        Intent i = new Intent(this, NavigationMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // sacamos los datos recibidos
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        Log.d("MSGNotifi", "Json notif:" +jsonObject.get("notification"));
        String jsonNotification = jsonObject.get("notification").toString();

        //Log.d(TAG, "Json not string 1:" +jsonNotification);

        // buscamos los datos de la notificacion
        JsonObject jsonNotificationObject = new JsonParser().parse(jsonNotification).getAsJsonObject();
        String titulo = jsonNotificationObject.get("title").toString();
        titulo = titulo.replace("\"","");
        String body= jsonNotificationObject.get("body").toString();
        body = body.replace("\"","");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, this.getResources().getString(R.string.notification_id_channel))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Tutorial test")
//                .setContentTitle("FCM notif test")
                .setContentTitle(titulo)
//                .setContentText(message)
                .setContentText(body)
                .setContentInfo("Information")
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
