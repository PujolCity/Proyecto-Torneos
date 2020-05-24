package com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.PosicionesRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionOrg;
import com.VeizagaTorrico.proyectotorneos.models.PositionCompetitor;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.PositionCompetitorSrv;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PosicionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;

    private PositionCompetitorSrv positionSrv;

    private CompetitionMin competition;
    private List<PositionCompetitor> posiciones;
    private CompetitionSrv competitionSrv;
    private Spinner spin_grupo, spin_jornada;
    private Integer nroGrupo;
    private LinearLayout barSpinners, linnTitlePoscion;
    private TextView tvSinTabla;
    private TextView tvPtsEmpatados, tvTitleJornada;
    private RecyclerView recyclePosicion;
    private PosicionesRecyclerViewAdapter adapterPosicion;

    public PosicionesFragment() {
        // Required empty public constructor
    }

    public static PosicionesFragment newInstance() {
        PosicionesFragment fragment = new PosicionesFragment();
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
        vista = inflater.inflate(R.layout.fragment_tabla_posiciones, container, false);

        initElements();
        showTablePosotion();
        return vista;
    }

    private void initElements() {
        spin_grupo = vista.findViewById(R.id.spinnerGrupo);
        // ocultamos los spinners que no necesitamos
        spin_jornada = vista.findViewById(R.id.spinnerJornada);
        tvTitleJornada = vista.findViewById(R.id.tv_title_jornada);
        spin_jornada.setVisibility(View.GONE);
        barSpinners = vista.findViewById(R.id.barGroup);
        tvSinTabla = vista.findViewById(R.id.tv_sinTabla);
        tvPtsEmpatados = vista.findViewById(R.id.tv_title_pe_posiciones);
        linnTitlePoscion = vista.findViewById(R.id.linn_title_tabla);
        recyclePosicion = vista.findViewById(R.id.posicionesList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(vista.getContext());
        recyclePosicion.setLayoutManager(manager);
        recyclePosicion.setHasFixedSize(true);
        adapterPosicion = new PosicionesRecyclerViewAdapter(vista.getContext());
        recyclePosicion.setAdapter(adapterPosicion);

        positionSrv = new RetrofitAdapter().connectionEnable().create(PositionCompetitorSrv.class);
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
    }

    private void showTablePosotion(){
        // vemos si es eliminatoria
        if(competition.getTypesOrganization().contains("Eliminatoria")){
            linnTitlePoscion.setVisibility(View.GONE);
            barSpinners.setVisibility(View.GONE);
            tvSinTabla.setVisibility(View.VISIBLE);
            recyclePosicion.setVisibility(View.GONE);
        }
        else{
            recyclePosicion.setVisibility(View.VISIBLE);
            tvSinTabla.setVisibility(View.GONE);
            // analizamos el tipo de competencia
            if(competition.getTypesOrganization().contains("Liga")){
                linnTitlePoscion.setVisibility(View.VISIBLE);
                barSpinners.setVisibility(View.GONE);
                callTablePosition(competition.getId());
            }
            if(competition.getTypesOrganization().contains("grupo")){
                spin_jornada.setVisibility(View.GONE);
                tvTitleJornada.setVisibility(View.GONE);
                barSpinners.setVisibility(View.VISIBLE);
                cargarSpinnerGrupo(competition.getId());
            }
        }
    }

    // recupera la tabla de posiciones de una competencia del tipo LIGA
    private void callTablePosition(int idCompetencia){
        Call<List<PositionCompetitor>> call = positionSrv.getTablePositions(idCompetencia);

        try{
            call.enqueue(new Callback<List<PositionCompetitor>>() {
                @Override
                public void onResponse(Call<List<PositionCompetitor>> call, Response<List<PositionCompetitor>> response) {
                    if(response.code() == 200){
                        posiciones = response.body();
                        // si recibimos los resultados de las posiciones de los competidores
                        if(posiciones != null){
                            adapterPosicion.setPosiciones(posiciones);
                            recyclePosicion.setAdapter(adapterPosicion);
                            if(posiciones.get(0).getEmpatados() == null){
                                tvPtsEmpatados.setVisibility(View.GONE);
                            }
                        }
                        else{
                            tvSinTabla.setVisibility(View.VISIBLE);
                            Toast.makeText(vista.getContext(), "Aun no se han disputados encuentros de la competencia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (response.code() == 400) {
//                        Log.d("RESP_TABLE_ERROR", "No hay tabla: " + response.errorBody());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("msg");
                            Toast.makeText(vista.getContext(), "No se puede mostrar la tabla:  << " + message + " >>", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }

                @Override
                public void onFailure(Call<List<PositionCompetitor>> call, Throwable t) {
                    Log.d("On failure", t.getMessage());
                    Toast.makeText(vista.getContext(), "No se pudieron recuperar las posiciones de la competencia", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // recupera la tabla de posiciones de una competencia por grupo, para el tipo FASE_DE_GRUPOS
    private void callTablePositionByGroup(int idCompetencia, int grupo){
        Call<List<PositionCompetitor>> call = positionSrv.getTablePositionsByGroup(idCompetencia, grupo);
        try{
            call.enqueue(new Callback<List<PositionCompetitor>>() {
                @Override
                public void onResponse(Call<List<PositionCompetitor>> call, Response<List<PositionCompetitor>> response) {
                    if(response.code() == 200){
                        posiciones = response.body();
                        // si recibimos los resultados de las posiciones de los competidores
                        if(posiciones != null){
                            // sino es definitavamente una liga
                            adapterPosicion.setPosiciones(posiciones);
                            recyclePosicion.setAdapter(adapterPosicion);
                        }
                        else{
                            Toast.makeText(vista.getContext(), "Aun no se han disputados encuentros de la competencia", Toast.LENGTH_SHORT).show();
                            tvPtsEmpatados.setVisibility(View.VISIBLE);
                        }
                    }

                    if (response.code() == 400) {
//                        Log.d("RESP_TABLE_ERROR", "No hay tabla: " + response.errorBody());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("msg");
                            Toast.makeText(vista.getContext(), "No se puede mostrar la tabla:  << " + message + " >>", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }

                @Override
                public void onFailure(Call<List<PositionCompetitor>> call, Throwable t) {
                    Log.d("On failure", t.getMessage());
                    Toast toast = Toast.makeText(vista.getContext(), "No se pudieron recuperar las posiciones de la competencia con el grupo ingresado", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // cargamos los datos del spinner de grupo en las competencias que lo demanden
    private void cargarSpinnerGrupo(int idCompetencia) {
        //En call viene el tipo de dato que espero del servidor
        Call<CompetitionOrg> call = competitionSrv.getFaseGrupoCompetition(idCompetencia);
        //Log.d("Request Encuentros", call.request().url().toString());
        call.enqueue(new Callback<CompetitionOrg>() {
            @Override
            public void onResponse(Call<CompetitionOrg> call, Response<CompetitionOrg> response) {
                CompetitionOrg datosSpinner;
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        datosSpinner = response.body();
                        Log.d("datosSpinner",response.body().toString());

                        // creamos la lista para el spinner de grupo
                        List<String> grupos = getItemGrupos(datosSpinner.getCantGrupos());
                        // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
                        ArrayAdapter<String> adapterGrupo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, grupos);
                        adapterGrupo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // seteo los adapter de cada spinner
                        spin_grupo.setAdapter(adapterGrupo);

                        // manejador del evento OnItemSelectedn del spinner de grupo
                        spin_grupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    // controlamos que no se elija el primer elemento
                                    if(spin_grupo.getSelectedItemPosition() == 0){
                                        nroGrupo = null;
                                    }
                                    else{
                                        nroGrupo = Integer.valueOf((String)spin_grupo.getSelectedItem());
                                        callTablePositionByGroup(competition.getId(), nroGrupo);
                                    }

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });

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

    // creamos la lista de opciones del spinner
    private List<String> getItemGrupos(int nroGrupos){
        List<String> itemGrupos = new ArrayList<>();
        itemGrupos.add("seleccione.. ");
        for (int i = 1; i <= nroGrupos ; i++) {
                itemGrupos.add(String.valueOf(i));
        }
        return itemGrupos;
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

    public void setCompetencia(CompetitionMin competition) {
        this.competition = competition;
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
