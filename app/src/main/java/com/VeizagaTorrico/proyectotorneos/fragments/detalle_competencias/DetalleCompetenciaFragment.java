package com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.ViewPagerAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.google.android.material.tabs.TabLayout;


public class DetalleCompetenciaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View vista;
    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private TabLayout tabLayout;

    public DetalleCompetenciaFragment() {
        // Required empty public constructor
    }

    public static DetalleCompetenciaFragment newInstance() {
        DetalleCompetenciaFragment fragment = new DetalleCompetenciaFragment();
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
        vista = inflater.inflate(R.layout.fragment_detalle_competencia, container, false);
        pager = vista.findViewById(R.id.pagerCompetencias);
        llenarPager();
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        tabLayout = vista.findViewById(R.id.tabLayoutCompetencias);
        tabLayout.setupWithViewPager(pager);

        return vista;
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

    public void llenarPager() {
        CompetitionMin competition = (CompetitionMin) getArguments().getSerializable("competencia");

        InfoGeneralCompetenciaFragment infoGeneral = new InfoGeneralCompetenciaFragment();
        PosicionesFragment posicionesFragment = new PosicionesFragment();
        EncuentrosFragment encuentrosFragment = new EncuentrosFragment();

        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(infoGeneral,"Info General");
        infoGeneral.setCompetencia(competition);

        adapter.addFragment(posicionesFragment, "Posiciones");

        adapter.addFragment(encuentrosFragment, "Encuentros");

        this.pager.setAdapter(adapter);

    }
}