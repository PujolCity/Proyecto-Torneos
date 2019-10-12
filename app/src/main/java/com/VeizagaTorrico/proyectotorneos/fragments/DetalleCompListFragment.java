package com.VeizagaTorrico.proyectotorneos.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Competition;



public class DetalleCompListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView nmb, dep, cat;
    private View vista;

    public DetalleCompListFragment() {
        // Required empty public constructor
    }
    public static DetalleCompListFragment newInstance(String param1, String param2) {
        DetalleCompListFragment fragment = new DetalleCompListFragment();
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
        vista = inflater.inflate(R.layout.fragment_detalle_competencias, container, false);

        //vista.setContentView(R.layout.fragment_detalle_competencias);

        nmb = vista.findViewById(R.id.txtNmbCompDet);
        dep = vista.findViewById(R.id.txtDepCompDet);
        cat = vista.findViewById(R.id.txtCatCompDet);

        Competition competition = (Competition) getArguments().getSerializable("competencia");

        nmb.setText(competition.getName());
        cat.setText(competition.getCategory().getNombreCat());
        dep.setText(competition.getCategory().getSport());

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
}
