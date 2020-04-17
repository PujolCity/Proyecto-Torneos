package com.VeizagaTorrico.proyectotorneos.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationEdit;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_CONFRONTATION_OFF;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_COUNT;

public class ManagerUpdateConfrontation {

    private static ManagerUpdateConfrontation instance = new ManagerUpdateConfrontation();
    ConfrontationSrv encuentrosSrv;
    private Gson adminJson;

    private ManagerUpdateConfrontation() {
        encuentrosSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);
        adminJson = new Gson();
    }

    public static ManagerUpdateConfrontation getInstance() {
        return instance;
    }

    public void updateConfrontations(Context context){
        int cantRegistrosEncuentros = ManagerSharedPreferences.getInstance().getCountConfrontation(context, FILE_SHARED_CONFRONTATION_OFF, KEY_COUNT);
        if(cantRegistrosEncuentros <= 0){
            Log.d("MANAGER_UPDATE", "No hay datos por actualizar");
        }
        else{
            List<ConfrontationEdit> encuentrosEdit = getConfrontationsUpdateDbLocal(context, cantRegistrosEncuentros);
            List<ConfrontationEdit> encuentrosMin = cleanRegisterUpdate(encuentrosEdit);

            Log.d("MANAGER_UPDATE", "Cant de lista de objetos minificada: "+encuentrosMin.size());

            // mandamos los encuentros a actualizar
            updateConfrontationServer(encuentrosMin, context);

        }
    }

    // recupera los encuentros pendientes por actualizacion
    private List<ConfrontationEdit> getConfrontationsUpdateDbLocal(Context context, int cantRegistrosEncuentros){
        List<ConfrontationEdit> encuentrosEdit = new ArrayList<>();
        // rearmamos los objetos encuentros y los mandamos a actualizar al server
        for (int i = 1; i <= cantRegistrosEncuentros ; i++) {
            String confrontationObject = ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(context, FILE_SHARED_CONFRONTATION_OFF, String.valueOf(i));
            ConfrontationEdit encuentro = adminJson.fromJson(confrontationObject, ConfrontationEdit.class);
            //Log.d("MANAGER_UPDATE", "Encuentro recuperado: "+encuentro);
            encuentrosEdit.add(encuentro);
        }
        Log.d("MANAGER_UPDATE", "Objetos encuentros creados: "+encuentrosEdit.size());

        return encuentrosEdit;
    }

    // unifica los cambios realizados a un mismo registro, deja el ultimo cambio
    private List<ConfrontationEdit> cleanRegisterUpdate(List<ConfrontationEdit> registros){
        List<ConfrontationEdit> registrosLimpios = new ArrayList<>();

        for (int i = 0; i < registros.size(); i++) {
            ConfrontationEdit registro = registros.get(i);
            // si el elemento no esta en la nueva lista lo inserto
            int indiceRegistro = existConfrontation(registrosLimpios, registro);
            if(indiceRegistro != -1){
                registrosLimpios.remove(indiceRegistro);
            }
            registrosLimpios.add(registro);
        }

        return registrosLimpios;
    }

    // devuelve el indice dentro de la lista, -1 si no se encuentra en la lista
    private int existConfrontation(List<ConfrontationEdit> registros, ConfrontationEdit registro) {
        //for(ConfrontationEdit encuentro : registros) {
        for(int i=0; i < registros.size(); i++){
            ConfrontationEdit encuentro = registros.get(i);
            if(encuentro.getId() == registro.getId()){
                return i;
            }
        }
        return -1;
    }

    // mandamos la peticion para actualizar en el SERVER
    private void updateConfrontationServer(List<ConfrontationEdit> registros, final Context context){
        String jsonEncuentrosEdit = adminJson.toJson(registros);
        Map<String,String> encuentrosBody = new HashMap<>();
//        Log.d("ENC_EDIT_OFF",jsonEncuentrosEdit);
        encuentrosBody.put("encuentros", jsonEncuentrosEdit);
        Call<MsgRequest> call = encuentrosSrv.syncToServer(encuentrosBody);
        Log.d("UPDATE_ENC_OFF", call.request().url().toString());
        //Log.d("UPDATE_ENC_OFF", encuentrosBody.toString());
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                try{
                    if(response.code() == 200){
                        Log.d("UPDATE_CONFRONTATION", "Encuentros actualizados.");
                        // actualizamos el registro de ecncuentro a actualizar
                        ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(context, FILE_SHARED_CONFRONTATION_OFF, KEY_COUNT, 0);
                        Toast.makeText(context, "Syncronizacion realizada : Los encuentros modificados localmente se han actualizados en el servidor", Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 400) {
                        Log.d("UPDATE_ENC_OFF_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            String userMessage = jsonObject.getString("msg");
                            Log.d("UPDATE_ENC_OFF", "Msg de la repuesta: "+userMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("UPDATE_ENC_OFF", "Error: "+e.toString());
                }
            }

            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Log.d("SERVER_ERROR: ", t.getMessage());
            }
        });
    }
}
