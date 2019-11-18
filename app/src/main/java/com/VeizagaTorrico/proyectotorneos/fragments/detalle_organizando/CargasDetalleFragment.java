package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;

public class CargasDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
    private Button btnPredio;
    private Button btnTurno;
    private Button btnJuez;

    public CargasDetalleFragment() {
        // Required empty public constructor
    }

    public static CargasDetalleFragment newInstance() {
        CargasDetalleFragment fragment = new CargasDetalleFragment();
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
        vista = inflater.inflate(R.layout.fragment_cargas_detalle, container, false);
        initElements();
        listenButtons();

        return vista;
    }

    private void initElements() {
        btnPredio = vista.findViewById(R.id.btnCargarPredio);
        btnTurno = vista.findViewById(R.id.btnCargarTurno);
        btnJuez = vista.findViewById(R.id.btnCargarJuez);
    }

    private void listenButtons() {
        // ponemos a la escucha el boton de cargar Predio y campos
        btnPredio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.cargarPredioFragment, bundle);
            }
        });

        // ponemos a la escucha el boton de cargar Turnos
        btnTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.cargarTurnoFragment, bundle);
            }
        });

        // ponemos a la escucha el boton de cargar Turnos
        btnJuez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.cargarJuezFragment, bundle);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
    }
}
