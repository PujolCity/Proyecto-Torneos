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

import com.VeizagaTorrico.proyectotorneos.models.RespSrvUser;
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
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_EMAIL;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_LASTNAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_NAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;

public class SinginActivity extends AppCompatActivity {

    private UserSrv apiUserService;
    RespSrvUser respSrvRegister;
    Button btnSingin;
    TextView tvRecoveryPass;
    EditText edtUsuario, edtPass;
    String usuario, pass;
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
                    getValuesFields();
                    // TODO: reestablecer  el pass
                    recoveryPass();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debe ingresar su usuario correctamente para reestablecer suscontraseña", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Sesion iniciada con exito ", Toast.LENGTH_SHORT).show();
                    response.raw();
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

    // reestablece la contraseña del usuario a su nombre de usuario
    private void recoveryPass() {
        // hacemos la conexion con el api de rest del servidor
        apiUserService = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userMapRecovery.put("usuario", usuario);

        Call<RespSrvUser> call = apiUserService.resetPass(userMapRecovery);
        call.enqueue(new Callback<RespSrvUser>() {
            @Override
            public void onResponse(Call<RespSrvUser> call, Response<RespSrvUser> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Contraseña reestablecida. Vuelva a iniciar sesion ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.code() == 400) {
                    Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(getApplicationContext(), "No se pudo reestablecer la contraseña:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
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

}
