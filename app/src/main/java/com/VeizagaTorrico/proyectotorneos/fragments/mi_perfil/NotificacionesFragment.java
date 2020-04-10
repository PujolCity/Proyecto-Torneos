package com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.ConfigNotification;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.services.RefereeSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;


public class NotificacionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private Button btnConfirmar;
    private Switch swNotifSeg;
    private Switch swNotifComp;
    private TextView tvSinConexion;

    private Map<String,String> datos;

    private UserSrv usersSrv;

    public NotificacionesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificacionesFragment newInstance(String param1, String param2) {
        NotificacionesFragment fragment = new NotificacionesFragment();
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
        vista = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        initElements();
        loadConfigNotifications();
        if(NetworkReceiver.existConnection(vista.getContext())){
            tvSinConexion.setVisibility(View.GONE);
            listenConfirmar();
        }
        else{
            tvSinConexion.setVisibility(View.VISIBLE);
        }

        return vista;
    }

    private void initElements() {
        datos = new HashMap<>();
        usersSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);
        btnConfirmar = vista.findViewById(R.id.btn_confir_conf_notif);
        swNotifSeg = vista.findViewById(R.id.swt_seguidor);
        swNotifComp = vista.findViewById(R.id.swt_competidor);
        tvSinConexion = vista.findViewById(R.id.tv_sin_conexion_mis_notificaciones);
    }

    private void listenConfirmar(){
        btnConfirmar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                updateConfigNotification();
            }
        });
    }

    private void loadConfigNotifications(){
        Call<ConfigNotification> call = usersSrv.getConfigNotification(Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID)));
        Log.d("LOAD_CONF_NOTIF",call.request().url().toString());
        call.enqueue(new Callback<ConfigNotification>() {
            @Override
            public void onResponse(Call<ConfigNotification> call, Response<ConfigNotification> response) {
                if(response.code() == 200){
                    // actualizamos los datos de los switches
                    ConfigNotification config = response.body();
                    swNotifSeg.setChecked(config.isSeguidor());
                    swNotifComp.setChecked(config.isCompetidor());
                }
                if (response.code() == 400) {
                    Log.d("LOAD_CONF_NOTIF", "ERROR: "+response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("LOAD_CONF_NOTIF", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(vista.getContext(), "No se pudo recuperara la configuracion:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            @Override
            public void onFailure(Call<ConfigNotification> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void updateConfigNotification() {
        datos.put("idUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
        datos.put("seguidor", String.valueOf(swNotifSeg.isChecked()));
        datos.put("competidor", String.valueOf(swNotifComp.isChecked()));

        Call<MsgRequest> call = usersSrv.updateConfigNotification(datos);
        Log.d("UPDAT_CONF_NOTIF_DATA", datos.toString());
        Log.d("UPDAT_CONF_NOTIF",call.request().url().toString());
        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                if(response.code() == 200){
                    Toast toast = Toast.makeText(vista.getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(" status", response.body().getMsg());
                }
                if (response.code() == 400) {
                    Log.d("RESP_CONF_NOTIF", "PETICION MAL FORMADA: "+response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_CONF_NOTIF", "Msg de la repuesta: "+userMessage);
                        Toast.makeText(vista.getContext(), "No se logro actualizar la configuracion:  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                Log.d(" ERROR_UPD_NOTIF", t.getMessage());
                Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
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
