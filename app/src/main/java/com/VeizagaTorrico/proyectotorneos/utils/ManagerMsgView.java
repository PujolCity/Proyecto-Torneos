package com.VeizagaTorrico.proyectotorneos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.R;

// clase estatica que facilita la administracionm de componentes visuales
public class ManagerMsgView {

    // devuelve un componente con el mje recibido
    public static ProgressDialog getMsgLoading(Context context, String msg){
        ProgressDialog msgLoading = new ProgressDialog(context);
        //Log.d("MSG_LOADING", "Entro a crear mje loading");
        msgLoading.setIcon(R.mipmap.ic_launcher);
        msgLoading.setMessage(msg);

        return msgLoading;
    }
}
