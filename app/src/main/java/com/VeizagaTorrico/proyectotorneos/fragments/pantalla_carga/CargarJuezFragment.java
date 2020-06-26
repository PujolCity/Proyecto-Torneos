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
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.RefereeSrv;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargarJuezFragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private CompetitionMin competencia;
    private View vista;
    private EditText etNombre, etApellido, etDNI;
    private String nombre, apellido, dni;
    private Button btnCrear;
    private RefereeSrv refereeSrv;
    private Map<String,String> datos;
    private List<Referee> jueces;


    public CargarJuezFragment() {
        // Required empty public constructor
    }

    public static CargarJuezFragment newInstance() {
        CargarJuezFragment fragment = new CargarJuezFragment();
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
        vista = inflater.inflate(R.layout.fragment_cargar_juez, container, false);
        initElements();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            // llenarSpinnerJuez();
            listeners();
        } else {
            sinInternet();
        }
        return vista;
    }

    private void listeners() {
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = etNombre.getText().toString();
                apellido = etApellido.getText().toString();
                dni = etDNI.getText().toString();
                if(validar()){
                    datos.put("nombre",nombre);
                    datos.put("apellido",apellido);
                    datos.put("dni", dni);
                    datos.put("idCompetencia",Integer.toString(competencia.getId()));
                    Call<Success> call = refereeSrv.createReferee(datos);
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            if(response.code() == 201){
                                Log.d("Juez Cargado", "exito");
                                // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                                Toast toast = Toast.makeText(vista.getContext(), "Juez Cargado!", Toast.LENGTH_SHORT);
                                toast.show();
                                // llenarSpinnerJuez();
                            }
                        }
                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }else {
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor completar todos los campos", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        etNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNombre.setText(null);
            }
        });
        etApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etApellido.setText(null);
            }
        });
        etDNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDNI.setText(null);
            }
        });
    }

    private void eliminarJuez(int idJuez) {
        Call<Success> call = refereeSrv.deleteReferee(idJuez);
        Log.d("Call Juez",call.request().url().toString());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if(response.code() == 200){
                    jueces.clear();
                    //llenarSpinnerJuez();
                    Toast toast = Toast.makeText(vista.getContext(), "Juez Eliminado!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

//    private void actualizarDatos(Referee juez) {
//        try {
//            etNombre.setText(juez.getNombre());
//            etApellido.setText(juez.getApellido());
//            etDNI.setText(Integer.toString(juez.getDni()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private boolean validar() {
        if(nombre.isEmpty())
            return false;
        if(apellido.isEmpty())
            return false;
        if(dni.isEmpty())
            return false;
        return true;
    }

    private void initElements() {
        datos = new HashMap<>();
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");
        refereeSrv = new RetrofitAdapter().connectionEnable().create(RefereeSrv.class);
        jueces = new ArrayList<>();

        btnCrear = vista.findViewById(R.id.btnAgregarJuez);

        etNombre = vista.findViewById(R.id.etNombreJuez);
        etApellido = vista.findViewById(R.id.etApellidoJuez);
        etDNI = vista.findViewById(R.id.etDNIJuez);

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
        btnCrear.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
