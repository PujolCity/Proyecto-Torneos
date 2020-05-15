package com.VeizagaTorrico.proyectotorneos.fragments.competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Category;
import com.VeizagaTorrico.proyectotorneos.models.Gender;
import com.VeizagaTorrico.proyectotorneos.models.Sport;
import com.VeizagaTorrico.proyectotorneos.models.TypesOrganization;
import com.VeizagaTorrico.proyectotorneos.services.CategorySrv;
import com.VeizagaTorrico.proyectotorneos.services.GenderSrv;
import com.VeizagaTorrico.proyectotorneos.services.SportsSrv;
import com.VeizagaTorrico.proyectotorneos.services.StatusSrv;
import com.VeizagaTorrico.proyectotorneos.services.TypesOrganizationSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;

public class FiltroFragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private Spinner spnnrDeporte, spnnrCategoria, spnnrOrganizacion, spnnrGenero, spnnrEstado;
    private EditText etNombre, etCiudad;
    private Button btnSiguiente;
    private TypesOrganizationSrv orgSrv;
    private SportsSrv sportsSrv;
    private GenderSrv genderSrv;
    private CategorySrv categorySrv;
    private StatusSrv apiStatusServicce;
    private List<Category> categorias;
    private Map <String,String> filtros;
    private int idUsuario;
    private String deporte,categoria,nombreCompetencia,organizacion,ciudad,genero, estado;
    private TextView tvSinConexion;
    private SwipeRefreshLayout refreshLayout;

    public FiltroFragment() {
    }

    public static FiltroFragment newInstance() {
        FiltroFragment fragment = new FiltroFragment();
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
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_filtro, container, false);
        initElements();
        refresh();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            tvSinConexion.setVisibility(View.GONE);
            llenarAllSpinners();
            listeners();
            refresh();
        } else {
            tvSinConexion.setVisibility(View.VISIBLE);
            sinInternet();
            refresh();
        }
        return vista;
    }

    private void llenarAllSpinners() {
        llenarSpinnerDeporte();
        llenarSpinnerCategoriaByServer(0);
        llenarSpinnerOrganizacion();
        llenarSpinnerGeneros();
        llenarSpinnerEstado();
    }

    private void refresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    tvSinConexion.setVisibility(View.GONE);
                    llenarAllSpinners();
                    btnSiguiente.setVisibility(View.VISIBLE);
                }
                else{
                    sinInternet();
                    tvSinConexion.setVisibility(View.VISIBLE);
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void listeners() {
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nombreCompetencia = etNombre.getText().toString();
                    if(!nombreCompetencia.isEmpty())
                        filtros.put("competencia",nombreCompetencia);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(!deporte.isEmpty()){
                        filtros.put("deporte",deporte);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {

                    if(!categoria.isEmpty()){
                        filtros.put("categoria",categoria);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    ciudad = etCiudad.getText().toString();
                    if(!ciudad.isEmpty())
                        filtros.put("ciudad",ciudad);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(!organizacion.isEmpty()){
                        filtros.put("tipo_organizacion",organizacion);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(!genero.isEmpty()){
                        filtros.put("genero",genero);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(!estado.isEmpty()){
                        filtros.put("estado",estado);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("filtros",(Serializable) filtros);
                Navigation.findNavController(vista).navigate(R.id.competenciasListFragment, bundle);
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
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento no se podran filtrar ni ver las competencias. Por favor intente mas tarde", Toast.LENGTH_LONG);
        toast.show();
        btnSiguiente.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void initElements() {
        refreshLayout = vista.findViewById(R.id.refreshFiltro);
        Category category = new Category(0,"Seleccione..."," ",0,0);
        idUsuario = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(getContext(), FILE_SHARED_DATA_USER, KEY_ID));

        categorias = new ArrayList<>();
        categorias.add(category);

        filtros = new HashMap<>();
        filtros.put("idUsuario",Integer.toString(idUsuario));

        categorySrv = new RetrofitAdapter().connectionEnable().create(CategorySrv.class);
        genderSrv = new RetrofitAdapter().connectionEnable().create(GenderSrv.class);
        orgSrv = new RetrofitAdapter().connectionEnable().create(TypesOrganizationSrv.class);
        sportsSrv = new RetrofitAdapter().connectionEnable().create(SportsSrv.class);

        etCiudad = vista.findViewById(R.id.etCiudad);
        etNombre = vista.findViewById(R.id.etNombre);
        spnnrCategoria = vista.findViewById(R.id.spnnrCategoria);
        spnnrDeporte= vista.findViewById(R.id.spnnrDeporte);
        spnnrGenero = vista.findViewById(R.id.spnnrGenero);
        spnnrOrganizacion = vista.findViewById(R.id.spnnrOrganizacion);
        spnnrEstado = vista.findViewById(R.id.spnnr_estado_filtro);

        btnSiguiente = vista.findViewById(R.id.btnFiltrar);
        tvSinConexion = vista.findViewById(R.id.tv_sin_conexion_filtro_comp);
    }

    private void llenarSpinnerOrganizacion(){
        Call<List<TypesOrganization>> call = orgSrv.getTypesOrganization();
        call.enqueue(new Callback<List<TypesOrganization>>() {
            @Override
            public void onResponse(Call<List<TypesOrganization>> call, Response<List<TypesOrganization>> response) {
                List<TypesOrganization> tipos = new ArrayList<>();
                TypesOrganization tipo = new TypesOrganization(0,"Seleccione..","");
                tipos.add(tipo);
                if(response.code() == 200) {
                    try {
                        tipos.addAll(response.body());
                        Log.d("RESPONSECODE OrgActvty", Integer.toString(response.code()));
                        ArrayAdapter<TypesOrganization> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, tipos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrOrganizacion.setAdapter(adapter);
                        spnnrOrganizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                TypesOrganization org = (TypesOrganization) spnnrOrganizacion.getSelectedItem();
                                Log.d("ItemSelected", org.getName());
                                if(org.getId() != 0){
                                    organizacion = Integer.toString(org.getId());
                                }else {
                                    organizacion = "";
                                }
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
            public void onFailure(Call<List<TypesOrganization>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(getContext(), "Por favor recargar pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
        });

    }

    private void llenarSpinnerDeporte() {
        //En call viene el tipo de dato que espero del servidor
        Call<List<Sport>> call = sportsSrv.getSports();
        call.enqueue(new Callback<List<Sport>>() {
            @Override
            public void onResponse(Call<List<Sport>> call, Response<List<Sport>> response) {
                List<Sport> deportes = new ArrayList<>();
                Sport sport = new Sport(0,"Seleccione...");
                deportes.add(sport);
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        //asigno a deportes lo que traje del servidor
                        deportes.addAll(response.body());
                        Log.d("RESPONSE CODE",  Integer.toString(response.code()) );
                        // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
                        ArrayAdapter<Sport> adapterDeporte = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, deportes);
                        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                        adapterDeporte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // seteo el adapter
                        spnnrDeporte.setAdapter(adapterDeporte);
                        // manejador del evento OnItemSelected

                        spnnrDeporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Sport dep;
                                //elemento obtenido del spinner lo asigno a deporte y traigo la lista de categories que tiene
                                dep = (Sport) spnnrDeporte.getSelectedItem();
                                if(dep.getId() != 0){
                                    categorias = dep.getCategories();
                                    llenarSpinnerCategoriaByServer(dep.getId());
                                    deporte = Integer.toString(dep.getId());
                                } else {
                                    deporte = "";
                                    llenarSpinnerCategoriaByServer(0);
                                }
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
            public void onFailure(Call<List<Sport>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarSpinnerCategoriaByServer(int idDeporte) {
        Call<List<Category>> call;
        if(idDeporte == 0){
            call = categorySrv.getCategorias();
        }else {
            call = categorySrv.getCategoriasDeporte(idDeporte);
        }
        //En call viene el tipo de dato que espero del servidor
        Log.d("request categoria", call.request().url().toString());
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categorias = new ArrayList<>();
                Category category = new Category(0,"Seleccione..."," ",0,0);
                categorias.add(category);

                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        //asigno a deportes lo que traje del servidor
                        categorias.addAll(response.body());
                        Log.d("RESPONSE CATEGORY CODE", response.body().toString() );//Integer.toString(response.code())
                        // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
                        ArrayAdapter<Category> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, categorias);
                        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // seteo el adapter
                        spnnrCategoria.setAdapter(adapter);
                        // manejador del evento OnItemSelected
                        spnnrCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Category category;
                                category = (Category) spnnrCategoria.getSelectedItem();
                                if(category.getId() != 0){
                                    categoria = Integer.toString(category.getId());
                                }else {
                                    categoria = "";
                                }
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
                    Toast toast = Toast.makeText(getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void llenarSpinnerGeneros() {
        //En call viene el tipo de dato que espero del servidor
        Call<List<Gender>> call = genderSrv.getGenders();
        Log.d("request gender", call.request().url().toString());
        call.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
                List<Gender> generos = new ArrayList<>();
                Gender gender = new Gender("Seleccione...");
                generos.add(gender);

                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        //asigno a deportes lo que traje del servidor
                        generos.addAll(response.body());
                        Log.d("RESPONSE GENDER CODE",  Integer.toString(response.code()));
                        // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, generos);
                        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // seteo el adapter
                        spnnrGenero.setAdapter(adapter);

                        // manejador del evento OnItemSelected
                        spnnrGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Gender gndr;
                                Gender gender = new Gender("Seleccione...");
                                gndr = (Gender) spnnrGenero.getSelectedItem();
                                if(!gndr.equals(gender)){
                                    genero = "'" + gndr.getNombre() + "'" ;
                                }else {
                                    genero = "";
                                }
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
                try {
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void llenarSpinnerEstado(){
        apiStatusServicce = new RetrofitAdapter().connectionEnable().create(StatusSrv.class);
        Call<List<Gender>> call = apiStatusServicce.getStatus();
        Log.d("Call Estados",call.request().url().toString());
        call.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
                List<Gender> estados = new ArrayList<>();
                Gender gender = new Gender("Seleccione...");
                estados.add(gender);
                try{
                    if(!response.body().isEmpty()){
                        estados.addAll(response.body());
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, estados);
                        adapter.setDropDownViewResource(R.layout.item_spinner_custom);
                        spnnrEstado.setAdapter(adapter);
                        spnnrEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Gender status;
                                Gender gender = new Gender("Seleccione...");
                                status = (Gender) spnnrEstado.getSelectedItem();
                                if(!status.equals(gender)){
                                    estado = "'" + status.getNombre() + "'" ;
                                }else {
                                    estado = "";
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
            public void onFailure(Call<List<Gender>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
