package com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.InputFilter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.TypesOrganization;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.TypesOrganizationSrv;
import com.VeizagaTorrico.proyectotorneos.utils.InputFilterMinMax;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;

public class CrearCompetencia3Fragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private LinearLayout linl_fase, linl_grupo, linl_max_comp;
    private Button btnCrear;
    private Spinner spinner, spinnerFase, spinnerGrupo;
    private TextView txtView, tvGrupo, tvMaxComp;
    private EditText etMaxCompetidores;
    private Competition competition;
    private TypesOrganizationSrv orgSrv;
    private CompetitionSrv competitionSrv;
    private View vista;
    private Map<String,String> competencia;
    private String maxCompetidores;
    private int fase, cantGrupo;
    private boolean hayGrupo, hayFase, hayMaxComp;
    private int usuario;
    private AlertDialog alertDialog;

    public CrearCompetencia3Fragment() {
        // Required empty public constructor
    }
    public static CrearCompetencia3Fragment newInstance(String param1, String param2) {
        CrearCompetencia3Fragment fragment = new CrearCompetencia3Fragment();
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
        hayGrupo = false;
        vista = inflater.inflate(R.layout.fragment_crear_competencia3, container, false);
        initElements();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            llenarSpinnerOrg();
            listeners();
        } else {
            sinInternet();
        }
        return vista;
    }

    private void listeners() {
        //LISTENER BOTONES
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(recuperarValoresIngresados()){
                    alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Creando competencia..");
                    Call<Competition> call = competitionSrv.createCompetition(competencia);
                    Log.d("Url",call.request().url().toString());
                    alertDialog.show();
                    call.enqueue(new Callback<Competition>() {
                        @Override
                        public void onResponse(Call<Competition> call, Response<Competition> response) {
                            Log.d("Response code",Integer.toString(response.code()));
                            alertDialog.dismiss();
                            if(response.code() == 201){
                                Log.d("COMPETENCIA CREADA", competencia.toString());
                                // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                                Toast toast = Toast.makeText(vista.getContext(), "Competencia Creada!", Toast.LENGTH_SHORT);
                                toast.show();
                                Navigation.findNavController(vista).navigate(R.id.misCompetencias);
                            }
                            if (response.code() == 400) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String userMessage = jsonObject.getString("messaging");
                                    Log.d("CREATE_COMP", "Msg de la repuesta: "+userMessage);
                                    Toast.makeText(vista.getContext(), "Error en la peticion: "+userMessage+" >>", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                            if (response.code() == 500) {
                                Log.d("CREATE_COMP_ERROR", "Problemas en el servidor.");
                                Toast.makeText(vista.getContext(), "Problemas en el servidor ", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Competition> call, Throwable t) {
                            try {
                                alertDialog.dismiss();
                                Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    // recuperamos los valores ingresados por el usuario
    private boolean recuperarValoresIngresados() {
        if(hayMaxComp){
            maxCompetidores = etMaxCompetidores.getText().toString();
            if(correctoMaxCompetidores(competition.getTypesOrganization().getName())){
                if(competition.getTypesOrganization().getName().contains("grupo")){
                    competencia.put("max_comp",String.valueOf(Integer.valueOf(maxCompetidores)*cantGrupo));
                }
                if(competition.getTypesOrganization().getName().contains("Liga")){
                    competencia.put("max_comp",maxCompetidores);
                }
            }
            else{
                if(competition.getTypesOrganization().getName().contains("grupo")){
                    Toast.makeText(vista.getContext(), "La cantidad de competidores debe estar entre 3 y 20 ", Toast.LENGTH_LONG).show();
                }
                if(competition.getTypesOrganization().getName().contains("Liga")){
                    Toast.makeText(vista.getContext(), "La cantidad de competidores debe estar entre 3 y 80 ", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        }
        else{
            competencia.put("max_comp", null);
        }

        competencia.put("nombre",competition.getName());
        competencia.put("fecha_ini",competition.getFechaInicio());
        competencia.put("ciudad", Integer.toString(competition.getCiudad().getId()));
        competencia.put("genero",competition.getGenero());
        competencia.put("categoria_id", Integer.toString(competition.getCategory().getId()));
        competencia.put("tipoorg_id",Integer.toString(competition.getTypesOrganization().getId()));
        competencia.put("user_id",Integer.toString(usuario));
        competencia.put("frecuencia",Integer.toString(competition.getFrecuencia()));

        if(hayGrupo) {
            competencia.put("cant_grupos", String.valueOf(cantGrupo));
        }
        else {
            competencia.put("cant_grupos", null);
        }

        if(hayFase){
            competencia.put("fase", Integer.toString(fase));
        }else {
            competencia.put("fase", null);
        }

        Log.d("BODY" , competencia.toString());
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

    @Override
    public void sinInternet() {
        Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento no se podran crear competencias.", Toast.LENGTH_LONG).show();
        btnCrear.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void initElements(){
        competencia = new HashMap<>();
        usuario = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(getContext(), FILE_SHARED_DATA_USER, KEY_ID));
        orgSrv = new RetrofitAdapter().connectionEnable().create(TypesOrganizationSrv.class);
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);

        txtView = vista.findViewById(R.id.descripcionTipoOrg);
        tvMaxComp = vista.findViewById(R.id.tv_mx_comp);
        etMaxCompetidores = vista.findViewById(R.id.etMaxComp);
        btnCrear = vista.findViewById(R.id.btnCCSig_3);
        spinner = vista.findViewById(R.id.spinnerTipoOrg);
        spinnerFase = vista.findViewById(R.id.spinnerFase);
        spinnerGrupo = vista.findViewById(R.id.spinnerCantGrupos);
        tvGrupo = vista.findViewById(R.id.tv_grupo_crearComp3);

        linl_fase = vista.findViewById(R.id.linl_fase);
        linl_grupo = vista.findViewById(R.id.linl_grupo);
        linl_max_comp = vista.findViewById(R.id.linl_max_comp);
        hayMaxComp = false;
    }

     private void llenarSpinnerOrg(){
         alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Espere un momento..");
         Call<List<TypesOrganization>> call = orgSrv.getTypesOrganization();
         alertDialog.show();
         call.enqueue(new Callback<List<TypesOrganization>>() {
             @Override
             public void onResponse(Call<List<TypesOrganization>> call, Response<List<TypesOrganization>> response) {
                 List<TypesOrganization> tipos;
                 alertDialog.dismiss();
                 if(response.code() == 200){
                     tipos = response.body();
                     Log.d("RESPONSECODE OrgActvty",  Integer.toString(response.code()) );
                     ArrayAdapter<TypesOrganization> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,tipos);
                     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     spinner.setAdapter(adapter);
                     spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                         @Override
                         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                             try {
                                 TypesOrganization org = (TypesOrganization) spinner.getSelectedItem();
                                 txtView.setText(org.getDescription());
                                 competition =(Competition) getArguments().getSerializable("competition");
                                 competition.setTypesOrganization(org);
                                 updateView(org.getName());

                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }

                         @Override
                         public void onNothingSelected(AdapterView<?> adapterView) {
                         }
                     });
                 }
             }

             @Override
             public void onFailure(Call<List<TypesOrganization>> call, Throwable t) {
                 alertDialog.dismiss();
                 Toast.makeText(getContext(), "Por favor recargar pestaña", Toast.LENGTH_SHORT).show();
                 Log.d("onFailure", t.getMessage());
             }
         });

     }

    private void llenarSpinnerFase() {
        final List<String> fases = new ArrayList<>();
        fases.add("Final");
        fases.add("Semi Final");
        fases.add("Cuartos");
        fases.add("Octavos");
        fases.add("16 avos");
        fases.add("32 avos");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,fases);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFase.setAdapter(adapter);
        spinnerFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fase = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void llenarSpinnerGrupo() {
        final List<Integer> numeros = new ArrayList<>();
        for (int i=2; i<= 20; i++){
            numeros.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item, numeros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapter);
        spinnerGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cantGrupo = i+2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateView(String typeOrg){
        if(typeOrg.contains("grupo")){
             //Log.d("prueba" , "entra?" );
             linl_max_comp.setVisibility(View.VISIBLE);
             linl_grupo.setVisibility(View.VISIBLE);
             hayGrupo = true;
             llenarSpinnerGrupo();
             hayMaxComp = true;
             tvMaxComp.setText("Competidores por grupo (entre 3 y 20): ");
         } else{
            linl_grupo.setVisibility(View.GONE);
             hayGrupo = false;
         }

         if(typeOrg.contains("Eliminatorias")){
             linl_max_comp.setVisibility(View.GONE);
             linl_fase.setVisibility(View.VISIBLE);
             hayFase = true;
             hayMaxComp = false;
             llenarSpinnerFase();
         } else{
             linl_fase.setVisibility(View.GONE);
             hayFase = false;
         }
        if(typeOrg.contains("Liga")) {
            //Log.d("prueba" , "entra?" );
            hayMaxComp = true;
            linl_max_comp.setVisibility(View.VISIBLE);
            tvMaxComp.setText("Competidores total (entre 3 y 80): ");
        }
    }

    private boolean correctoMaxCompetidores(String tipoOrg) {
        if(tipoOrg.contains("grupo")){
            return inRange(3, 20, Integer.valueOf(etMaxCompetidores.getText().toString()));
        }
        if(tipoOrg.contains("Liga")){
            return inRange(3, 80, Integer.valueOf(etMaxCompetidores.getText().toString()));
        }
        return false;
    }

    // determina si un valor se encuentra en un rango dado
    private boolean inRange(int nroDesde, int nroHasta, int valor){
//        Log.d("SPIN_MAX_COMP", String.valueOf(valor));
        if(valor < nroDesde){
            return false;
        }
        if(valor > nroHasta){
            return false;
        }
        return true;
    }

}
