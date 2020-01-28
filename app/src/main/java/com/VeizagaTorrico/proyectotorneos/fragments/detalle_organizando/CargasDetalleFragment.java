package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

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
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;

public class CargasDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
 private Button btnPredio;
    private Button btnTurno;
    private Button btnJuez;
    private Button btnGenerar;
    private Button btnInvitar;
    private Button btnSigFase;

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
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.cargarPredioFragment, bundle);
            }
        });

        // ponemos a la escucha el boton de cargar Turnos
        btnTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.cargarTurnoFragment, bundle);
            }
        });

        // ponemos a la escucha el boton de cargar Turnos
        btnJuez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.cargarJuezFragment, bundle);
            }
        });

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<MsgRequest> call = competenciaSrv.generarEncuentros(competencia.getId());
                Log.d("Url Call", call.request().url().toString());
                try {
                    call.enqueue(new Callback<MsgRequest>() {
                        @Override
                        public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                            Log.d("response",Integer.toString(response.code()));
                            if(response.code() == 200) {
                                Toast toast = Toast.makeText(vista.getContext(), "Encuentros generados", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MsgRequest> call, Throwable t) {
                            Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.coOrganizadorFragment, bundle);

            }
        });

        btnSigFase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competencia", competencia);
                Navigation.findNavController(vista).navigate(R.id.cargaFaseFragment, bundle);
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
