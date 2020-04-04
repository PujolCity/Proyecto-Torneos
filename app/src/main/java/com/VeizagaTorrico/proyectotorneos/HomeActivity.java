package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_SESSION;

public class HomeActivity extends AppCompatActivity {

    Button btnRegister, btnSingin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(obtenerEstadoButton()){
            passToSingin();
        }
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
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toInitApp);
    }

    private void passToRegister(){
        Intent toRegisterUser= new Intent(this, UserRegisterActivity.class);
        toRegisterUser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toRegisterUser);
    }

    private boolean obtenerEstadoButton() {
        return ManagerSharedPreferences.getInstance().getSessionFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_SESSION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            try {
              showView(bundle);
                //aquí va tu código en el cual validas el tipo de dato
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // mostramos una pantalla distinta dependiendo de los datos recibidos
    private void showView(Bundle dataNotification){
//        Log.d("NOTIF_FORGROUND", "IdCompetencia: "+dataNotification.getString(Constants.EXTRA_KEY_ID_COMPETENCIA));
//        Log.d("NOTIF_FORGROUND", "Tipo de pantalla: "+dataNotification.getString(Constants.EXTRA_KEY_VIEW));
        String typeView = dataNotification.getString(Constants.EXTRA_KEY_VIEW);

        if(typeView.equals(Constants.EXTRA_NOTIF_VIEW_SOLICITUD)){
            String idCompetencia = dataNotification.getString(Constants.EXTRA_KEY_ID_COMPETENCIA);
            // vamos al activity creado
            Intent toMisSolicitudes = new Intent(this, MisSolicitudesActivity.class);
            toMisSolicitudes.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetencia);
            toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toMisSolicitudes);
        }
    }
}
