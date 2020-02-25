package com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.models.Turn;
import com.VeizagaTorrico.proyectotorneos.services.TurnSrv;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargarTurnosFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CompetitionMin competencia;
    private View vista;
    private Button btnTurno;
    private ImageButton delete;
    private Spinner spinnerTurnos;
    private EditText etHoraDesde, etDuracion, etCantidad;
    private final Calendar c = Calendar.getInstance();
    private List<String> hsDesde;
    private String duracion, cantidad;
    private Map<String,String> datos;
    private String segundos;
    private final int hora = c.get(Calendar.HOUR_OF_DAY);
    private final int minuto = c.get(Calendar.MINUTE);
    private static final int DIFERENCIA_MINUTOS = 10;
    private TurnSrv turnSrv;
    private List<Turn> turnos;
    private Turn turnoSeleccionado;

    public CargarTurnosFragment() {
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
        llenarSpinner();
        etHoraDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               hsDesde = obtenerHora(etHoraDesde);
            }
        });

        btnTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    duracion = etDuracion.getText().toString();
                    cantidad = etCantidad.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    Date date1= null;
                    if(!hsDesde.isEmpty()){
                        date1 = sdf.parse(hsDesde.get(0));
                        Log.d("HORAS ELEGIDAS", date1.toString());

                        if(validar(date1, cantidad)){
                            datos.put("idCompetencia", Integer.toString(competencia.getId()));
                            datos.put("horaInicio",sdf.format(date1));
                            datos.put("cantidad", cantidad);
                            if(!duracion.isEmpty()){
                                datos.put("duracion",duracion);
                            }
                            Log.d("DATOS", datos.toString());
                            Call<Success> call = turnSrv.createTurn(datos);
                            Log.d("URL TURNO", call.request().url().toString());
                            call.enqueue(new Callback<Success>() {
                                @Override
                                public void onResponse(Call<Success> call, Response<Success> response) {
                                    if(response.code() == 201) {
                                        Log.d("response code TURNO", Integer.toString(response.code()));
                                        Toast toast = Toast.makeText(vista.getContext(), "Turno Creado", Toast.LENGTH_SHORT);
                                        toast.show();
                                        llenarSpinner();
                                    }
                                    if (response.code() == 400) {
                                        Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response.errorBody().string());
                                            String userMessage = jsonObject.getString("messaging");
                                            Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                                            Toast.makeText(vista.getContext(), "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<Success> call, Throwable t) {
                                 try {
                                     Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                                     toast.show();
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                                }
                            });
                        }else {
                            Toast toast = Toast.makeText(vista.getContext(), "El turno debe tener al menos "+ DIFERENCIA_MINUTOS + " minutos de duracion", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }else {
                        Toast toast = Toast.makeText(vista.getContext(), "Por favor verificar las horas asignadas", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        listenerBorrar();
        longListenerBorrar();
        return vista;
    }

    private void longListenerBorrar() {
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast toast = Toast.makeText(vista.getContext(), "BORRAR TODOS LOS TURNOS", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        });
    }

    private void listenerBorrar() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(vista.getContext(), "BORRAR UN TURNO", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void llenarSpinner() {
        turnos.clear();
        Call<List<Turn>> call = turnSrv.getTurnsByCompetition(competencia.getId());
        call.enqueue(new Callback<List<Turn>>() {
            @Override
            public void onResponse(Call<List<Turn>> call, Response<List<Turn>> response) {
                try {
                    if (response.code() == 400) {
                        Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            String userMessage = jsonObject.getString("messaging");
                            Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                            Toast.makeText(vista.getContext(), "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(!response.body().isEmpty()){
                        delete.setVisibility(View.VISIBLE);
                        turnos = response.body();
                        ArrayAdapter<Turn> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,turnos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTurnos.setAdapter(adapter);
                        spinnerTurnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                turnoSeleccionado = (Turn) spinnerTurnos.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else {
                        delete.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Turn>> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private boolean validar(Date date1, String cant) {
        if(date1.getTime() == 0)
            return false;
        if(cant.isEmpty())
            return false;
        return  true;
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
                hs.add(horaFormateada + ":"+ minutoFormateado + ":" + segundos);

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
        turnSrv = new RetrofitAdapter().connectionEnable().create(TurnSrv.class);
        competencia = (CompetitionMin) getArguments().getSerializable("competencia");
        hsDesde = new ArrayList<>();
        turnos = new ArrayList<>();
        datos = new HashMap<>();
        segundos = "00";
        btnTurno = vista.findViewById(R.id.btnCrearTurno);
        delete = vista.findViewById(R.id.deleteTurno);
        spinnerTurnos = vista.findViewById(R.id.spinnerCargaTurno);
        etHoraDesde = vista.findViewById(R.id.horaHasta);
        etDuracion = vista.findViewById(R.id.etDuracion);
        etCantidad = vista.findViewById(R.id.et_cantidadTurnos_turnos);
    }
}
