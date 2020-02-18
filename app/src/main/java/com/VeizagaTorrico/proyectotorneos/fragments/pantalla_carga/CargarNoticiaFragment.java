package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.NewsSrv;
import com.VeizagaTorrico.proyectotorneos.services.RefereeSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;

public class CargarNoticiaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText edtTitle, edtResume, edtDescripcion;
    private Button btnPublicar;

    private NewsSrv newsSrv;
    private CompetitionMin competencia;

    private Map<String,String> datos;

    private View vista;

    public CargarNoticiaFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CargarNoticiaFragment newInstance(String param1, String param2) {
        CargarNoticiaFragment fragment = new CargarNoticiaFragment();
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
        vista = inflater.inflate(R.layout.fragment_cargar_noticia, container, false);

        initElements();
        listenPublish();

        return vista;
    }

    // HACEMOS LA CONEXION CON LA VISTA
    private void initElements() {
        datos = new HashMap<>();
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");
        newsSrv = new RetrofitAdapter().connectionEnable().create(NewsSrv.class);

        btnPublicar = vista.findViewById(R.id.btn_publish_new);

        edtTitle = vista.findViewById(R.id.edt_title_news);
        edtResume = vista.findViewById(R.id.edt_resume_news);
        edtDescripcion = vista.findViewById(R.id.edt_descripcion_new);
    }

    private void listenPublish(){
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(validar()){
                datos.put("idCompetencia", String.valueOf(competencia.getId()));
                datos.put("idPublicador", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(getContext(), FILE_SHARED_DATA_USER, KEY_ID));
                datos.put("titulo", edtTitle.getText().toString());
                datos.put("resumen", edtResume.getText().toString());
                datos.put("descripcion", edtDescripcion.getText().toString());

                Call<MsgRequest> call = newsSrv.publishNew(datos);
                call.enqueue(new Callback<MsgRequest>() {
                    @Override
                    public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                        if(response.code() == 200){
                            Log.d("INFO NEWS", " Noticia publicada");
                            // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                            Toast toast = Toast.makeText(vista.getContext(), "Noticia cargada con exito!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        if (response.code() == 400) {
                            Log.d("RESP_NEWS_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("messaging");
                                Log.d("RESP_NEWS_ERROR", "Msg de la repuesta: "+message);
                                Toast.makeText( getContext(), "No se pudo publicar la noticia:  << "+message+" >>", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    @Override
                    public void onFailure(Call<MsgRequest> call, Throwable t) {
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
    }

    private boolean validar() {
        if(Validations.isEmpty(edtTitle))
            return false;
        if(Validations.isEmpty(edtResume))
            return false;
        if(Validations.isEmpty(edtDescripcion))
            return false;
        return true;
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
