package com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;

import java.util.HashMap;
import java.util.Map;



public class InfoGeneralCompetenciaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CompetitionMin competition;
    private Map<String,Integer> compFollow;
    private TextView nmb, cat, org, ciudad, genero;
    private ImageButton follow, noFollow;
    private CompetitionSrv competitionSrv;
    private Button inscribirse;
    private View vista;

    public InfoGeneralCompetenciaFragment() {
        // Required empty public constructor
    }

    public static InfoGeneralCompetenciaFragment newInstance() {
        InfoGeneralCompetenciaFragment fragment = new InfoGeneralCompetenciaFragment();
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
        vista = inflater.inflate(R.layout.fragment_info_general_competencia, container, false);

        initElements();
        ocultarBotones();
        Log.d("Competencia recibida", competition.toString());
        try{
            nmb.setText(competition.getName());
            cat.setText(competition.getCategory());
            org.setText(competition.getTypesOrganization());
            ciudad.setText(competition.getCiudad());
            genero.setText(competition.getGenero());

        } catch (Exception e) {
            e.printStackTrace();
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compFollow.put("idUsuario", 5);
                compFollow.put("idCompetencia",competition.getId());

                Call<Success> call = competitionSrv.followCompetition(compFollow);
                Log.d("URL Seguir", call.request().url().toString());
                try{
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            Log.d("Response Seguir",Integer.toString(response.code()));
                            if(response.code() == 200){
                                follow.setVisibility(View.INVISIBLE);
                                noFollow.setVisibility(View.VISIBLE);
                                Toast toast = Toast.makeText(vista.getContext(), "Siguiendo", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            Log.d("On failure", t.getMessage());
                            Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        noFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compFollow.put("idUsuario", 5);
                compFollow.put("idCompetencia",competition.getId());

                Call<Success> call = competitionSrv.noFollowCompetition(compFollow);
                Log.d("URL ", call.request().url().toString());

                try{
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            Log.d("Response dejar Seguir",Integer.toString(response.code()));
                            if(response.code() == 200){
                                follow.setVisibility(View.VISIBLE);
                                noFollow.setVisibility(View.INVISIBLE);
                                Toast toast = Toast.makeText(vista.getContext(), "Ya no sigues esta competencia", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            Log.d("On failure", t.getMessage());
                            Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return vista;
    }

    private void initElements() {
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
        compFollow = new HashMap<>();

        nmb = vista.findViewById(R.id.txtNmbCompDet);
        cat = vista.findViewById(R.id.txtCatCompDet);
        org = vista.findViewById(R.id.txtOrgCompDet);
        ciudad = vista.findViewById(R.id.txtCityCompDet);
        genero = vista.findViewById(R.id.txtGenderCompDet);
        follow = vista.findViewById(R.id.btnFollow);
        noFollow = vista.findViewById(R.id.btnNoFollow);
        inscribirse = vista.findViewById(R.id.inscribirse);
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

    public void setCompetencia(CompetitionMin competition) {
        this.competition = competition;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void ocultarBotones(){
    }

}