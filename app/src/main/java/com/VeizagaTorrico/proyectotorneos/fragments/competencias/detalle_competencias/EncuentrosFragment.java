package com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import android.widget.ImageButton;
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
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerCompetitionOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuentrosFragment extends Fragment {

    private EncuentrosDetalleFragment.OnFragmentInteractionListener mListener;

    private View vista;
    private ConfrontationSrv confrontationSrv;
    private EncuentrosRecyclerViewAdapter adapter;
    private List<Confrontation> encuentros;
    private RecyclerView recycleCon;
    private RecyclerView.LayoutManager manager;
    private CompetitionMin competencia;
//    private Spinner spinnerJornada;
//    private Spinner spinnerGrupo;
    private CompetitionSrv competitionSrv;
//    private String nroJornada;
//    private String nroGrupo;
    private Spinner spinnerJornada, spinnerGrupo, spinnerFase;
    private boolean enableSpinJornada, enableSpinFase, enableSpinGrupo;
    private CompetitionOrg dataOrgCompetition;
    private List<String> itemsJornada, itemsFase, itemsGrupo;
    private String nroJornada, nroGrupo, nroFase;
    private ImageButton btnBuscar;
    private Map<String,String> fecha_grupo;
    private TextView sinEncuentrosTv;

    private ManagerConfrontationOff adminEncuentrosLocal;
    private ManagerCompetitionOff adminCopeteitionLocal;

    public EncuentrosFragment() {
        // Required empty public constructor
    }

    public static EncuentrosFragment newInstance() {
        EncuentrosFragment fragment = new EncuentrosFragment();
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
        vista = inflater.inflate(R.layout.fragment_encuentros_detalle, container, false);
        initElements();

        return vista;
    }


    private void initElements() {
        try {
            fecha_grupo = new HashMap<>();
            sinEncuentrosTv = vista.findViewById(R.id.tv_sinEncuentros);
            confrontationSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);
            competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
            encuentros = new ArrayList<>();
            recycleCon = vista.findViewById(R.id.recyclerEncuentrosDetalle);
            manager = new LinearLayoutManager(vista.getContext());
            recycleCon.setLayoutManager(manager);
            recycleCon.setHasFixedSize(true);
            adapter = new EncuentrosRecyclerViewAdapter(vista.getContext());
            recycleCon.setAdapter(adapter);
            spinnerJornada = vista.findViewById(R.id.spinnerJornada);
            spinnerGrupo = vista.findViewById(R.id.spinnerGrupo);
            spinnerFase = vista.findViewById(R.id.spinnerFase);
            itemsJornada = new ArrayList<>();
            itemsFase = new ArrayList<>();
            itemsGrupo = new ArrayList<>();
            btnBuscar = vista.findViewById(R.id.btnBuscar);

            if(NetworkReceiver.existConnection(vista.getContext())) {
                cargarSpinnerFiltroEncuentros();
            }
            else{
                cargarSpinnerFiltroEncuentrosOffline();
            }
//            if(NetworkReceiver.existConnection(vista.getContext())){
//                getEncuentros(fecha_grupo);
//            }
//            else{
//                getEncuentrosOffline(fecha_grupo);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEncuentros(Map<String, String> fechaGrupo) {
        Log.d("REQ_ENCUENTROS_BODY", fecha_grupo.toString());

        Call<List<Confrontation>> call = confrontationSrv.getConfrontations(competencia.getId(), fechaGrupo);
        Log.d("REQ_ENCUENTROS_URL",call.request().url().toString());
        call.enqueue(new Callback<List<Confrontation>>() {
            @Override
            public void onResponse(Call<List<Confrontation>> call, Response<List<Confrontation>> response) {
                if(response.code() == 200){
                    try {
                        Log.d("REQ_ENCUENTROS_RESP", Integer.toString(response.code()));
                        encuentros = response.body();
                        mostrarEncuentros();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<List<Confrontation>> call, Throwable t) {
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

    // recupera los datos almacenados en la DB local
    private void getEncuentrosOffline(Map<String, String> fechaGrupo){
        adminEncuentrosLocal = new ManagerConfrontationOff(vista.getContext());
        encuentros = adminEncuentrosLocal.confrontationByCompetition(competencia.getId(), fecha_grupo);
        Log.d("ENC_LOCAL", "Cant de encuentros almacenados localmente "+encuentros.size()+" de compId: "+competencia.getId());

        mostrarEncuentros();
    }

    private void mostrarEncuentros(){
        if(encuentros.size() != 0 ){
            try {
                adapter.setEncuentros(encuentros);
                recycleCon.setAdapter(adapter);
                if(competencia.getRol().contains("ORGANIZADOR")){
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Confrontation encuentro = encuentros.get(recycleCon.getChildAdapterPosition(view));
                            Bundle bundle = new Bundle();
                            encuentro.setIdCompetencia(competencia.getId());
                            bundle.putSerializable("encuentro", encuentro);
                            Navigation.findNavController(vista).navigate(R. id.detalleEncuentroFragment, bundle);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sinEncuentros();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EncuentrosDetalleFragment.OnFragmentInteractionListener) {
            mListener = (EncuentrosDetalleFragment.OnFragmentInteractionListener) context;
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

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
    }

//    private void cargarSpinnerFiltroEncuentros() {
//        //En call viene el tipo de dato que espero del servidor
//        Call<CompetitionOrg> call = competitionSrv.getFaseGrupoCompetition(competencia.getId());
//        Log.d("Request Encuentros", call.request().url().toString());
//        call.enqueue(new Callback<CompetitionOrg>() {
//            @Override
//            public void onResponse(Call<CompetitionOrg> call, Response<CompetitionOrg> response) {
//                CompetitionOrg datosSpinner;
//                //codigo 200 si salio tdo bien
//                if (response.code() == 200) {
//                    try{
//                        fecha_grupo.clear();
//                        Log.d("RESPONSE FILTER CODE",  Integer.toString(response.code()));
//                        //asigno a deportes lo que traje del servidor
//                        datosSpinner = response.body();
//                        Log.d("datosSpinner",response.body().toString());
//                        if(contieneJornadas(datosSpinner.getCantJornadas())){
//                            //List<Integer> jornadas = getAllIntegerRange(1 , datosSpinner.getN_jornadas());
//                            List<String> jornadas = getItemJornadas(datosSpinner.getCantJornadas());
//                            // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
//                            ArrayAdapter<String> adapterJornada = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, jornadas);
//                            //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
//                            adapterJornada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinnerJornada.setAdapter(adapterJornada);
//
//                            // manejador del evento OnItemSelectedn del spinner de jornada
//                            spinnerJornada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                    // controlamos que no se elija el primer elemento
//                                    if(spinnerJornada.getSelectedItemPosition() == 0){
//                                        nroJornada = null;
//                                        fecha_grupo.put("fase", nroJornada);
//                                        getEncuentros(fecha_grupo);
//                                    }
//                                    else{
//                                        nroJornada = (String) spinnerJornada.getSelectedItem();
//                                        fecha_grupo.put("fase", nroJornada);
//                                        getEncuentros(fecha_grupo);
//                                    }
//                                }
//                                @Override
//                                public void onNothingSelected(AdapterView<?> adapterView) {
//                                }
//                            });
//                        }
//
//                        if(contieneGrupos(datosSpinner.getCantGrupos(), datosSpinner.getCantJornadas())){
//                            // List<Integer> grupos = getAllIntegerRange(1 , datosSpinner.getN_grupos());
//                            List<String> grupos = getItemGrupos(datosSpinner.getCantGrupos());
//                            // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
//                            ArrayAdapter<String> adapterGrupo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, grupos);
//                            adapterGrupo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            // seteo los adapter de cada spinner
//                            spinnerGrupo.setAdapter(adapterGrupo);
//
//                            // manejador del evento OnItemSelectedn del spinner de grupo
//                            spinnerGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                    // controlamos que no se elija el primer elemento
//                                    if(spinnerGrupo.getSelectedItemPosition() == 0){
//                                        nroGrupo = null;
//                                        fecha_grupo.put("grupo", nroGrupo);
//                                        getEncuentros(fecha_grupo);
//                                    }
//                                    else{
//                                        nroGrupo = (String) spinnerGrupo.getSelectedItem();
//                                        fecha_grupo.put("grupo", nroGrupo);
//                                        getEncuentros(fecha_grupo);
//                                    }
//                                }
//                                @Override
//                                public void onNothingSelected(AdapterView<?> adapterView) {
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<CompetitionOrg> call, Throwable t) {
//                try {
//                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
//                    toast.show();
//                    Log.d("onFailure", t.getMessage());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void cargarSpinnerFiltroEncuentrosOffline(){
        // controlamos que la competencia se encuentre almacenada localmente
        adminCopeteitionLocal = new ManagerCompetitionOff(vista.getContext());
        // recuperamos el nro de grupo y fase de la competencia
        int cantGrupos = adminCopeteitionLocal.cantGroupByCompetition(competencia.getId());
        int cantJornadas = adminCopeteitionLocal.cantJornadaByCompetition(competencia.getId());

        if(contieneJornadas(cantJornadas)){
            List<String> jornadas = getItemJornadas(cantJornadas);
            // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
            ArrayAdapter<String> adapterJornada = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, jornadas);
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
                        fecha_grupo.put("fase", nroJornada);
                        getEncuentrosOffline(fecha_grupo);
                    }
                    else{
                        nroJornada = (String) spinnerJornada.getSelectedItem();
                        fecha_grupo.put("fase", nroJornada);
                        getEncuentrosOffline(fecha_grupo);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }

        if(contieneGrupos(cantGrupos, cantJornadas)){
            List<String> grupos = getItemGrupos(cantGrupos);
            // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
            ArrayAdapter<String> adapterGrupo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, grupos);
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
                        fecha_grupo.put("grupo", nroGrupo);
                        //getEncuentros(fecha_grupo);
                    }
                    else{
                        nroGrupo = (String) spinnerGrupo.getSelectedItem();
                        fecha_grupo.put("grupo", nroGrupo);
                        //getEncuentros(fecha_grupo);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    private  List<String> getItemJornadas(int nroJornadas){
        List<String> itemJornadas = new ArrayList<>();
        itemJornadas.add("Jornada");
        if(contieneJornadas(nroJornadas)){
            for (int i = 1; i <= nroJornadas ; i++) {
                itemJornadas.add(String.valueOf(i));
            }
        }
        return itemJornadas;
    }

    private List<String> getItemGrupos(int nroGrupos){
        List<String> itemGrupos = new ArrayList<>();
        itemGrupos.add("Grupo ");
        if(contieneJornadas(nroGrupos)){
            for (int i = 1; i <= nroGrupos ; i++) {
                itemGrupos.add(String.valueOf(i));
            }
        }
        return itemGrupos;
    }

    private boolean contieneJornadas(int nroJornadas) {
        if(nroJornadas <= 0){
            spinnerJornada.setEnabled(false);
            Toast.makeText(getContext(), "La competencia aun no contiene jornadas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean contieneGrupos(int nroGrupos, int nroJornada) {
        if ((nroGrupos <= 0) || (nroJornada <= 0)) {
            spinnerGrupo.setEnabled(false);
            Toast.makeText(getContext(), "La competencia aun no contiene grupos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sinEncuentros() {
        recycleCon.setVisibility(View.INVISIBLE);
        sinEncuentrosTv.setVisibility(View.VISIBLE);
    }

    // ###########################################################################################
    // ####################################### EL REFACTOR #######################################

    // actualizamos la barra de busqueda segun el tipo y la fase actual de la competencia
    // llenamos los spinnersen base a la misma info
    private void updateDataSpinners(){
        spinnerJornada.setVisibility(View.GONE);
        spinnerGrupo.setVisibility(View.GONE);
        spinnerFase.setVisibility(View.GONE);
        itemsJornada.clear();
        itemsGrupo.clear();
        itemsFase.clear();
        itemsJornada.add("Jornada");
        itemsFase.add("Fase");
        itemsGrupo.add("Grupo");

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
                loadItemsFaseElim(dataOrgCompetition.getCantFases(), itemsFase);

                enableSpinJornada = true;
                itemsJornada.add("Ida");
                Log.d("LOAD_ITEM_JORN", "tipo: "+competencia.getTypesOrganization());
                if(competencia.getTypesOrganization().contains(Constants.TIPO_ELIMINATORIAS_DOBLE)){
                    itemsJornada.add("Vuelta");
                }
            }
        }
        if(competencia.getTypesOrganization().equals(Constants.TIPO_GRUPOS)){
            if(dataOrgCompetition.getCantFases().length != 0) {
                enableSpinFase = true;
                loadItemsFaseElim(dataOrgCompetition.getCantFases(), itemsFase);
                if(competencia.getFaseActual().equals(Constants.FASE_GRUPOS)){
                    enableSpinJornada = true;
                    loadItems(dataOrgCompetition.getCantJornadas(), itemsJornada);
                    enableSpinGrupo = true;
                    loadItems(dataOrgCompetition.getCantGrupos(), itemsGrupo);
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
//        Log.d("LOAD_ITEM_ELEM", "Entro a cagar: "+arrayItems.length);
        if(arrayItems.length == 0){
            return;
        }
        int cantItems;
        // viene ordenado de la DB
        if((arrayItems[arrayItems.length - 1]).equals("0")){
            items.add(getFaseElim("0"));
            cantItems = arrayItems.length -1;
//            Log.d("LOAD_ITEM_0", "Encontro el cero ");
        }
        else{
            cantItems = arrayItems.length;
        }
//        Log.d("LOAD_ITEM_CANT", "Cant de items fase:  "+cantItems);
        // agregamos los items
        for (int i = 0; i < cantItems ; i++) {
            items.add(getFaseElim(arrayItems[i]));
        }
        return;
    }

    // cuando llega la info con los datos de la competencia actualizamos los spiners
    private void cargarSpinnerFiltroEncuentros() {
        //En call viene el tipo de dato que espero del servidor
        Call<CompetitionOrg> call = competitionSrv.getFaseGrupoCompetition(competencia.getId());
//        Log.d("SPIN_ENC_REQ", call.request().url().toString());
        call.enqueue(new Callback<CompetitionOrg>() {
            @Override
            public void onResponse(Call<CompetitionOrg> call, Response<CompetitionOrg> response) {
                Log.d("SPIN_ENC_RESP", response.body().toString());
                if (response.code() == 200) {
                    try{
                        fecha_grupo.clear();
                        //asigno a deportes lo que traje del servidor
                        dataOrgCompetition = response.body();
                        // actualizamos la vissualizacion de los spiners y sus datos
                        updateDataSpinners();
                        if(enableSpinFase){
                            spinnerFase.setVisibility(View.VISIBLE);
                            loadSpinnerFase();
                        }
                        else{
                            spinnerFase.setVisibility(View.GONE);
                        }
                        if(enableSpinJornada){
                            spinnerJornada.setVisibility(View.VISIBLE);
                            loadSpinnerJornada();
                        }
                        else{
                            spinnerJornada.setVisibility(View.GONE);
                        }
                        if(enableSpinGrupo) {
                            spinnerGrupo.setVisibility(View.VISIBLE);
                            loadSpinnerGrupo();
                        }
                        else{
                            spinnerGrupo.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<CompetitionOrg> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onFailure", t.getMessage());
            }
        });
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
                    nroFase = getNroFaseElim(itemSelected);
                }
                if(competencia.getTypesOrganization().contains("Liga")){
                    // vemos si selecciono la vuel
                    if((nroFase != null) && (nroFase.equals("2"))){
                        nroJornada = String.valueOf(Integer.valueOf(nroJornada) + dataOrgCompetition.getCantJornadas()/2);
                    }
                }
                fecha_grupo.put("fase", nroFase);
                getEncuentros(fecha_grupo);
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
                    fecha_grupo.put("jornada", nroJornada);
                    // vemos si selecciono la vuelta
                    if((nroFase != null) && (nroFase.equals("2"))){
                        nroJornada = String.valueOf(Integer.valueOf((String) spinnerJornada.getSelectedItem()) + dataOrgCompetition.getCantJornadas()/2);
                    }
                }
                // recuperamos los datos para una eliminatoria (IDA/VUELTA)
                if(competencia.getTypesOrganization().contains("Eliminatoria")){
                    nroJornada = getNroFaseElim((String) spinnerJornada.getSelectedItem());
                    fecha_grupo.put("jornada", nroJornada);
                }
                getEncuentros(fecha_grupo);
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
                getEncuentros(fecha_grupo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private String getFaseElim(String f){
        String fase = null;
        if(f == "0"){
            fase = "Grupos";
        }
        if(f == "1"){
            fase = "Final";
        }
        if(f == "2"){
            fase = "Semi";
        }
        if(f == "3"){
            fase = "4º final";
        }
        if(f == "4"){
            fase = "8º final";
        }
        if(f == "5"){
            fase = "16º final";
        }
        if(f == "6"){
            fase = "32º final";
        }

        return fase;
    }

    private String getNroFaseElim(String f){
        Log.d("SPIN_SEL_FASE", "Opcion elegida: "+f);
        String fase = null;
        if(f == "Grupos"){
            fase = "0";
        }
        if((f == "Final") || f == "Ida"){
            fase = "1";
        }
        if((f == "Semi") || (f == "Vuelta")){
            fase = "2";
        }
        if(f == "4º final"){
            fase = "3";
        }
        if(f == "8º final"){
            fase = "4";
        }
        if(f == "16º final"){
            fase = "5";
        }
        if(f == "32º final"){
            fase = "6";
        }
        Log.d("SPIN_SEL_FASE", "Opcion elegida nro: "+fase);

        return fase;
    }
}
