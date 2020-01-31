package com.VeizagaTorrico.proyectotorneos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ManagerSharedPreferences {

    private static final ManagerSharedPreferences MY_MANAGER_SHARED_PREFERENCES = new ManagerSharedPreferences();

    private ManagerSharedPreferences() {}

    public static ManagerSharedPreferences getInstance() {
        return MY_MANAGER_SHARED_PREFERENCES;
    }

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
