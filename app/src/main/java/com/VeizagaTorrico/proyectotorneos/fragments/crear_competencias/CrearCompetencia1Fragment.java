package com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.City;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.Gender;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.CitySrv;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.GenderSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrearCompetencia1Fragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

   // ***************************************** //
    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    private String fecha_ini = "";

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    private TextView txtFecha, tvSelCiudad;
    private ImageButton ibObtenerFecha;
    private EditText txtNmbComp,etCiudad;
    private Spinner spnrGenero,spnnerCiudad;
    private Button btnSig;
    private String nombreComp, ciudad;
    private Competition competition;
    private CompetitionSrv competitionSrv;
    private View vista;
    private GenderSrv genderSrv;
    private CitySrv citySrv;
    private List<City> ciudades;
    private Gender genero;
    private TextView tvSinConexion;
    private ImageButton ibBuscar;
    private City ciudadSeleccionada;
    private AlertDialog alertDialog;

    public CrearCompetencia1Fragment() {
        // Required empty public constructor
    }

    public static CrearCompetencia1Fragment newInstance(String param1, String param2) {
        CrearCompetencia1Fragment fragment = new CrearCompetencia1Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_competencia1, container, false);
        initElements();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            tvSinConexion.setVisibility(View.GONE);
            llenarSpinnerGeneros();
            listeners();
        }else {
            tvSinConexion.setVisibility(View.VISIBLE);
            sinInternet();
        }
        return vista;
    }

    private void listeners() {
        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });
        btnSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreComp = txtNmbComp.getText().toString();
//                ciudad = etCiudad.getText().toString();

                Log.d("dato enviado",nombreComp);
                Call<Success> call = competitionSrv.comprobar(nombreComp);
                call.enqueue(new Callback<Success>() {
                    @Override
                    public void onResponse(Call<Success> call, Response<Success> response) {
                        Log.d("onResponse", Integer.toString(response.code()));
                        if ( response.code() == 200 && !response.body().isExiste()) {
                            if(validar()){
                                try{
                                    Bundle bundle = new Bundle();
                                    competition.setName(txtNmbComp.getText().toString());
                                    competition.setFechaInicio(fecha_ini);
                                    competition.setGenero(genero.getNombre());
                                    competition.setCiudad(ciudadSeleccionada);
                                    bundle.putSerializable("competition", competition);
                                    reset();
                                    // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                                    Navigation.findNavController(vista).navigate(R.id.crearCompetencia2Fragment, bundle);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Toast toast = Toast.makeText(vista.getContext(), "Por favor revisar que esten bien completos los datos", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }else {
                            Toast toast = Toast.makeText(vista.getContext(), "Competencia Existente", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Success> call, Throwable t) {
                       try {
                           Toast toast = Toast.makeText(getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                           toast.show();
                           Log.d("onFailure", t.getMessage());
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                    }
                });
            }
        });
        ibBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                ciudad = etCiudad.getText().toString();
                if(!ciudad.isEmpty()){
                    Call<List<City>> call = citySrv.buscarCiudad(ciudad);
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
                                    if(response.body().size() > 0){
                                        tvSelCiudad.setVisibility(View.VISIBLE);
                                        tvSelCiudad.setText("Seleccione una ciudad");
                                        spnnerCiudad.setVisibility(View.VISIBLE);
                                        mostrarCiudades();
                                    }
                                }
                                if(response.code() == 400){
                                    Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.errorBody().string());
                                        String userMessage = jsonObject.getString("messaging");
                                        Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                                        tvSelCiudad.setVisibility(View.VISIBLE);
                                        tvSelCiudad.setText("No se han encontrado resultados. Intente con otro nombre.");
                                        spnnerCiudad.setVisibility(View.GONE);
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
                } else {
                    alertDialog.dismiss();
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor complete el campo Ciudad", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void reset() {
        ciudadSeleccionada = null;
        etCiudad.setText(null);
        ciudad = "";
    }

    private void mostrarCiudades() {
        ArrayAdapter<City> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, ciudades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnnerCiudad.setAdapter(adapter);

        spnnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ciudadSeleccionada = (City) spnnerCiudad.getSelectedItem();
                ciudad = ciudadSeleccionada.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean validar() {
        if(ciudadSeleccionada == null)
            return false;
        if(nombreComp.isEmpty())
            return false;
        if(fecha_ini.isEmpty())
            return false;
        return true;
    }

      private void initElements(){
          competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
          genderSrv = new RetrofitAdapter().connectionEnable().create(GenderSrv.class);
          citySrv = new RetrofitAdapter().connectionEnable().create(CitySrv.class);

          ciudades = new ArrayList<>();
          competition =  new Competition();
          ibBuscar = vista.findViewById(R.id.ib_buscar_compe);
          spnnerCiudad = vista.findViewById(R.id.spinner_ciudad_compe);
          btnSig = vista.findViewById(R.id.btnCCSig_1);
          txtNmbComp = vista.findViewById(R.id.etNmbComp);
          tvSelCiudad = vista.findViewById(R.id.tv_rdos_buscar);
          tvSelCiudad.setVisibility(View.GONE);
          spnnerCiudad.setVisibility(View.GONE);
          //Widget TextView donde se mostrara la fecha obtenida
          txtFecha = vista.findViewById(R.id.etfechaComp);
          //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
          ibObtenerFecha = vista.findViewById(R.id.ib_obtener_fecha);
          //Evento setOnClickListener - clic
          etCiudad = vista.findViewById(R.id.etCiudad);
          spnrGenero = vista.findViewById(R.id.spinnerGenero);
          tvSinConexion = vista.findViewById(R.id.tv_sin_conexion_create);
          alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Espere un momento..");
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
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento no se podran crear competencias.", Toast.LENGTH_LONG);
        toast.show();
        btnSig.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void obtenerFecha(){
        DatePickerDialog datePicker = new DatePickerDialog(vista.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                String mes = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf( mesActual+1);
                //Muestro la fecha con el formato deseado
                c.set(year,mesActual ,dayOfMonth);
                txtFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                fecha_ini = year + "-" + mesFormateado + "-" + diaFormateado;
//                fecha_fin = year + "-" + mes + "-" + diaFormateado;

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /*
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        datePicker.show();
    }

    private void llenarSpinnerGeneros() {
        //En call viene el tipo de dato que espero del servidor
        Call<List<Gender>> call = genderSrv.getGenders();
        Log.d("request gender", call.request().url().toString());
        call.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
                List<Gender> generos;
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        //asigno a deportes lo que traje del servidor
                        generos = response.body();
                        Log.d("RESPONSE GENDER CODE",  Integer.toString(response.code()));
                        // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, generos);
                        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // seteo el adapter
                        spnrGenero.setAdapter(adapter);

                        // manejador del evento OnItemSelected
                        spnrGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Gender gender;
                                genero = (Gender) spnrGenero.getSelectedItem();
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
            public void onFailure(Call<List<Gender>> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

}