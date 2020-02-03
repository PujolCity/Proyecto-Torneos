package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;

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

    private Map<String,String> userMapCodVerif= new HashMap<>();
    private UserSrv apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod_verification);

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
                if(Validations.isEmpty(edtCodVerif)){
                    sendCodeVerification();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Formulario INCORRECTO!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // reestablece la contraseña del usuario a su nombre de usuario
    private void sendCodeVerification() {
        // TODO: cambiar por serv de envio de cod verif
        // hacemos la conexion con el api de rest del servidor
        apiUserService = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userMapCodVerif.put("usuario", edtCodVerif.getText().toString());

        Call<MsgRequest> call = apiUserService.resetPass(userMapCodVerif);
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Contraseña reestablecida. Vuelva a iniciar sesion ", Toast.LENGTH_SHORT).show();
                    // TODO: passToCodVerification();
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
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Log.d("RESP_CREATE_ERROR", "error: "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
