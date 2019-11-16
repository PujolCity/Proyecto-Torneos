package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.FieldSrv;
import com.VeizagaTorrico.proyectotorneos.services.GroundSrv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargarPredioFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private String predioNombre, predioDire, predioCiudad, campoNombre, campoCapacidad,campoDimensiones;
    private EditText nombrePred, direPred, ciudadPred, nombreCampo, capacidadCampo, dimensionesCampo;
    private Button btnPredio, btnCampo;
    private Spinner spinnerPredio, spinnerCampo;
    private FieldSrv camposSrv;
    private Ground predioSeleccionado;
    private GroundSrv predioSrv;
    private Map<String,String> predio;
    private Map<String,String> campo;
    private CompetitionMin competencia;
    private List<Ground> predios;
    private List<Field> campos;
    private ArrayAdapter<Field> adapterCampo;

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

        llenarSpinnerPredio();

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

                        predio.put("idCompetencia",Integer.toString(competencia.getId()));

                        predio.put("direccion", predioDire);

                        predio.put("ciudad",predioCiudad);
                    Log.d("body predio", predio.toString());

                    Call<Success> call = predioSrv.createGround(predio);
                    Log.d("Call url", call.request().url().toString());
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            Log.d("Response Predio", Integer.toString(response.code()));

                            if(response.code() == 201){
                                Log.d("Predio Cargado", "exito");

                                // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                                Toast toast = Toast.makeText(vista.getContext(), "Predio Cargado!", Toast.LENGTH_SHORT);
                                toast.show();
                                llenarSpinnerPredio();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }else {

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
                            }
                        }
                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }else {

                }
            }
        });

        return vista;
    }

    private void llenarSpinnerPredio() {
        Call<List<Ground>> call = predioSrv.getGrounds(competencia.getId());
        Log.d("call predio",call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    Log.d("encuentro response", Integer.toString(response.code()));

                    if(!response.body().isEmpty()) {
                        predios.clear();
                        Ground predio = new Ground(0, "Elije un predio", "", "");
                        predios.add(predio);
                        predios.addAll(response.body());
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPredio.setAdapter(adapter);
                        spinnerPredio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                predioSeleccionado = (Ground) spinnerPredio.getSelectedItem();
                                if (predioSeleccionado.getId() != 0) {
                                    llenarSpinnerCampo(predioSeleccionado.getId());

                                } else {
                                    btnCampo.setVisibility(View.INVISIBLE);
                                    spinnerCampo.setVisibility(View.INVISIBLE);
                                    msjCampos();
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
                            adapterCampo = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, campos);
                            adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCampo.setAdapter(adapterCampo);
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

        predios = new ArrayList<>();
        campos = new ArrayList<>();

        nombrePred = vista.findViewById(R.id.etNombrePredio);
        direPred = vista.findViewById(R.id.etDireccionPredio);
        ciudadPred = vista.findViewById(R.id.etCiudadPredio);

        nombreCampo = vista.findViewById(R.id.etCampoNombre);
        capacidadCampo = vista.findViewById(R.id.etCapacidadCampo);
        dimensionesCampo = vista.findViewById(R.id.etDimensionesCampo);

        btnPredio = vista.findViewById(R.id.btnAgregarPredio);
        btnCampo = vista.findViewById(R.id.btnAgregarCampo);

        spinnerPredio = vista.findViewById(R.id.spinnerCargaPredio);
        spinnerCampo = vista.findViewById(R.id.spinnerCargarCampo);

        camposSrv = new RetrofitAdapter().connectionEnable().create(FieldSrv.class);
        predioSrv = new RetrofitAdapter().connectionEnable().create(GroundSrv.class);

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

    private void msjCampos() {
        Field campo = new Field(0,"Sin campos",0,0,null);
        campos.clear();
        campos.add(campo);
        adapterCampo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,campos);
        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampo.setAdapter(adapterCampo);

    }
}

