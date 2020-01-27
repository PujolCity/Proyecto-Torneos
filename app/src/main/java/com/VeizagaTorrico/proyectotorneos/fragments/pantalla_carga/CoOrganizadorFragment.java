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

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;

import java.util.ArrayList;
import java.util.List;

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

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

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
        userSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);
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
