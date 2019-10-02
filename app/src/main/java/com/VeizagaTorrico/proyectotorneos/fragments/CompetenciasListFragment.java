package com.VeizagaTorrico.proyectotorneos.fragments;

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
import com.VeizagaTorrico.proyectotorneos.adapters.CompetitionAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.AdapterRecyclerCompView;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompetenciasListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompetenciasListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompetenciasListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CompetitionSrv competitionSrv;
    private AdapterRecyclerCompView adapter;
    private List<Competition> competitions;
    private View vista;
    private RecyclerView recycleComp;
    private RecyclerView.LayoutManager manager;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_competencias_list, container, false);

        competitionSrv = new CompetitionAdapter().connectionEnable();

        initAdapter();

        //En call viene el tipo de dato que espero del servidor
        Call<List<Competition>> call = competitionSrv.getCompetitions();
        call.enqueue(new Callback<List<Competition>>() {
            @Override
            public void onResponse(Call<List<Competition>> call, Response<List<Competition>> response) {
                Log.d("RESP CODE COMPETITION", Integer.toString(response.code()));
                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    //asigno a deportes lo que traje del servidor
                    competitions = response.body();
                    Log.d("RESP CODE COMPETITION", Integer.toString(response.code()));
                    adapter.setCompetencias(competitions);
                }
                //CREO EL ADAPTER Y LO SETEO PARA QUE INFLE EL LAYOUT
                recycleComp.setAdapter(adapter);

                //LISTENER PARA EL ELEMENTO SELECCIONADO
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Competition competition = competitions.get(recycleComp.getChildAdapterPosition(view));

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("deporte", competition);

                        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                        Navigation.findNavController(vista).navigate(R.id.detalleCompListFragment, bundle);

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Competition>> call, Throwable t) {
                Toast toast = Toast.makeText(getContext(), "No anda una mierda", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onResponse", t.getMessage());
            }
        });
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
public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void initAdapter(){
        // COSAS PARA LLENAR El RECYCLERVIEW
        competitions = new ArrayList<>();
        recycleComp = vista.findViewById(R.id.recycleCompView);
        manager = new LinearLayoutManager(getContext());
        recycleComp.setLayoutManager(manager);
        recycleComp.setHasFixedSize(true);
        adapter = new AdapterRecyclerCompView(vista.getContext(),competitions);
        recycleComp.setAdapter(adapter);
    }
}
