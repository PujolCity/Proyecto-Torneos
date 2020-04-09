package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_SESSION;

public class HomeActivity extends AppCompatActivity {

    Button btnRegister, btnSingin;
    String typeView;
    String idCompetenciaSolicitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(sesionIniciada()){
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
        typeView = null;
        idCompetenciaSolicitudes = null;
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
        Intent toSignIn = new Intent(this, SinginActivity.class);
        toSignIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toSignIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toSignIn);
    }

    private void passToSinginWithData(String typeView, String idCompetencia){
        Intent toSignIn = new Intent(this, SinginActivity.class);
        if(idCompetencia != null){
            toSignIn.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetencia);
        }
        toSignIn.putExtra(Constants.EXTRA_KEY_VIEW, typeView);
        toSignIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toSignIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toSignIn);
    }

    private void passToRegister(){
        Intent toRegisterUser= new Intent(this, UserRegisterActivity.class);
        toRegisterUser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toRegisterUser);
    }

    private boolean sesionIniciada() {
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
        //si no existe una sesion iniciada pasamos a iniciar sesion
        String typeView = dataNotification.getString(Constants.EXTRA_KEY_VIEW);

        if(sesionIniciada()){
            if(typeView.equals(Constants.NOTIF_VIEW_SOLICITUD)){
                String idCompetencia = dataNotification.getString(Constants.EXTRA_KEY_ID_COMPETENCIA);
                // vamos al activity creado
                Intent toMisSolicitudes = new Intent(this, MisSolicitudesActivity.class);
                toMisSolicitudes.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetencia);
                toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMisSolicitudes);
            }
            if(typeView.equals(Constants.NOTIF_VIEW_INVITACION)){
                // vamos al activity creado
                Intent toMisInvitaciones = new Intent(this, MisInvitacionesActivity.class);
                toMisInvitaciones.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                toMisInvitaciones.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMisInvitaciones);
            }
//            Log.d("OPEN_NOTIF", "Sesion ya iniciada");
        }
        else{
//            Log.d("OPEN_NOTIF", "Sesion NO iniciada");
            if(typeView.equals(Constants.NOTIF_VIEW_SOLICITUD)){
                passToSinginWithData(typeView, dataNotification.getString(Constants.EXTRA_KEY_ID_COMPETENCIA));
            }
            if(typeView.equals(Constants.NOTIF_VIEW_INVITACION)){
                passToSinginWithData(typeView, null);
            }
        }
    }

    private void passToInitApp() {
        Intent toInitApp = new Intent(this, NavigationMainActivity.class);
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toInitApp);
        finish();
    }

    @Override
    public void onBackPressed() {
        passToInitApp();
    }
}
