package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Gender;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.RespSrvUser;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.GenderSrv;
import com.VeizagaTorrico.proyectotorneos.services.StatusSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_EMAIL;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_LASTNAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_NAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditCompetenciaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditCompetenciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCompetenciaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View vista;

    private CompetitionSrv apiCompetitionService;
    private GenderSrv apiGenderService;
    private StatusSrv apiStatusServicce;
    private CompetitionMin competencia;

    //RespSrvUser respSrvRegister;
    Button btnUpdateCompetition;

    EditText edtCiudad;
    Spinner spinGenero, spinEstado;
    String ciudadNueva;
    Gender generoNuevo;
    Gender estadoNuevo;

    // capturan resp de los servicios del server para los spinners
    private List<Gender> estados;
    private List<Gender> generos;

    private Map<String,String> competitionMapUpdate = new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public EditCompetenciaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCompetenciaFragment newInstance(String param1, String param2) {
        EditCompetenciaFragment fragment = new EditCompetenciaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_edit_competencia, container, false);

        updateUi();
        listenBotonUpdateCompetition();
        llenarSpinnerGenero();
        llenarSpinnerEstado();
        loadaDataCompetition();

        return vista;
    }

    // realizamos los binding con los componentes de la vista
    private void updateUi(){
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");

        edtCiudad = vista.findViewById(R.id.edt_ciudad_edit_comp);

        spinGenero = vista.findViewById(R.id.spinner_genero_edit);
        spinEstado = vista.findViewById(R.id.spinner_estado_edit);

        generos = new ArrayList<>();
        estados = new ArrayList<>();

        btnUpdateCompetition = vista.findViewById(R.id.btn_update_edit_comp);
    }

    private void listenBotonUpdateCompetition(){
        btnUpdateCompetition.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(formCorrect()){
                    updateData();
                }
                else{
                    Toast.makeText(getContext(), "Formulario INCORRECTO!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean formCorrect(){
        if(Validations.isEmpty(edtCiudad)){
            //Log.d("VALIDACIONES",  "Nombre de usuario vacio");
            return false;
        }

        return true;
    }

    private void loadaDataCompetition() {
        Log.d("LOAD_DATA_COMP", "EXITO: "+competencia.getGenero()+" - "+competencia.getEstado());
        edtCiudad.setText(competencia.getCiudad());
        ciudadNueva = competencia.getCiudad();
    }

    private void updateData() {
        // hacemos la conexion con el api de rest del servidor
        apiCompetitionService = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);

        // recuperamos los datos nuevos
        competitionMapUpdate.put("id", String.valueOf(this.competencia.getId()));
        competitionMapUpdate.put("ciudad", ciudadNueva);
        competitionMapUpdate.put("genero", generoNuevo.getNombre());
        competitionMapUpdate.put("estado", estadoNuevo.getNombre());

        Log.d("DATA_UPDATE_ERROR", "Datos nuevos de la competencia: "+competitionMapUpdate);

        Call<MsgRequest> call = apiCompetitionService.updateCompetition(competitionMapUpdate);
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if (response.code() == 200) {
                    Log.d("UPDATE_DATA", "Datos actualizados con exito");
                    Toast.makeText(getContext(), "Datos actualizados con exito ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.code() == 400) {
                    Log.d("DATA_UPDATE_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("DATA_UPDATE_ERROR", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(getContext(), "No se pudieron registrar los cambios:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Log.d("RESP_CREATE_ERROR", "error: "+t.getMessage());
                Toast.makeText(getContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarSpinnerGenero() {
        generos.clear();

        apiGenderService = new RetrofitAdapter().connectionEnable().create(GenderSrv.class);
        Call<List<Gender>> call = apiGenderService.getGenders();
        Log.d("Call Generos",call.request().url().toString());
        call.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
                try{
                    if(!response.body().isEmpty()){
                        generos = response.body();
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_custom, generos);
                        adapter.setDropDownViewResource(R.layout.item_spinner_custom);
                        spinGenero.setAdapter(adapter);
                        spinGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                generoNuevo = (Gender) spinGenero.getSelectedItem();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        // seteamos el item seleccionado del spinner
                        Gender generoCompetencia = new Gender(competencia.getGenero());
                        int spinnerPosition = adapter.getPosition(generoCompetencia);
                        spinGenero.setSelection(spinnerPosition);
                        Log.d("Genero encontrado", ""+spinnerPosition);
                        generoNuevo = (Gender)spinEstado.getItemAtPosition(spinnerPosition);
                    }else {
//                        Referee referee = new Referee(0, "Sin Jueces", " ",0,null);
//                        jueces.add(referee);
//                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerJuez.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Gender>> call, Throwable t) {
                Toast toast = Toast.makeText(getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void llenarSpinnerEstado(){
        estados.clear();

        apiStatusServicce = new RetrofitAdapter().connectionEnable().create(StatusSrv.class);
        Call<List<Gender>> call = apiStatusServicce.getStatus();
        Log.d("Call Estados",call.request().url().toString());
        call.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
                try{
                    if(!response.body().isEmpty()){
                        estados = response.body();
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, estados);
                        adapter.setDropDownViewResource(R.layout.item_spinner_custom);
                        spinEstado.setAdapter(adapter);
                        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                estadoNuevo = (Gender) spinEstado.getSelectedItem();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        // seteamos el item seleccionado del spinner
                        Gender estadoCompetencia = new Gender(competencia.getEstado());
                        int spinnerPosition = adapter.getPosition(estadoCompetencia);
                        spinEstado.setSelection(spinnerPosition);
                        estadoNuevo = (Gender)spinEstado.getItemAtPosition(spinnerPosition);
                        Log.d("Estado encontrado", ""+spinnerPosition);
                    }else {
//                        Referee referee = new Referee(0, "Sin Jueces", " ",0,null);
//                        jueces.add(referee);
//                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerJuez.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Gender>> call, Throwable t) {
                Toast toast = Toast.makeText(getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
