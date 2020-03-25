package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.RefereeSrv;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargaJuezCompetenciaFragment extends Fragment implements MensajeSinInternet {

    private View vista;
    private CompetitionMin competencia;
    private Spinner spnnrJuez, spnnrJuezAsignado;
    private TextView tvJuezDni;
    private Button btnJuez;
    private ImageButton iBAgregar,iBDelete;
    private List<Referee> jueces, juecesAsignados;
    private Referee juezSeleccionado, miJuezSeleccionado;
    private RefereeSrv refereeSrv;
    private Map<String,String> data;

    public CargaJuezCompetenciaFragment() {
    }

    public static CargaJuezCompetenciaFragment newInstance() {
        CargaJuezCompetenciaFragment fragment = new CargaJuezCompetenciaFragment();
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
        vista = inflater.inflate(R.layout.fragment_carga_juez_competencia, container, false);
        initElements();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            listeners();
            llenarSpinners();
        } else {
            sinInternet();
        }


        return vista;
    }

    private void initElements() {
        refereeSrv = new RetrofitAdapter().connectionEnable().create(RefereeSrv.class);

        competencia = (CompetitionMin) getArguments().getSerializable("competencia");
        spnnrJuez = vista.findViewById(R.id.spinner_jueces_juez);
        spnnrJuezAsignado = vista.findViewById(R.id.spinner_misJueces_juez);
        tvJuezDni = vista.findViewById(R.id.tv_dni_juez);
        btnJuez = vista.findViewById(R.id.btn_nuevo_juez);
        iBAgregar = vista.findViewById(R.id.btn_asignarJuez_juez);
        iBDelete = vista.findViewById(R.id.btn_delete_juez);

        jueces = new ArrayList<>();
        juecesAsignados = new ArrayList<>();
        data = new HashMap<>();
    }

    private void llenarSpinners() {
        llenarSpinerJueces();
        llenarMisJueces();
    }

    private void llenarSpinerJueces() {
        Call<List<Referee>> call = refereeSrv.getReferees();
        Log.d("URL JUECES", call.request().url().toString());
        call.enqueue(new Callback<List<Referee>>() {
            @Override
            public void onResponse(Call<List<Referee>> call, Response<List<Referee>> response) {
                try {
                    Log.d("JuezComp resp", Integer.toString(response.code()));
                    if(!response.body().isEmpty()) {
                        iBAgregar.setVisibility(View.VISIBLE);
                        jueces.clear();
                        jueces.addAll(response.body());
                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, jueces);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrJuez.setAdapter(adapter);
                        spnnrJuez.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                juezSeleccionado = (Referee) spnnrJuez.getSelectedItem();
                                if(juezSeleccionado.getId() != 0){
                                    tvJuezDni.setText("DNI: " + juezSeleccionado.getDni());
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }else {
                        iBAgregar.setVisibility(View.INVISIBLE);
                        Referee referee = new Referee(0,"Sin ", "Jueces ", 000000);
                        jueces.add(referee);
                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, jueces);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrJuez.setAdapter(adapter);
                    }

                    } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Referee>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarMisJueces() {
        juecesAsignados.clear();
        Call<List<Referee>> call = refereeSrv.getJuecesAsignados(this.competencia.getId());
        Log.d("URL Jueces ASIGNADOS", call.request().url().toString());
        call.enqueue(new Callback<List<Referee>>() {
            @Override
            public void onResponse(Call<List<Referee>> call, Response<List<Referee>> response) {
                try {
                    if(!response.body().isEmpty()){
                        iBDelete.setVisibility(View.VISIBLE);
                        juecesAsignados.clear();
                        juecesAsignados.addAll(response.body());
                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, juecesAsignados);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrJuezAsignado.setAdapter(adapter);
                        spnnrJuezAsignado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                miJuezSeleccionado = (Referee) spnnrJuezAsignado.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        iBDelete.setVisibility(View.INVISIBLE);
                        juecesAsignados.clear();
                        Referee referee = new Referee(0,"Sin ", "Jueces ", 000000);
                        juecesAsignados.add(referee);
                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, juecesAsignados);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrJuezAsignado.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Referee>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void listeners() {
        btnJuez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToCreateReferee();
            }
        });
        iBDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarJuezCompetencia();
            }
        });
        iBAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarJuezCompetencia();
            }
        });
    }

    private void asignarJuezCompetencia() {
        data.put("idCompetencia", Integer.toString(competencia.getId()));
        data.put("idJuez", Integer.toString(juezSeleccionado.getId()));
        Log.d("Data Asignar", data.toString());
        Call<Success> call = refereeSrv.asignarJuezCompetencia(data);
        Log.d("URL SET JUEZ",call.request().url().toString());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if (response.code() == 201) {
                    Log.d("INFO REFEREE", " JUEZ ASIGNADO");
                    // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                    Toast toast = Toast.makeText(vista.getContext(), "JUEZ ASIGNADO CON EXITO", Toast.LENGTH_SHORT);
                    toast.show();
                    llenarMisJueces();
                }
                if (response.code() == 400) {
                    Log.d("RESP_GROUND_ERROR", "PETICION MAL FORMADA: " + response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("messaging");
                        Log.d("RESP_GROUN_ERROR", "Msg de la repuesta: " + message);
                        Toast.makeText(vista.getContext(), "No se pudo asignar el predio:  << " + message + " >>", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_LONG);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void eliminarJuezCompetencia() {
        data.put("idCompetencia", Integer.toString(competencia.getId()));
        data.put("idJuez", Integer.toString(miJuezSeleccionado.getId()));
        Log.d("Data DELETE", data.toString());
        Call<Success> call = refereeSrv.deleteJuezCompetencia(data);
        Log.d("URL DELETE_REFEREE", call.request().url().toString());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if (response.code() == 200) {
                    Log.d("INFO REFEREE", " JUEZ ELIMINADO");
                    // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                    Toast toast = Toast.makeText(vista.getContext(), "JUEZ ELIMINADO CON EXITO", Toast.LENGTH_LONG);
                    toast.show();
                    llenarMisJueces();
                }
                if (response.code() == 400) {
                    Log.d("RESP_GROUND_ERROR", "PETICION MAL FORMADA: " + response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("messaging");
                        Log.d("RESP_GROUN_ERROR", "Msg de la repuesta: " + message);
                        Toast.makeText(vista.getContext(), "No se pudo asignar el predio:  << " + message + " >>", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_LONG);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void passToCreateReferee() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", competencia);
        Navigation.findNavController(vista).navigate(R.id.cargarJuezFragment,bundle);
    }

    @Override
    public void sinInternet() {
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento quedaran deshabilitadas algunas funciones.", Toast.LENGTH_LONG);
        toast.show();
        btnJuez.setVisibility(View.INVISIBLE);
    }
}
