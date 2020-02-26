package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.services.InscriptionSrv;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearInscripcionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    private List<String> fechaIni ;
    private List<String> fechaCierre ;

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private View vista;
    private EditText etFechaInicio,etFechacierre, etMonto, etRequisitos;
    private CompetitionMin competencia;
    private Button btnCrearInscripcion;
    private Date inicio, cierre;
    private Map<String,String> body;
    private String monto, requisito;
    private InscriptionSrv inscriptionSrv;

    private boolean incrispcionCreada;

    public CrearInscripcionFragment() {
    }

    public static CrearInscripcionFragment newInstance() {
        CrearInscripcionFragment fragment = new CrearInscripcionFragment();
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
        vista = inflater.inflate(R.layout.fragment_crear_inscripcion, container, false);
        initElements();
        listenButton();

        return vista;
    }

    private void initElements() {
        incrispcionCreada = false;
        inscriptionSrv = new RetrofitAdapter().connectionEnable().create(InscriptionSrv.class);

        competencia = (CompetitionMin) getArguments().getSerializable("competencia");
        fechaIni = new ArrayList<>();
        fechaCierre = new ArrayList<>();
        body = new HashMap<>();

        etFechaInicio = vista.findViewById(R.id.et_fechaInicio_inscripcion);
        etFechacierre = vista.findViewById(R.id.et_fechaCierre_inscripcion);
        etMonto = vista.findViewById(R.id.et_monto_inscripcion);
        etRequisitos = vista.findViewById(R.id.et_requisitos_inscripcion);
        btnCrearInscripcion = vista.findViewById(R.id.btn_crear_inscripcion);
    }

    private void listenButton() {
        btnCrearInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(incrispcionCreada){
                    Toast toast = Toast.makeText(vista.getContext(), "La competencia ya cuenta con una inscripcion", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(validar()){
                    try {
                        inicio = new SimpleDateFormat("yyyy/MM/dd").parse(fechaIni.get(1));
                        cierre = new SimpleDateFormat("yyyy/MM/dd").parse(fechaCierre.get(1));

                        if(inicio.compareTo(cierre) < 0){
                                body.put("idCompetencia",Integer.toString(competencia.getId()));
                                body.put("fechaInicio", fechaIni.get(0));
                                body.put("fechaCierre", fechaCierre.get(0));
                                monto = etMonto.getText().toString();
                                requisito = etRequisitos.getText().toString();
                                if(!monto.isEmpty())
                                    body.put("monto", monto);
                                if(!requisito.isEmpty())
                                    body.put("requisitos",requisito);

                                Log.d("Body INSCRIPCION",body.toString());
                                Call<MsgRequest> call = inscriptionSrv.crearInscripcion(body);
                                Log.d("URL INSCRIPCION", call.request().url().toString());
                                call.enqueue(new Callback<MsgRequest>() {
                                    @Override
                                    public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                                        Log.d("Response Inscripcion", Integer.toString(response.code()));
                                        if(response.code() == 201) {
                                            Log.d("response INSCRIPCION", Integer.toString(response.code()));
                                            Toast toast = Toast.makeText(vista.getContext(), "Inscripcion Creada", Toast.LENGTH_SHORT);
                                            toast.show();
                                            incrispcionCreada = true;
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
                                    public void onFailure(Call<MsgRequest> call, Throwable t) {
                                        try {
                                            Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_LONG);
                                            toast.show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        }else {
                            Toast toast = Toast.makeText(vista.getContext(), "La fecha de Cierre debe ser mayor a la fecha de Inicio", Toast.LENGTH_LONG);
                            toast.show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor complete los campos vacios", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        etFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaIni = obtenerFecha(etFechaInicio);
            }
        });
        etFechacierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaCierre = obtenerFecha(etFechacierre);
            }
        });
    }

    private boolean validar() {
        if(fechaIni.isEmpty())
            return false;
        if(fechaCierre.isEmpty())
            return false;
        return  true;
    }


    private List<String> obtenerFecha(final EditText setFecha){
        final List<String> fecha = new ArrayList<>();

        DatePickerDialog datePicker = new DatePickerDialog(vista.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                String mes = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf( mesActual+1);
                //Muestro la fecha con el formato deseado
                c.set(year,mesActual ,dayOfMonth);
                setFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                fecha.add(year + "-" + mesFormateado + "-" + diaFormateado);
                fecha.add(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /*
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        datePicker.show();
        return fecha;
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
