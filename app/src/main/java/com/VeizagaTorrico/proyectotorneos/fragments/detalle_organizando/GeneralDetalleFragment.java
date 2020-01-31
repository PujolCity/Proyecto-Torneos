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
    private ImageButton follow,noFollow;
    private Button btnIncribirse;
    private Button btnEditar;

    private TextView nmb, cat, org, ciudad, genero, estado;

    public GeneralDetalleFragment() {
        // Required empty public constructor
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
        vista = inflater.inflate(R.layout.fragment_info_general_competencia, container, false);

        initElements();
        ocultarBotones();
        listenButtonEdit();

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
        nmb = vista.findViewById(R.id.txtNmbCompDet);
        cat = vista.findViewById(R.id.txtCatCompDet);
        org = vista.findViewById(R.id.txtOrgCompDet);
        ciudad = vista.findViewById(R.id.txtCityCompDet);
        genero = vista.findViewById(R.id.txtGenderCompDet);
        estado = vista.findViewById(R.id.tv_estado_infograll);
        follow = vista.findViewById(R.id.btnFollow);
        follow.setVisibility(View.INVISIBLE);
        noFollow = vista.findViewById(R.id.btnNoFollow);
        noFollow.setVisibility(View.INVISIBLE);
        btnEditar = vista.findViewById(R.id.btn_edit_competencia);
        // en esta pantalla ocultamos el boton
        btnIncribirse = vista.findViewById(R.id.inscribirse);
        btnIncribirse.setVisibility(View.INVISIBLE);
    }

    private void ocultarBotones(){
        btnEditar.setVisibility(View.INVISIBLE);
        List<String> roles = this.competencia.getRol();

        for (int i = 0 ; i < roles.size(); i++) {
            if (roles.get(i).contains("ORGANIZADOR")) {
                btnEditar.setVisibility(View.VISIBLE);
            }
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
                Toast.makeText(getContext(), "ACCION NO PERMITIDA: no se pueden editar los datos de una competencia con inscripcion abierta.", Toast.LENGTH_LONG).show();
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
