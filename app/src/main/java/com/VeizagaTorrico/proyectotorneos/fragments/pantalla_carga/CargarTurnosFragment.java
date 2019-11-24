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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.VeizagaTorrico.proyectotorneos.R;

import java.util.Calendar;

public class CargarTurnosFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private TimePicker tmpHorario;
    private ImageButton btnHasta;
    private EditText etHora;
    private final Calendar c = Calendar.getInstance();

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

        btnHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHora();
                Log.d("horaOb 1...",Integer.toString(horaOb) + " : " + Integer.toString(minutoOb) );

                Log.d("hora",Integer.toString(hora) + " : " + Integer.toString(minuto) );
            }
        });


        return vista;
    }
    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(vista.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);
                etHora.setText(horaFormateada +" : "+ minutoFormateado + " hs");
                horaOb = hourOfDay;
                minutoOb = minute;
                Log.d("horaOb 2...",Integer.toString(horaOb) + " : " + Integer.toString(minutoOb) );

            }
        },hora,minuto,true);

        recogerHora.show();

        Log.d("HORA@",Integer.toString(hora) + " : " + Integer.toString(minuto) );
        Log.d("horaOb 3...",Integer.toString(horaOb) + " : " + Integer.toString(minutoOb) );

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
        // ponemos el timepicker en 24 hs
        tmpHorario = vista.findViewById(R.id.tm_pick);
        tmpHorario.setIs24HourView(true);

        btnHasta =  vista.findViewById(R.id.btnHoraHasta);
        etHora = vista.findViewById(R.id.horaHasta);

    }
}
