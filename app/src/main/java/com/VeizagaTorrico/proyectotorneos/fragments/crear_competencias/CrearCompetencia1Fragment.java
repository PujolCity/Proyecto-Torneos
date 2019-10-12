package com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;

import java.util.Calendar;

public class CrearCompetencia1Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

   // ***************************************** //
    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    private TextView txtView;
    private ImageButton ibObtenerFecha;
    private EditText txtNmbComp, txtFecha;
    private Button btnSig;
    private String nombreComp;
    private Competition competition;
    private CompetitionSrv competitionSrv;
    private View vista;

    public CrearCompetencia1Fragment() {
        // Required empty public constructor
    }

    public static CrearCompetencia1Fragment newInstance(String param1, String param2) {
        CrearCompetencia1Fragment fragment = new CrearCompetencia1Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_competencia1, container, false);

        competition =  new Competition();
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
        //Widget TextView donde se mostrara el nombre de la competencia
        //txtView = vista.findViewById(R.id.txtNmbComp);
        //Wdget de donde tomo el posible nombre de la competencia
        txtNmbComp = vista.findViewById(R.id.txtNmbComp);
        //Widget TextView donde se mostrara la fecha obtenida
        txtFecha = vista.findViewById(R.id.txtfechaComp);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = vista.findViewById(R.id.ib_obtener_fecha);
        //Evento setOnClickListener - clic

        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });
        btnSig = vista.findViewById(R.id.btnCCSig_1);
        btnSig.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          nombreComp = txtNmbComp.getText().toString();
                                          Log.d("dato enviado",nombreComp);
                                          Call<Success> call = competitionSrv.comprobar(nombreComp);
                                          call.enqueue(new Callback<Success>() {
                                              @Override
                                              public void onResponse(Call<Success> call, Response<Success> response) {
                                                  Log.d("onResponse", Integer.toString(response.code()));
                                                  if ( response.code() == 200 && !response.body().isExiste()) {
                                                      Bundle bundle = new Bundle();
                                                      competition.setName(txtNmbComp.getText().toString());
                                                      competition.setInitDate(c);
                                                      bundle.putSerializable("competition", competition);

                                                      // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                                                      Navigation.findNavController(vista).navigate(R.id.crearCompetencia2Fragment, bundle);
                                                  }else {
                                                      Toast toast = Toast.makeText(getContext(), "Competencia Existente", Toast.LENGTH_SHORT);
                                                      toast.show();
                                                  }
                                              }
                                              @Override
                                              public void onFailure(Call<Success> call, Throwable t) {
                                                  Toast toast = Toast.makeText(getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                                                  toast.show();
                                                  Log.d("onFailure", t.getMessage());

                                              }
                                          });
                                      }
                                  }
        );
        return vista;
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

    private void obtenerFecha(){
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                c.set(year,mesActual ,dayOfMonth);
                txtFecha.setText(diaFormateado+ BARRA + mesFormateado + BARRA + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /*
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        datePicker.show();
    }
}