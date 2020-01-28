package com.VeizagaTorrico.proyectotorneos.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

public class Validations {

    public static boolean isEmail(EditText field){
        String email = field.getText().toString().trim();
        Pattern pattern = Patterns.EMAIL_ADDRESS;
//        return pattern.matcher(email).matches();

        if(pattern.matcher(email).matches()){
            return true;
        }
        else{
            field.setError("Email invalido");
            field.requestFocus();
            return false;
        }
    }

    //metodo para validar si editext esta vacio
    public static boolean isEmpty(EditText field){
        String dato = field.getText().toString().trim();
        if(TextUtils.isEmpty(dato)){
            field.setError("Campo Requerido");
            field.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }


    public static boolean isNombre(EditText field){
        String dato = field.getText().toString().trim();
        if(!dato.matches("^[A-Za-z]+$")){
            field.setError("Dato incorrecto");
            field.requestFocus();
            return false;
        }
        return true;
    }

}
