package com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;


public class InfoGeneralCompetenciaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CompetitionMin competition;

    private TextView nmb, cat, org, ciudad, genero;
    private ImageButton follow;
    private View vista;

    public InfoGeneralCompetenciaFragment() {
        // Required empty public constructor
    }

    public static InfoGeneralCompetenciaFragment newInstance() {
        InfoGeneralCompetenciaFragment fragment = new InfoGeneralCompetenciaFragment();
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
        vista = inflater.inflate(R.layout.fragment_info_general_competencia, container, false);

        initElements();

        try{
            nmb.setText(competition.getName());
            cat.setText(competition.getCategory());
            org.setText(competition.getTypesOrganization());
            ciudad.setText(competition.getCiudad());
            genero.setText(competition.getGenero());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vista;
    }

    private void initElements() {
        nmb = vista.findViewById(R.id.txtNmbCompDet);
        cat = vista.findViewById(R.id.txtCatCompDet);
        org = vista.findViewById(R.id.txtOrgCompDet);
        ciudad = vista.findViewById(R.id.txtCityCompDet);
        genero = vista.findViewById(R.id.txtGenderCompDet);
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

    public void setCompetencia(CompetitionMin competition) {
        this.competition = competition;
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
