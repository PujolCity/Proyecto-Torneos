package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

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

public class DetalleOrganizandoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private TabLayout tabLayout;


    public DetalleOrganizandoFragment() {
        // Required empty public constructor
    }

    public static DetalleOrganizandoFragment newInstance(String param1, String param2) {
        DetalleOrganizandoFragment fragment = new DetalleOrganizandoFragment();
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
        vista = inflater.inflate(R.layout.fragment_detalle_organizando, container, false);

        pager = vista.findViewById(R.id.pagerDetalle);
        llenarPager();
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        tabLayout = vista.findViewById(R.id.tabLayoutDetalle);
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

        GeneralDetalleFragment general = new GeneralDetalleFragment();
        CompetidoresDetalleFragment competidoresDetalleFragment = new CompetidoresDetalleFragment();
        CargasDetalleFragment cargasDetalleFragment = new CargasDetalleFragment();
        EncuentrosDetalleFragment encuentrosDetalleFragment = new EncuentrosDetalleFragment();

        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(general,"Info General");
        general.setCompetencia(competition);

        adapter.addFragment(competidoresDetalleFragment,"Competidores");
        competidoresDetalleFragment.setCompetencia(competition);

        adapter.addFragment(cargasDetalleFragment,"Cargas");

        adapter.addFragment(encuentrosDetalleFragment,"Encuentros");
        encuentrosDetalleFragment.setCompetencia(competition);

        this.pager.setAdapter(adapter);
    }

}
