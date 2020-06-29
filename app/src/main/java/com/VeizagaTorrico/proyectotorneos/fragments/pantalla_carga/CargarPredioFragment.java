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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
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
    private String nombreBuscarPredio, predioNombre, predioDire, predioCiudad, campoNombre, campoCapacidad,campoDimensiones;
    private EditText etDirePredio, etNombrePredio, nombreCampo, capacidadCampo, dimensionesCampo, etBuscarCiudadPredio, etBuscarPredio;
    private Button btnCrearPredio;
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
//    private ImageButton btnDeleteCampo, ibBuscarCiudad, ibBuscarPredio;
    private ImageButton ibBuscarCiudad, ibBuscarPredio;
    private Button btnNuevoCampo, btnCrearCampo, btnCancelarNuevo;
    private TextView tvSinRdosCiudad, tvSinRdosPredio;
    private LinearLayout linRdosBuscarCiudad, linRdosBuscarPredio, linSelecCampo, linDatosCampo, linBtnAdminCampos;
    private boolean creandoCampo;
    private AlertDialog alertDialog;

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
            listeners();
        }else {
            sinInternet();
        }
        return vista;
    }

    // ponemos a la escucha los botones de accion de la pantalla
    private void listeners() {
        btnCrearPredio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPredio();
            }
        });

        btnNuevoCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creandoCampo = true;
                linDatosCampo.setVisibility(View.VISIBLE);
                setDataAdminCampo();
            }
        });

        btnCancelarNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creandoCampo = false;
                resetDataAdminCampo();
            }
        });

        btnCrearCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCampo();
            }
        });

