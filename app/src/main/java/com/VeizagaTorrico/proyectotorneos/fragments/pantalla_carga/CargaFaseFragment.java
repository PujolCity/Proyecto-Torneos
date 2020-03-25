package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Classified;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.ConfrontationMin;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Phase;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CargaFaseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
    private ImageButton delete;
    private TextView vs;
    private EditText comp1, comp2;
    private Button btnCrear, btnEncuentro;
    private Spinner spinnerFase, spinnerCompetidor, spinnerEncuentro;
    private List<ConfrontationMin> encuentros;
    private Map<String,Object> body;
    private UserSrv userSrv;
    private List<Classified> usuarios;
    private ArrayAdapter<Classified> adapterUser;
    private Classified seleccionadoSpinner,select1,select2;
    private List<Integer> seleccionados;
    private List<List<Integer>> datosBody;
    private final List<String> fases = new ArrayList<>();
    private CompetitionSrv competitionSrv;
    private int cantEncuentros;
    private ConfrontationMin encDelete;

    private Map<String,Integer> datos;
    private int fase;


    public CargaFaseFragment() {

    }

    public static CargaFaseFragment newInstance() {
        CargaFaseFragment fragment = new CargaFaseFragment();
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
        vista = inflater.inflate(R.layout.fragment_carga_fase, container, false);
        initElements();
        llenarSpinnerFase();

        btnListener();

        return vista;
    }

    private void llenarSpinnerFase() {
        Call<Phase> call = competitionSrv.getFase(competencia.getId());
        call.enqueue(new Callback<Phase>() {
            @Override
            public void onResponse(Call<Phase> call, Response<Phase> response) {
                List<String> fs = new ArrayList<>();
                fs.add("Final");
                fs.add("Semi Final");
                fs.add("Cuartos");
                fs.add("Octavos");
                fs.add("16 avos");
                fs.add("32 avos");
                for(int i = 0; fases.size() < response.body().getFase(); i++){
                    fases.add(fs.get(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,fases);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFase.setAdapter(adapter);
                spinnerFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        fase = i+1;
                        datos.put("idCompetencia",competencia.getId());
                        datos.put("fase",fase);
                        servidorUsers(datos);
                        encuentros.clear();
                        cantEncuentros = (int) Math.pow(2,(fase -1));
                        llenarSpinnerEncuentros();
                        visibleBtn();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            @Override
            public void onFailure(Call<Phase> call, Throwable t) {

            }
        });
    }

    private void btnListener() {

        comp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select1 = seleccionadoSpinner;
                comp1.setText(select1.getAlias());
                visibleBtn();
            }
        });

        comp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select2 = seleccionadoSpinner;
                comp2.setText(select2.getAlias());
                visibleBtn();
            }
        });

        btnEncuentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){
                    encuentros.add(new ConfrontationMin(select1,select2));
                    usuarios.remove(select1);
                    usuarios.remove(select2);
                    llenarSpinnerUsers(usuarios);
                    clearSeleccionados();
                    llenarSpinnerEncuentros();
                    visibleBtn();
                }

            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(encuentros.size() == cantEncuentros){
                    try {
                        body = armarBody();
                        Call<MsgRequest> call = competitionSrv.generarSiguienteFase(body);
//                        Log.d("a ver", Integer.toString(cantEncuentros));
                        Log.d("URL Generar Fase", call.request().url().toString());
                        call.enqueue(new Callback<MsgRequest>() {
                            @Override
                            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                                try {
                                    if(response.code() == 200){
                                        encuentros.clear();
                                        llenarSpinnerEncuentros();
                                        Toast.makeText(vista.getContext(), "Encuentros guardados", Toast.LENGTH_SHORT).show();
                                        visibleBtn();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                    }
                    @Override
                    public void onFailure(Call<MsgRequest> call, Throwable t) {
                        try {
                            Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Toast toast = Toast.makeText(vista.getContext(), "La cantidad de encuentros no coincide con la fase", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarios.add(encDelete.getCompetidor2());
                usuarios.add(encDelete.getCompetidor1());
                encuentros.remove(encDelete);
                llenarSpinnerUsers(usuarios);
                llenarSpinnerEncuentros();
                visibleBtn();
            }
        });

    }

    private void visibleBtn() {
        if((select1 != null && select2 != null) || !usuarios.isEmpty()){
            btnEncuentro.setVisibility(View.VISIBLE);
            spinnerEncuentro.setVisibility(View.VISIBLE);
        }
        if(!encuentros.isEmpty()){
            delete.setVisibility(View.VISIBLE);
        }else {
            delete.setVisibility(View.INVISIBLE);
        }
        if(encuentros.size() == cantEncuentros){
            btnCrear.setVisibility(View.VISIBLE);
        }else {
            btnCrear.setVisibility(View.INVISIBLE);
        }
    }

    private Map<String,Object> armarBody() {
        datosBody = new ArrayList<>();
        Map<String, Object> generados = new HashMap<>();

        for(ConfrontationMin enc : encuentros){
            seleccionados = new ArrayList<>();
            seleccionados.add(enc.getCompetidor1().getId());
            seleccionados.add(enc.getCompetidor2().getId());
            datosBody.add(seleccionados);
        }
        generados.put("idCompetencia",competencia.getId());
        generados.put("fase",fase);
        generados.put("encuentros",datosBody);

        Log.d("DATOS BODY", generados.toString());

        return generados;
    }

    private void clearSeleccionados() {
        select1 = null;
        select2 = null;
        comp1.setText(" ");
        comp2.setText(" ");
    }

    private void llenarSpinnerEncuentros() {
        ArrayAdapter<ConfrontationMin> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,encuentros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEncuentro.setAdapter(adapter);
        spinnerEncuentro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                encDelete = (ConfrontationMin) spinnerEncuentro.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private boolean validar() {
        if(select1 == null || select2 == null){
            Toast toast = Toast.makeText(vista.getContext(), "No hay clasificados seleccionados o no completo los campos correspondiente", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(select1.getId() == select2.getId()){
            Toast toast = Toast.makeText(vista.getContext(), "Los clasificados deben ser diferentes", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
            return true;
    }

    private void initElements() {
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");

        datos = new HashMap<>();
        body = new HashMap<>();

        vs = vista.findViewById(R.id.asd);
        vs.setVisibility(View.INVISIBLE);
        comp1 = vista.findViewById(R.id.etComp1);
        comp1.setVisibility(View.INVISIBLE);
        comp2 = vista.findViewById(R.id.etComp2);
        comp2.setVisibility(View.INVISIBLE);

        spinnerFase = vista.findViewById(R.id.spinnerFaseCarga);
        spinnerCompetidor = vista.findViewById(R.id.spinnerCompetidores);
        spinnerEncuentro = vista.findViewById(R.id.spinnerEncunetrosGenerados);

        btnEncuentro = vista.findViewById(R.id.guardarEncuentro);
        btnCrear = vista.findViewById(R.id.crearEncuentros);
        delete = vista.findViewById(R.id.deleteEncuentro);

        userSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);

        usuarios = new ArrayList<>();
        seleccionados = new ArrayList<>();
        encuentros = new ArrayList<>();
        datosBody = new ArrayList<>();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void servidorUsers(final Map<String,Integer> data) {
        Call<List<Classified>> call = userSrv.getClasificados(data);
        Log.d("Call URL", call.request().url().toString());
        try {
            call.enqueue(new Callback<List<Classified>>() {
                @Override
                public void onResponse(Call<List<Classified>> call, Response<List<Classified>> response) {
                    if(!response.body().isEmpty()){
                        comp1.setVisibility(View.VISIBLE);
                        comp2.setVisibility(View.VISIBLE);
                        vs.setVisibility(View.VISIBLE);
                        usuarios = response.body();
                        Log.d("usuarios recuperados",usuarios.toString());
                        llenarSpinnerUsers(usuarios);
                    }
                }

                @Override
                public void onFailure(Call<List<Classified>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void llenarSpinnerUsers(List<Classified> users) {
        adapterUser = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, users);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetidor.setAdapter(adapterUser);
        spinnerCompetidor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seleccionadoSpinner = (Classified) spinnerCompetidor.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
