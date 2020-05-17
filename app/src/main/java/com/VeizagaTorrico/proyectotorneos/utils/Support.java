package com.VeizagaTorrico.proyectotorneos.utils;

import android.util.Log;

public class Support {

    // recibe un nro como string y devuelve la fase correspondiente
    public static String spinnerGetFaseElim(String f){
        String fase = null;

        if(f.equals("0")){
            fase = "Grupos";
        }
        if(f.equals("1")){
            fase = "Final";
        }
        if(f.equals("2")){
            fase = "Semi";
        }
        if(f.equals("3")){
            fase = "4º final";
        }
        if(f.equals("4")){
            fase = "8º final";
        }
        if(f.equals("5")){
            fase = "16º final";
        }
        if(f.equals("6")){
            fase = "32º final";
        }
        return fase;
    }

    // recibe un string q represnta una fase y devuelve su nro
    public static String spinnerGetNroFaseElim(String f){
//        Log.d("SPIN_SEL_FASE", "Opcion elegida: "+f);
        String fase = null;
        if(f.equals("Grupos")){
            fase = "0";
        }
        if((f.equals("Final")) || f.equals("Ida")){
            fase = "1";
        }
        if((f.equals("Semi")) || (f.equals("Vuelta"))){
            fase = "2";
        }
        if(f.equals("4º final")){
            fase = "3";
        }
        if(f.equals("8º final")){
            fase = "4";
        }
        if(f.equals("16º final")){
            fase = "5";
        }
        if(f.equals("32º final")){
            fase = "6";
        }
//        Log.d("SPIN_SEL_FASE", "Opcion elegida nro: "+fase);

        return fase;
    }
}
