package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    Button btnRegister, btnSingin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        updateUi();
        listenBotonSingin();
        listenBotonRegister();
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        btnRegister = findViewById(R.id.btn_register_home);
        btnSingin = findViewById(R.id.btn_singin_home);
    }

    private void listenBotonSingin(){
        btnSingin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO: cambiar a singin
                passToSingin();
            }
        });
    }

    private void listenBotonRegister(){
        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                passToRegister();
            }
        });
    }

    private void passToSingin(){
        Intent toInitApp = new Intent(this, SinginActivity.class);
        startActivity(toInitApp);
    }

    private void passToRegister(){
        Intent toRegisterUser= new Intent(this, UserRegisterActivity.class);
        startActivity(toRegisterUser);
    }
}
