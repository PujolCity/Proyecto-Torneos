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

public class CodVerification extends AppCompatActivity {

    private Button btnSendCod;
    private EditText edtCodVerif;
    private String usuario;

    private Map<String,String> userMapCodVerif= new HashMap<>();
    private UserSrv apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod_verification);

        usuario = getIntent().getExtras().getString("usuario");

        updateUi();
        listenBotonSendCodVerification();
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        edtCodVerif = findViewById(R.id.edt_cod_verif);

        btnSendCod = findViewById(R.id.btn_send_cod);;
    }

    private void listenBotonSendCodVerification(){
        btnSendCod.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!Validations.isEmpty(edtCodVerif)){
                    sendCodeVerification();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ingrese el codigo de verificacion!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // reestablece la contraseña del usuario a su nombre de usuario
    private void sendCodeVerification() {
        // hacemos la conexion con el api de rest del servidor
        apiUserService = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userMapCodVerif.put("usuario", usuario);
        userMapCodVerif.put("pass", edtCodVerif.getText().toString());

        Call<RespSrvUser> call = apiUserService.initAccount(userMapCodVerif);
        call.enqueue(new Callback<RespSrvUser>() {
            @Override
            public void onResponse(Call<RespSrvUser> call, Response<RespSrvUser> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Codigo correcto. Debe cambiar su contraseña ", Toast.LENGTH_LONG).show();
                    passToChangePass();
                    return;
                }
                if (response.code() == 400) {
                    Log.d("RESET_PASS_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(getApplicationContext(), "No se pudo verificar el codigo con exito:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<RespSrvUser> call, Throwable t) {
                Log.d("RESET_PASS_ERROR", "error: "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void passToChangePass() {
        Intent toChangePass = new Intent(this, ResetPassActivity.class);
        toChangePass.putExtra("usuarioReset", usuario);
        startActivity(toChangePass);
    }
}
