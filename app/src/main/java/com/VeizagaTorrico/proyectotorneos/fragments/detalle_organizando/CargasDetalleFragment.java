package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.MsgResponse;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class CargasDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
    private Button btnPredio, btnTurno, btnJuez, btnGenerar, btnInvitar, btnSigFase, btnNoticias;
    private AlertDialog alertDialog;

    private CompetitionSrv competenciaSrv;

    public CargasDetalleFragment() {
        // Required empty public constructor
    }

    public static CargasDetalleFragment newInstance() {
        CargasDetalleFragment fragment = new CargasDetalleFragment();
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
        vista = inflater.inflate(R.layout.fragment_cargas_detalle, container, false);
        initElements();
        listenButtons();

        return vista;
    }

    private void initElements() {
        competenciaSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);

        btnPredio = vista.findViewById(R.id.btnCargarPredio);
        btnTurno = vista.findViewById(R.id.btnCargarTurno);
        btnJuez = vista.findViewById(R.id.btnCargarJuez);
        btnGenerar = vista.findViewById(R.id.btnGenerarEncuentros);
        btnInvitar = vista.findViewById(R.id.btnInvitar);
        btnSigFase = vista.findViewById(R.id.btnSigFase);
        btnNoticias = vista.findViewById(R.id.btnPubNoticias);

        if(competencia.getRol().contains("ORGANIZADOR")){
            btnInvitar.setVisibility(View.VISIBLE);
        }
        else{
            btnInvitar.setVisibility(View.GONE);
        }

        if(competencia.getTypesOrganization().contains("grupo") || competencia.getTypesOrganization().contains("Eliminatorias")){
            btnSigFase.setVisibility(View.VISIBLE);
        }else {
            btnSigFase.setVisibility(View.INVISIBLE);
        }
    }

    private void listenButtons() {
        // ponemos a la escucha el boton de cargar Predio y campos
        btnPredio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("competencia", competencia);
                    Navigation.findNavController(vista).navigate(R.id.cargaPredioCompetenciaFragment    , bundle);
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ponemos a la escucha el boton de cargar Turnos
        btnTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    if(!competencia.getEstado().contains("INICIADA")) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("competencia", competencia);
                        Navigation.findNavController(vista).navigate(R.id.cargarTurnoFragment, bundle);
                    }
                    else{
                        Toast.makeText(vista.getContext(), "No se pueden modificar los turnos de una competencia iniciada", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ponemos a la escucha el boton de cargar Turnos
        btnJuez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("competencia", competencia);
                    Navigation.findNavController(vista).navigate(R.id.cargaJuezCompetenciaFragment, bundle);
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ponemos a la escucha el boton de cargar Turnos
        btnNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("competencia", competencia);
                    Navigation.findNavController(vista).navigate(R.id.cargarNoticiaFragment, bundle);
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    Call<MsgRequest> call = competenciaSrv.generarEncuentros(competencia.getId());
                    Log.d("Url Call", call.request().url().toString());
                    // mostramos mje de carga
                    alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Generando encuentros..");
                    alertDialog.show();
                    try {
                        call.enqueue(new Callback<MsgRequest>() {
                            @Override
                            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                                Log.d("response", Integer.toString(response.code()));
                                if (response.code() == 200) {
                                    alertDialog.dismiss();
                                    Toast toast = Toast.makeText(vista.getContext(), "Encuentros generados", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                if (response.code() == 400) {
                                    alertDialog.dismiss();
                                    Log.d("RESP_SIGNIN_ERROR", "PETICION MAL FORMADA: " + response.errorBody().toString());
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.errorBody().string());
                                        String userMessage = jsonObject.getString("msg");
                                        Log.d("RESP_SIGNIN_ERROR", "Msg de la repuesta: " + userMessage);
                                        Toast.makeText(vista.getContext(), "No se pueden generar mas encuentros:  << " + userMessage + " >>", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return;
                                }
                            }

                            @Override
                            public void onFailure(Call<MsgRequest> call, Throwable t) {
                                alertDialog.dismiss();
                                Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("competencia", competencia);
                    Navigation.findNavController(vista).navigate(R.id.coOrganizadorFragment, bundle);
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSigFase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    if (competencia.getFaseActual().equals("1")) {
                        Toast.makeText(vista.getContext(), "La competencia ya se encuentra en su fase final", Toast.LENGTH_LONG).show();
                    } else {
                        Call<MsgResponse> call = competenciaSrv.faseCompleta(competencia.getId());
                        Log.d("Url Call", call.request().url().toString());
                        try {
                            call.enqueue(new Callback<MsgResponse>() {
                                @Override
                                public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                                    Log.d("response", Integer.toString(response.code()));
                                    if (response.code() == 200) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("competencia", competencia);
                                        Navigation.findNavController(vista).navigate(R.id.cargaFaseFragment, bundle);
                                    }
                                    if (response.code() == 400) {
                                        Log.d("BTN_SIG_FASE_ERROR", "PETICION MAL FORMADA: " + response.errorBody());
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response.errorBody().string());
                                            String userMessage = jsonObject.getString("messaging");
                                            Log.d("BTN_SIG_FASE", "Msg de la repuesta: " + userMessage);
                                            Toast.makeText(vista.getContext(), "No es posible esta opcion:  << " + userMessage + " >>", Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                }

                                @Override
                                public void onFailure(Call<MsgResponse> call, Throwable t) {
                                    Toast.makeText(vista.getContext(), "Problemas con el servidor: intente recargar la pestaña", Toast.LENGTH_SHORT).show();
                                    Log.d("onFailure", t.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
