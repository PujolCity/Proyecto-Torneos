package com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil;

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


public class TabPerfilFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View vista;
    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private SwipeRefreshLayout refreshLayout;
    private TextView sinConexion;

    public TabPerfilFragment() {
        // Required empty public constructor
    }

    public static TabPerfilFragment newInstance() {
        TabPerfilFragment fragment = new TabPerfilFragment();
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

        vista = inflater.inflate(R.layout.fragment_tab_perfil, container, false);
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

    private void initElements() {
        sinConexion = vista.findViewById(R.id.tv_sin_conexion_tabPerfil);
        toolbar = vista.findViewById(R.id.toolbarLayout);
        refreshLayout = vista.findViewById(R.id.refreshPerfil);
        tabLayout = vista.findViewById(R.id.tabPerfil);
        pager = vista.findViewById(R.id.pagerPerfil);

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });

        tabLayout.setupWithViewPager(pager);
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

    private void llenarPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MiPerfilFragment(),"Mis datos");
        adapter.addFragment(new InvitacionesFragment(), "Mis invitaciones");
        adapter.addFragment(new NotificacionesFragment(), "Mis notificaciones");

        this.pager.setAdapter(adapter);

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
