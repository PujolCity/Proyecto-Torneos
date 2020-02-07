package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_TOKEN;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;

public class SinginActivity extends AppCompatActivity {

    private UserSrv apiUserService;
    RespSrvUser respSrvRegister;
    private Map<String,String> bodyRequest;
    Button btnSingin;
    TextView tvRecoveryPass;
    EditText edtUsuario, edtPass;
    String usuario, pass;
    private NotificationSrv notificationSrv;
    private Map<String,String> userMapSingin = new HashMap<>();
    private Map<String,String> userMapRecovery = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        updateUi();
        listenBotonSingin();
        listenBotonRecoveryPass();
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        edtUsuario = findViewById(R.id.edt_usuario_signin);
        edtPass = findViewById(R.id.edt_pass_singin);

        btnSingin = findViewById(R.id.btn_signin);
        tvRecoveryPass = findViewById(R.id.tv_recovery_pass);
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
            //Log.d("VALIDACIONES",  "Nombre de usuario vacio");
            return false;
        }
        if(Validations.isEmpty(edtPass)){
            //Log.d("VALIDACIONES",  "Correo vacio");
            return false;
        }

        return true;
    }

    private void getValuesFields() {
        usuario = edtUsuario.getText().toString();
        pass = edtPass.getText().toString();

        //Toast.makeText(getApplicationContext(), nombre+" - "+apellido+" - "+usuario+" - "+correo+" - "+pass+" - "+confPass, Toast.LENGTH_SHORT).show();
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
                    // TODO: guardar datos del usuario localmente
                    saveDataUserLocally(respSrvRegister);
                    reportTokenFirebase();
                    Toast.makeText(getApplicationContext(), "Sesion iniciada con exito ", Toast.LENGTH_SHORT).show();
                    passToInitApp();
                    return;
                }
                if (response.code() == 400) {
                    Log.d("RESP_SIGNIN_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
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
                Log.d("RESP_CREATE_ERROR", "error: "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reportTokenFirebase() {
        notificationSrv = new RetrofitAdapter().connectionEnable().create(NotificationSrv.class);

        bodyRequest = new HashMap<>();
        bodyRequest.put("nombreUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_USERNAME));
        bodyRequest.put("token", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN));
//        Log.d("MANAGER_SHARED singin", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_TOKEN_FIREBASE, KEY_TOKEN));
        Call<MsgRequest> call = notificationSrv.reportChangeTokenToServer(bodyRequest);
        Log.d("Req updateToken", call.request().toString());
        Log.d("Req updateToken", bodyRequest.toString());
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if(response.code() == 200){
                    Log.d("response code", Integer.toString(response.code()));
                    Toast toast = Toast.makeText(getApplicationContext(), "Token actualizado correctamente.: ", Toast.LENGTH_SHORT);
                    toast.show();
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
                    Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
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
                Toast.makeText(getApplicationContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
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

        Log.d("USER_SHARED", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_ID));
        Log.d("USER_SHARED", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_NAME));
        Log.d("USER_SHARED", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_LASTNAME));
        Log.d("USER_SHARED", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_EMAIL));
        Log.d("USER_SHARED", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_USERNAME));
    }

    private void passToInitApp() {
        Intent toInitApp = new Intent(this, NavigationMainActivity.class);
        startActivity(toInitApp);
    }

    private void passToVerification(String user) {
//        Intent mIntent = new Intent(this, Example.class);
//        mIntent.putExtra(key, value);
        Intent toVerification = new Intent(this, CodVerification.class);
        toVerification.putExtra("usuario", user);
        startActivity(toVerification);
    }

}
