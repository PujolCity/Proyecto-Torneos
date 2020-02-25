package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;

import java.util.List;

public class GeneralDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
    private Button btnEditar,btnInscripcion;

    private TextView nmb, cat, org, ciudad, genero, estado;

    public GeneralDetalleFragment() {
    }

    public static GeneralDetalleFragment newInstance() {
        GeneralDetalleFragment fragment = new GeneralDetalleFragment();
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
        vista = inflater.inflate(R.layout.info_general_organizando_competencia, container, false);

        initElements();
        ocultarBotones();
        listenButtonEdit();
        listenButtonInscripcion();

        Log.d("competencia",this.competencia.toString());
        try{
            nmb.setText(competencia.getName());
            cat.setText(competencia.getCategory());
            org.setText(competencia.getTypesOrganization());
            ciudad.setText(competencia.getCiudad());
            genero.setText(competencia.getGenero());
            estado.setText(competencia.getEstado());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vista;
    }

    private void initElements() {
        nmb = vista.findViewById(R.id.txtNmbCompDet_organizando);
        cat = vista.findViewById(R.id.txtCatCompDet_organizando);
        org = vista.findViewById(R.id.txtOrgCompDet_organizando);
        ciudad = vista.findViewById(R.id.txtCityCompDet_organizando);
        genero = vista.findViewById(R.id.txtGenderCompDet_organizando);
        estado = vista.findViewById(R.id.tv_estado_infograll_organizando);
        btnEditar = vista.findViewById(R.id.btn_edit_competencia_organizando);
        btnInscripcion = vista.findViewById(R.id.generar_inscripcion_organizando);
    }

    private void listenButtonInscripcion() {
        btnInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(competencia.getEstado().contains("SIN_INSCRIPCION")) {
                    passToCrearInscripcion();
                } else{
                    btnInscripcion.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void ocultarBotones(){
        try {
            btnEditar.setVisibility(View.INVISIBLE);
            List<String> roles = this.competencia.getRol();
            for (int i = 0 ; i < roles.size(); i++) {
                if (roles.get(i).contains("ORGANIZADOR")) {
                    btnEditar.setVisibility(View.VISIBLE);
                }
            }
            if(!competencia.getEstado().contains("SIN_INSCRIPCION")) {
                btnInscripcion.setVisibility(View.INVISIBLE);
                btnEditar.setVisibility(View.INVISIBLE);
            }
            } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void listenButtonEdit() {
        // ponemos a la escucha el boton de cargar Predio y campos
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(competencia.getEstado().contains("SIN_INSCRIPCION")){
                passToEditCompetencia();
            }
            else{
                Toast.makeText(vista.getContext(), "ACCION NO PERMITIDA: no se pueden editar los datos de una competencia con inscripcion abierta.", Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    private void passToEditCompetencia(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", this.competencia);
        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
        Navigation.findNavController(vista).navigate(R.id.editCompetenciaFragment, bundle);
    }

    private void passToCrearInscripcion(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", this.competencia);
        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
        Navigation.findNavController(vista).navigate(R.id.crearInscripcionFragment, bundle);
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

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
    }
}
