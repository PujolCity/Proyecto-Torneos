package com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VeizagaTorrico.proyectotorneos.R;


public class InscripcionDetalleFragment extends Fragment {

    private View vista;

    public InscripcionDetalleFragment() {
    }

    public static InscripcionDetalleFragment newInstance() {
        InscripcionDetalleFragment fragment = new InscripcionDetalleFragment();
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
        vista = inflater.inflate(R.layout.fragment_inscripcion_detalle, container, false);


        return vista;
    }

}
