package com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.PositionCompetitor;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.VeizagaTorrico.proyectotorneos.services.PositionCompetitorSrv;

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
    private Spinner spin_grupo;
    private Spinner spin_jornada;
    private String nroGrupo;

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
        tablaPosiciones = vista.findViewById(R.id.TablaPosiciones);
        spin_grupo = vista.findViewById(R.id.spinnerGrupo);
        spin_jornada = vista.findViewById(R.id.spinnerJornada);
        spin_jornada.setVisibility(View.INVISIBLE);
        initElements();
        //callTablePosition(competition.getId());
        showTablePosotion(competition.getId());
        return vista;
    }

    private void initElements() {
        positionSrv = new RetrofitAdapter().connectionEnable().create(PositionCompetitorSrv.class);
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
    }

    private void showTablePosotion(int idCompetencia){
        // analizamos el tipo de competencia
        if(competition.getTypesOrganization().contains("Liga")){
            callTablePosition(competition.getId());
        }
        if(competition.getTypesOrganization().contains("grupo")){
            spin_grupo.setVisibility(View.VISIBLE);
            // cargamos las opciones del spinner y ponemos los botones a la escucha para llamar el serv de tabla segun grupo y competencia
            // determinar serv de tabla segun competencia y grupo
            cargarSpinnerGrupo(competition.getId());
        }
        else{
            spin_grupo.setVisibility(View.INVISIBLE);
        }
        // vemos si es eliminatoria
        if(competition.getTypesOrganization().contains("Eliminatoria")){
            Toast toast = Toast.makeText(vista.getContext(), "Las eliminatorias no cuentan con una tabla de posiciones", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void callTablePosition(int idCompetencia){
        Call<List<PositionCompetitor>> call = positionSrv.getTablePositions(idCompetencia);
        try{
            call.enqueue(new Callback<List<PositionCompetitor>>() {
                @Override
                public void onResponse(Call<List<PositionCompetitor>> call, Response<List<PositionCompetitor>> response) {
                    if(response.code() == 200){
                        posiciones = response.body();
                    }

                    // si recibimos los resultados de las posiciones de los competidores
                    if(posiciones != null){
                        // sino es definitavamente una liga
                        showTablePositions(posiciones);
                    }
                }

                @Override
                public void onFailure(Call<List<PositionCompetitor>> call, Throwable t) {
                    Log.d("On failure", t.getMessage());
                    Toast toast = Toast.makeText(vista.getContext(), "No se pudieron recuperar las posiciones de la competencia", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        //fecha_grupo.clear();
                        Log.d("RESPONSE FILTER CODE",  Integer.toString(response.code()));
                        //asigno a deportes lo que traje del servidor
                        datosSpinner = response.body();
                        Log.d("datosSpinner",response.body().toString());


                        // List<Integer> grupos = getAllIntegerRange(1 , datosSpinner.getN_grupos());
                        List<String> grupos = getItemGrupos(datosSpinner.getN_grupos());
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
                                        //fecha_grupo.put("grupo", nroGrupo);
                                    }
                                    else{
                                        nroGrupo = (String) spin_grupo.getSelectedItem();
                                        //fecha_grupo.put("grupo", nroGrupo);
                                        //getEncuentros(fecha_grupo);
                                        // aca segun la opcion deberiamos mostrar la tabla correspondiente
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
        tbrow.addView(tvCompetidor);
        TextView tvPj = new TextView(vista.getContext());
        tvPj.setText(posicionCompetidor.getJugados());
        tvPj.setTextColor(Color.BLACK);
        tvPj.setGravity(Gravity.CENTER);
        tbrow.addView(tvPj);
        TextView tvPg = new TextView(vista.getContext());
        tvPg.setText(posicionCompetidor.getGanados());
        tvPg.setTextColor(Color.BLACK);
        tvPg.setGravity(Gravity.CENTER);
        tbrow.addView(tvPg);
        TextView tvPe = new TextView(vista.getContext());
        tvPe.setText(posicionCompetidor.getEmpatados());
        tvPe.setTextColor(Color.BLACK);
        tvPe.setGravity(Gravity.CENTER);
        tbrow.addView(tvPe);
        TextView tvPp = new TextView(vista.getContext());
        tvPp.setText(posicionCompetidor.getPerdidos());
        tvPp.setTextColor(Color.BLACK);
        tvPp.setGravity(Gravity.CENTER);
        tbrow.addView(tvPp);
        TextView tvPts = new TextView(vista.getContext());
        tvPts.setText(posicionCompetidor.getPuntos());
        tvPts.setTextColor(Color.BLACK);
        tvPts.setGravity(Gravity.CENTER);
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
        //tvCompetidor.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        tvCompetidor.setTextColor(Color.GRAY);
        tvCompetidor.setGravity(Gravity.CENTER);
        tbrow.addView(tvCompetidor);
        TextView tvPj = new TextView(vista.getContext());
        tvPj.setText("PJ");
        //tvPj.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        tvPj.setTextColor(Color.GRAY);
        tvPj.setGravity(Gravity.CENTER);
        tbrow.addView(tvPj);
        TextView tvPg = new TextView(vista.getContext());
        tvPg.setText("PG");
        //tvPg.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        tvPg.setTextColor(Color.GRAY);
        tvPg.setGravity(Gravity.CENTER);
        tbrow.addView(tvPg);
        TextView tvPe = new TextView(vista.getContext());
        tvPe.setText("PE");
        //tvPe.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        tvPe.setTextColor(Color.GRAY);
        tvPe.setGravity(Gravity.CENTER);
        tbrow.addView(tvPe);
        TextView tvPp = new TextView(vista.getContext());
        tvPp.setText("PP");
        //tvPp.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        tvPp.setTextColor(Color.GRAY);
        tvPp.setGravity(Gravity.CENTER);
        tbrow.addView(tvPp);
        TextView tvPts = new TextView(vista.getContext());
        tvPts.setText("Pts");
        //tvPts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        tvPts.setTextColor(Color.GRAY);
        tvPts.setGravity(Gravity.CENTER);
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
