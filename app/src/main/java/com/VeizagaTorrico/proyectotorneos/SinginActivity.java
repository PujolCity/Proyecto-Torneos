package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.RespSrvUser;
import com.VeizagaTorrico.proyectotorneos.services.NotificationSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_TOKEN_FIREBASE;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_EMAIL;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_LASTNAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_NAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_SESSION;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_TOKEN;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;

public class SinginActivity extends AppCompatActivity {

    private UserSrv apiUserService;
    private RespSrvUser respSrvRegister;
    private Map<String,String> bodyRequest;
    private Button btnSingin;
    private TextView tvRecoveryPass;
    private EditText edtUsuario, edtPass;
    private String usuario, pass;
    private NotificationSrv notificationSrv;
    private Map<String,String> userMapSingin = new HashMap<>();
    private Map<String,String> userMapRecovery = new HashMap<>();
    private RadioButton noCerrar;
    private boolean isActivated;
    // llegada notificacion
    private boolean passToMisSolicitudes;
    private String idCompetenciamisSolicitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        if(obtenerEstadoButton()){
            passToInitApp();
        }
        updateUi();
        listenBotonSingin();
        listenBotonRecoveryPass();
        listenRadioButton();

        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            try {
                String view = bundle.getString(Constants.EXTRA_KEY_VIEW);
                if(view.equals(Constants.EXTRA_NOTIF_VIEW_SOLICITUD)) {
                    passToMisSolicitudes = true;
                    idCompetenciamisSolicitudes = bundle.getString(Constants.EXTRA_KEY_ID_COMPETENCIA);
//                    Intent toMisSolicitudes = new Intent(this, MisSolicitudesActivity.class);
//                    String idCompetencia = bundle.getString(Constants.EXTRA_KEY_ID_COMPETENCIA);
//                    toMisSolicitudes.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetencia);
//                    toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(toMisSolicitudes);
                }
                //aquí va tu código en el cual validas el tipo de dato
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        edtUsuario = findViewById(R.id.edt_usuario_signin);
        edtPass = findViewById(R.id.edt_pass_singin);
        noCerrar = findViewById(R.id.radioNoCerrarSesion);
        btnSingin = findViewById(R.id.btn_signin);
        tvRecoveryPass = findViewById(R.id.tv_recovery_pass);
        isActivated = noCerrar.isChecked();

        passToMisSolicitudes = false;
        idCompetenciamisSolicitudes = "";
    }

