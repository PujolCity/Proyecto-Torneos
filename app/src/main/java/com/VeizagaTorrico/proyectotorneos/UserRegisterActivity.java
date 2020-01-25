package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRegisterActivity extends AppCompatActivity {

    Button btn_register;
    EditText edt_nombre, edt_apellido, edt_nombreUsuario, edt_correo, edt_pass, edt_confPass;
    String nombre, apellido, usuario, correo, pass, confPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        updateUi();

        listenReggister();

//        btn_register.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                if(formCorrect()){
//                    getValuesFields();
//                    registerUser();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Formulario INCORRECTO!!", Toast.LENGTH_SHORT).show();
//                }
////                getValuesFields();
////                registerUser();
//            }
//        });
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        edt_nombre = findViewById(R.id.edtNombre);
        edt_apellido = findViewById(R.id.edtApellido);
        edt_nombreUsuario = findViewById(R.id.edtNombreUsuario);
        edt_correo = findViewById(R.id.edtCorreo);
        edt_pass = findViewById(R.id.edtPassword);
        edt_confPass = findViewById(R.id.edtConfPassword);

        btn_register = findViewById(R.id.btnRegistrar);
    }

    private void listenReggister(){
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(formCorrect()){
                    getValuesFields();
                    if(passCorrect()){
                        registerUser();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No se pudo confirmar la contraseña. Vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Formulario INCORRECTO!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean formCorrect(){
        if(!Validations.isNombre(edt_nombre)){
//            Log.d("VALIDACIONES",  "Nombre incorrecto");
            return false;
        }
        if(!Validations.isNombre(edt_apellido)){
//            Log.d("VALIDACIONES",  "Apellido incorrecto");
            return false;
        }
        if(Validations.isEmpty(edt_nombreUsuario)){
//            Log.d("VALIDACIONES",  "Nombre de usuario vacio");
            return false;
        }
        if(Validations.isEmpty(edt_correo)){
//            Log.d("VALIDACIONES",  "Correo vacio");
            return false;
        }
        if(Validations.isEmpty(edt_pass)){
//            Log.d("VALIDACIONES",  "Pass vacio");
            return false;
        }
        if(Validations.isEmpty(edt_confPass)){
//            Log.d("VALIDACIONES",  "Conf pass vacio");
            return false;
        }

        if (!Validations.isEmail(edt_correo)){
//            Log.d("VALIDACIONES",  "Email incorrecto");
            return false;
        }

        return true;
    }

    private void getValuesFields() {
        nombre = edt_nombre.getText().toString();
        apellido = edt_apellido.getText().toString();
        usuario = edt_nombreUsuario.getText().toString();
        correo = edt_correo.getText().toString();
        pass = edt_pass.getText().toString();
        confPass = edt_confPass.getText().toString();

        //Toast.makeText(getApplicationContext(), nombre+" - "+apellido+" - "+usuario+" - "+correo+" - "+pass+" - "+confPass, Toast.LENGTH_SHORT).show();
    }

    private boolean passCorrect() {
        if(pass.equals(confPass)){
            return true;
        }
        return false;
    }

    private void registerUser(){

        // probando restrofit
//        Retrofit frameRestrofit = new Retrofit.Builder()
////                .baseUrl("http://fipmdeps.ddns.net:20203/api/")
//                .baseUrl("http://192.168.1.53:8000/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        Apirest myApiRest = frameRestrofit.create(Apirest.class);
//
//        Usuario nuevoUsuario = new Usuario();
//        nuevoUsuario.setNombre(nombre);
//        nuevoUsuario.setApellido(apellido);
//        nuevoUsuario.setUsuario(usuario);
//        nuevoUsuario.setCorreo(correo);
//        nuevoUsuario.setPass(pass);
//
////        Call<String> call = myApiRest.newUser(nombre, apellido, usuario, correo, pass);
//        Call<RespCreateUser> call = myApiRest.newUser(nuevoUsuario);
//
//        Log.d("RESP_CREATE_ERROR", "url request: " + call.request().url().toString());
//        Log.d("RESP_CREATE_ERROR", "body request: " + call.request().body().toString());
//        Log.d("RESP_CREATE_ERROR", "body data: " + nuevoUsuario.toString());
//
//        call.enqueue(new Callback<RespCreateUser>() {
//            @Override
//            public void onResponse(Call<RespCreateUser> call, Response<RespCreateUser> response) {
//                if(!response.isSuccessful()){
//                    Log.d("RESP_CREATE_ERROR", "Cod estado: "+response.code());
//                    return;
//                }
//                Log.d("RESP_CREATE_SUCCESS", "EXITO: "+response.body());
//            }
//
//            @Override
//            public void onFailure(Call<RespCreateUser> call, Throwable t) {
//                String TAG =  "RESP_CREATE_ERROR";
//                Log.d(TAG, "error: "+t.getMessage());
//            }
//        });

    }
}
