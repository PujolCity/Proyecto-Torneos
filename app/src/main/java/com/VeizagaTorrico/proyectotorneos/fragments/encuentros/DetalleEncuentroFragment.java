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

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.services.FieldSrv;
import com.VeizagaTorrico.proyectotorneos.services.GroundSrv;

import java.util.ArrayList;
import java.util.List;

public class DetalleEncuentroFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private EditText r1,r2;
    private TextView comp1, comp2,fecha,hora;
    private ImageButton fechaImg, horaImg;
    private Spinner spinnerPredio,spinnerCampo,spinnerJuez;
    private Confrontation encuentro;
    private GroundSrv prediosSrv;
    private FieldSrv camposSrv;
    private List<Ground> predios;
    private List<Field> campos;
    private ArrayAdapter<Field> adapterCampo;

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

        llenarSpinnerPredio();

        return vista;
    }

    private void llenarSpinnerPredio(){
        Ground predio = new Ground(0,"Elije un predio","","");
        predios.add(predio);
        Call<List<Ground>> call = prediosSrv.getGrounds(encuentro.getIdCompetencia());
        Log.d("call predio",call.request().url().toString());
        call.enqueue(new Callback<List<Ground>>() {
            @Override
            public void onResponse(Call<List<Ground>> call, Response<List<Ground>> response) {
                Log.d("encuentro response", response.body().toString());
                if(response.code() == 200){
                    try {
                        predios.addAll(response.body());
                        ArrayAdapter<Ground> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,predios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPredio.setAdapter(adapter);
                        spinnerPredio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Ground predio = (Ground) spinnerPredio.getSelectedItem();
                                if(predio.getId() != 0){
                                    // hacer algo para despues actualizar el encuentro
                                    llenarSpinnerCampos(predio.getId());
                                }else {

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Ground>> call, Throwable t) {

            }
        });

    }

    private void llenarSpinnerCampos(int idPredio){


        Call<List<Field>> call = camposSrv.getFieldsByGround(idPredio);
        Log.d("calls campo",call.request().url().toString());
        call.enqueue(new Callback<List<Field>>() {
            @Override
            public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                Log.d("campos response", response.body().get(1).getNombre());
                if(response.code() == 200){
                    try {
                        campos.addAll(response.body());
                        adapterCampo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,campos);
                        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCampo.setAdapter(adapterCampo);
                        spinnerCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Field>> call, Throwable t) {

            }
        });


    }

    private void initElements(){
        this.encuentro = (Confrontation) getArguments().getSerializable("encuentro");
        predios = new ArrayList<>();
        campos = new ArrayList<>();

        comp1 = vista.findViewById(R.id.txtComp1Titulo);
        comp2 = vista.findViewById(R.id.txtComp2Titulo);
        r1 = vista.findViewById(R.id.etResultadoC1);
        r2 = vista.findViewById(R.id.etResultadoC2);
        spinnerPredio = vista.findViewById(R.id.spinnerPredio);
        spinnerCampo = vista.findViewById(R.id.spinnerCampo);
        spinnerJuez = vista.findViewById(R.id.spinnerJuez);
        fechaImg = vista.findViewById(R.id.calendarImg);
        horaImg = vista.findViewById(R.id.relojImg);
        fecha = vista.findViewById(R.id.txtDiaEncuentro);
        hora = vista.findViewById(R.id.txtHoraEncuentro);

        prediosSrv = new RetrofitAdapter().connectionEnable().create(GroundSrv.class);
        camposSrv = new RetrofitAdapter().connectionEnable().create(FieldSrv.class);

        Field campo = new Field(0,"Elije un capo de Juego",0,0,null);
        campos.add(campo);
        adapterCampo = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,campos);
        adapterCampo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampo.setAdapter(adapterCampo);



        comp1.setText(encuentro.getCompetidor1());
        comp2.setText(encuentro.getCompetidor2());
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
