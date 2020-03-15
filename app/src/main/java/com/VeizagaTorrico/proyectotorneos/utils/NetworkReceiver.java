package com.VeizagaTorrico.proyectotorneos.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (checkNetworkConnection(context)){
            Log.d("MONITOR_NETWORK", "Conexion activa");
            // vamos a actualizar los encuentros
            ManagerUpdateConfrontation.getInstance().updateConfrontations(context);
        } else {
            Log.d("MONITOR_NETWORK", "Conexion perdida");
        }
    }

    private boolean checkNetworkConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo!= null && networkInfo.isConnected());
    }

    public static boolean existConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo!= null && networkInfo.isConnected());
    }
}
