package com.VeizagaTorrico.proyectotorneos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.VeizagaTorrico.proyectotorneos.models.Gender;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.GenderSrv;
import com.VeizagaTorrico.proyectotorneos.services.StatusSrv;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCompetenciaActivity extends AppCompatActivity {

    private CompetitionSrv apiCompetitionService;
    private GenderSrv apiGenderService;
    private StatusSrv apiStatusServicce;

    //RespSrvUser respSrvRegister;
    Button btnUpdateCompetition;

    EditText edtCiudad;
    Spinner spinGenero, spinEstado;
    String ciudad;
    Gender genero;
    Gender estado;

    // capturan resp de los servicios del server para los spinners
    private List<Gender> estados;
    private List<Gender> generos;

    private Map<String,String> competitionMapUpdate = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_competencia);

        updateUi();
        listenBotonUpdateCompetition();
        llenarSpinnerGenero();
        llenarSpinnerEstado();
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        edtCiudad = findViewById(R.id.edt_ciudad_edit_comp);

        spinGenero = findViewById(R.id.spinner_genero_edit);
        spinEstado = findViewById(R.id.spinner_estado_edit);

        generos = new ArrayList<>();
        estados = new ArrayList<>();

        btnUpdateCompetition= findViewById(R.id.btn_update_edit_comp);
    }

    private void listenBotonUpdateCompetition(){
        btnUpdateCompetition.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(formCorrect()){
                    getValuesFields();
                    updateData();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Formulario INCORRECTO!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean formCorrect(){
        if(Validations.isEmpty(edtCiudad)){
            //Log.d("VALIDACIONES",  "Nombre de usuario vacio");
            return false;
        }

        return true;
    }

    private void getValuesFields() {
        ciudad = edtCiudad.getText().toString();
//        genero = edtUsuario.getText().toString();
//        estado = edtPass.getText().toString();

        //Toast.makeText(getApplicationContext(), nombre+" - "+apellido+" - "+usuario+" - "+correo+" - "+pass+" - "+confPass, Toast.LENGTH_SHORT).show();
    }

    private void updateData() {
        // hacemos la conexion con el api de rest del servidor
        apiCompetitionService = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);

        competitionMapUpdate.put("ciudad", ciudad);
//        competitionMapUpdate.put("genero", genero);
//        competitionMapUpdate.put("estado", estado);

        Call<MsgRequest> call = apiCompetitionService.updateCompetition(competitionMapUpdate);
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if (response.code() == 200) {
                    //Log.d("RESP_SIGNIN_ERROR", "EXITO: "+response.body().getMsg());
//                    respSrvRegister = response.body();
                    //Log.d("RESP_SIGNIN_ERROR", "EXITO - usuario: "+respSrvRegister);
                    Toast.makeText(getApplicationContext(), "Sesion iniciada con exito ", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Log.d("RESP_CREATE_ERROR", "error: "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarSpinnerGenero() {
        generos.clear();

        apiGenderService = new RetrofitAdapter().connectionEnable().create(GenderSrv.class);
        Call<List<Gender>> call = apiGenderService.getGenders();
        Log.d("Call Generos",call.request().url().toString());
        call.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
                try{
                    if(!response.body().isEmpty()){
                        generos = response.body();
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner_custom, generos);
                        adapter.setDropDownViewResource(R.layout.item_spinner_custom);
                        spinGenero.setAdapter(adapter);
                        spinGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                genero = (Gender) spinGenero.getSelectedItem();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
//                        Referee referee = new Referee(0, "Sin Jueces", " ",0,null);
//                        jueces.add(referee);
//                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerJuez.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Gender>> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void llenarSpinnerEstado() {
        estados.clear();

        apiStatusServicce = new RetrofitAdapter().connectionEnable().create(StatusSrv.class);
        Call<List<Gender>> call = apiStatusServicce.getStatus();
        Log.d("Call Estados",call.request().url().toString());
        call.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
                try{
                    if(!response.body().isEmpty()){
                        estados = response.body();
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, estados);
                        adapter.setDropDownViewResource(R.layout.item_spinner_custom);
                        spinEstado.setAdapter(adapter);
                        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                estado = (Gender) spinEstado.getSelectedItem();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
//                        Referee referee = new Referee(0, "Sin Jueces", " ",0,null);
//                        jueces.add(referee);
//                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerJuez.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Gender>> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}
