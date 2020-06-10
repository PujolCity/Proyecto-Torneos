package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.models.RespSrvUser;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.VeizagaTorrico.proyectotorneos.Constants.ACTUALIZANDO_DATOS;

public class UserRegisterActivity extends AppCompatActivity {

    private UserSrv apiUserService;
    RespSrvUser respSrvRegister;
    Button btn_register;
    EditText edt_nombre, edt_apellido, edt_nombreUsuario, edt_correo, edt_pass, edt_confPass;
    String nombre, apellido, usuario, correo, pass, confPass;
    private AlertDialog alertDialog;
    private Map<String,String> userMapRegister = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        updateUi();
        listenBotonRegister();
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

    private void listenBotonRegister(){
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
        nombre = edt_nombre.getText().toString().trim();
        apellido = edt_apellido.getText().toString().trim();
        usuario = edt_nombreUsuario.getText().toString().trim();
        correo = edt_correo.getText().toString().trim();
        pass = edt_pass.getText().toString();
        confPass = edt_confPass.getText().toString();

        //Toast.makeText(getApplicationContext(), nombre+" - "+apellido+" - "+usuario+" - "+correo+" - "+pass+" - "+confPass, Toast.LENGTH_SHORT).show();
    }

    //
    private boolean passCorrect() {
        if(pass.equals(confPass)){
            return true;
        }
        return false;
    }

    private void registerUser(){
        // hacemos la conexion con el api de rest del servidor
        apiUserService = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userMapRegister.put("nombre", nombre);
        userMapRegister.put("apellido", apellido);
        userMapRegister.put("usuario", usuario);
        userMapRegister.put("correo", correo);
        userMapRegister.put("pass", pass);
        // cambiar este dato
        userMapRegister.put("token", "ACA_VA_EL_TOKEN_FIREBASE");

        Call<RespSrvUser> call = apiUserService.register(userMapRegister);
        alertDialog = ManagerMsgView.getMsgLoading(this, ACTUALIZANDO_DATOS);
        alertDialog.show();
        call.enqueue(new Callback<RespSrvUser>() {
            @Override
            public void onResponse(Call<RespSrvUser> call, Response<RespSrvUser> response) {
                alertDialog.dismiss();
                if (response.code() == 201) {
                    Toast.makeText(getApplicationContext(), "Se registro el usuario exitosamente ", Toast.LENGTH_SHORT).show();
                    response.raw();
                    passToInit();
                    return;
                }
                if (response.code() == 400) {
                    Log.d("RESP_CREATE_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_CREATE_ERROR", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(getApplicationContext(), "No se pudo registrar el usuario:  << "+userMessage+" >>", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<RespSrvUser> call, Throwable t) {
                alertDialog.dismiss();
                Log.d("RESP_CREATE_ERROR", "error: "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Existen problemas con el servidor ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void passToInit(){
        Intent toInitApp = new Intent(this, HomeActivity.class);
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toInitApp);
    }
}
