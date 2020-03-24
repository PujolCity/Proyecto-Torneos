package com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerCompetitionOff;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;

public class OrganizandoFragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;
    private CompetitionSrv competitionSrv;
    private CompetenciasMinRecyclerViewAdapter adapter;
    private List<CompetitionMin> competitions;
    private View vista;
    private RecyclerView recycleComp;
    private RecyclerView.LayoutManager manager;
    private TextView sinCompetenciasTv;

    private ManagerCompetitionOff adminCompetenciasOff;

    public OrganizandoFragment() {
    }

    public static OrganizandoFragment newInstance() {
        OrganizandoFragment fragment = new OrganizandoFragment();
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
        vista = inflater.inflate(R.layout.fragment_organizando, container, false);
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);

        initAdapter();
        inflarRecycler();

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

    @Override
    public void sinInternet() {
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, se utilizaran datos descargados previamente", Toast.LENGTH_LONG);
        toast.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void initAdapter(){
        sinCompetenciasTv = vista.findViewById(R.id.tv_Organizando);
        // COSAS PARA LLENAR El RECYCLERVIEW
        competitions = new ArrayList<>();
        recycleComp = vista.findViewById(R.id.recycleOrganizando);
        manager = new LinearLayoutManager(vista.getContext());
        recycleComp.setLayoutManager(manager);
        recycleComp.setHasFixedSize(true);
        adapter = new CompetenciasMinRecyclerViewAdapter(vista.getContext());
        recycleComp.setAdapter(adapter);
    }


    private void inflarRecycler() {
        int idUsuarioregistrado = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));

        if(NetworkReceiver.existConnection(vista.getContext())) {
            Call<List<CompetitionMin>> call = competitionSrv.getCompetitionsOrganize(idUsuarioregistrado);
            Log.d("request retrofit", call.request().url().toString());
            call.enqueue(new Callback<List<CompetitionMin>>() {
                @Override
                public void onResponse(Call<List<CompetitionMin>> call, Response<List<CompetitionMin>> response) {
                    Log.d("RESP CODE COMPETITION", Integer.toString(response.code()));
                    //codigo 200 si salio tdo bien
                    if (response.code() == 200) {
                        competitions = response.body();
                        mostrarCompetencias();
                    }
                }

                @Override
                public void onFailure(Call<List<CompetitionMin>> call, Throwable t) {
                    try {
                        Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d("onFailure", t.getMessage());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else{
            adminCompetenciasOff = new ManagerCompetitionOff(vista.getContext());
            competitions = adminCompetenciasOff.competitionByRol("ORGANIZADOR");
            mostrarCompetencias();
            sinInternet();
        }
    }

    private void mostrarCompetencias(){
        if (competitions.size() != 0) {
            try {
                adapter.setCompetencias(competitions);
                //CREO EL ADAPTER Y LO SETEO PARA QUE INFLE EL LAYOUT
                recycleComp.setAdapter(adapter);
                //LISTENER PARA EL ELEMENTO SELECCIONADO
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CompetitionMin competition = competitions.get(recycleComp.getChildAdapterPosition(view));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("competencia", competition);
                        Log.d("COMPENTENCIA SC", competition.toString());
                        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                        Navigation.findNavController(vista).navigate(R.id.detalleOrganizandoFragment, bundle);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sinCompetencias();
        }
    }

    private void sinCompetencias() {
        sinCompetenciasTv.setVisibility(View.VISIBLE);
        recycleComp.setVisibility(View.INVISIBLE);
    }
}
