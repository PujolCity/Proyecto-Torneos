package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.services.RefereeSrv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargaJuezCompetenciaFragment extends Fragment {

    private View vista;
    private CompetitionMin competencia;
    private Spinner spnnrJuez, spnnrJuezAsignado;
    private TextView tvJuezDni;
    private Button btnJuez;
    private ImageButton iBAgregar,iBDelete;
    private List<Referee> jueces, juecesAsignados;
    private Referee juecSeleccionado, miJuezSeleccionado;
    private RefereeSrv refereeSrv;
    private Map<String,String> data;

    public CargaJuezCompetenciaFragment() {
    }

    public static CargaJuezCompetenciaFragment newInstance() {
        CargaJuezCompetenciaFragment fragment = new CargaJuezCompetenciaFragment();
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
        vista = inflater.inflate(R.layout.fragment_carga_juez_competencia, container, false);
        initElements();
        listeners();

        return vista;
    }

    private void initElements() {
        refereeSrv = new RetrofitAdapter().connectionEnable().create(RefereeSrv.class);

        competencia = (CompetitionMin) getArguments().getSerializable("competencia");
        spnnrJuez = vista.findViewById(R.id.spinner_jueces_juez);
        spnnrJuezAsignado = vista.findViewById(R.id.spinner_misJueces_juez);
        tvJuezDni = vista.findViewById(R.id.tv_dni_juez);
        btnJuez = vista.findViewById(R.id.btn_nuevo_juez);
        iBAgregar = vista.findViewById(R.id.btn_asignarJuez_juez);
        iBDelete = vista.findViewById(R.id.btn_delete_juez);

        jueces = new ArrayList<>();
        juecesAsignados = new ArrayList<>();
        data = new HashMap<>();
    }

    private void listeners() {
        btnJuez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToCreateReferee();
            }
        });
        iBDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarJuezCompetencia();
            }
        });
        iBAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarJuezCompetencia();
            }
        });
    }

    private void asignarJuezCompetencia() {
    }

    private void eliminarJuezCompetencia() {
    }

    private void passToCreateReferee() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", competencia);
        Navigation.findNavController(vista).navigate(R.id.cargarJuezFragment,bundle);
    }

}
