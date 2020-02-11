package com.VeizagaTorrico.proyectotorneos.fragments.noticias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;

import java.util.ArrayList;
import java.util.List;


public class NoticiasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View vista;
    private List<String> noticias;
    private RecyclerView recyclerNoticias;
    private TextView textNoticias;

    public NoticiasFragment() {
        // Required empty public constructor
    }

    public static NoticiasFragment newInstance() {
        NoticiasFragment fragment = new NoticiasFragment();
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
        vista = inflater.inflate(R.layout.fragment_noticias, container, false);
        initElement();
        if(this.noticias.size() == 0){
            sinNoticias();
        }

        return vista;
    }

    private void initElement() {
        recyclerNoticias = vista.findViewById(R.id.recyclerNoticias);
        textNoticias = vista.findViewById(R.id.tv_noticias);
        this.noticias = new ArrayList<>();
    }

    private void sinNoticias() {
        recyclerNoticias.setVisibility(View.INVISIBLE);
        textNoticias.setVisibility(View.VISIBLE);
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
