package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Invitation;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.InvitationSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoOrganizadorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
    private EditText etUsername;
    private Spinner spinnerUsuarios;
    private Button btnComprobar, btnEnviar;
    private UserSrv userSrv;
    private List<User> usuarios;
    private ArrayAdapter<User> adapterUser;
    private User usuario;
    private InvitationSrv invitationSrv;
    private Map<String,String> invitacion;

    public CoOrganizadorFragment() {
    }

    public static CoOrganizadorFragment newInstance() {
        CoOrganizadorFragment fragment = new CoOrganizadorFragment();
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
        vista = inflater.inflate(R.layout.fragment_co_organizador, container, false);
        initElements();
        buttonListeners();

        return vista;
    }

    private void buttonListeners() {
        btnComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                if(validar(username)){
                    llenarSpinnerUser(username);
                }else {
                    Log.d("Llenar el campo","edit text vacio");
                }

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.getId() != 0){
                    invitacion.put("idCompetencia",Integer.toString(competencia.getId()));
                    invitacion.put("idUsuario", Integer.toString(usuario.getId()));
                    Log.d("BODY INVITACION", invitacion.toString());
                    Call<MsgRequest> call = invitationSrv.invitarUsuario(invitacion);
                    Log.d("URL INVITACION", call.request().url().toString());
                    call.enqueue(new Callback<MsgRequest>() {
                        @Override
                        public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                            if(response.code() == 200) {
                                Log.d("response code", Integer.toString(response.code()));
                                Toast toast = Toast.makeText(vista.getContext(), "Invitacion Enviada", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            if (response.code() == 400) {
                                Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String userMessage = jsonObject.getString("messaging");
                                    Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                                    Toast.makeText(vista.getContext(), "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MsgRequest> call, Throwable t) {
                            Log.d("RESP_RECOVERY_ERROR", "error: "+t.getMessage());
                            Toast.makeText(vista.getContext(), "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void llenarSpinnerUser(final String username) {
        Call<List<User>> call = userSrv.getUsuariosByUsername(username);
        Log.d("Call URL", call.request().url().toString());
        try {
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if(!response.body().isEmpty()){
                        usuarios = response.body();
                        adapterUser = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, usuarios);
                        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUsuarios.setAdapter(adapterUser);
                        spinnerUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                usuario = (User) spinnerUsuarios.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                usuario = new User(0,"","","","","","");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validar(String username) {
        if(username.isEmpty())
            return false;
        return true;

    }

    private void initElements() {
        usuario = new User(0,"","","","","","");
        userSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);
        invitationSrv = new RetrofitAdapter().connectionEnable().create(InvitationSrv.class);

        invitacion = new HashMap<>();
        usuarios = new ArrayList<>();
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");

        etUsername = vista.findViewById(R.id.etUsername);
        btnComprobar = vista.findViewById(R.id.btnComprobar);
        btnEnviar = vista.findViewById(R.id.btnEnviarInvitacion);
        spinnerUsuarios = vista.findViewById(R.id.spinnerUsers);

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
}