    private void listenRadioButton(){
        noCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isActivated){
                    noCerrar.setChecked(false);
                }
                isActivated = noCerrar.isChecked();
//                if(isActivated){
//                    Log.d("FLAG_RAD_BUTTON", "TRUE");
//                }
//                else{
//                    Log.d("FLAG_RAD_BUTTON", "FALSE");
//                }
                ManagerSharedPreferences.getInstance().setSessionFromSharedPreferences(getApplicationContext(),FILE_SHARED_DATA_USER, KEY_SESSION, isActivated);

            }
        });
    }

    private void listenBotonSingin(){
        btnSingin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(formCorrect()){
                    getValuesFields();
                    login();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Formulario INCORRECTO!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean obtenerEstadoButton() {
        String inicioSesion;
        if(ManagerSharedPreferences.getInstance().getSessionFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_SESSION))
            inicioSesion = "true";
        else
            inicioSesion = "false";
        Log.d("FLAG_SESION_SIGN", inicioSesion);
        return ManagerSharedPreferences.getInstance().getSessionFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_SESSION);
    }

    private void listenBotonRecoveryPass(){
        tvRecoveryPass.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!Validations.isEmpty(edtUsuario)){
                    recoveryPass();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debe ingresar su nombre de usuario o correo correctamente para reestablecer su contraseña", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean formCorrect(){
        if(Validations.isEmpty(edtUsuario)){
            return false;
        }
        if(Validations.isEmpty(edtPass)){
            return false;
        }

        return true;
    }

    private void getValuesFields() {
        usuario = edtUsuario.getText().toString();
        pass = edtPass.getText().toString();
    }

    private void login() {
        // hacemos la conexion con el api de rest del servidor
        apiUserService = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userMapSingin.put("usuario", usuario);
        userMapSingin.put("pass", pass);

        Call<RespSrvUser> call = apiUserService.initAccount(userMapSingin);
        call.enqueue(new Callback<RespSrvUser>() {
            @Override
            public void onResponse(Call<RespSrvUser> call, Response<RespSrvUser> response) {
                if (response.code() == 200) {
                    //Log.d("RESP_SIGNIN_ERROR", "EXITO: "+response.body().getMsg());
                    respSrvRegister = response.body();
                    Log.d("RESP_SIGNIN_ERROR", "EXITO - usuario: "+respSrvRegister);
                    // guardamos los datos del usuario localmente
                    saveDataUserLocally(respSrvRegister);
                    reportTokenFirebase();
                    Toast.makeText(getApplicationContext(), "Sesion iniciada con exito ", Toast.LENGTH_SHORT).show();
                    if(passToMisSolicitudes){
                        Intent toMisSolicitudes = new Intent(getApplicationContext(), MisSolicitudesActivity.class);
                        toMisSolicitudes.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetenciamisSolicitudes);
                        toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toMisSolicitudes);
                    }
                    else{
                        passToInitApp();
                    }
                    return;
                }
                if (response.code() == 400) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_SIGNIN_ERROR", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(getApplicationContext(), "No se pudo registrar el usuario:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<RespSrvUser> call, Throwable t) {
                try{
                    Log.d("RESP_CREATE_ERROR", "error: "+t.getMessage());
                    Toast.makeText(getApplicationContext(), "Problemas de conexion con el servidor ", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void reportTokenFirebase() {
        notificationSrv = new RetrofitAdapter().connectionEnable().create(NotificationSrv.class);

        bodyRequest = new HashMap<>();
        bodyRequest.put("nombreUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_USERNAME));
        bodyRequest.put("token", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN));
        Call<MsgRequest> call = notificationSrv.reportChangeTokenToServer(bodyRequest);
        Log.d("Req updateToken", call.request().toString());
//        Log.d("Req updateToken", bodyRequest.toString());
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if(response.code() == 200){
                    Log.d("UPDATE_TOKEN", Integer.toString(response.code()));
                }
            }
            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "No se pudo actualizar el token del usuario.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    // envia un cod de verificacion al correo del usuario para recestablecer su contrseña
    private void recoveryPass() {
        // hacemos la conexion con el api de rest del servidor
        apiUserService = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userMapRecovery.put("usuario", edtUsuario.getText().toString());

        Call<MsgRequest> call = apiUserService.resetPass(userMapRecovery);
        Log.d("RESP_RECOVERY_ERROR", "PETICION: "+call.request());
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Se envio un cod de verificacion a su cuenta de correo. Ingresela para poder reestablecer su contraseña.", Toast.LENGTH_LONG).show();
                    passToVerification(edtUsuario.getText().toString());
                    return;
                }
                if (response.code() == 400) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(getApplicationContext(), "Hubo un problema para recuperar su contraseña.:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Log.d("RESP_RECOVERY_ERROR", "error: "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Problemas de conexion con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // guardamos los datos del usuario de manera local
    private void saveDataUserLocally(RespSrvUser respSrvRegister) {
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_ID, String.valueOf(respSrvRegister.getId()));
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_NAME, respSrvRegister.getNombre());
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_LASTNAME, respSrvRegister.getApellido());
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_EMAIL, respSrvRegister.getCorreo());
        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_USERNAME, respSrvRegister.getUsuario());
        ManagerSharedPreferences.getInstance().setSessionFromSharedPreferences(this.getApplicationContext(),FILE_SHARED_DATA_USER, KEY_SESSION, isActivated);
    }



    private void passToInitApp() {
        Intent toInitApp = new Intent(this, NavigationMainActivity.class);
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toInitApp);
        finish();
    }

    private void passToVerification(String user) {
        Intent toVerification = new Intent(this, CodVerification.class);
        toVerification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toVerification.putExtra("usuario", user);
        startActivity(toVerification);
    }

}
