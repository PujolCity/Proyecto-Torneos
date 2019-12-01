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
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CargaFaseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
    private TextView vs;
    private EditText comp1, comp2;
    private Button btnGuardar, btnEncuentro;
    private Spinner spinnerFase, spinnerCompetidor, spinnerEncuentro;
    private List<Confrontation> encuentros;
    private Map<String,List <Integer>> body;
    private UserSrv userSrv;
    private List<User> usuarios;
    private ArrayAdapter<User> adapterUser;
    private User seleccionado;

    public CargaFaseFragment() {
    }

    public static CargaFaseFragment newInstance() {
        CargaFaseFragment fragment = new CargaFaseFragment();
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
        vista = inflater.inflate(R.layout.fragment_carga_fase, container, false);
        initElements();
        llenarSpinnerUser("a");
        btnListener();

        return vista;
    }

    private void btnListener() {
        comp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comp2.setText(seleccionado.getNombreUsuario());
            }
        });

        comp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comp1.setText(seleccionado.getNombreUsuario());
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnEncuentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initElements() {
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");

        vs = vista.findViewById(R.id.asd);
        vs.setVisibility(View.INVISIBLE);
        comp1 = vista.findViewById(R.id.etComp1);
        comp1.setVisibility(View.INVISIBLE);
        comp2 = vista.findViewById(R.id.etComp2);
        comp2.setVisibility(View.INVISIBLE);

        spinnerFase = vista.findViewById(R.id.spinnerFaseCarga);
        spinnerCompetidor = vista.findViewById(R.id.spinnerCompetidores);
        spinnerEncuentro = vista.findViewById(R.id.spinnerEncunetrosGenerados);

        btnEncuentro = vista.findViewById(R.id.guardarEncuentro);
        btnGuardar = vista.findViewById(R.id.crearEncuentros);

        userSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);
        usuarios = new ArrayList<>();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void llenarSpinnerUser(final String username) {
        Call<List<User>> call = userSrv.getUsuariosByUsername(username);
        Log.d("Call URL", call.request().url().toString());
        try {
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if(!response.body().isEmpty()){
                        comp1.setVisibility(View.VISIBLE);
                        comp2.setVisibility(View.VISIBLE);
                        vs.setVisibility(View.VISIBLE);
                        usuarios = response.body();
                        adapterUser = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, usuarios);
                        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCompetidor.setAdapter(adapterUser);
                        spinnerCompetidor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                seleccionado = (User) spinnerCompetidor.getSelectedItem();
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
