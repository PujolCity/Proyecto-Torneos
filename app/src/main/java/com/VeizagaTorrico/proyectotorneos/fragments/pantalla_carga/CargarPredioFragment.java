package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.City;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.CitySrv;
import com.VeizagaTorrico.proyectotorneos.services.FieldSrv;
import com.VeizagaTorrico.proyectotorneos.services.GroundSrv;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargarPredioFragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private String predioNombre, predioDire, predioCiudad, campoNombre, campoCapacidad,campoDimensiones;
    private EditText nombrePred, direPred, ciudadPred, nombreCampo, capacidadCampo, dimensionesCampo;
    private Button btnPredio, btnCampo;
    private Spinner spinnerPredio, spinnerCampo, spnnerCiudad;
    private FieldSrv camposSrv;
    private Ground predioSeleccionado;
    private Field campoSeleccionado;
    private GroundSrv predioSrv;
    private Map<String,String> predio;
    private Map<String,String> campo;
    private Map<String,Integer> delete;
    private CompetitionMin competencia;
    private List<Ground> predios;
    private List<Field> campos;
    private ArrayAdapter<Field> adapterCampo;
    private CitySrv citySrv;
    private List<City> ciudades;
    private City ciudadSeleccionada;
    //  private ImageButton btnDeletePredio,btnDeleteCampo;
    private ImageButton btnDeleteCampo, ibCiudad;
    private Ground defaultPred;

    public CargarPredioFragment() {
        // Required empty public constructor
    }

    public static CargarPredioFragment newInstance() {
        CargarPredioFragment fragment = new CargarPredioFragment();
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
        vista = inflater.inflate(R.layout.fragment_cargar_predio, container, false);
        initElements();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            llenarSpinnerPredio();
            listeners();
        }else {
            sinInternet();
        }
        return vista;
    }

    private void listeners() {
        btnPredio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    predioNombre = nombrePred.getText().toString();
                    predioDire = direPred.getText().toString();
                    predioCiudad = ciudadPred.getText().toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(validarPredio()){
                    predio.put("nombre", predioNombre);
                    predio.put("direccion", predioDire);
                    predio.put("ciudad",Integer.toString(ciudadSeleccionada.getId()));

                    Log.d("body predio", predio.toString());

                    Call<Success> call = predioSrv.createGround(predio);
                    Log.d("Call url", call.request().url().toString());
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            Log.d("Response Predio", Integer.toString(response.code()));

                            if(response.code() == 201){
                                Log.d("Predio Cargado", "exito");
                                reset();
                                // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                                Toast toast = Toast.makeText(vista.getContext(), "Predio Cargado!", Toast.LENGTH_SHORT);
                                toast.show();
                                llenarSpinnerPredio();
                            } else {
                                Toast toast = Toast.makeText(vista.getContext(), "Predio ya cargado", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }else {
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor completar todos los campos", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        btnCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    campoNombre = nombreCampo.getText().toString();
                    campoCapacidad = capacidadCampo.getText().toString();
                    campoDimensiones = dimensionesCampo.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(validarCampo()){
                    campo.put("nombre",campoNombre);
                    campo.put("idPredio",Integer.toString(predioSeleccionado.getId()));
                    campo.put("dimensiones",campoDimensiones);
                    campo.put("capacidad",campoCapacidad);
                    Log.d("body campo", predio.toString());

                    Call<Success> call = camposSrv.createField(campo);
                    Log.d("Call url", call.request().url().toString());
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            Log.d("Response Campo", Integer.toString(response.code()));
                            if(response.code() == 201){
                                Log.d("Predio Cargado", "exito");

                                // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                                Toast toast = Toast.makeText(vista.getContext(), "Campo Cargado!", Toast.LENGTH_SHORT);
                                toast.show();
                                llenarSpinnerCampo(predioSeleccionado.getId());
                            }else {
                                Toast toast = Toast.makeText(vista.getContext(), "Campo ya cargado", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }else {
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor completar todos los campos", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        btnDeleteCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        nombrePred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombrePred.setText(null);
            }
        });
        ciudadPred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ciudadPred.setText(null);
            }
        });
        direPred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direPred.setText(null);
            }
        });
        nombreCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreCampo.setText(null);
            }
        });
        dimensionesCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dimensionesCampo.setText(null);
            }
        });
        capacidadCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capacidadCampo.setText(null);
            }
        });

        ibCiudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predioCiudad = ciudadPred.getText().toString();
                if(!predioCiudad.isEmpty()){
                    Call<List<City>> call = citySrv.buscarCiudad(predioCiudad);
                    Log.d("URL BUSC CIUDAD", call.request().url().toString());
                    call.enqueue(new Callback<List<City>>() {
                        @Override
                        public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                            try {
                                ciudades.clear();
                                if(response.code() == 200){
                                    if(response.body() != null){
                                        ciudades.addAll(response.body());
                                    }
                                    adapterCiudades();
                                    spnnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            ciudadSeleccionada = (City) spnnerCiudad.getSelectedItem();
                                            predioCiudad = ciudadSeleccionada.toString();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }
                                if(response.code() == 400){
                                    Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.errorBody().string());
                                        String userMessage = jsonObject.getString("messaging");
                                        Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                                        Toast.makeText(vista.getContext(), "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                                        ciudades.add(new City(0,"El nombre usado no existe, intentar con otro nombre",null));
                                        adapterCiudades();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<City>> call, Throwable t) {
                            Toast toast = Toast.makeText(getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                            toast.show();
                            Log.d("onFailure", t.getMessage());
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor complete el campo Ciudad", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    private void reset() {
        ciudadSeleccionada = null;
        ciudadPred.setText(null);
        predioCiudad = "";
        ciudades = new ArrayList<>();
        adapterCiudades();
    }

    private void adapterCiudades() {
        ArrayAdapter<City> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, ciudades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnnerCiudad.setAdapter(adapter);
    }


    private void eliminarCampo(final int idPredio, int idCampo) {
        delete.put("idPredio", idPredio);
        delete.put("idCampo", idCampo);

        Call<Success> call = camposSrv.deleteField(delete);
        Log.d("url delete campo", call.request().url().toString());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if(response.code() == 200){
                    campos.clear();
                    llenarSpinnerCampo(idPredio);
                    Toast toast = Toast.makeText(vista.getContext(), "Campo Eliminado!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Success> call, Throwable t) {

            }
        });
    }

    private void eliminarPredio(int idCompetencia, int idPredio) {
        delete.put("idCompetencia",idCompetencia);
        delete.put("idPredio",idPredio);

        Call<Success> call = predioSrv.deleteGround(delete);
        Log.d("url delete Predio", call.request().url().toString());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if(response.code() == 200){
                    predios.clear();
                    llenarSpinnerPredio();
                    Toast toast = Toast.makeText(vista.getContext(), "Predio Eliminado!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Hay algo mal que no esta bien", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private void llenarSpinnerPredio() {
        Call<List<Ground>> call = predioSrv.getGrounds();
        Log.d("call predio",call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    Log.d("encuentro response", Integer.toString(response.code()));
                    if(!response.body().isEmpty()) {
                        predios.clear();
                        predios.add(defaultPred);
                        predios.addAll(response.body());
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPredio.setAdapter(adapter);
                        spinnerPredio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                reset();
                                predioSeleccionado = (Ground) spinnerPredio.getSelectedItem();
                                if (predioSeleccionado.getId() != 0) {
                                    llenarSpinnerCampo(predioSeleccionado.getId());
                                    actualizarDatosPredio(predioSeleccionado);
                   //                 btnDeletePredio.setVisibility(View.VISIBLE);
                                } else {
                                    ciudadPred.setText(null);
                                    direPred.setText(null);
                                    nombrePred.setText(null);
                                    capacidadCampo.setText(null);
                                    dimensionesCampo.setText(null);
                                    nombreCampo.setText(null);
                                    btnCampo.setVisibility(View.INVISIBLE);
                                    spinnerCampo.setVisibility(View.INVISIBLE);
                    //                btnDeletePredio.setVisibility(View.INVISIBLE);
                                    btnDeleteCampo.setVisibility(View.INVISIBLE);
                                    reset();
                                   // msjCampos();
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
                        spinnerPredio.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Ground>> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void actualizarDatosPredio(Ground predio) {
        try {
            nombrePred.setText(predio.getNombre());
            direPred.setText(predio.getDireccion());
            ciudadPred.setText(predio.getCiudad());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void llenarSpinnerCampo(int idPredio) {
        if(idPredio != 0){
            btnCampo.setVisibility(View.VISIBLE);
            spinnerCampo.setVisibility(View.VISIBLE);
            Call<List<Field>> call = camposSrv.getFieldsByGround(idPredio);
            Log.d("calls campo",call.request().url().toString());
            call.enqueue(new Callback<List<Field>>() {
                @Override
                public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                    try {
                        if(!response.body().isEmpty()) {
                            campos.clear();
                            campos.addAll(response.body());
                            btnDeleteCampo.setVisibility(View.VISIBLE);
                            adapterCampo = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, campos);
                            adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCampo.setAdapter(adapterCampo);
                            spinnerCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    campoSeleccionado = (Field) spinnerCampo.getSelectedItem();
                                    actualizarDatosCampo(campoSeleccionado);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }else {
                            msjCampos();
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
        }else {
           msjCampos();
        }
    }

    private void actualizarDatosCampo(Field campo) {
        try {
            nombreCampo.setText(campo.getNombre());
            capacidadCampo.setText(Integer.toString(campo.getCapacidad()));
            dimensionesCampo.setText(Integer.toString(campo.getDimensiones()) + " mts2");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean validarPredio() {
        if(predioNombre.isEmpty())
            return false;
        if(predioCiudad.isEmpty())
                return false;
        if(predioDire.isEmpty())
            return false;
        return true;
    }

    private boolean validarCampo(){
        if(campoNombre.isEmpty())
            return false;
        if(campoCapacidad.isEmpty())
            return false;
        if(campoDimensiones.isEmpty())
            return false;
        return true;
    }

    private void initElements() {
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");

        predio = new HashMap<>();
        campo = new HashMap<>();
        delete = new HashMap<>();

        predios = new ArrayList<>();
        campos = new ArrayList<>();
        ciudades = new ArrayList<>();

        nombrePred = vista.findViewById(R.id.etNombrePredio);
        direPred = vista.findViewById(R.id.etDireccionPredio);
        ciudadPred = vista.findViewById(R.id.etCiudadPredio);

        nombreCampo = vista.findViewById(R.id.etCampoNombre);
        capacidadCampo = vista.findViewById(R.id.etCapacidadCampo);
        dimensionesCampo = vista.findViewById(R.id.etDimensionesCampo);

        btnPredio = vista.findViewById(R.id.btnAgregarPredio);
        btnCampo = vista.findViewById(R.id.btnAgregarCampo);
        ibCiudad = vista.findViewById(R.id.ib_buscar_predio);
    /*    btnDeletePredio = vista.findViewById(R.id.btnDeletePredio);
        btnDeleteCampo = vista.findViewById(R.id.btnDeleteCampo);
*/
        btnDeleteCampo = vista.findViewById(R.id.btnDeleteCampo);
        spinnerPredio = vista.findViewById(R.id.spinnerCargaPredio);
        spinnerCampo = vista.findViewById(R.id.spinnerCargarCampo);
        spnnerCiudad = vista.findViewById(R.id.spinner_ciudad_predio);

        camposSrv = new RetrofitAdapter().connectionEnable().create(FieldSrv.class);
        predioSrv = new RetrofitAdapter().connectionEnable().create(GroundSrv.class);
        citySrv = new RetrofitAdapter().connectionEnable().create(CitySrv.class);

        defaultPred = new Ground(0,"Predios disponibles","","");
        predios.add(defaultPred);
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

    @Override
    public void sinInternet() {
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento quedaran deshabilitadas algunas funciones.", Toast.LENGTH_LONG);
        toast.show();
        btnPredio.setVisibility(View.INVISIBLE);
        btnCampo.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void msjCampos() {
        Field campo = new Field(0,"Sin campos",0,0,null);
        btnDeleteCampo.setVisibility(View.INVISIBLE);
        campos.clear();
        campos.add(campo);
        adapterCampo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,campos);
        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampo.setAdapter(adapterCampo);
    }
}