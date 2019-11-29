package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.VeizagaTorrico.proyectotorneos.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CargarTurnosFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private Button btnTurno;
    private ImageButton delete;
    private Spinner spinnerTurnos;
    private EditText etHoraDesde, etHoraHasta;
    private final Calendar c = Calendar.getInstance();
    private List<String> hsDesde,hsHasta;
    private String segundos;
    private final int hora = c.get(Calendar.HOUR_OF_DAY);
    private final int minuto = c.get(Calendar.MINUTE);

    private int horaOb ;
    private int minutoOb ;


    public CargarTurnosFragment() {

        // Required empty public constructor
    }

    public static CargarTurnosFragment newInstance() {
        CargarTurnosFragment fragment = new CargarTurnosFragment();
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
        vista = inflater.inflate(R.layout.fragment_cargar_turnos, container, false);
        initElements();

        etHoraDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               hsDesde = obtenerHora(etHoraDesde);
            }
        });

        etHoraHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hsHasta = obtenerHora(etHoraHasta);
            }
        });

        btnTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Lo que se envia", hsDesde.toString());
                Log.d("Lo OTRO", hsHasta.toString());

                if(validar()){

                }

            }
        });
        return vista;
    }

    private boolean validar() {
        return false;
    }

    private List<String> obtenerHora(final EditText setHora){
        final List<String> hs = new ArrayList<>();
        TimePickerDialog recogerHora = new TimePickerDialog(vista.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);
                setHora.setText(horaFormateada+" : "+minutoFormateado+" hs");
                hs.add(horaFormateada);
                hs.add(minutoFormateado);
                hs.add(segundos);
            }
        },hora,minuto,true);

        recogerHora.show();
        return hs;
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

    private void initElements() {
        segundos = "00";

        btnTurno = vista.findViewById(R.id.btnCrearTurno);
        delete = vista.findViewById(R.id.deleteTurno);
        spinnerTurnos = vista.findViewById(R.id.spinnerCargaTurno);
        etHoraDesde = vista.findViewById(R.id.horaHasta);
        etHoraHasta = vista.findViewById(R.id.etHoraHasta);
    }


}
