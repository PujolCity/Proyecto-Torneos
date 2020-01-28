package com.VeizagaTorrico.proyectotorneos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ManagerSharedPreferences {

//    private static final String FILE_TOKEN = "TokenFirebase";
//    private static final String KEY_TOKEN = "token";
    private static final ManagerSharedPreferences MY_MANAGER_SHARED_PREFERENCES = new ManagerSharedPreferences();

    private ManagerSharedPreferences() {}

    public static ManagerSharedPreferences getInstance() {
        return MY_MANAGER_SHARED_PREFERENCES;
    }



//    public String getTokenInternal(Context context){
//        // abrimos el archivo o lo creamos si no lo esta
//        SharedPreferences sharedPref = context.getSharedPreferences(FILE_TOKEN, Context.MODE_PRIVATE);
//
//        String token = sharedPref.getString(KEY_TOKEN, null);
//
//        return token;
//    }

//    public void setTokenInternal(Context context, String newToken){
//
//        // abrimos el archivo o lo creamos si no lo esta
//        SharedPreferences sharedPref = context.getSharedPreferences(FILE_TOKEN, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//
//        editor.putString(KEY_TOKEN, newToken);
//        editor.commit();
//    }

    public String getDataFromSharedPreferences(Context context, String nameFileShared, String keyData){
        // abrimos el archivo o lo creamos si no lo esta
        SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);

        String dataSaved = sharedPref.getString(keyData, null);

        return dataSaved;
    }

    public void setDataFromSharedPreferences(Context context, String nameFileShared, String keyData, String newData){
        // abrimos el archivo o lo creamos si no lo esta
        SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(keyData, newData);
        editor.commit();
    }

}
