package com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionOrg;
import com.VeizagaTorrico.proyectotorneos.models.PositionCompetitor;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.PositionCompetitorSrv;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PosicionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;

    private PositionCompetitorSrv positionSrv;

    private TableLayout tablaPosiciones;
    private CompetitionMin competition;
    private List<PositionCompetitor> posiciones;
    private CompetitionSrv competitionSrv;
    private Spinner spin_grupo, spin_jornada, spin_fase;
    private Integer nroGrupo;
    private ConstraintLayout barSpinners;
    private TextView tvSinTabla;

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
        vista = inflater.inflate(R.layout.fragment_posiciones, container, false);

        initElements();
        showTablePosotion();
        return vista;
    }

    private void initElements() {
        tablaPosiciones = vista.findViewById(R.id.TablaPosiciones);
        spin_grupo = vista.findViewById(R.id.spinnerGrupo);
        // ocultamos los spinners que no necesitamos
        spin_jornada = vista.findViewById(R.id.spinnerJornada);
        spin_jornada.setVisibility(View.GONE);
        spin_fase = vista.findViewById(R.id.spinnerFase);
        spin_fase.setVisibility(View.GONE);
        barSpinners = vista.findViewById(R.id.barGroup);
        tvSinTabla = vista.findViewById(R.id.tv_sinTabla);

        positionSrv = new RetrofitAdapter().connectionEnable().create(PositionCompetitorSrv.class);
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
    }

    private void showTablePosotion(){
        // vemos si es eliminatoria
        if(competition.getTypesOrganization().contains("Eliminatoria")){
            barSpinners.setVisibility(View.GONE);
            tvSinTabla.setVisibility(View.VISIBLE);
            tablaPosiciones.setVisibility(View.GONE);
        }
        else{
            // analizamos el tipo de competencia
            if(competition.getTypesOrganization().contains("Liga")){
                barSpinners.setVisibility(View.GONE);

                tablaPosiciones.setVisibility(View.VISIBLE);
                tvSinTabla.setVisibility(View.INVISIBLE);
                callTablePosition(competition.getId());
            }
            if(competition.getTypesOrganization().contains("grupo")){
                barSpinners.setVisibility(View.VISIBLE);

                tablaPosiciones.setVisibility(View.VISIBLE);
                tvSinTabla.setVisibility(View.INVISIBLE);
                cargarSpinnerGrupo(competition.getId());
            }
        }
    }

    // vaciar la pantalla de posiciones para mostrar solo una tabla de posiciones
    private void emptyScreenTable(){
        if (tablaPosiciones.getChildCount() > 0)
            tablaPosiciones.removeAllViews();
    }

    // recupera la tabla de posiciones de una competencia del tipo LIGA
    private void callTablePosition(int idCompetencia){
        emptyScreenTable();
        Call<List<PositionCompetitor>> call = positionSrv.getTablePositions(idCompetencia);

        try{
            call.enqueue(new Callback<List<PositionCompetitor>>() {
                @Override
                public void onResponse(Call<List<PositionCompetitor>> call, Response<List<PositionCompetitor>> response) {
                    if(response.code() == 200){
                        posiciones = response.body();
                        // si recibimos los resultados de las posiciones de los competidores
                        if(posiciones != null){
                            // sino es definitavamente una liga
                            showTablePositions(posiciones);
                        }
                        else{
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
        emptyScreenTable();
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
                            showTablePositions(posiciones);
                        }
                        else{
                            Toast.makeText(vista.getContext(), "Aun no se han disputados encuentros de la competencia", Toast.LENGTH_SHORT).show();
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
        itemGrupos.add("Grupo ");
        for (int i = 1; i <= nroGrupos ; i++) {
                itemGrupos.add(String.valueOf(i));
        }
        return itemGrupos;
    }

    // crea una tabla de posiciones con los resultados
    // pre: existen equipos para mostrar
    private void showTablePositions(List<PositionCompetitor> posiciones) {
        Collections.sort(posiciones, new Comparator<PositionCompetitor>() {
            @Override
            public int compare(PositionCompetitor pos1, PositionCompetitor pos2) {
                return pos1.getPuntos().compareTo(pos2.getPuntos());
            }
        });
        Collections.reverse(posiciones);

        // agregamos la cabecera a la tabla
        addHeaderTable();

        // agregamos los resultados de las posiciones
        for (int i = 0; i < posiciones.size(); i++) {
            TableRow resultadoCompetidor = getRowTable(posiciones.get(i));
            tablaPosiciones.addView(resultadoCompetidor);
        }
//        Log.d("TABLA_POS", "entro a MostararTabla");
    }

    // crea una nueva fila para la tabla de resultados desde la info recuperada del servidor
    private TableRow getRowTable(PositionCompetitor posicionCompetidor){
        // creamos una fila vacia
        TableRow tbrow = new TableRow(vista.getContext());

        // agregamos los valores de las columnas
        TextView tvCompetidor = new TextView(vista.getContext());
        tvCompetidor.setText(posicionCompetidor.getCompetidor());
        tvCompetidor.setTextColor(Color.BLACK);
        tvCompetidor.setGravity(Gravity.LEFT);
        tvCompetidor.setPadding(20,5,20,5);
        tbrow.addView(tvCompetidor);
        TextView tvPj = new TextView(vista.getContext());
        tvPj.setText(posicionCompetidor.getJugados());
        tvPj.setTextColor(Color.BLACK);
        tvPj.setGravity(Gravity.CENTER);
        tvPj.setPadding(230,5,20,5);
        tbrow.addView(tvPj);
        TextView tvPg = new TextView(vista.getContext());
        tvPg.setText(posicionCompetidor.getGanados());
        tvPg.setTextColor(Color.BLACK);
        tvPg.setGravity(Gravity.CENTER);
        tvPg.setPadding(20,5,20,5);
        tbrow.addView(tvPg);
        TextView tvPe = new TextView(vista.getContext());
        tvPe.setText(posicionCompetidor.getEmpatados());
        tvPe.setTextColor(Color.BLACK);
        tvPe.setGravity(Gravity.CENTER);
        tvPe.setPadding(20,5,20,5);
        tbrow.addView(tvPe);
        TextView tvPp = new TextView(vista.getContext());
        tvPp.setText(posicionCompetidor.getPerdidos());
        tvPp.setTextColor(Color.BLACK);
        tvPp.setGravity(Gravity.CENTER);
        tvPp.setPadding(20,5,20,5);
        tbrow.addView(tvPp);
        TextView tvPts = new TextView(vista.getContext());
        tvPts.setText(posicionCompetidor.getPuntos());
        tvPts.setTextColor(Color.BLACK);
        tvPts.setGravity(Gravity.CENTER);
        tvPts.setPadding(20,5,20,5);
        tbrow.addView(tvPts);

        return tbrow;
    }

    // crea una nueva fila para la tabla de resultados desde la info recuperada del servidor
    private TableRow addHeaderTable(){
        // creamos una fila vacia
        TableRow tbrow = new TableRow(vista.getContext());
        tbrow.setBackgroundColor(Color.CYAN);

        // agregamos los valores de las columnas
        TextView tvCompetidor = new TextView(vista.getContext());
        tvCompetidor.setText("COMPETIDOR");
        tvCompetidor.setTextColor(Color.GRAY);
        tvCompetidor.setGravity(Gravity.CENTER);
        tvCompetidor.setPadding(20,5,20,5);
        tbrow.addView(tvCompetidor);
        TextView tvPj = new TextView(vista.getContext());
        tvPj.setText("PJ");
        tvPj.setTextColor(Color.GRAY);
        tvPj.setGravity(Gravity.CENTER);
        tvPj.setPadding(230,5,20,5);
        tbrow.addView(tvPj);
        TextView tvPg = new TextView(vista.getContext());
        tvPg.setText("PG");
        tvPg.setTextColor(Color.GRAY);
        tvPg.setGravity(Gravity.CENTER);
        tvPg.setPadding(20,5,20,5);
        tbrow.addView(tvPg);
        TextView tvPe = new TextView(vista.getContext());
        tvPe.setText("PE");
        tvPe.setTextColor(Color.GRAY);
        tvPe.setGravity(Gravity.CENTER);
        tvPe.setPadding(20,5,20,5);
        tbrow.addView(tvPe);
        TextView tvPp = new TextView(vista.getContext());
        tvPp.setText("PP");
        tvPp.setTextColor(Color.GRAY);
        tvPp.setGravity(Gravity.CENTER);
        tvPp.setPadding(20,5,20,5);
        tbrow.addView(tvPp);
        TextView tvPts = new TextView(vista.getContext());
        tvPts.setText("Pts");
        tvPts.setTextColor(Color.GRAY);
        tvPts.setGravity(Gravity.CENTER);
        tvPts.setPadding(20,5,20,5);
        tbrow.addView(tvPts);

        tablaPosiciones.addView(tbrow);

        // agregamos el separador
        TableRow tbrowsep = new TableRow(vista.getContext());
        FrameLayout separator = new FrameLayout(vista.getContext());

        separator.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 2));
        separator.setBackgroundColor(Color.WHITE);
        tbrowsep.addView(separator);

        tablaPosiciones.addView(tbrowsep);

        return tbrow;
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
