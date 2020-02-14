package com.VeizagaTorrico.proyectotorneos.fragments.noticias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.News;


public class DetalleNoticiasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View vista;
    private News news;
    private TextView competencia, titulo, subtitulo, cuerpo;
    public DetalleNoticiasFragment() {
        // Required empty public constructor
    }

    public static DetalleNoticiasFragment newInstance() {
        DetalleNoticiasFragment fragment = new DetalleNoticiasFragment();
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
        vista = inflater.inflate(R.layout.fragment_detalle_noticias, container, false);
        initElements();
        return vista;
    }

    private void initElements() {
        this.news = (News) getArguments().getSerializable("noticia");

        competencia = vista.findViewById(R.id.tv_competenciaDetalle_noticia);
        titulo = vista.findViewById(R.id.tv_tituloDetalle_noticia);
        subtitulo = vista.findViewById(R.id.tv_subtituloDetalle_noticia);
        cuerpo = vista.findViewById(R.id.tv_cuerpoDetalle_noticia);

        competencia.setText(news.getCompetencia());
        titulo.setText(news.getTitulo());
        subtitulo.setText(news.getSubtitulo());
        cuerpo.setText(news.getCuerpo());

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
