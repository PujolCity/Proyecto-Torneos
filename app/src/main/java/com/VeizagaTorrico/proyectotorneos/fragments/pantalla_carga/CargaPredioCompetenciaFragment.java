package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargaPredioCompetenciaFragment extends Fragment implements MensajeSinInternet {

    private View vista;
    private CompetitionMin competencia;
    private GroundSrv groundSrv;
    private Spinner spnnrPredio, spnnrCampo,spnnrPredioAsignado;
    private TextView tvPredioDire, tvCampoCapacidad, tvCampoDimension, tvSinResultados;
    private EditText edt_nombre_buscar;
    private LinearLayout linElimPredios;
    private TextView tvSinPredios;
    private Button btnPredio, btnBuscar, btnAsignarSeleccionado;
    private ImageButton iBDelete;
    private List<Ground> predios, prediosAsignados;
    private List<Field> campos;
    private Ground predioSeleccionado, miPredioSeleccionado;
    private FieldSrv fieldSrv;
    private ArrayAdapter<Field> adapterCampo;
    private Field campoSeleccionado;
    private Map<String,String> data;
    private LinearLayout lin_rdos_busqueda;
    private AlertDialog alertDialog;

    public CargaPredioCompetenciaFragment() {
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
        if(NetworkReceiver.existConnection(vista.getContext())) {
            listeners();
            llenarMisPredios();
        } else {
            sinInternet();
        }
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
        tvCampoCapacidad = vista.findViewById(R.id.tv_capacidad_predio);
        tvCampoDimension = vista.findViewById(R.id.tv_dimension_predio);
        tvSinResultados = vista.findViewById(R.id.tv_no_resultados);
        edt_nombre_buscar = vista.findViewById(R.id.edt_nombre_predio);

        iBDelete = vista.findViewById(R.id.btn_delete_predio);
        btnPredio = vista.findViewById(R.id.btn_crear_predio);
        btnAsignarSeleccionado = vista.findViewById(R.id.btn_asginar_sel);
        btnBuscar = vista.findViewById(R.id.btn_buscar_predio);

        lin_rdos_busqueda = vista.findViewById(R.id.lin_resultado_busqueda);
        lin_rdos_busqueda.setVisibility(View.GONE);
        tvSinResultados.setVisibility(View.GONE);
        iBDelete.setVisibility(View.GONE);
        linElimPredios = vista.findViewById(R.id.linn_elim_predios);
        tvSinPredios = vista.findViewById(R.id.tvSinPredios);
        alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Espere un momento..");

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
        btnAsignarSeleccionado.setOnClickListener(new View.OnClickListener() {
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

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llenarSpinnerPredio();
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
                        Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
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

    private void llenarMisPredios() {
        prediosAsignados.clear();
        Call<List<Ground>> call = groundSrv.getPrediosAsignados(competencia.getId());
        Log.d("URL PREDIOS ASIGNADOS", call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    if(!response.body().isEmpty()) {
                        //iBDelete.setVisibility(View.VISIBLE);
                        linElimPredios.setVisibility(View.VISIBLE);
                        tvSinPredios.setVisibility(View.GONE);
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
                        linElimPredios.setVisibility(View.INVISIBLE);
                        tvSinPredios.setVisibility(View.VISIBLE);
                        prediosAsignados.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Ground>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarSpinnerPredio() {
        String nombrePredio;
        if(edt_nombre_buscar.getText().toString().equals("")){
            nombrePredio = null;
        }
        else{
            nombrePredio = edt_nombre_buscar.getText().toString();
        }
        Call<List<Ground>> call = groundSrv.findLikeName(nombrePredio);
        Log.d("CALL_LIKE_PREDIO",call.request().url().toString());
        alertDialog.show();
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    Log.d("predioComp resp", Integer.toString(response.code()));

                    if(!response.body().isEmpty()) {
                        alertDialog.dismiss();
                        tvSinResultados.setVisibility(View.GONE);
                        lin_rdos_busqueda.setVisibility(View.VISIBLE);
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
                                    tvPredioDire.setText("Ubicacion: "+predioSeleccionado.getDireccion()+ " - "+predioSeleccionado.getCiudad());
                                    llenarSpinnerCampo(predioSeleccionado.getId());
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        alertDialog.dismiss();
                        tvSinResultados.setVisibility(View.VISIBLE);
                        lin_rdos_busqueda.setVisibility(View.GONE);
                        //iBAgregar.setVisibility(View.INVISIBLE);
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
                alertDialog.dismiss();
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_LONG);
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
                                        tvCampoCapacidad.setText("Capacidad: "+campoSeleccionado.getCapacidad()+ " individuos");
                                        tvCampoDimension.setText("Dimensiones: "+campoSeleccionado.getDimensiones() + " mts2");
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

    @Override
    public void sinInternet() {
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento quedaran deshabilitadas algunas funciones.", Toast.LENGTH_LONG);
        toast.show();
        btnPredio.setVisibility(View.INVISIBLE);
    }
}
