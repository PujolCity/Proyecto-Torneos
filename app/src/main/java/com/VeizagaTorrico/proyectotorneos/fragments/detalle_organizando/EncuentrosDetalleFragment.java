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
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.EncuentrosRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionOrg;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.ConfrontationFull;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuentrosDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private ConfrontationSrv confrontationSrv;
    private EncuentrosRecyclerViewAdapter adapter;
    private List<ConfrontationFull> encuentros;
    private RecyclerView recycleCon;
    private RecyclerView.LayoutManager manager;
    private CompetitionMin competencia;
    private Spinner spinnerJornada;
    private Spinner spinnerGrupo;
    private CompetitionSrv competitionSrv;
    private String nroJornada;
    private String nroGrupo;
    private ImageButton btnBuscar;
    private Map<String,String> fecha_grupo;

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
        //inflarRecycler();
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nroJornada != null){
                    fecha_grupo.put("fase", nroJornada);
                }
                if(nroGrupo != null){
                    fecha_grupo.put("grupo", nroGrupo);
                }
                getEncuentros(fecha_grupo);
            }
        });

        return vista;
    }

//    private void inflarRecycler() {
//        Call<List<Confrontation>> call = confrontationSrv.getConfrontation(competencia.getId());
//        Log.d("call competencia",call.request().url().toString());
//        call.enqueue(new Callback<List<Confrontation>>() {
//            @Override
//            public void onResponse(Call<List<Confrontation>> call, Response<List<Confrontation>> response) {
//                if(response.code() == 200){
//                    try {
//                        Log.d("ENCUENTROS response", Integer.toString(response.code()));
//                        encuentros = response.body();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(encuentros != null){
//                    try {
//                        adapter.setEncuentros(encuentros);
//                        recycleCon.setAdapter(adapter);
//                        adapter.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Confrontation encuentro = encuentros.get(recycleCon.getChildAdapterPosition(view));
//                                Bundle bundle = new Bundle();
//                                encuentro.setIdCompetencia(competencia.getId());
//                                bundle.putSerializable("encuentro", encuentro);
//                                Navigation.findNavController(vista).navigate(R. id.detalleEncuentroFragment, bundle);
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Confrontation>> call, Throwable t) {
//                try {
//                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
//                    toast.show();
//                    Log.d("onFailure", t.getMessage());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//       }

    private void initElements() {
        fecha_grupo = new HashMap<>();

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
        cargarSpinnerFiltroEncuentros(competencia.getId());
        getEncuentros(fecha_grupo);
    }

    private void getEncuentros(Map<String, String> fechaGrupo) {
        Call<List<ConfrontationFull>> call = confrontationSrv.getConfrontations(competencia.getId(), fechaGrupo);
        Log.d("call competencia FG",call.request().url().toString());
        call.enqueue(new Callback<List<ConfrontationFull>>() {
            @Override
            public void onResponse(Call<List<ConfrontationFull>> call, Response<List<ConfrontationFull>> response) {
                if(response.code() == 200){
                    try {
                        Log.d("ENCUENTROS_FG response", response.body().get(0).toString());
                        encuentros = response.body();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(encuentros != null){
                    try {
                        adapter.setEncuentros(encuentros);
                        recycleCon.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ConfrontationFull encuentro = encuentros.get(recycleCon.getChildAdapterPosition(view));
                                Bundle bundle = new Bundle();
                                encuentro.setIdCompetencia(competencia.getId());
                                bundle.putSerializable("encuentro", encuentro);
                                Navigation.findNavController(vista).navigate(R. id.detalleEncuentroFragment, bundle);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ConfrontationFull>> call, Throwable t) {
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
                                    }
                                    else{
                                        nroJornada = (String) spinnerJornada.getSelectedItem();
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
                                    }
                                    else{
                                        nroGrupo = (String) spinnerGrupo.getSelectedItem();
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
        itemJornadas.add("Jornada nro");
        if(contieneJornadas(nroJornadas)){
            for (int i = 1; i <= nroJornadas ; i++) {
                itemJornadas.add(String.valueOf(i));
            }
        }
         return itemJornadas;
    }

    private List<String> getItemGrupos(int nroGrupos){
        List<String> itemGrupos = new ArrayList<>();
        itemGrupos.add("Grupo nro");
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

    // devuelve una lista de Integer desde el cero hasta el nro recibido
//    private List<Integer> getAllIntegerRange(int begin, int end) {
//        List<Integer> values = new ArrayList<>();
//
//        for (int i = begin; i <= end; i++) {
//            values.add(i);
//        }
//
//        return values;
//    }
}