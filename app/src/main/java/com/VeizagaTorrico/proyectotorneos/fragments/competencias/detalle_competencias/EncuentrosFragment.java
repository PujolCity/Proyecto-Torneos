package com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.EncuentrosDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.EncuentrosRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionOrg;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.ConfrontationsCompetition;
import com.VeizagaTorrico.proyectotorneos.offline.admin.AdminDataOff;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;
import com.VeizagaTorrico.proyectotorneos.utils.Support;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuentrosFragment extends Fragment  implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private ConfrontationSrv confrontationSrv;
    private EncuentrosRecyclerViewAdapter adapter;
    private List<Confrontation> encuentros;
    private RecyclerView recycleCon;
    private RecyclerView.LayoutManager manager;
    private CompetitionMin competencia;
    private CompetitionSrv competitionSrv;
    private Spinner spinnerJornada, spinnerGrupo, spinnerFase;
    private boolean enableSpinJornada, enableSpinFase, enableSpinGrupo;
    private CompetitionOrg dataOrgCompetition;
    private List<String> itemsJornada, itemsFase, itemsGrupo;
    private String nroJornada, nroGrupo, nroFase;
    private Map<String,String> fecha_grupo;
    private TextView sinEncuentrosTv, competidorLibre, tvTitleGrupo, tvTitleJornada;
    private AdminDataOff adminDataOff;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout barJorGrupo, barFase;
    private AlertDialog alertDialog;

    public EncuentrosFragment() {
        // Required empty public constructor
    }

    public static EncuentrosDetalleFragment newInstance() {
        EncuentrosDetalleFragment fragment = new EncuentrosDetalleFragment();
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
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_encuentros, container, false);
        initElements();

        return vista;
    }

    private void initElements() {
        try {
            swipeRefreshLayout = vista.findViewById(R.id.refreshEncuentro);
            barJorGrupo = vista.findViewById(R.id.bar_jor_gupo);
            barFase = vista.findViewById(R.id.bar_fase);

            fecha_grupo = new HashMap<>();
            sinEncuentrosTv = vista.findViewById(R.id.tv_sinEncuentros);
            sinEncuentrosTv.setVisibility(View.GONE);
            competidorLibre = vista.findViewById(R.id.tv_comp_libre_encuentro);
            competidorLibre.setVisibility(View.GONE);
            confrontationSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);
            competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
            encuentros = new ArrayList<>();
            recycleCon = vista.findViewById(R.id.recyclerEncuentros);
            manager = new LinearLayoutManager(vista.getContext());
            recycleCon.setLayoutManager(manager);
            recycleCon.setHasFixedSize(true);
            adapter = new EncuentrosRecyclerViewAdapter(vista.getContext());
            recycleCon.setAdapter(adapter);
            tvTitleJornada = vista.findViewById(R.id.tv_title_jornada);
            spinnerJornada = vista.findViewById(R.id.spinnerJornada);
            tvTitleGrupo = vista.findViewById(R.id.tv_title_grupo);
            spinnerGrupo = vista.findViewById(R.id.spinnerGrupo);
            spinnerFase = vista.findViewById(R.id.spinnerFase);
            itemsJornada = new ArrayList<>();
            itemsFase = new ArrayList<>();
            itemsGrupo = new ArrayList<>();
            adminDataOff = new AdminDataOff(vista.getContext());
            alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Espere un momento..");

            if(NetworkReceiver.existConnection(vista.getContext())) {
                cargarSpinnerFiltroEncuentros();
                refresh();
            }
            else{
                cargarSpinnerFiltroEncuentrosOffline();
                sinInternet();
                refresh();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    cargarSpinnerFiltroEncuentros();
                } else {
                    cargarSpinnerFiltroEncuentrosOffline();
                    sinInternet();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getEncuentros(Map<String, String> fechaGrupo) {
        Log.d("REQ_ENCUENTROS_BODY", fecha_grupo.toString());
        alertDialog.show();
        Call<ConfrontationsCompetition> call = confrontationSrv.getConfrontations(competencia.getId(), fechaGrupo);
        Log.d("REQ_ENCUENTROS_URL",call.request().url().toString());
        call.enqueue(new Callback<ConfrontationsCompetition>() {
            @Override
            public void onResponse(Call<ConfrontationsCompetition> call, Response<ConfrontationsCompetition> response) {
                if (response.code() == 200) {
                    try {
                        alertDialog.dismiss();
                        Log.d("REQ_ENCUENTROS_RESP", response.body().toString());
                        encuentros.clear();
                        encuentros.addAll(response.body().getEncuentros());
                        mostrarEncuentros();
                        mostrarCompetidorLibre(response.body().getCompetidorLibre());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ConfrontationsCompetition> call, Throwable t) {
                try {
                    alertDialog.dismiss();
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // recupera los datos almacenados en la DB local
    private void getEncuentrosOffline(Map<String, String> fecha_grupo){
        encuentros = adminDataOff.getConfrontationByCompetition(competencia.getId(), competencia.getTypesOrganization(), fecha_grupo);
        Log.d("ENC_LOCAL", "Cant de encuentros almacenados localmente "+encuentros.size()+" de compId: "+competencia.getId());

        mostrarEncuentros();

        String competidorLibre = adminDataOff.competitorFreeByJornada(competencia.getId(), competencia.getTypesOrganization(), fecha_grupo);
        if(fecha_grupo.get("jornada") != null){
            mostrarCompetidorLibre(competidorLibre);
        }
    }

    private void mostrarEncuentros(){
        Log.d("ROL COMPETENCIA", competencia.getRol().toString());

        if((encuentros != null) && (encuentros.size() != 0)){
            conEncuentros();
            try {
                adapter.setEncuentros(encuentros);
                recycleCon.setAdapter(adapter);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sinEncuentros();
        }
    }

    // mostramos el alias del competidor libre si es que lo hay
    private void mostrarCompetidorLibre(String competidor){
        Log.d("SHOW_FREE", "Competidor libre: "+competidor);
        if(competidor == null){
            competidorLibre.setVisibility(View.GONE);
        }
        else{
            Log.d("SHOW_FREE", "Setea VISIBLE el tv: "+competidor);
            competidorLibre.setVisibility(View.VISIBLE);
            competidorLibre.setText("LIBRE: " + competidor);
        }
    }

    // cuando llega la info con los datos de la competencia actualizamos los spiners
    private void cargarSpinnerFiltroEncuentros() {
        //En call viene el tipo de dato que espero del servidor
        alertDialog.show();
        Call<CompetitionOrg> call = competitionSrv.getFaseGrupoCompetition(competencia.getId());
        Log.d("SPIN_ENC_REQ", call.request().url().toString());
        call.enqueue(new Callback<CompetitionOrg>() {
            @Override
            public void onResponse(Call<CompetitionOrg> call, Response<CompetitionOrg> response) {
                alertDialog.dismiss();
                if (response.code() == 200) {
                    try{
                        fecha_grupo.clear();
                        //asigno a deportes lo que traje del servidor
                        dataOrgCompetition = response.body();
                        // actualizamos la vissualizacion de los spiners y sus datos
                        updateDataSpinners();
                        updateViewSpinners();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == 204){
                    sinEncuentros();
                }
                if (response.code() == 400) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RE_ENC_DET", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(vista.getContext(), "Error en la peticion: "+userMessage+" >>", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (response.code() == 500) {
                    Log.d("CREATE_COMP_ERROR", "Problemas en el servidor.");
                    Toast.makeText(vista.getContext(), "Problemas en el servidor ", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CompetitionOrg> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(vista.getContext(), "Problemas con el servidor: intente recargar la pestaña", Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    private void cargarSpinnerFiltroEncuentrosOffline(){
        // controlamos que la competencia se encuentre almacenada localmente
        if(adminDataOff.competitionWtihConfrontation(competencia.getId())){
            int cantGrupos = adminDataOff.groupsByCompetition(competencia.getId());
            int cantJornadas = adminDataOff.jornadaByCompetition(competencia.getId());
            String[] fases = adminDataOff.phasesByCompetition(competencia.getId());

            fecha_grupo.clear();
            //asigno a deportes lo que traje del servidor
            dataOrgCompetition = new CompetitionOrg(cantGrupos,cantJornadas,fases,"Creado desde la DB local");
            updateDataSpinners();
            updateViewSpinners();
        }
        else{
            sinEncuentros();
        }
    }

    private void sinEncuentros() {
        barFase.setVisibility(View.GONE);
        barJorGrupo.setVisibility(View.GONE);
        sinEncuentrosTv.setVisibility(View.VISIBLE);
    }
    private void conEncuentros() {
        sinEncuentrosTv.setVisibility(View.INVISIBLE);
    }


    // actualizamos la barra de busqueda segun el tipo y la fase actual de la competencia
    // llenamos los spinnersen base a la misma info
    private void updateDataSpinners(){
        spinnerJornada.setVisibility(View.GONE);
        spinnerGrupo.setVisibility(View.GONE);
        spinnerFase.setVisibility(View.GONE);
        itemsJornada.clear();
        itemsGrupo.clear();
        itemsFase.clear();
        itemsJornada.add("-");
        itemsFase.add("Seleccione");
        itemsGrupo.add("-");

        if(competencia.getTypesOrganization().contains("Liga")){
            if(dataOrgCompetition.getCantJornadas() != 0){
                enableSpinJornada = true;
                enableSpinFase = true;
                itemsFase.add("Ida");
                int cantJornadas;
                if (competencia.getTypesOrganization().equals(Constants.TIPO_LIGA_DOBLE)) {
                    itemsFase.add("Vuelta");
                    cantJornadas = dataOrgCompetition.getCantJornadas()/2;
                }else{
                    cantJornadas = dataOrgCompetition.getCantJornadas();
                }
                loadItems(cantJornadas, itemsJornada);
            }
        }
        if(competencia.getTypesOrganization().contains("Eliminatoria")){
            if(dataOrgCompetition.getCantFases().length != 0) {
                enableSpinFase = true;
//                Log.d("LOAD_ITEM_ACT", "Cant de fases a cargar: "+dataOrgCompetition.getCantFases().length);
                loadItemsFaseElim(dataOrgCompetition.getCantFases(), itemsFase);

                enableSpinJornada = true;
                itemsJornada.add("Ida");
                if(competencia.getTypesOrganization().contains("Double")){
                    itemsJornada.add("Vuelta");
                }
            }
        }
        if(competencia.getTypesOrganization().equals(Constants.TIPO_GRUPOS)){
            if(dataOrgCompetition.getCantFases().length != 0) {
                enableSpinFase = true;
                loadItemsFaseElim(dataOrgCompetition.getCantFases(), itemsFase);
                loadItems(dataOrgCompetition.getCantJornadas(), itemsJornada);
                loadItems(dataOrgCompetition.getCantGrupos(), itemsGrupo);
                Log.d("LOAD_ITEM_ACT", "Fase actual db local comp: "+competencia.getFaseActual());
                if(competencia.getFaseActual().equals(Constants.FASE_GRUPOS)){
                    enableSpinJornada = true;
                    enableSpinGrupo = true;
                }
            }
        }
    }

    // le agrega un cjto de valores a una lista segun el numero recibido
    private void loadItems(int numbers, List<String> items){
        for (int i = 1; i <= numbers ; i++) {
            items.add(String.valueOf(i));
        }
        return;
    }

    // le agrega un cjto de valores a una lista segun el numero recibido
    private void loadItemsFaseElim(String[] arrayItems, List<String> items){
        if(arrayItems.length == 0){
            return;
        }
        int cantItems;
        // viene ordenado de la DB
        if((arrayItems[arrayItems.length - 1]).equals("0")){
            items.add(Support.spinnerGetFaseElim("0"));
            cantItems = arrayItems.length -1;
        }
        else{
            cantItems = arrayItems.length;
        }
        // agregamos los items
        for (int i = 0; i < cantItems ; i++) {
            items.add(Support.spinnerGetFaseElim(arrayItems[i]));
        }
        return;
    }

    private void loadSpinnerFase(){
        ArrayAdapter<String> adapterFase = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, itemsFase);
        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
        adapterFase.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFase.setAdapter(adapterFase);

        // manejador del evento OnItemSelectedn del spinner de jornada
        spinnerFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // controlamos que no se elija el primer elemento
                if(spinnerFase.getSelectedItemPosition() == 0){
                    nroFase = null;
                    Toast.makeText(vista.getContext(), "Seleccione una fase disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String itemSelected = (String) spinnerFase.getSelectedItem();
                    nroFase = Support.spinnerGetNroFaseElim(itemSelected);
                }
                barJorGrupo.setVisibility(View.VISIBLE);
                if(competencia.getTypesOrganization().contains("Liga")){
                    // vemos si selecciono la vuelta
                    if((nroFase != null) && (nroFase.equals("2"))){
                        if(nroJornada != null) {
                            nroJornada = String.valueOf(Integer.valueOf(nroJornada) + dataOrgCompetition.getCantJornadas() / 2);
                        }
                    }
                    if(nroJornada != null){
                        if((nroFase.equals("1")) && (Integer.valueOf(nroJornada) > dataOrgCompetition.getCantJornadas()/2)){
                            nroJornada = String.valueOf(Integer.valueOf(nroJornada) - dataOrgCompetition.getCantJornadas()/2);
                        }
                    }
                    fecha_grupo.put("jornada", nroJornada);
                    nroFase = null;
                }
                // analizamos en el caso de un GRUPO
                if(competencia.getTypesOrganization().contains("grupo")){
                    if(nroFase.equals("0")){
                        spinnerJornada.setVisibility(View.VISIBLE);
                        tvTitleJornada.setVisibility(View.VISIBLE);
                        spinnerGrupo.setVisibility(View.VISIBLE);
                        tvTitleGrupo.setVisibility(View.VISIBLE);
                        loadSpinnerJornada();
                        loadSpinnerGrupo();
                        fecha_grupo.put("jornada", null);
                        fecha_grupo.put("grupo", null);
                        encuentros.clear();
                    }
                    else{
                        barJorGrupo.setVisibility(View.GONE);
                        spinnerJornada.setVisibility(View.GONE);
                        tvTitleJornada.setVisibility(View.GONE);
                        spinnerGrupo.setVisibility(View.GONE);
                        tvTitleGrupo.setVisibility(View.GONE);
                    }
                }
                fecha_grupo.put("fase", nroFase);
                if(NetworkReceiver.existConnection(vista.getContext())){
                    getEncuentros(fecha_grupo);
                }
                else{
                    getEncuentrosOffline(fecha_grupo);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void loadSpinnerJornada(){
        // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
        ArrayAdapter<String> adapterJornada = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, itemsJornada);
        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
        adapterJornada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJornada.setAdapter(adapterJornada);

        // manejador del evento OnItemSelectedn del spinner de jornada
        spinnerJornada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // controlamos que no se elija el primer elemento
                if(spinnerJornada.getSelectedItemPosition() == 0){
                    nroJornada = null;
                    Toast.makeText(vista.getContext(), "Seleccione una jornada disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    nroJornada = (String) spinnerJornada.getSelectedItem();
                }
                // recuperamos los datos para una liga (jornada 1, 2, 3 ...)
                if(competencia.getTypesOrganization().contains("Liga")){
                    if(competencia.getTypesOrganization().contains("Single")){
                        nroFase = null;
                    }
                    // vemos si selecciono la vuelta
                    if((nroFase != null) && (nroFase.equals("2"))){
                        nroJornada = String.valueOf(Integer.valueOf((String) spinnerJornada.getSelectedItem()) + dataOrgCompetition.getCantJornadas()/2);
                    }
                }
                // recuperamos los datos para una eliminatoria (IDA/VUELTA)
                if(competencia.getTypesOrganization().contains("Eliminatoria")){
                    nroJornada = Support.spinnerGetNroFaseElim((String) spinnerJornada.getSelectedItem());
                }
                fecha_grupo.put("jornada", nroJornada);
                if(NetworkReceiver.existConnection(vista.getContext())){
                    getEncuentros(fecha_grupo);
                }
                else{
                    getEncuentrosOffline(fecha_grupo);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void loadSpinnerGrupo(){
        // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
        ArrayAdapter<String> adapterGrupo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, itemsGrupo);
        adapterGrupo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // seteo los adapter de cada spinner
        spinnerGrupo.setAdapter(adapterGrupo);

        // manejador del evento OnItemSelectedn del spinner de grupo
        spinnerGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // controlamos que no se elija el primer elemento
                if(spinnerGrupo.getSelectedItemPosition() == 0){
                    nroGrupo = null;
                    Toast.makeText(vista.getContext(), "Seleccione un grupo disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    nroGrupo = (String) spinnerGrupo.getSelectedItem();
                }
                fecha_grupo.put("grupo", nroGrupo);
                if(NetworkReceiver.existConnection(vista.getContext())){
                    getEncuentros(fecha_grupo);
                }
                else{
                    getEncuentrosOffline(fecha_grupo);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void updateViewSpinners(){
        if(enableSpinFase){
            spinnerFase.setVisibility(View.VISIBLE);
            loadSpinnerFase();
        }
        else{
            spinnerFase.setVisibility(View.GONE);
        }
        if(enableSpinJornada && !competencia.getTypesOrganization().contains("grupo")){
            spinnerJornada.setVisibility(View.VISIBLE);
            tvTitleJornada.setVisibility(View.VISIBLE);
            loadSpinnerJornada();
        }
        else{
            spinnerJornada.setVisibility(View.GONE);
            tvTitleJornada.setVisibility(View.GONE);
        }
        if(enableSpinGrupo && !competencia.getTypesOrganization().contains("grupo")) {
            spinnerGrupo.setVisibility(View.VISIBLE);
            tvTitleGrupo.setVisibility(View.VISIBLE);
            loadSpinnerGrupo();
        }
        else{
            spinnerGrupo.setVisibility(View.INVISIBLE);
            tvTitleGrupo.setVisibility(View.INVISIBLE);
        }

        // mostramos la 2da barra de spinner solo cuando hay seleccionado una fase
        if(competencia.getTypesOrganization().contains("grupo")){
            barJorGrupo.setVisibility(View.GONE);
        }

        return;
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

    @Override
    public void sinInternet() {
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet", Toast.LENGTH_LONG);
        toast.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
    }
}
