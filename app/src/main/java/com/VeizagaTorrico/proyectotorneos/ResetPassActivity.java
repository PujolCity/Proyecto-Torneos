package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.RespSrvUser;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassActivity extends AppCompatActivity {

    private Button btnResetPass;
    private EditText edtPass, edtconfirPass;
    private String usuario;

    private Map<String,String> userMapPass= new HashMap<>();
    private UserSrv apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        usuario = getIntent().getExtras().getString("usuarioReset");

        updateUi();
        listenBotonChangePass();
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        edtPass = findViewById(R.id.edt_pass_resetpass);
        edtconfirPass = findViewById(R.id.edt_confpass_resetpass);

        btnResetPass = findViewById(R.id.btn_reset_pass);;
    }

    private void listenBotonChangePass(){
        btnResetPass.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(formCorrect()){
                    changePass();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Controle que los campos coinciden o no esten vacios!.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean formCorrect(){
        if(Validations.isEmpty(edtPass)){
            //Log.d("VALIDACIONES",  "Nombre de usuario vacio");
            return false;
        }
        if(Validations.isEmpty(edtconfirPass)){
            //Log.d("VALIDACIONES",  "Correo vacio");
            return false;
        }
        if(!edtPass.getText().toString().equals(edtconfirPass.getText().toString())){
            return false;
        }

        return true;
    }

    // reestablece la contraseña del usuario a su nombre de usuario
    private void changePass() {
        // hacemos la conexion con el api de rest del servidor
        apiUserService = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userMapPass.put("usuario", usuario);
        userMapPass.put("pass", edtPass.getText().toString());

        Call<MsgRequest> call = apiUserService.changePass(userMapPass);
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Su contraseña fue cambiada con exito. ", Toast.LENGTH_LONG).show();
                    passToInitAccount();
                    return;
                }
                if (response.code() == 400) {
                    Log.d("CHANGE_PASS_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("CHANGE_PASS_ERROR", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(getApplicationContext(), "No se pudo cambiar la contraseña:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Log.d("CHANGE_PASS_ERROR", "error: "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void passToInitAccount() {
        Intent toInitApp = new Intent(this, SinginActivity.class);
        startActivity(toInitApp);
    }
}
