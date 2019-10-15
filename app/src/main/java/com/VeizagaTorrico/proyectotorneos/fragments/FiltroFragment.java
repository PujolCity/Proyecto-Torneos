package com.VeizagaTorrico.proyectotorneos.fragments;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.VeizagaTorrico.proyectotorneos.services.TypesOrganizationSrv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltroFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CheckBox checkNombre, checkDeporte, checkCategoria,checkOrganizacion,checkCiudad,checkGenero;
    private Spinner spnnrDeporte, spnnrCategoria, spnnrOrganizacion, spnnrGenero;
    private EditText etNombre, etCiudad;
    private Button btnSiguiente;
    private TypesOrganizationSrv orgSrv;
    private SportsSrv sportsSrv;
    private GenderSrv genderSrv;
    private CategorySrv categorySrv;
    private List<Category> categorias;
    private Map <String,String> filtros;
    private String deporte,categoria,nombreCompetencia,organizacion,ciudad,genero;

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

        checkNombre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                etNombre.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        checkDeporte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spnnrDeporte.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                sportsSrv = new RetrofitAdapter().connectionEnable().create(SportsSrv.class);
                if(checkDeporte.isChecked()){
                    llenarSpinnerDeporte();
                }else {
                    if(checkCategoria.isChecked()){
                        llenarSpinnerCategoriaByServer();
                    }
                }
            }
        });

        checkCategoria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spnnrCategoria.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                categorySrv = new RetrofitAdapter().connectionEnable().create(CategorySrv.class);

                if(checkDeporte.isChecked()){
                    llenarSpinnerCategoriaByDeporte();
                }else {
                    llenarSpinnerCategoriaByServer();
                }
            }
        });

        checkCiudad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                etCiudad.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        checkOrganizacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spnnrOrganizacion.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                orgSrv = new RetrofitAdapter().connectionEnable().create(TypesOrganizationSrv.class);
                llenarSpinnerOrganizacion();

            }
        });

        checkGenero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spnnrGenero.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                genderSrv = new RetrofitAdapter().connectionEnable().create(GenderSrv.class);

                llenarSpinnerGeneros();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNombre.isChecked()) {
                    try {
                        nombreCompetencia = etNombre.getText().toString();
                        if(!nombreCompetencia.isEmpty())
                            filtros.put("competencia",nombreCompetencia);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(checkDeporte.isChecked()){
                    filtros.put("deporte",deporte);
                }
                if(checkCategoria.isChecked()){
                    filtros.put("categoria",categoria);
                }
                if(checkCiudad.isChecked()){
                    try{
                        ciudad = etCiudad.getText().toString();
                        if(!ciudad.isEmpty())
                            filtros.put("ciudad",ciudad);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(checkOrganizacion.isChecked()){
                    filtros.put("tipo_organizacion",organizacion);
                }
                if(checkGenero.isChecked()){
                    filtros.put("genero",genero);
                }
                if(validar()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("filtros",(Serializable) filtros);
                    Navigation.findNavController(vista).navigate(R.id.competenciasListFragment, bundle);
                }else {
                    Toast toast = Toast.makeText(getContext(), "Por favor complete los campos vacios", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return vista;
    }

    private boolean validar() {
        if(checkNombre.isChecked() && nombreCompetencia.isEmpty())
            return  false;
        if(checkCiudad.isChecked() && ciudad.isEmpty())
            return false;

        return true;
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

    private void initElements() {
        categorias = new ArrayList<>();
        filtros = new HashMap<>();

        checkNombre = vista.findViewById(R.id.checkNombre);
        checkDeporte = vista.findViewById(R.id.checkDeporte);
        checkCategoria = vista.findViewById(R.id.checkCategoria);
        checkCiudad= vista.findViewById(R.id.checkCiudad);
        checkOrganizacion = vista.findViewById(R.id.checkOrganizacion);
        checkGenero = vista.findViewById(R.id.checkGenero);
        etCiudad = vista.findViewById(R.id.etCiudad);
        etNombre = vista.findViewById(R.id.etNombre);
        spnnrCategoria = vista.findViewById(R.id.spnnrCategoria);
        spnnrDeporte= vista.findViewById(R.id.spnnrDeporte);
        spnnrGenero = vista.findViewById(R.id.spnnrGenero);
        spnnrOrganizacion = vista.findViewById(R.id.spnnrOrganizacion);

        btnSiguiente = vista.findViewById(R.id.btnFiltrar);
    }

    private void llenarSpinnerOrganizacion(){
        Call<List<TypesOrganization>> call = orgSrv.getTypesOrganization();
        call.enqueue(new Callback<List<TypesOrganization>>() {
            @Override
            public void onResponse(Call<List<TypesOrganization>> call, Response<List<TypesOrganization>> response) {
                List<TypesOrganization> tipos;
                if(response.code() == 200) {
                    try {
                        tipos = response.body();
                        Log.d("RESPONSECODE OrgActvty", Integer.toString(response.code()));
                        ArrayAdapter<TypesOrganization> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, tipos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnnrOrganizacion.setAdapter(adapter);
                        spnnrOrganizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                TypesOrganization org = (TypesOrganization) spnnrOrganizacion.getSelectedItem();
                                Log.d("ItemSelected", org.getName());
                                organizacion = Integer.toString(org.getId());
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
                Toast toast = Toast.makeText(getContext(), "Por favor recargar pestaña", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onFailure", t.getMessage());
            }
        });

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
                    try{
                        //asigno a deportes lo que traje del servidor
                        deportes = response.body();
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
                                categorias = dep.getCategories();
                                llenarSpinnerCategoriaByDeporte();
                                deporte = Integer.toString(dep.getId());
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
                Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onFailure", t.getMessage());
            }
        });
    }


    private void llenarSpinnerCategoriaByDeporte() {
        try {
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,categorias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnnrCategoria.setAdapter(adapter);
            spnnrCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Category category = (Category) spnnrCategoria.getSelectedItem();
                    Log.d("Categoria Selected",category.getNombreCat());
                    categoria = Integer.toString(category.getId());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void llenarSpinnerCategoriaByServer() {
        //En call viene el tipo de dato que espero del servidor
        Call<List<Category>> call = categorySrv.getCategorias();
        Log.d("request categoria", call.request().url().toString());
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categorias;
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    try{
                        //asigno a deportes lo que traje del servidor
                        categorias = response.body();
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
                                categoria = Integer.toString(category.getId());
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
                Toast toast = Toast.makeText(getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onFailure", t.getMessage());
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
                        spnnrGenero.setAdapter(adapter);

                        // manejador del evento OnItemSelected
                        spnnrGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Gender gender;
                                gender = (Gender) spnnrGenero.getSelectedItem();
                                genero = "'" + gender.getNombre() + "'" ;
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
                Toast toast = Toast.makeText(getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

}
