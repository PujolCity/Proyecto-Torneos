package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.CompetidoresRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.offline.admin.AdminDataOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerCompetitorOff;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

public class CompetidoresDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private UserSrv userSrv;
    private List<User> competidores;
    private View vista;
    private RecyclerView recycle;
    private CompetitionMin competencia;
    private ImageButton solicitudes;
    private TextView sinCompetidores;
    private CompetidoresRecyclerViewAdapter adapter;
    private AdminDataOff adminDataOff;

    public CompetidoresDetalleFragment() {
        // Required empty public constructor
    }

    public static CompetidoresDetalleFragment newInstance(String param1, String param2) {
        CompetidoresDetalleFragment fragment = new CompetidoresDetalleFragment();
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
        vista = inflater.inflate(R.layout.fragment_competidores_detalle, container, false);
        initElement();
        listeners();
        inflarRecycler();

        return vista;
    }

    private void listeners() {
        solicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.EXTRA_KEY_ID_COMPETENCIA, String.valueOf(competencia.getId()));

                    // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                    Navigation.findNavController(vista).navigate(R.id.competidoresListFragment, bundle);
                }
                else{
                    Toast.makeText(vista.getContext(), R.string.mjeSinConexion, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inflarRecycler() {
        recycle.setVisibility(View.VISIBLE);
        if(NetworkReceiver.existConnection(vista.getContext())) {
            getCompetitors();
        }
        else{
            getCompetitiorsOffline();
        }
    }

    private void getCompetitors(){
        Call<List<User>> call = userSrv.getCompetidoresByCompetencia(competencia.getId());
        Log.d("call competencia",call.request().url().toString());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("RESPONSE CODE USERS", Integer.toString(response.code()));
                if(response.code() == 200){
                    try {
                        competidores = response.body();
                        if(competidores.size() != 0){
                            Log.d("COMPETIDORES",competidores.toString());
                            adapter.setCompetidores(competidores);
                            recycle.setAdapter(adapter);
                            sinCompetidores.setVisibility(View.INVISIBLE);
                        } else {
                            sinCompetidores();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                try {
                    Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT).show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // recupera los competidores de la competencia almacenados localmente
    private void getCompetitiorsOffline(){
        competidores = adminDataOff.competitorsByCompetition(competencia.getId());
        if(competidores.size() > 0){
            Log.d("COMPETIDORES",competidores.toString());
            adapter.setCompetidores(competidores);
            recycle.setAdapter(adapter);
            sinCompetidores.setVisibility(View.INVISIBLE);
        } else {
            sinCompetidores();
        }
    }

    private void sinCompetidores() {
        sinCompetidores.setVisibility(View.VISIBLE);
        recycle.setVisibility(View.INVISIBLE);
    }

    private void initElement() {
        userSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);
        sinCompetidores = vista.findViewById(R.id.tv_sinCompetidores);
        competidores = new ArrayList<>();
        recycle = vista.findViewById(R.id.recyclerCompetidores);
        solicitudes = vista.findViewById(R.id.btnSolicitudes);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(vista.getContext());
        recycle.setLayoutManager(manager);
        recycle.setHasFixedSize(true);
        adapter = new CompetidoresRecyclerViewAdapter(vista.getContext());
        recycle.setAdapter(adapter);
        adminDataOff = new AdminDataOff(vista.getContext());
    }

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
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
