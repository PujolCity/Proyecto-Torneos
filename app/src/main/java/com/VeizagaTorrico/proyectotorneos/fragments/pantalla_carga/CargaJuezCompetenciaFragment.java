package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VeizagaTorrico.proyectotorneos.R;

public class CargaJuezCompetenciaFragment extends Fragment {

    private View vista;

    public CargaJuezCompetenciaFragment() {
    }

    public static CargaJuezCompetenciaFragment newInstance() {
        CargaJuezCompetenciaFragment fragment = new CargaJuezCompetenciaFragment();
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
        vista = inflater.inflate(R.layout.fragment_carga_juez_competencia, container, false);

        return vista;
    }
}
