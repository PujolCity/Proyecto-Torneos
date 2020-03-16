package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

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

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.EncuentrosRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionOrg;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerCompetitionOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuentrosDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private ConfrontationSrv confrontationSrv;
    private List<Confrontation> encuentros;
    private EncuentrosRecyclerViewAdapter adapter;
    private RecyclerView recycleCon;
    private RecyclerView.LayoutManager manager;
    private CompetitionSrv competitionSrv;
    private CompetitionMin competencia;
    private Spinner spinnerJornada;
    private Spinner spinnerGrupo;
    private String nroJornada;
    private String nroGrupo;
    private ImageButton btnBuscar;
    private Map<String,String> fecha_grupo;
    private TextView sinEncuentrosTv;
    private ManagerConfrontationOff adminEncuentrosLocal;
    private ManagerCompetitionOff adminCopeteitionLocal;

    public EncuentrosDetalleFragment() {
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
            btnBuscar = vista.findViewById(R.id.btnBuscar);
            getEncuentros(fecha_grupo);

            if(NetworkReceiver.existConnection(vista.getContext())) {
                cargarSpinnerFiltroEncuentros(competencia.getId());
            }
            else{
                cargarSpinnerFiltroEncuentrosOffline();
            }
            if(NetworkReceiver.existConnection(vista.getContext())){
                getEncuentros(fecha_grupo);
            }
            else{
                getEncuentrosOffline(fecha_grupo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEncuentros(Map<String, String> fechaGrupo) {
        Log.d("ENCUENTROS_FG body", fecha_grupo.toString());

        Call<List<Confrontation>> call = confrontationSrv.getConfrontations(competencia.getId(), fechaGrupo);
        Log.d("call competencia FG",call.request().url().toString());
        call.enqueue(new Callback<List<Confrontation>>() {
            @Override
            public void onResponse(Call<List<Confrontation>> call, Response<List<Confrontation>> response) {
                if (response.code() == 200) {
                    try {
                        Log.d("ENCUENTROS_FG response", Integer.toString(response.code()));
                        encuentros = response.body();

                        Log.d("ENCUENTROS_FG response", response.body().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mostrarEncuentros();
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
    private void getEncuentrosOffline(Map<String, String> fecha_grupo){
        adminEncuentrosLocal = new ManagerConfrontationOff(vista.getContext());
        encuentros = adminEncuentrosLocal.confrontationByCompetition(competencia.getId(), fecha_grupo);
        Log.d("ENC_LOCAL", "Cant de encuentros almacenados localmente "+encuentros.size()+" de compId: "+competencia.getId());

        mostrarEncuentros();
    }


    private void mostrarEncuentros(){
        Log.d("ROL COMPETENCIA", competencia.getRol().toString());

        if((encuentros != null) && (encuentros.size() != 0)){
            try {
                adapter.setEncuentros(encuentros);
                recycleCon.setAdapter(adapter);

                if((competencia.getRol().contains("ORGANIZADOR")) || (competencia.getRol().contains("CO-ORGANIZADOR") && NetworkReceiver.existConnection(vista.getContext()))){
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
                        getEncuentrosOffline(fecha_grupo);
                    }
                    else{
                        nroGrupo = (String) spinnerGrupo.getSelectedItem();
                        fecha_grupo.put("grupo", nroGrupo);
                        getEncuentrosOffline(fecha_grupo);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    private void sinEncuentros() {
        recycleCon.setVisibility(View.INVISIBLE);
        sinEncuentrosTv.setVisibility(View.VISIBLE);
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

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
    }

    private void cargarSpinnerFiltroEncuentros(int idCompetencia) {
        //En call viene el tipo de dato que espero del servidor
        Call<CompetitionOrg> call = competitionSrv.getFaseGrupoCompetition(idCompetencia);
        Log.d("Request Encuentros", call.request().url().toString());
        call.enqueue(new Callback<CompetitionOrg>() {
            @Override
            public void onResponse(Call<CompetitionOrg> call, Response<CompetitionOrg> response) {
                CompetitionOrg datosSpinner;
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        fecha_grupo.clear();
                        Log.d("RESPONSE FILTER CODE",  Integer.toString(response.code()));
                        //asigno a deportes lo que traje del servidor
                        datosSpinner = response.body();
                        Log.d("datosSpinner",response.body().toString());
                        if(contieneJornadas(datosSpinner.getN_jornadas())){
                            //List<Integer> jornadas = getAllIntegerRange(1 , datosSpinner.getN_jornadas());
                            List<String> jornadas = getItemJornadas(datosSpinner.getN_jornadas());
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
                                        getEncuentros(fecha_grupo);
                                    }
                                    else{
                                        nroJornada = (String) spinnerJornada.getSelectedItem();
                                        fecha_grupo.put("fase", nroJornada);
                                        getEncuentros(fecha_grupo);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        }

                        if(contieneGrupos(datosSpinner.getN_grupos(), datosSpinner.getN_jornadas())){
                            // List<Integer> grupos = getAllIntegerRange(1 , datosSpinner.getN_grupos());
                            List<String> grupos = getItemGrupos(datosSpinner.getN_grupos());
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
                                        getEncuentros(fecha_grupo);
                                    }
                                    else{
                                        nroGrupo = (String) spinnerGrupo.getSelectedItem();
                                        fecha_grupo.put("grupo", nroGrupo);
                                        getEncuentros(fecha_grupo);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
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
        if(nroJornadas == 0){
            spinnerJornada.setEnabled(false);
            Toast.makeText(getContext(), "La competencia aun no contiene jornadas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean contieneGrupos(int nroGrupos, int nroJornada) {
        if ((nroGrupos == 0) || (nroJornada == 0)) {
            spinnerGrupo.setEnabled(false);
            Toast.makeText(getContext(), "La competencia aun no contiene grupos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
