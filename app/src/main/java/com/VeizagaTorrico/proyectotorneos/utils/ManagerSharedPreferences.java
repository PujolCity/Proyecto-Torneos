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

    public void setSessionFromSharedPreferences(Context context, String nameFileShared, String keyData, boolean newData){
        SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean(keyData,newData).apply();
    }
    public boolean getSessionFromSharedPreferences(Context context, String nameFileShared, String keyData){
        SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);
        return  sharedPref.getBoolean(keyData,false);
    }

   public int getCountConfrontation(Context context, String nameFileShared, String keyData) {
       // abrimos el archivo o lo creamos si no lo esta
       SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);
       int dataSaved = sharedPref.getInt(keyData,0);

       return dataSaved;
   }

    public void setCountConfrontation(Context context, String nameFileShared, String keyData, int newData){
        // abrimos el archivo o lo creamos si no lo esta
        SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(keyData, newData);
        editor.commit();
    }

    public String getConfrontationFromSharedPreferences(Context context, String nameFileShared, String keyData){
        // abrimos el archivo o lo creamos si no lo esta
        SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);

        String dataSaved = sharedPref.getString(keyData, null);

        return dataSaved;
    }

    public void setConfrontationFromSharedPreferences(Context context, String nameFileShared, String keyData, String newData){
        // abrimos el archivo o lo creamos si no lo esta
        SharedPreferences sharedPref = context.getSharedPreferences(nameFileShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(keyData, newData);
        editor.commit();
    }

}
