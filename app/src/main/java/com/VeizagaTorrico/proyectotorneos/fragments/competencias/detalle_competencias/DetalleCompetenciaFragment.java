package com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.ViewPagerAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;
import com.google.android.material.tabs.TabLayout;


public class DetalleCompetenciaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View vista;
    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private TabLayout tabLayout;
    private SwipeRefreshLayout refreshLayout;
    private TextView sinConexion;

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
        initElements();
        llenarPager();

        if(NetworkReceiver.existConnection(vista.getContext())) {
            sinConexion.setVisibility(View.GONE);
            refresh();
        }
        else{
            sinConexion.setVisibility(View.VISIBLE);
            refresh();
        }

        return vista;
    }

    private void refresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    sinConexion.setVisibility(View.GONE);
                    llenarPager();
                }
                else{
                    sinConexion.setVisibility(View.VISIBLE);
                    llenarPager();
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initElements() {
        sinConexion = vista.findViewById(R.id.tv_sin_conexion_tabCompetencias);
        refreshLayout = vista.findViewById(R.id.refreshTabCompetencias);
        pager = vista.findViewById(R.id.pagerCompetencias);
        tabLayout = vista.findViewById(R.id.tabLayoutCompetencias);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        tabLayout.setupWithViewPager(pager);

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

        try {
            boolean otherFragment = getArguments().getBoolean("OtherFragment");
            if(otherFragment){
                infoGeneral.setOtherFragment(otherFragment);
            }else {
                infoGeneral.setOtherFragment(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        // le pasamos la competencia a cada fragment
        adapter.addFragment(infoGeneral,"Info general");
        infoGeneral.setCompetencia(competition);

        adapter.addFragment(encuentrosFragment, "Encuentros");
        encuentrosFragment.setCompetencia(competition);

        adapter.addFragment(posicionesFragment, "Posiciones");
        posicionesFragment.setCompetencia(competition);

        this.pager.setAdapter(adapter);

    }
}