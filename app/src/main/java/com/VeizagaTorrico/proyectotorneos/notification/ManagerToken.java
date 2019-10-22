package com.VeizagaTorrico.proyectotorneos.notification;

import android.content.Context;
import android.content.SharedPreferences;

public class ManagerToken {

    private static final String FILE_TOKEN = "TokenFirebase";
    private static final String KEY_TOKEN = "token";
    private static final ManagerToken MY_MANAGER_TOKEN = new ManagerToken();

    private ManagerToken() {}

    public static ManagerToken getInstance() {
        return MY_MANAGER_TOKEN;
    }


    public String getTokenInternal(Context context){

        // abrimos el archivo o lo creamos si no lo esta
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_TOKEN, Context.MODE_PRIVATE);

        String token = sharedPref.getString(KEY_TOKEN, null);

        return token;
    }

    public void setTokenInternal(Context context, String newToken){

        // abrimos el archivo o lo creamos si no lo esta
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_TOKEN, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(KEY_TOKEN, newToken);
        editor.commit();
    }

}
