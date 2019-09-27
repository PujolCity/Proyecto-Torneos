package com.VeizagaTorrico.proyectotorneos.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
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

import com.VeizagaTorrico.proyectotorneos.NavigationMainActivity;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.adapters.CompetitionAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrearCompetencia1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrearCompetencia1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearCompetencia1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearCompetencia1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearCompetencia1Fragment newInstance(String param1, String param2) {
        CrearCompetencia1Fragment fragment = new CrearCompetencia1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_competencia1, container, false);

        competition =  new Competition();
        competitionSrv = new CompetitionAdapter().connectionEnable();
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
                                                  if (!response.body().isExiste()) {
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
                                                  Toast toast = Toast.makeText(getContext(), "No anda una mierda", Toast.LENGTH_SHORT);
                                                  toast.show();
                                                  Log.d("onResponse", "no anda");

                                              }
                                          });
                                      }
                                  }
        );
        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        datePicker.show();
    }
}