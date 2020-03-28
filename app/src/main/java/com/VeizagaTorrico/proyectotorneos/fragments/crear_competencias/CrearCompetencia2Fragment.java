package com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Category;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.Sport;
import com.VeizagaTorrico.proyectotorneos.services.CategorySrv;
import com.VeizagaTorrico.proyectotorneos.services.SportsSrv;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

public class CrearCompetencia2Fragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private List<Category> categories;
    private Spinner spinnerDeporte, spinnerCategoria, spinnerFrecuencia;
    private TextView txtDescripcion;
    private SportsSrv sportsSrv;
    private Button sigBtn;
    private CategorySrv categorySrv;
    private Competition competencia;
    private View vista;

    public CrearCompetencia2Fragment() {
        // Required empty public constructor
    }

    public static CrearCompetencia2Fragment newInstance() {
        CrearCompetencia2Fragment fragment = new CrearCompetencia2Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        competencia = (Competition) getArguments().getSerializable("competition");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_crear_competencia2, container, false);
        initElement();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            llenarSpinnerFrecuencia();
            listeners();
            llenarSpinnerDeporte();
        }else {
            sinInternet();
        }

        return vista;
    }

    private void llenarSpinnerDeporte() {
        //En call viene el tipo de dato que espero del servidor
        Call<List<Sport>> call = sportsSrv.getSports();
        call.enqueue(new Callback<List<Sport>>() {
            @Override
            public void onResponse(Call<List<Sport>> call, Response<List<Sport>> response) {
                List<Sport> deportes;

                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    //asigno a deportes lo que traje del servidor
                    deportes = response.body();
                    Log.d("RESPONSE CODE",  Integer.toString(response.code()) );
                    // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
                    ArrayAdapter<Sport> adapterDeporte = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, deportes);
                    //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                    adapterDeporte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // seteo el adapter
                    spinnerDeporte.setAdapter(adapterDeporte);
                    // manejador del evento OnItemSelected

                    spinnerDeporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Sport dep;
                            //elemento obtenido del spinner lo asigno a deporte y traigo la lista de categories que tiene
                            dep = (Sport) spinnerDeporte.getSelectedItem();

                            llenarSpinnerCategoriaByServer(dep.getId());

                            //categories = dep.getCategories();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }
            }
            @Override
            public void onFailure(Call<List<Sport>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void listeners() {
        sigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competition", competencia);

                // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                Navigation.findNavController(vista).navigate(R.id.crearCompetencia3Fragment, bundle);
            }
        });
    }

    private void initElement() {
        competencia = (Competition) getArguments().getSerializable("competition");

        spinnerDeporte = vista.findViewById(R.id.spinnerDeporte);
        spinnerCategoria = vista.findViewById(R.id.spinnerCategoria);
        txtDescripcion = vista.findViewById(R.id.descripcionCategoria);
        sportsSrv = new RetrofitAdapter().connectionEnable().create(SportsSrv.class);
        sigBtn = vista.findViewById(R.id.btnCCSig_2);
        spinnerFrecuencia = vista.findViewById(R.id.spinner_frecuencia_creaComp);

    }

    private void llenarSpinnerFrecuencia() {
        List<Integer> frecuencia = new ArrayList<>();
        for(int i = 1 ; i <= 14 ; i++){
            frecuencia.add(i);
        }
        ArrayAdapter<Integer> adapterFrecuencia = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, frecuencia);
        adapterFrecuencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrecuencia.setAdapter(adapterFrecuencia);
        spinnerFrecuencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                competencia.setFrecuencia((Integer) spinnerFrecuencia.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



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
        sigBtn.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void llenarSpinnerCategoriaByServer(int idDeporte) {
        categorySrv = new RetrofitAdapter().connectionEnable().create(CategorySrv.class);

        Call<List<Category>> call = categorySrv.getCategoriasDeporte(idDeporte);

        //En call viene el tipo de dato que espero del servidor
        Log.d("request categoria", call.request().url().toString());
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categorias = new ArrayList<>();
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        //asigno a deportes lo que traje del servidor
                        categorias = response.body();

                        Log.d("RESPONSE CATEGORY CODE", Integer.toString(response.code()));
                        // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
                        ArrayAdapter<Category> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, categorias);
                        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // seteo el adapter
                        spinnerCategoria.setAdapter(adapter);
                        // manejador del evento OnItemSelected
                        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                // EN ESTA LINEA ES DONDE RECIBE EL DATO DE OTRO FRAGMENT
                                Category cat;
                                // para mostrar la descripcion de la categoria
                                cat = (Category) spinnerCategoria.getSelectedItem();
                                txtDescripcion.setText(cat.getDescripcion() + "\nDuracion de un partido aproximado: " + cat.getDuracion()+ " minutos.");
                                if(cat != null)
                                    competencia.setCategory(cat);
                                Log.d("A ver que trajo", competencia.getCategory().toString());
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
            public void onFailure(Call<List<Category>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



}