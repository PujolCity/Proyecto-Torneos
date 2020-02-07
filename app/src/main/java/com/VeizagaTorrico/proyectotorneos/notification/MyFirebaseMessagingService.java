package com.VeizagaTorrico.proyectotorneos.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

//    private Map<String,String> bodyRequest;
//    private NotificationSrv notificationSrv;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            Log.d("Probando FIREBASE", remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "aca van los datos: " + remoteMessage.getData());

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

    private void squeduleJob() {
    }

    private void handleNow() {
    }

    private void showNotificationPush(String message) {
        Intent i = new Intent(this, NavigationMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // sacamos los datos recibidos
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        //Log.d(TAG, "Json convertido:" +jsonObject.get("notification"));
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
//        notificationSrv = new RetrofitAdapter().connectionEnable().create(NotificationSrv.class);
//        Log.d("MANAGER_SHARED newToken", newToken);
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN, newToken);
//        Log.d("MANAGER_SHARED_PREF", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN));
        // creamos el body de la peticion y agregamos el token

        // al enviar los token con distintos usuarios controlar ver como pedir un nuevo token, para
        // que los distintos usuarios no tengan un mismo token

        // esto solo lo hacemos al inicar sesion
//        bodyRequest = new HashMap<>();
//        bodyRequest.put("nombreUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_USERNAME));
//        bodyRequest.put("token", newToken);
//        Call<MsgRequest> call = notificationSrv.reportChangeTokenToServer(bodyRequest);
//        call.enqueue(new Callback<MsgRequest>() {
//            @Override
//            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
//                if(response.code() == 200){
//                    Log.d("response code", Integer.toString(response.code()));
//                    Toast toast = Toast.makeText(getApplicationContext(), "Token actualizado en DB NEW: "+response.toString(), Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//            @Override
//            public void onFailure(Call<MsgRequest> call, Throwable t) {
//                Toast toast = Toast.makeText(getApplicationContext(), "Fallo la peticion", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
    }

}
