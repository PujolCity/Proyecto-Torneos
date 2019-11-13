package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

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
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.EncuentrosRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;

import java.util.ArrayList;
import java.util.List;

public class EncuentrosDetalleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private ConfrontationSrv confrontationSrv;
    private EncuentrosRecyclerViewAdapter adapter;
    private List<Confrontation> encuentros;
    private RecyclerView recycleCon;
    private RecyclerView.LayoutManager manager;
    private CompetitionMin competencia;


    public EncuentrosDetalleFragment() {
        // Required empty public constructor
    }

    public static EncuentrosDetalleFragment newInstance() {
        EncuentrosDetalleFragment fragment = new EncuentrosDetalleFragment();
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
        vista = inflater.inflate(R.layout.fragment_encuentros_detalle, container, false);
        initElements();
        inflarRecycler();

        return vista;
    }

    private void inflarRecycler() {
        Call<List<Confrontation>> call = confrontationSrv.getConfrontation(competencia.getId());
        Log.d("call competencia",call.request().url().toString());
        call.enqueue(new Callback<List<Confrontation>>() {
            @Override
            public void onResponse(Call<List<Confrontation>> call, Response<List<Confrontation>> response) {
                if(response.code() == 200){
                    try {
                        Log.d("ENCUENTROS response", Integer.toString(response.code()));
                        encuentros = response.body();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(encuentros != null){
                    try {
                        adapter.setEncuentros(encuentros);
                        recycleCon.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Confrontation encuentro = encuentros.get(recycleCon.getChildAdapterPosition(view));
                                Bundle bundle = new Bundle();
                                encuentro.setIdCompetencia(competencia.getId());
                                bundle.putSerializable("encuentro", encuentro);
                                Navigation.findNavController(vista).navigate(R. id.detalleEncuentroFragment, bundle);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Confrontation>> call, Throwable t) {
                try {
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
       }

    private void initElements() {
        confrontationSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);
        encuentros = new ArrayList<>();
        recycleCon = vista.findViewById(R.id.recyclerEncuentrosDetalle);
        manager = new LinearLayoutManager(vista.getContext());
        recycleCon.setLayoutManager(manager);
        recycleCon.setHasFixedSize(true);
        adapter = new EncuentrosRecyclerViewAdapter(vista.getContext());
        recycleCon.setAdapter(adapter);
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
