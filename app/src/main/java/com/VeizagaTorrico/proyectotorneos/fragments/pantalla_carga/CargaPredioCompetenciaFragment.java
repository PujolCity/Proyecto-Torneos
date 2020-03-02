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
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.FieldSrv;
import com.VeizagaTorrico.proyectotorneos.services.GroundSrv;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargaPredioCompetenciaFragment extends Fragment {

    private View vista;
    private CompetitionMin competencia;
    private GroundSrv groundSrv;
    private String predioDire, predioCiudad,campoCapacidad, campoDimension;
    private Spinner spnnrPredio, spnnrCampo,spnnrPredioAsignado;
    private TextView tvPredioDire, tvPredioCiudad, tvCampoCapacidad, tvCampoDimension;
    private Button btnPredio;
    private ImageButton iBAgregar,iBDelete;
    private List<Ground> predios, prediosAsignados;
    private List<Field> campos;
    private Ground predioSeleccionado, miPredioSeleccionado;
    private FieldSrv fieldSrv;
    private ArrayAdapter<Field> adapterCampo;
    private Field campoSeleccionado;
    private Map<String,String> data;

    public CargaPredioCompetenciaFragment() {
        // Required empty public constructor
    }

    public static CargaPredioCompetenciaFragment newInstance() {
        CargaPredioCompetenciaFragment fragment = new CargaPredioCompetenciaFragment();
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
        vista = inflater.inflate(R.layout.fragment_carga_predio_competencia, container, false);
        initElement();
        listeners();
        llenarSpinners();


        return vista;
    }


    private void initElement() {
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");
        groundSrv = new RetrofitAdapter().connectionEnable().create(GroundSrv.class);
        fieldSrv = new RetrofitAdapter().connectionEnable().create(FieldSrv.class);

        spnnrPredio = vista.findViewById(R.id.spinner_predios_predio);
        spnnrPredioAsignado = vista.findViewById(R.id.spinner_misPredios_predio);
        spnnrCampo = vista.findViewById(R.id.spinner_campos_predio);
        tvPredioDire = vista.findViewById(R.id.tv_direccion_predio);
        tvPredioCiudad = vista.findViewById(R.id.tv_ciudad_predio);
        tvCampoCapacidad = vista.findViewById(R.id.tv_capacidad_predio);
        tvCampoDimension = vista.findViewById(R.id.tv_dimension_predio);

        iBAgregar = vista.findViewById(R.id.btn_asignar_predio);
        iBDelete = vista.findViewById(R.id.btn_delete_predio);
        btnPredio = vista.findViewById(R.id.btn_nuevo_predio);

        predios = new ArrayList<>();
        prediosAsignados = new ArrayList<>();
        campos = new ArrayList<>();
        data = new HashMap<>();
    }

    private void listeners() {
        btnPredio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToCreateGround();
            }
        });
        iBAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarPredioCompetencia();
            }
        });
        iBDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarPredioCompetencia();
            }
        });
    }

    private void eliminarPredioCompetencia() {
        data.put("idCompetencia", Integer.toString(competencia.getId()));
        data.put("idPredio", Integer.toString(miPredioSeleccionado.getId()));
        Log.d("Data Eliminar", data.toString());
        Call<Success> call = groundSrv.deletePredioCompetencia(data);
        Log.d("URL DELETE_PREDIO", call.request().url().toString());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if (response.code() == 200) {
                    Log.d("INFO DELETE", "PREDIO ELIMINADO");
                    // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                    Toast toast = Toast.makeText(vista.getContext(), "Predio Eliminado con exito!", Toast.LENGTH_SHORT);
                    toast.show();
                    llenarMisPredios();
                }
                if (response.code() == 400) {
                    Log.d("RESP_GROUND_ERROR", "PETICION MAL FORMADA: " + response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("messaging");
                        Log.d("RESP_GROUN_ERROR", "Msg de la repuesta: " + message);
                        Toast.makeText(vista.getContext(), "No se pudo eliminar el predio:  << " + message + " >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;

                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                try {
                    Log.d("OnFailure DEL_PREDIO",t.getMessage());
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void asignarPredioCompetencia() {
            data.put("idCompetencia", Integer.toString(competencia.getId()));
            data.put("idPredio", Integer.toString(predioSeleccionado.getId()));
            Log.d("Data Asignar", data.toString());
            Call<Success> call = groundSrv.asignarPredioCompetencia(data);
            Log.d("URL SET PREDIO",call.request().url().toString());
            call.enqueue(new Callback<Success>() {
                @Override
                public void onResponse(Call<Success> call, Response<Success> response) {
                    if (response.code() == 201) {
                        Log.d("INFO GROUND", " PREDIO ASIGNADO");
                        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                        Toast toast = Toast.makeText(vista.getContext(), "PREDIO ASIGNADO CON EXITO", Toast.LENGTH_SHORT);
                        toast.show();
                        llenarMisPredios();
                    }
                    if (response.code() == 400) {
                        Log.d("RESP_GROUND_ERROR", "PETICION MAL FORMADA: " + response.errorBody());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("messaging");
                            Log.d("RESP_GROUN_ERROR", "Msg de la repuesta: " + message);
                            Toast.makeText(vista.getContext(), "No se pudo asignar el predio:  << " + message + " >>", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;

                    }
                }
                @Override
                public void onFailure(Call<Success> call, Throwable t) {
                    try {
                        Log.d("OnFailure SETPREDIO",t.getMessage());
                        Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    private void passToCreateGround() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", competencia);
        Navigation.findNavController(vista).navigate(R.id.cargarPredioFragment, bundle);
    }

    private void llenarSpinners() {
        llenarSpinnerPredio();
        llenarMisPredios();
    }

    private void llenarMisPredios() {
        prediosAsignados.clear();
        Call<List<Ground>> call = groundSrv.getPrediosAsignados(competencia.getId());
        Log.d("URL PREDIOS ASIGNADOS", call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    if(!response.body().isEmpty()) {
                        iBDelete.setVisibility(View.VISIBLE);
                        prediosAsignados.clear();
                        prediosAsignados.addAll(response.body());
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, prediosAsignados);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrPredioAsignado.setAdapter(adapter);
                        spnnrPredioAsignado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                miPredioSeleccionado = (Ground) spnnrPredioAsignado.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        iBDelete.setVisibility(View.INVISIBLE);
                        prediosAsignados.clear();
                        Ground predio = new Ground(0, "Sin Predios", "", "");
                        prediosAsignados.add(predio);
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, prediosAsignados);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrPredioAsignado.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Ground>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarSpinnerPredio() {
        Call<List<Ground>> call = groundSrv.getGrounds();
        Log.d("call predio",call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    Log.d("encuentro response", Integer.toString(response.code()));

                    if(!response.body().isEmpty()) {
                        predios.clear();
                        predios.addAll(response.body());
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrPredio.setAdapter(adapter);
                        spnnrPredio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                predioSeleccionado = (Ground) spnnrPredio.getSelectedItem();
                                if (predioSeleccionado.getId() != 0) {
                                    tvPredioCiudad.setText(predioSeleccionado.getCiudad());
                                    tvPredioDire.setText("Direccion: " + predioSeleccionado.getDireccion());
                                    llenarSpinnerCampo(predioSeleccionado.getId());
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        Ground predio = new Ground(0, "Sin Predios", "", "");
                        predios.add(predio);
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrPredio.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Ground>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarSpinnerCampo(int idPredio) {
        if(idPredio != 0){
            spnnrCampo.setVisibility(View.VISIBLE);
            Call<List<Field>> call = fieldSrv.getFieldsByGround(idPredio);
            Log.d("calls campo",call.request().url().toString());
            call.enqueue(new Callback<List<Field>>() {
                @Override
                public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                    try {
                        if(!response.body().isEmpty()) {
                            campos.clear();
                            campos.addAll(response.body());
                            //        btnDeleteCampo.setVisibility(View.VISIBLE);
                            adapterCampo = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, campos);
                            adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnnrCampo.setAdapter(adapterCampo);
                            spnnrCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        campoSeleccionado = (Field) spnnrCampo.getSelectedItem();
                                        tvCampoCapacidad.setText("Capacidad: "+campoSeleccionado.getCapacidad());
                                        tvCampoDimension.setText("Dimension: "+campoSeleccionado.getDimensiones() + " mts2");
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
                @Override
                public void onFailure(Call<List<Field>> call, Throwable t) {
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }

    }

}
