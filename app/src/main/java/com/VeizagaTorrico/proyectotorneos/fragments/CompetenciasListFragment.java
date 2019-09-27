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
import com.VeizagaTorrico.proyectotorneos.adapters.SportsAdapter;
import com.VeizagaTorrico.proyectotorneos.recycle_view_adapters.AdapterRecyclerCompView;
import com.VeizagaTorrico.proyectotorneos.models.Sport;
import com.VeizagaTorrico.proyectotorneos.services.SportsSrv;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View vista;
    private SportsSrv sportsSrv;
    private RecyclerView recycleComp;
    private AdapterRecyclerCompView adapter;
    private List<Sport> deportes;
    private RecyclerView.LayoutManager manager;

    public CompetenciasListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompetenciasListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompetenciasListFragment newInstance(String param1, String param2) {
        CompetenciasListFragment fragment = new CompetenciasListFragment();
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
        vista = inflater.inflate(R.layout.fragment_competencias_list, container, false);

        sportsSrv = new SportsAdapter().connectionEnable();

        initAdapter();

        //En call viene el tipo de dato que espero del servidor
        Call<List<Sport>> call = sportsSrv.getSports();
        call.enqueue(new Callback<List<Sport>>() {
            @Override
            public void onResponse(Call<List<Sport>> call, Response<List<Sport>> response) {
                deportes = new ArrayList<Sport>();

                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    //asigno a deportes lo que traje del servidor
                    deportes = response.body();
                    Log.d("RESPONSE CODE", Integer.toString(response.code()));
                    adapter.setDeportes(deportes);
                }
                //CREO EL ADAPTER Y LO SETEO PARA QUE INFLE EL LAYOUT
                recycleComp.setAdapter(adapter);

                //LISTENER PARA EL ELEMENTO SELECCIONADO
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Sport deporte = deportes.get(recycleComp.getChildAdapterPosition(view));

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("deporte", deporte);

                        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
                        Navigation.findNavController(vista).navigate(R.id.detalleCompListFragment, bundle);

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Sport>> call, Throwable t) {
                Toast toast = Toast.makeText(getContext(), "No anda una mierda", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onResponse", "no anda");
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

    public void initAdapter(){
        // COSAS PARA LLENAR El RECYCLERVIEW
        recycleComp = vista.findViewById(R.id.recycleCompView);
        manager = new LinearLayoutManager(getContext());
        recycleComp.setLayoutManager(manager);
        recycleComp.setHasFixedSize(true);
        adapter = new AdapterRecyclerCompView(vista.getContext(),deportes);
        recycleComp.setAdapter(adapter);
    }
}
