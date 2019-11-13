package com.VeizagaTorrico.proyectotorneos.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.OrganizandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.ParticipandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.SiguiendoFragment;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class MisCompetenciasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private Toolbar toolbar;
    private TabLayout tabLayout;

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
        View vista = inflater.inflate(R.layout.fragment_mis_competencias, container, false);

        toolbar = vista.findViewById(R.id.toolbarLayout);

        pager = vista.findViewById(R.id.pager);
        llenarPager();

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        tabLayout = vista.findViewById(R.id.tabLayout);
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

    public void llenarPager(){
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SiguiendoFragment(),"Siguiendo");
        adapter.addFragment(new ParticipandoFragment(), "Participando");
        adapter.addFragment(new OrganizandoFragment(), "Organizando");

        this.pager.setAdapter(adapter);
    }
}
