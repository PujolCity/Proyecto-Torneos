package com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil;

import android.content.Context;
import android.net.Network;
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
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.SolicitudesOrganizadorRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Invitation;
import com.VeizagaTorrico.proyectotorneos.services.InvitationSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;


public class InvitacionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private SolicitudesOrganizadorRecyclerViewAdapter invitacionAdapter;
    private InvitationSrv invitationSrv;
    private List<Invitation> invitaciones;
    private RecyclerView recyclerInv;
    private RecyclerView.LayoutManager manager;
    private TextView sinInitacionesTv;

    public InvitacionesFragment() {
    }

    public static InvitacionesFragment newInstance() {
        InvitacionesFragment fragment = new InvitacionesFragment();
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
        vista = inflater.inflate(R.layout.fragment_invitaciones, container, false);
        initAdapter();
        if(NetworkReceiver.existConnection(vista.getContext())){
            inflarRecycler();
        }

        return vista;
    }

    private void inflarRecycler() {
        int idUsuarioregistrado = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
        Call<List<Invitation>> call = invitationSrv.getInvitaciones(idUsuarioregistrado);
        call.enqueue(new Callback<List<Invitation>>() {
            @Override
            public void onResponse(Call<List<Invitation>> call, Response<List<Invitation>> response) {
                Log.d("RESP CODE INVITATION", Integer.toString(response.code()));
                if (response.code() == 200) {
                    try {
                        invitaciones = response.body();
                        if(invitaciones.size() != 0){
                            try {
                                invitacionAdapter.setInvitaciones(invitaciones);
                                recyclerInv.setAdapter(invitacionAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast toast = Toast.makeText(vista.getContext(), "No hay Invitaciones para mostrar", Toast.LENGTH_LONG);
                            toast.show();
                            sinInvitaciones();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Invitation>> call, Throwable t) {
                try{
                    Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("onFailure", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sinInvitaciones() {
        recyclerInv.setVisibility(View.INVISIBLE);
        sinInitacionesTv.setVisibility(View.VISIBLE);
    }

    private void initAdapter() {
        sinInitacionesTv = vista.findViewById(R.id.tv_sinInvitaciones);
        invitationSrv = new RetrofitAdapter().connectionEnable().create(InvitationSrv.class);
        // COSAS PARA LLENAR El RECYCLERVIEW
        invitaciones = new ArrayList<>();
        recyclerInv = vista.findViewById(R.id.recyclerSolicitudes);
        manager = new LinearLayoutManager(vista.getContext());
        recyclerInv.setLayoutManager(manager);
        recyclerInv.setHasFixedSize(true);
        invitacionAdapter = new SolicitudesOrganizadorRecyclerViewAdapter(vista.getContext());
        recyclerInv.setAdapter(invitacionAdapter);
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
