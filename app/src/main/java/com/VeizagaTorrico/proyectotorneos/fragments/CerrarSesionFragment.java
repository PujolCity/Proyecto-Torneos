package com.VeizagaTorrico.proyectotorneos.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VeizagaTorrico.proyectotorneos.HomeActivity;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_SESSION;

public class CerrarSesionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View vista;

    public CerrarSesionFragment() {
    }

    public static CerrarSesionFragment newInstance() {
        CerrarSesionFragment fragment = new CerrarSesionFragment();
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
        vista = inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);

        ManagerSharedPreferences.getInstance().setSessionFromSharedPreferences(vista.getContext(),FILE_SHARED_DATA_USER, KEY_SESSION, false);
        passToHome();

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

    private void passToHome() {
        ManagerSharedPreferences.getInstance().setSessionFromSharedPreferences(vista.getContext(),FILE_SHARED_DATA_USER, KEY_SESSION, false);
        Intent toInitApp = new Intent(getActivity(), HomeActivity.class);
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toInitApp);
        getActivity().finish();
    }
}