//        btnDeleteCampo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                eliminarCampo();
//            }
//        });

        ibBuscarCiudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarCiudad();
            }
        });

        ibBuscarPredio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarPredio();
            }
        });

    }

    private void setDataAdminCampo() {
        spinnerCampo.setSelection(0);
        setBtnAdminCampo();
        resetDataCampo();
        camposEditable();
    }

    private void resetDataCampo(){
        nombreCampo.setText("");
        capacidadCampo.setText("");
        dimensionesCampo.setText("");
    }

    private void setBtnAdminCampo() {
        btnNuevoCampo.setVisibility(View.GONE);
        btnCancelarNuevo.setVisibility(View.VISIBLE);
        btnCrearCampo.setVisibility(View.VISIBLE);
    }

    private void resetDataAdminCampo() {
        spinnerCampo.setSelection(0);
        linDatosCampo.setVisibility(View.GONE);
        resetBtnAdminCampoInicio();
        actualizarDatosCampo(null);
    }

    private void buscarPredio() {
        nombreBuscarPredio = etBuscarPredio.getText().toString();
        if(!nombreBuscarPredio.isEmpty()){
            alertDialog.show();
            Call<List<Ground>> call = predioSrv.findLikeName(nombreBuscarPredio);
            Log.d("call predio",call.request().url().toString());
            call.enqueue(new Callback<List<Ground>>() {
                @Override
                public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                    alertDialog.dismiss();
                    Log.d("encuentro response", Integer.toString(response.code()));
                    if(!response.body().isEmpty()) {
                        tvSinRdosPredio.setVisibility(View.GONE);
                        linRdosBuscarPredio.setVisibility(View.VISIBLE);
                        predios.clear();
                        predios.add(new Ground(0, "Seleccione un predio", "Direccion", "Ciudad"));
                        predios.addAll(response.body());
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPredio.setAdapter(adapter);
                        spinnerPredio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i != 0){
                                    predioSeleccionado = (Ground) spinnerPredio.getSelectedItem();
                                    llenarSpinnerCampo(predioSeleccionado.getId());
                                    resetBtnAdminCampoInicio();
                                }
                                else{
                                    linBtnAdminCampos.setVisibility(View.GONE);
                                    linDatosCampo.setVisibility(View.GONE);
                                    linSelecCampo.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        linBtnAdminCampos.setVisibility(View.GONE);
                        linSelecCampo.setVisibility(View.GONE);
                        tvSinRdosPredio.setVisibility(View.VISIBLE);
                        linRdosBuscarPredio.setVisibility(View.GONE);
                        linSelecCampo.setVisibility(View.GONE);
                        linDatosCampo.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onFailure(Call<List<Ground>> call, Throwable t) {
                    alertDialog.dismiss();
                    linBtnAdminCampos.setVisibility(View.GONE);
                    linSelecCampo.setVisibility(View.GONE);
                    tvSinRdosPredio.setVisibility(View.VISIBLE);
                    linRdosBuscarPredio.setVisibility(View.GONE);
                    linSelecCampo.setVisibility(View.GONE);
                    linDatosCampo.setVisibility(View.GONE);
                    Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(vista.getContext(), "Debe introducir un nombre para poder realizar la busqueda", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarCiudad() {
        predioCiudad = etBuscarCiudadPredio.getText().toString();
        if(!predioCiudad.isEmpty()){
            alertDialog.show();
            Call<List<City>> call = citySrv.buscarCiudad(predioCiudad);
            Log.d("URL BUSC CIUDAD", call.request().url().toString());
            call.enqueue(new Callback<List<City>>() {
                @Override
                public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                    alertDialog.dismiss();
                    try {
                        ciudades.clear();
                        if(response.code() == 200){
                            if(response.body() != null){
                                ciudades.addAll(response.body());
                            }
                            mostrarSeccionCiudadesEncontradas();
                            adapterCiudades();
                            spnnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    ciudadSeleccionada = (City) spnnerCiudad.getSelectedItem();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                        if(response.code() == 400){
                            tvSinRdosCiudad.setVisibility(View.VISIBLE);
                            linRdosBuscarCiudad.setVisibility(View.GONE);
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
                    alertDialog.dismiss();
                    Toast.makeText(getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT).show();
                    Log.d("onFailure", t.getMessage());
                }
            });
        }
        else {
            Toast.makeText(vista.getContext(), "Debe introducir un nombre para poder realizar la busqueda", Toast.LENGTH_SHORT).show();
        }
    }

    private void crearCampo() {
        campoNombre = nombreCampo.getText().toString();
        campoCapacidad = capacidadCampo.getText().toString();
        campoDimensiones = dimensionesCampo.getText().toString();

        if (validarCampo()) {
            campo.put("nombre", campoNombre);
            campo.put("idPredio", Integer.toString(predioSeleccionado.getId()));
            campo.put("dimensiones", campoDimensiones);
            campo.put("capacidad", campoCapacidad);
            Log.d("body campo", predio.toString());

            Call<Success> call = camposSrv.createField(campo);
            Log.d("Call url", call.request().url().toString());
            alertDialog.show();
            call.enqueue(new Callback<Success>() {
                @Override
                public void onResponse(Call<Success> call, Response<Success> response) {
                    alertDialog.dismiss();
                    Log.d("Response Campo", Integer.toString(response.code()));
                    if (response.code() == 201) {
                        Log.d("CAMPO_CREATE", "exito");
                        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                        Toast.makeText(vista.getContext(), "Campo Cargado!", Toast.LENGTH_SHORT).show();
                        llenarSpinnerCampo(predioSeleccionado.getId());
                        creandoCampo = false;
                        resetDataAdminCampo();
                    } else {
                        Toast.makeText(vista.getContext(), "Campo ya cargado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Success> call, Throwable t) {
                    alertDialog.dismiss();
                    Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(vista.getContext(), "Por favor completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void crearPredio() {
        predioNombre = etNombrePredio.getText().toString();
        predioDire = etDirePredio.getText().toString();

        if(validarPredio()){
            predio.put("nombre", predioNombre);
            predio.put("direccion", predioDire);
            predio.put("ciudad",Integer.toString(ciudadSeleccionada.getId()));

            Log.d("body predio", predio.toString());

            alertDialog.show();
            Call<Success> call = predioSrv.createGround(predio);
            Log.d("Call url", call.request().url().toString());
            call.enqueue(new Callback<Success>() {
                @Override
                public void onResponse(Call<Success> call, Response<Success> response) {
                    Log.d("Response Predio", Integer.toString(response.code()));
                    alertDialog.dismiss();

                    if(response.code() == 201){
                        Log.d("Predio Cargado", "exito");
                        resetPostCrearPredio();
                        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                        Toast.makeText(vista.getContext(), "Predio creado con exito.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(vista.getContext(), "El predio ya existe", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Success> call, Throwable t) {
                    alertDialog.dismiss();
                    Toast.makeText(vista.getContext(), "problemas con el servidor", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(vista.getContext(), "Por favor completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarSeccionCiudadesEncontradas() {
        linRdosBuscarCiudad.setVisibility(View.VISIBLE);
        tvSinRdosCiudad.setVisibility(View.GONE);
    }

    private void resetPostCrearPredio() {
        ciudadSeleccionada = null;
        etBuscarCiudadPredio.setText(null);
        etNombrePredio.setText("");
        etDirePredio.setText("");
        linRdosBuscarCiudad.setVisibility(View.GONE);
        ciudades = new ArrayList<>();
        adapterCiudades();
    }

    private void adapterCiudades() {
        ArrayAdapter<City> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, ciudades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnnerCiudad.setAdapter(adapter);
    }


//    private void eliminarCampo() {
//        if(campoSeleccionado.getId() == 0){
//            Toast.makeText(vista.getContext(), "Seleccione uno de los campos disponibles", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            delete.put("idPredio", predioSeleccionado.getId());
//            delete.put("idCampo", campoSeleccionado.getId());
//
//            Call<Success> call = camposSrv.deleteField(delete);
//            Log.d("url delete campo", call.request().url().toString());
//            alertDialog.show();
//            call.enqueue(new Callback<Success>() {
//                @Override
//                public void onResponse(Call<Success> call, Response<Success> response) {
//                    alertDialog.dismiss();
//                    creandoCampo = false;
//                    if(response.code() == 200){
//                        campos.clear();
//                        llenarSpinnerCampo(predioSeleccionado.getId());
//                        setDataAdminCampo();
//                        Toast.makeText(vista.getContext(), "Campo eliminado con exito.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void onFailure(Call<Success> call, Throwable t) {
//                    alertDialog.dismiss();
//                    Toast.makeText(vista.getContext(), "Problemas con el servidor.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

    private void llenarSpinnerCampo(int idPredio) {
        linSelecCampo.setVisibility(View.VISIBLE);
        if(idPredio != 0){
            linDatosCampo.setVisibility(View.GONE);
            Call<List<Field>> call = camposSrv.getFieldsByGround(idPredio);
            Log.d("calls campo",call.request().url().toString());
            call.enqueue(new Callback<List<Field>>() {
                @Override
                public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                    if(!response.body().isEmpty()) {
                        campos.clear();
                        campos.add(new Field(0,"Seleccione un campo",0,0, null));
                        campos.addAll(response.body());
                        adapterCampo = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, campos);
                        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCampo.setAdapter(adapterCampo);
                        spinnerCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                campoSeleccionado = (Field) spinnerCampo.getSelectedItem();
                                if(i != 0){
                                    resetBtnAdminCampoInicio();
                                    linDatosCampo.setVisibility(View.VISIBLE);
                                    actualizarDatosCampo(campoSeleccionado);
                                }
                                else{
                                    if(!creandoCampo){
                                        resetDataCampo();
                                        linDatosCampo.setVisibility(View.GONE);
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        msjCampos();
                    }
                }
                @Override
                public void onFailure(Call<List<Field>> call, Throwable t) {
                    Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
           msjCampos();
        }
    }

    private void resetBtnAdminCampoInicio() {
        linBtnAdminCampos.setVisibility(View.VISIBLE);
        btnCancelarNuevo.setVisibility(View.GONE);
        btnCrearCampo.setVisibility(View.GONE);
        btnNuevoCampo.setVisibility(View.VISIBLE);
    }

    private void actualizarDatosCampo(Field campo) {
        if(campo != null){
            nombreCampo.setText(campo.getNombre());
            capacidadCampo.setText(Integer.toString(campo.getCapacidad()));
            dimensionesCampo.setText(Integer.toString(campo.getDimensiones()));
        }
        else{
            resetDataCampo();
        }
        camposNoEditable();
    }

    private void camposNoEditable() {
        nombreCampo.setEnabled(false);
        capacidadCampo.setEnabled(false);
        dimensionesCampo.setEnabled(false);
    }

    private void camposEditable() {
        nombreCampo.setEnabled(true);
        capacidadCampo.setEnabled(true);
        dimensionesCampo.setEnabled(true);
    }

    private boolean validarPredio() {
        if(predioNombre.isEmpty())
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

        etNombrePredio = vista.findViewById(R.id.etNombrePredio);
        etDirePredio = vista.findViewById(R.id.etDireccionPredio);
        etBuscarCiudadPredio = vista.findViewById(R.id.et_buscar_ciudad_predio);
        etBuscarPredio = vista.findViewById(R.id.et_buscar_predio);

        nombreCampo = vista.findViewById(R.id.etCampoNombre);
        capacidadCampo = vista.findViewById(R.id.etCapacidadCampo);
        dimensionesCampo = vista.findViewById(R.id.etDimensionesCampo);
        creandoCampo = false;

        btnCrearPredio = vista.findViewById(R.id.btnAgregarPredio);
        ibBuscarCiudad = vista.findViewById(R.id.ib_buscar_ciudad_predio);
        ibBuscarPredio = vista.findViewById(R.id.ib_buscar_predio);

        btnNuevoCampo = vista.findViewById(R.id.btnNuevoCampo);
        btnCrearCampo = vista.findViewById(R.id.btnAgregarCampo);
        btnCancelarNuevo = vista.findViewById(R.id.btnCancelarNuevoCampo);
        tvSinRdosCiudad = vista.findViewById(R.id.tv_sin_rdos_buscar_ciudad);
        tvSinRdosPredio =  vista.findViewById(R.id.tv_sin_rdos_buscar_predios);
        linRdosBuscarCiudad = vista.findViewById(R.id.lin_rdos_buscar_ciudad_predio);
        linRdosBuscarPredio  = vista.findViewById(R.id.lin_rdos_buscar_predio);
        linSelecCampo  = vista.findViewById(R.id.lin_sel_campo);
        linDatosCampo = vista.findViewById(R.id.lin_datos_campo);
        linBtnAdminCampos = vista.findViewById(R.id.lin_btn_admin_campo);
        tvSinRdosCiudad.setVisibility(View.GONE);
        tvSinRdosPredio.setVisibility(View.GONE);
        linRdosBuscarCiudad.setVisibility(View.GONE);
        linRdosBuscarPredio.setVisibility(View.GONE);
        linSelecCampo.setVisibility(View.GONE);
        linDatosCampo.setVisibility(View.GONE);
        resetBtnAdminCampoInicio();
        linBtnAdminCampos.setVisibility(View.GONE);

//        btnDeleteCampo = vista.findViewById(R.id.btnDeleteCampo);
        spinnerPredio = vista.findViewById(R.id.spinnerCargaPredio);
        spinnerCampo = vista.findViewById(R.id.spinnerCargarCampo);
        spnnerCiudad = vista.findViewById(R.id.spinner_ciudad_predio);

        camposSrv = new RetrofitAdapter().connectionEnable().create(FieldSrv.class);
        predioSrv = new RetrofitAdapter().connectionEnable().create(GroundSrv.class);
        citySrv = new RetrofitAdapter().connectionEnable().create(CitySrv.class);

        alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Espere un momento ..");
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
        Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento quedaran deshabilitadas algunas funciones.", Toast.LENGTH_LONG).show();
        btnCrearPredio.setVisibility(View.INVISIBLE);
        btnCrearCampo.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void msjCampos() {
        Field campo = new Field(0,"Sin campos",0,0,null);
//        btnDeleteCampo.setVisibility(View.INVISIBLE);
        campos.clear();
        campos.add(campo);
        adapterCampo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,campos);
        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampo.setAdapter(adapterCampo);
    }
}