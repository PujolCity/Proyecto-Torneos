package com.VeizagaTorrico.proyectotorneos.fragments.competencias;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.CompetenciasMinRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompetenciasListFragment extends Fragment implements MensajeSinInternet{

    private OnFragmentInteractionListener mListener;

    private CompetitionSrv competitionSrv;
    private CompetenciasMinRecyclerViewAdapter adapter;
    private List<CompetitionMin> competitions;
    private View vista;
    private TextView sinCompetencias;
    private RecyclerView recycleComp;
    private RecyclerView.LayoutManager manager;
    private Map<String,String> filtros;
    private TextView sinConexion;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog alertDialog;

    public CompetenciasListFragment() {
        // Required empty public constructor
    }

     public static CompetenciasListFragment newInstance() {
        CompetenciasListFragment fragment = new CompetenciasListFragment();
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
        vista = inflater.inflate(R.layout.fragment_competencias_list, container, false);
        initAdapter();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            inflarRecycler();
            refresh();
        } else {
            sinInternet();
            refresh();
        }
        return vista;
    }

    private void refresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    inflarRecycler();
                } else {
                    sinInternet();
                }
                swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void sinInternet() {
        sinConexion.setVisibility(View.VISIBLE);
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento no se podran ver las competencias. Por favor intente mas tarde", Toast.LENGTH_LONG);
        toast.show();
        sinCompetencias();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void initAdapter(){
        filtros = (Map<String, String>) getArguments().getSerializable("filtros");
        Log.d("Filtros recibidos",filtros.toString());

        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
        sinCompetencias = vista.findViewById(R.id.tv_sinCompetencias);
        // COSAS PARA LLENAR El RECYCLERVIEW
        competitions = new ArrayList<>();
        recycleComp = vista.findViewById(R.id.recycleCompView);
        manager = new LinearLayoutManager(vista.getContext());
        recycleComp.setLayoutManager(manager);
        recycleComp.setHasFixedSize(true);
        adapter = new CompetenciasMinRecyclerViewAdapter(vista.getContext());
        recycleComp.setAdapter(adapter);
        sinConexion = vista.findViewById(R.id.tv_sin_conexion_compe);
        swipeRefreshLayout = vista.findViewById(R.id.refreshCompe);
        alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Espere un momento..");

    }


    private void siguienteFragment(CompetitionMin competition){

        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", competition);

        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
        Navigation.findNavController(vista).navigate(R.id.detalleCompetenciaFragment, bundle);
    }


    private void inflarRecycler() {
        recycleComp.setVisibility(View.VISIBLE);
        sinConexion.setVisibility(View.GONE);
        alertDialog.show();
        //En call viene el tipo de dato que espero del servidor
        Call<List<CompetitionMin>> call = competitionSrv.findCompetitionsByFilters(filtros);
        Log.d("URL FILTROS: ",call.request().url().toString());
        call.enqueue(new Callback<List<CompetitionMin>>() {
            @Override
            public void onResponse(Call<List<CompetitionMin>> call, Response<List<CompetitionMin>> response) {
                alertDialog.dismiss();
                Log.d("RESP CODE COMPETITION", Integer.toString(response.code()));
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    //asigno a deportes lo que traje del servidor
                    competitions = response.body();
                    Log.d("RESP CODE COMPETITION", competitions.toString());
                    if(competitions.size() != 0){
                        sinCompetencias.setVisibility(View.INVISIBLE);
                        adapter.setCompetencias(competitions);
                        //CREO EL ADAPTER Y LO SETEO PARA QUE INFLE EL LAYOUT
                        recycleComp.setAdapter(adapter);
                        //LISTENER PARA EL ELEMENTO SELECCIONADO
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CompetitionMin competition = competitions.get(recycleComp.getChildAdapterPosition(view));
                                siguienteFragment(competition);
                            }
                        });
                    }else {
                        sinCompetencias();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CompetitionMin>> call, Throwable t) {
                try{
                    alertDialog.dismiss();
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_LONG);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                    sinCompetencias();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sinCompetencias() {
        recycleComp.setVisibility(View.INVISIBLE);
        sinCompetencias.setVisibility(View.VISIBLE);
    }
}
