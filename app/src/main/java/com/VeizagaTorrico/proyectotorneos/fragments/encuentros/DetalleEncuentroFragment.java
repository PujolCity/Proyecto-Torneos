package com.VeizagaTorrico.proyectotorneos.fragments.encuentros;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.ConfrontationFull;
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.models.Turn;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.VeizagaTorrico.proyectotorneos.services.FieldSrv;
import com.VeizagaTorrico.proyectotorneos.services.GroundSrv;
import com.VeizagaTorrico.proyectotorneos.services.RefereeSrv;
import com.VeizagaTorrico.proyectotorneos.services.TurnSrv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.MacSpi;

public class DetalleEncuentroFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private EditText r1,r2;
    private TextView comp1, comp2,txtCampo,txtPredio,txtJuez,txtTurno;
    private ImageButton confirmarEdit;
    private Spinner spinnerPredio,spinnerCampo,spinnerJuez,spinnerTurno;
    private Confrontation encuentro;
    private ConfrontationSrv confrontationSrv;
    private GroundSrv prediosSrv;
    private FieldSrv camposSrv;
    private List<Ground> predios;
    private List<Field> campos;
    private ArrayAdapter<Field> adapterCampo;
    private RefereeSrv refereeSrv;
    private String juezSeleccionado,campoSeleccionado,turnoSeleccionado,rdo_1,rdo_2;
    private List<Referee> jueces;
    private TurnSrv turnoSrv;
    private List<Turn> turnos;
    private Map<String,String> editEncuentro;

    private Ground predio;
    private Referee referee;
    private Field campo;
    private Turn turno;

    public DetalleEncuentroFragment() {
        // Required empty public constructor
    }

    public static DetalleEncuentroFragment newInstance() {
        DetalleEncuentroFragment fragment = new DetalleEncuentroFragment();
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
        vista = inflater.inflate(R.layout.fragment_detalle_encuentro, container, false);
        initElements();

        llenarSpinnerJuez();
        llenarSpinnerPredio();
        llenarSpinnerTurno();

        confirmarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEncuentro.clear();

                rdo_1 = r1.getText().toString();
                rdo_2 = r2.getText().toString();
                editEncuentro.put("idCompetencia", Integer.toString(encuentro.getIdCompetencia()));
                editEncuentro.put("idEncuentro", Integer.toString(encuentro.getId()));

                if(!juezSeleccionado.isEmpty())
                    editEncuentro.put("idJuez",juezSeleccionado);
                if(!campoSeleccionado.isEmpty())
                    editEncuentro.put("idCampo",campoSeleccionado);
                if(!turnoSeleccionado.isEmpty())
                    editEncuentro.put("idTurno",turnoSeleccionado);
                if(validar()){
                    editEncuentro.put("rdo_comp1", rdo_1);
                    editEncuentro.put("rdo_comp2",rdo_2);
                }
                Log.d("body edit",editEncuentro.toString());

                Call<MsgRequest> call = confrontationSrv.editEncuentro(editEncuentro);
                Log.d("url call", call.request().url().toString());
                call.enqueue(new Callback<MsgRequest>() {
                    @Override
                    public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                        try{
                            if(response.code() == 200){
                                Toast toast = Toast.makeText(vista.getContext(), "Edicion realizada con exito", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                Toast toast = Toast.makeText(vista.getContext(), "Pasaron cosas", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgRequest> call, Throwable t) {

                    }
                });
            }
        });

        return vista;
    }

    private boolean validar() {
        if(rdo_1.isEmpty())
            return false;
        if(rdo_2.isEmpty())
            return false;
        return true;
    }


    private void initElements(){
        this.encuentro = (Confrontation) getArguments().getSerializable("encuentro");
        predios = new ArrayList<>();
        campos = new ArrayList<>();
        jueces = new ArrayList<>();
        turnos = new ArrayList<>();

        editEncuentro = new HashMap<>();
        confirmarEdit = vista.findViewById(R.id.checConfirmar);

        comp1 = vista.findViewById(R.id.txtComp1Titulo);
        comp2 = vista.findViewById(R.id.txtComp2Titulo);

        txtCampo = vista.findViewById(R.id.txtCampo);
        txtPredio = vista.findViewById(R.id.txtPredio);
        txtJuez = vista.findViewById(R.id.txtJuez);
        txtTurno = vista.findViewById(R.id.txtTurno);

        r1 = vista.findViewById(R.id.resultadoComp1);
        r1.setText(Integer.toString(encuentro.getRdoc1()));
        r2 = vista.findViewById(R.id.resultadoComp2);
        r2.setText(Integer.toString(encuentro.getRdoc2()));

        referee = new Referee(0, "Elija un juez", " ",0,null);
        predio = new Ground(0, "Elije un predio", "", "");
        campo = new Field(0, "Elije un capo de Juego", 0, 0, null);
        turno = new Turn(0,0,"Elije un turno", "");

        if(encuentro.getCampo() != null){
            txtCampo.setText(encuentro.getCampo().toString());
            if(encuentro.getCampo().getPredio() != null){
                txtPredio.setText(encuentro.getCampo().getPredio().toString());
            }
        }
        if(encuentro.getJuez() != null){
            txtJuez.setText(encuentro.getJuez().toString());
        }
        if(encuentro.getTurno() != null){
            try {
                Log.d("hora", encuentro.getTurno().toString());
                txtTurno.setText(encuentro.getTurno().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        spinnerPredio = vista.findViewById(R.id.spinnerPredio);
        spinnerCampo = vista.findViewById(R.id.spinnerCampo);
        spinnerJuez = vista.findViewById(R.id.spinnerJuez);
        spinnerTurno = vista.findViewById(R.id.spinnerTurno);

        juezSeleccionado = "";
        campoSeleccionado = "";
        turnoSeleccionado= "";
        prediosSrv = new RetrofitAdapter().connectionEnable().create(GroundSrv.class);
        camposSrv = new RetrofitAdapter().connectionEnable().create(FieldSrv.class);
        refereeSrv = new RetrofitAdapter().connectionEnable().create(RefereeSrv.class);
        turnoSrv = new RetrofitAdapter().connectionEnable().create(TurnSrv.class);
        confrontationSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);
        msjCampos();


        comp1.setText(encuentro.getCompetidor1());
        comp2.setText(encuentro.getCompetidor2());

    }

    private void msjCampos() {
        campos.clear();
        campo = new Field(0,"Por favor agregar campos en la pantalla de carga",0,0,null);
        campos.add(campo);
        adapterCampo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,campos);
        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampo.setAdapter(adapterCampo);

    }

    private void llenarSpinnerTurno() {
        Call<List<Turn>> call = turnoSrv.getTurnsByCompetition(encuentro.getIdCompetencia());
        call.enqueue(new Callback<List<Turn>>() {
            @Override
            public void onResponse(Call<List<Turn>> call, Response<List<Turn>> response) {
                if(response.code() == 200){
                    try {
                        if(!response.body().isEmpty()){
                            turnos.add(turno);

                            turnos.addAll(response.body());
                            ArrayAdapter<Turn> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,turnos);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTurno.setAdapter(adapter);
                            spinnerTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Turn turnoSel = (Turn) spinnerTurno.getSelectedItem();
                                    if(turnoSel.getId() != 0 ){
                                        turnoSeleccionado = Integer.toString(turnoSel.getId());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }else {
                            turno = new Turn(0,0,"Cargar turnos en pantalla de carga", "Cargar turnos en pantalla de carga");
                            turnos.add(turno);
                            ArrayAdapter<Turn> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,turnos);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTurno.setAdapter(adapter);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Turn>> call, Throwable t) {

            }
        });
    }

    private void llenarSpinnerJuez() {

        Call<List<Referee>> call = refereeSrv.getReferees(encuentro.getIdCompetencia());
        Log.d("Call Juez",call.request().url().toString());
        call.enqueue(new Callback<List<Referee>>() {
            @Override
            public void onResponse(Call<List<Referee>> call, Response<List<Referee>> response) {
                try{
                    if(!response.body().isEmpty()){
                        jueces.add(referee);
                        jueces.addAll(response.body());
                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerJuez.setAdapter(adapter);
                        spinnerJuez.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Referee juezSel = (Referee) spinnerJuez.getSelectedItem();
                                if(juezSel.getId() != 0){
                                    juezSeleccionado = Integer.toString(juezSel.getId());
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        referee = new Referee(0, "Por favor agregar jueces en la pantalla de carga", " ",0,null);
                        jueces.add(referee);
                        ArrayAdapter<Referee> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,jueces);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerJuez.setAdapter(adapter);
                    }

                } catch (Exception e) {
                        e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Referee>> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void llenarSpinnerPredio(){
        Call<List<Ground>> call = prediosSrv.getGrounds(encuentro.getIdCompetencia());
        Log.d("call predio",call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                try {
                    Log.d("encuentro response", Integer.toString(response.code()));

                    if(!response.body().isEmpty()) {
                        predios.add(predio);
                        predios.addAll(response.body());
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPredio.setAdapter(adapter);
                        spinnerPredio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Ground predio = (Ground) spinnerPredio.getSelectedItem();
                                if (predio.getId() != 0) {
                                    // hacer algo para despues actualizar el encuentro
                                    llenarSpinnerCampos(predio.getId());
                                } else {
                                    msjCampos();
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        predio = new Ground(0, "Por favor agregar predios en la pantalla de carga", "", "");
                        predios.add(predio);
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPredio.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Ground>> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void llenarSpinnerCampos(int idPredio){
        if(idPredio != 0){
            Call<List<Field>> call = camposSrv.getFieldsByGround(idPredio);
            Log.d("calls campo",call.request().url().toString());
            call.enqueue(new Callback<List<Field>>() {
                @Override
                public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                    try {
                        if(!response.body().isEmpty()) {
                            campos.clear();
                            campos.add(campo);
                            campos.addAll(response.body());
                            adapterCampo = new ArrayAdapter<>(vista.getContext(), android.R.layout.simple_spinner_item, campos);
                            adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCampo.setAdapter(adapterCampo);
                            spinnerCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Field campoSel = (Field) spinnerCampo.getSelectedItem();
                                    if(campoSel.getId() != 0){
                                        campoSeleccionado = Integer.toString(campoSel.getId());
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }else {
                            msjCampos();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Field>> call, Throwable t) {
                    try{
                        Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            msjCampos();
        }
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
