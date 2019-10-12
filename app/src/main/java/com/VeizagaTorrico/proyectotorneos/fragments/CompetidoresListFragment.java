package com.VeizagaTorrico.proyectotorneos.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.CompetidoresRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;

import java.util.ArrayList;
import java.util.List;


public class CompetidoresListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private UserSrv userSrv;
    private CompetidoresRecyclerViewAdapter adapter;
    private List<User> competidores;
    private View vista;
    private RecyclerView recycle;


    public CompetidoresListFragment() {
        // Required empty public constructor
    }

    public static CompetidoresListFragment newInstance(String param1, String param2) {
        CompetidoresListFragment fragment = new CompetidoresListFragment();
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
        vista = inflater.inflate(R.layout.fragment_competidores_list, container, false);

        userSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        initAdapter();


        //En call viene el tipo de dato que espero del servidor
        Call<List<User>> call = userSrv.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("RESPONSE CODE USERS", Integer.toString(response.code()));

                if(response.code() == 200){
                    competidores = response.body();
                    adapter.setCompetidores(competidores);
                }
            recycle.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast toast = Toast.makeText(vista.getContext(), "No anda una mierda", Toast.LENGTH_SHORT);
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
        void onFragmentInteraction(Uri uri);
    }

    public void initAdapter(){
        // COSAS PARA LLENAR El RECYCLERVIEW
        competidores = new ArrayList<>();
        recycle = vista.findViewById(R.id.CompetidoresList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(vista.getContext());
        recycle.setLayoutManager(manager);
        recycle.setHasFixedSize(true);
        adapter = new CompetidoresRecyclerViewAdapter(vista.getContext(),competidores);
        recycle.setAdapter(adapter);

    }
}
