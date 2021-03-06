package com.VeizagaTorrico.proyectotorneos.fragments.encuentros;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.EdicionesRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.Edition;
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.models.Turn;
import com.VeizagaTorrico.proyectotorneos.offline.admin.AdminDataOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.VeizagaTorrico.proyectotorneos.services.FieldSrv;
import com.VeizagaTorrico.proyectotorneos.services.GroundSrv;
import com.VeizagaTorrico.proyectotorneos.services.RefereeSrv;
import com.VeizagaTorrico.proyectotorneos.services.TurnSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_CONFRONTATION_OFF;
import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_COUNT;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;

public class DetalleEncuentroFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private EditText r1,r2;
    private TextView comp1, comp2,txtCampo,txtPredio,txtJuez,txtTurno;
//    private ImageButton confirmarEdit;
    private Button confirmarEdit;
    private Spinner spinnerPredio,spinnerCampo,spinnerJuez,spinnerTurno;
    private Confrontation encuentro;
    private ConfrontationSrv confrontationSrv;
    private RecyclerView recycleEdicion;
    private EdicionesRecyclerViewAdapter adapterEdicion;
    private List<Edition> ediciones;
    private Switch swtMostrarEdicion;
    private TextView tvSinEdiciones;
    private LinearLayout barEdicion;

    private GroundSrv prediosSrv;
    private FieldSrv camposSrv;
    private List<Ground> predios;
    private List<Field> campos;
    private ArrayAdapter<Field> adapterCampo;
    private RefereeSrv refereeSrv;
    private String juezSeleccionado,campoSeleccionado,turnoSeleccionado,rdo_1,rdo_2;
    private List<Referee> jueces;
    private TurnSrv turnoSrv;
    private List<Turn> turnos;
    private Map<String,String> editEncuentro;

    private Ground predio;
    private Referee referee;
    private Field campo;
    private Turn turno;

    private ManagerConfrontationOff adminEncuentrosLocal;
    private AdminDataOff adminDataOff;
    private Gson gson;
    private AlertDialog alertDialog;

    public DetalleEncuentroFragment() {
    }

    public static DetalleEncuentroFragment newInstance() {
        DetalleEncuentroFragment fragment = new DetalleEncuentroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_detalle_encuentro, container, false);
        initElements();
        llenarDatosAsignados();
        getEditions();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            llenarSpinnerJuez();
            llenarSpinnerPredio();
            llenarSpinnerTurno();
            listenerEditar();
            barEdicion.setVisibility(View.VISIBLE);
            listenSwitch();
        }
        else{
            adminDataOff = new AdminDataOff(vista.getContext());
            barEdicion.setVisibility(View.GONE);
            recycleEdicion.setVisibility(View.GONE);
            llenarSpinersOffline();
            editarOffLine();
        }
        return vista;
    }

    private void editarOffLine() {
        confirmarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json;
                editEncuentro.clear();
                rdo_1 = r1.getText().toString();
                rdo_2 = r2.getText().toString();
                editEncuentro.put("idEncuentro", Integer.toString(encuentro.getId()));
                editEncuentro.put("idUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
                if(validar()){
                    editEncuentro.put("rdo_comp1", rdo_1);
                    editEncuentro.put("rdo_comp2",rdo_2);
                    json = gson.toJson(editEncuentro);
                    // persisttimos un registro de la edicion del encuentro usando la cant de registro como clave
                    int cantRegistrosEncuentros = ManagerSharedPreferences.getInstance().getCountConfrontation(vista.getContext(), FILE_SHARED_CONFRONTATION_OFF, KEY_COUNT);
                    Log.d("ENC_OFF_CONTADOR", "Cant de registros: "+Integer.toString(cantRegistrosEncuentros));
                    String keyNuevoResgitro = String.valueOf(cantRegistrosEncuentros + 1);
                    ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(vista.getContext(),FILE_SHARED_CONFRONTATION_OFF ,keyNuevoResgitro ,json);
                    String guardado = ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(),FILE_SHARED_CONFRONTATION_OFF, keyNuevoResgitro);
                    Log.d("ENC_OFF_SAVE", "Elemento guardado"+ guardado);
                    // actualizamos el encuentro localmente, en la DB local
                    adminEncuentrosLocal.updateByCompetition(encuentro.getId(),encuentro.getIdCompetencia(), Integer.valueOf(rdo_1),Integer.valueOf(rdo_2));
                    // actualizamos la cant de registros editados
                    ManagerSharedPreferences.getInstance().setDataFromSharedPreferences(vista.getContext(),FILE_SHARED_CONFRONTATION_OFF, KEY_COUNT,(cantRegistrosEncuentros + 1));
                    Toast.makeText(vista.getContext(), "Edicion local realizada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void llenarSpinersOffline(){
        // ocultamos el spin de predio
        spinnerPredio.setVisibility(View.GONE);
        // recuperamos los datos del juez y llenamos el spinner de JUEZ
        Referee refereeDbLocal = adminDataOff.getRefereeConfrontation(encuentro.getId());
        //Log.d("JUEZ_ENC_LOCAL"," Nombre y apellido: "+refereeDbLocal.getNombre()+" "+refereeDbLocal.getApellido());
        if(refereeDbLocal != null){
            jueces.add(refereeDbLocal);
            ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerJuez.setAdapter(adapter);
        }
        // recuperamos los datos del juez y llenamos el spinner de CAMPO
        Field fieldDbLocal = adminDataOff.getFieldConfrontation(encuentro.getId());
        //Log.d("CAMPO_ENC_LOCAL"," Nombre: "+fieldDbLocal.getNombre());
        if(fieldDbLocal != null){
            campos.add(fieldDbLocal);
            ArrayAdapter<Field> adapterCampos = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, campos);
            adapterCampos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCampo.setAdapter(adapterCampos);
        }
        // recuperamos los datos del turno y llenamos el spinner de TURNO
        String stringTurn = adminDataOff.turnConfrontation(encuentro.getId());
        if(stringTurn != null){
            Log.d("TURNO_ENC_LOCAL"," Horario: "+stringTurn);
            Turn turnDbLocal = new Turn();
            // vemos si hay turnos disponible
            if(stringTurn.length() > 3){
                turnDbLocal.setHora_desde(stringTurn.substring(0,5));
                turnDbLocal.setHora_hasta(stringTurn.substring(8,13));
                turnos.add(turnDbLocal);
                ArrayAdapter<Turn> adapterTurnos = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, turnos);
                adapterTurnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTurno.setAdapter(adapterTurnos);
            }
        }

    }

    private void listenerEditar() {
        confirmarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEncuentro.clear();
                rdo_1 = r1.getText().toString();
                rdo_2 = r2.getText().toString();
                editEncuentro.put("idCompetencia", Integer.toString(encuentro.getIdCompetencia()));
                editEncuentro.put("idEncuentro", Integer.toString(encuentro.getId()));
                editEncuentro.put("idUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));

                if(!juezSeleccionado.isEmpty())
                    editEncuentro.put("idJuez",juezSeleccionado);
                if(!campoSeleccionado.isEmpty())
                    editEncuentro.put("idCampo",campoSeleccionado);
                if(!turnoSeleccionado.isEmpty())
                    editEncuentro.put("idTurno",turnoSeleccionado);
                if(validar()){
                    editEncuentro.put("rdo_comp1", rdo_1);
                    editEncuentro.put("rdo_comp2",rdo_2);
                }
                Log.d("body edit",editEncuentro.toString());
                alertDialog.show();
                Call<MsgRequest> call = confrontationSrv.editEncuentro(editEncuentro);
                Log.d("url call", call.request().url().toString());
                call.enqueue(new Callback<MsgRequest>() {
                    @Override
                    public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                        alertDialog.dismiss();
                        try{
                            if(response.code() == 200){
                                Toast toast = Toast.makeText(vista.getContext(), "Edicion realizada con exito", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            if (response.code() == 400) {
                                Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String userMessage = jsonObject.getString("msg");
                                    Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                                    Toast.makeText(vista.getContext(), "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgRequest> call, Throwable t) {
                        Log.d("SERVER_ERROR: ", t.getMessage());
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    private boolean validar() {
        Log.d("VALORES", rdo_1+ "   "+rdo_2);
        if(rdo_1.isEmpty()){
            Log.d("VACIO", "NO ENTRA");
            return false;}
        if(rdo_2.isEmpty())
            return false;
        return true;
    }


    private void initElements(){
        alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Guardando cambios..");
        adminEncuentrosLocal = new ManagerConfrontationOff(vista.getContext());
        confrontationSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);
        prediosSrv = new RetrofitAdapter().connectionEnable().create(GroundSrv.class);
        camposSrv = new RetrofitAdapter().connectionEnable().create(FieldSrv.class);
        refereeSrv = new RetrofitAdapter().connectionEnable().create(RefereeSrv.class);
        turnoSrv = new RetrofitAdapter().connectionEnable().create(TurnSrv.class);

        this.encuentro = (Confrontation) getArguments().getSerializable("encuentro");
        predios = new ArrayList<>();
        campos = new ArrayList<>();
        jueces = new ArrayList<>();
        turnos = new ArrayList<>();
        editEncuentro = new HashMap<>();

        confirmarEdit = vista.findViewById(R.id.btn_guardar_cambios);
        comp1 = vista.findViewById(R.id.txtComp1Titulo);
        comp2 = vista.findViewById(R.id.txtComp2Titulo);

        txtCampo = vista.findViewById(R.id.tv_campo_edit_enc);
        txtPredio = vista.findViewById(R.id.tv_predio_edit_enc);
        txtJuez = vista.findViewById(R.id.tv_juez_edit_enc);
        txtTurno = vista.findViewById(R.id.tv_turno_edit_enc);

        r1 = vista.findViewById(R.id.resultadoComp1);
        r2 = vista.findViewById(R.id.resultadoComp2);

        spinnerPredio = vista.findViewById(R.id.spinnerPredio);
        spinnerCampo = vista.findViewById(R.id.spinnerCampo);
        spinnerJuez = vista.findViewById(R.id.spinnerJuez);
        spinnerTurno = vista.findViewById(R.id.spinnerTurno);

        swtMostrarEdicion = vista.findViewById(R.id.sw_mostrar_edicion);
        swtMostrarEdicion.setChecked(false);
        swtMostrarEdicion.setVisibility(View.GONE);
        tvSinEdiciones =  vista.findViewById(R.id.tv_sin_ediciones);

        recycleEdicion = vista.findViewById(R.id.edicionesList);
        recycleEdicion.setVisibility(View.INVISIBLE);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(vista.getContext());
        recycleEdicion.setLayoutManager(manager);
        recycleEdicion.setHasFixedSize(true);
        adapterEdicion = new EdicionesRecyclerViewAdapter(vista.getContext());
        recycleEdicion.setAdapter(adapterEdicion);
        barEdicion = vista.findViewById(R.id.bar_title_edicion);

//        juezSeleccionado = "";
//        campoSeleccionado = "";
//        turnoSeleccionado= "";
        comp1.setText(encuentro.getCompetidor1());
        comp2.setText(encuentro.getCompetidor2());

        gson = new Gson();  //Instancia Gson.

        if(encuentro.getRdoc1() != -1 && encuentro.getRdoc2() != -1){
            r1.setText(Integer.toString(encuentro.getRdoc1()));
            r2.setText(Integer.toString(encuentro.getRdoc2()));
        }

        referee = new Referee(0, "-", " ",0);
        predio = new Ground(0, "-", "", "");
        campo = new Field(0, "-", 0, 0, null);
        turno = new Turn(0,0,"-", "");

    }

    private void llenarDatosAsignados(){
        Log.d("DATOS: ", "Aca van los datos del encuentro");
        if(encuentro.getCampo() != null){
//            campos.clear();
//            campo = new Field(encuentro.getCampo().getId(), encuentro.getCampo().toString(), 0, 0, encuentro.getCampo().getPredio());
            Log.d("INIT CAMPO", campo.toString());
            //campos.add(campo);
            txtCampo.setText(encuentro.getCampo().toString());
            txtPredio.setText(encuentro.getCampo().getPredio().toString());
        }
        else{
            txtCampo.setText("Sin asignar");
            txtPredio.setText("Sin asignar");
        }
        if(encuentro.getJuez() != null){
//            jueces.clear();
//            referee = new Referee(encuentro.getJuez().getId(), encuentro.getJuez().getNombre(), encuentro.getJuez().getApellido(),0);
            //jueces.add(referee);
            txtJuez.setText(encuentro.getJuez().toString());
        }
        else{
            txtJuez.setText("Sin asignar");
        }
        if(encuentro.getTurno() != null){
            txtTurno.setText(encuentro.getTurno().toString());
        }
        else{
            txtTurno.setText("Sin asignar");
        }
    }

//    private void msjCampos() {
//        campos.clear();
//        campo = new Field(0,"-sdad",0,0,null);
//        campos.add(campo);
//        adapterCampo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,campos);
//        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCampo.setAdapter(adapterCampo);
//    }

    private void llenarSpinnerTurno() {
        spinnerPredio.setVisibility(View.VISIBLE);
        Call<List<Turn>> call = turnoSrv.getTurnsByCompetition(encuentro.getIdCompetencia());
        Log.d("URL TURNO!",call.request().url().toString());        call.enqueue(new Callback<List<Turn>>() {
            @Override
            public void onResponse(Call<List<Turn>> call, Response<List<Turn>> response) {
                if(response.code() == 200){
                    try {
                        if(!response.body().isEmpty()){
                            //turnos.add(turno);
                            turnos.addAll(response.body());
                            ArrayAdapter<Turn> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,turnos);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTurno.setAdapter(adapter);
                            spinnerTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Turn turnoSel = (Turn) spinnerTurno.getSelectedItem();
                                    if(turnoSel.getId() != 0 ){
                                        turnoSeleccionado = Integer.toString(turnoSel.getId());
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        }else {
                            // ver si colocar algun mje
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Turn>> call, Throwable t) {
                try {
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarSpinnerJuez() {
        Call<List<Referee>> call = refereeSrv.getJuecesAsignados(encuentro.getIdCompetencia());
        Log.d("Call Juez",call.request().url().toString());
        call.enqueue(new Callback<List<Referee>>() {
            @Override
            public void onResponse(Call<List<Referee>> call, Response<List<Referee>> response) {
                try{
                    if(!response.body().isEmpty()){
                        List<Referee> aux = response.body();
                        //jueces.add(referee);
                        for(int i = 0; i < aux.size(); i++){
//                            if(!referee.equals(aux.get(i))){
//                                jueces.add(aux.get(i));
//                            }
                            jueces.add(aux.get(i));
                        }
                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerJuez.setAdapter(adapter);
                        spinnerJuez.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Referee juezSel = (Referee) spinnerJuez.getSelectedItem();
                                if(juezSel.getId() != 0){
                                    juezSeleccionado = Integer.toString(juezSel.getId());
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }else {
                        // ver si colocar alguna mje
                    }
                } catch (Exception e) {
                        e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Referee>> call, Throwable t) {
                try {
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarSpinnerPredio(){
        Call<List<Ground>> call = prediosSrv.getPrediosAsignados(encuentro.getIdCompetencia());
        Log.d("call predio",call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    Log.d("encuentro response", Integer.toString(response.code()));
                    if(!response.body().isEmpty()) {
                        List<Ground> aux = response.body();
//                        predios.add(predio);
//                        for(int i = 0; i < aux.size(); i++){
//                            if(!predio.equals(aux.get(i))){
//                                predios.add(aux.get(i));
//                            }
//                        }
                        for(int i = 0; i < aux.size(); i++){
                            predios.add(aux.get(i));
                        }
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPredio.setAdapter(adapter);
                        spinnerPredio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Ground predio = (Ground) spinnerPredio.getSelectedItem();
                                if (predio.getId() != 0) {
                                    // hacer algo para despues actualizar el encuentro
                                    llenarSpinnerCampos(predio.getId());
                                }
//                                else {
//                                    msjCampos();
//                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        // ver si colocar alguna mje
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Ground>> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void llenarSpinnerCampos(final int idPredio){
        if(idPredio != 0){
            Call<List<Field>> call = camposSrv.getFieldsByGround(idPredio);
            Log.d("calls campo",call.request().url().toString());
            call.enqueue(new Callback<List<Field>>() {
                @Override
                public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                    try {
                        if(!response.body().isEmpty()) {
                            campos.clear();
                            Log.d("RESPONSE CAMPOS", response.body().toString());
                            if(campo.getPredio() != null){
//                                campos.add(campo);
                                List<Field> aux = response.body();
                                for(int i = 0; i < aux.size(); i++){
//                                    if(!campo.equals(aux.get(i))){
//                                        campos.add(aux.get(i));
//                                    }
                                    campos.add(aux.get(i));
                                }
                            } else {
                                campos.addAll(response.body());
                            }
                            adapterCampo = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, campos);
                            adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCampo.setAdapter(adapterCampo);
                            spinnerCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Field campoSel = (Field) spinnerCampo.getSelectedItem();
                                    if(campoSel.getId() != 0){
                                        campoSeleccionado = Integer.toString(campoSel.getId());
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                        else{
                            // ver si colocar alguna mje
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Field>> call, Throwable t) {
                    try{
                        Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
//        else {
//            msjCampos();
//        }
    }

    // recuperamos las ediciones del encuentro
    private void getEditions(){
        Call<List<Edition>> call = confrontationSrv.getEditions(encuentro.getId());
        Log.d("GET_EDITIONS",call.request().url().toString());
        call.enqueue(new Callback<List<Edition>>() {
            @Override
            public void onResponse(Call<List<Edition>> call, Response<List<Edition>> response) {
                Log.d("GET_EDITIONS", Integer.toString(response.code()));
                if(response.code() == 200){
                    try {
                        ediciones = response.body();
                        if(ediciones.size() != 0){
                            Log.d("EDICIONES ",ediciones.toString());
                            mostrarBarraEdiciones();
                            adapterEdicion.setEdiciones(ediciones);
                            recycleEdicion.setAdapter(adapterEdicion);
                        } else {
                            ocultarBarraEdiciones();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Edition>> call, Throwable t) {
                try {
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void mostrarBarraEdiciones(){
        swtMostrarEdicion.setVisibility(View.VISIBLE);
        tvSinEdiciones.setVisibility(View.GONE);
    }

    private void ocultarBarraEdiciones(){
        swtMostrarEdicion.setVisibility(View.GONE);
        tvSinEdiciones.setVisibility(View.VISIBLE);
    }

    private void listenSwitch(){
         swtMostrarEdicion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recycleEdicion.setVisibility(View.VISIBLE);
                }
                else {
                    recycleEdicion.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
