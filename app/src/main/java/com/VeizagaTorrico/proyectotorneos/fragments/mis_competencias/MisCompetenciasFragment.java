package com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.ViewPagerAdapter;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;
import com.google.android.material.tabs.TabLayout;


public class MisCompetenciasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private SwipeRefreshLayout refreshLayout;
    private TextView sinConexion;
    private View vista;
    public MisCompetenciasFragment() {
        // Required empty public constructor
    }

    public static MisCompetenciasFragment newInstance() {
        MisCompetenciasFragment fragment = new MisCompetenciasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_mis_competencias, container, false);
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
        toolbar = vista.findViewById(R.id.toolbarLayout);
        refreshLayout = vista.findViewById(R.id.refreshTabMisCompetencias);
        sinConexion = vista.findViewById(R.id.tv_sin_conexion_tabMisCompetencias);
        pager = vista.findViewById(R.id.pager);
        tabLayout = vista.findViewById(R.id.tabLayout);

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

    public void llenarPager(){
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SiguiendoFragment(),"Siguiendo");
        adapter.addFragment(new ParticipandoFragment(), "Participando");
        adapter.addFragment(new OrganizandoFragment(), "Organizando");

        this.pager.setAdapter(adapter);
    }
}
