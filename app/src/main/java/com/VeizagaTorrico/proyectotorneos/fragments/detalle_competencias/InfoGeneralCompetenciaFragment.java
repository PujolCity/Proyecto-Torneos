package com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;


public class InfoGeneralCompetenciaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CompetitionMin competition;
    private Map<String,Integer> compFollow;
    private TextView nmb, cat, org, ciudad, genero, estado;
    private ImageButton follow, noFollow;
    private CompetitionSrv competitionSrv;
    private Button inscribirse, btnEditCompetencia;
    private View vista;
    private AlertDialog dialog;
    private Map<String,String> solicitud;
    private boolean comprobado;


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
            estado.setText(competition.getEstado());

        } catch (Exception e) {
            e.printStackTrace();
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUsuario = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(getContext(), FILE_SHARED_DATA_USER, KEY_ID));
                compFollow.put("idUsuario", idUsuario);
                compFollow.put("idCompetencia",competition.getId());

                Call<Success> call = competitionSrv.followCompetition(compFollow);
                Log.d("URL_Seguir", call.request().url().toString());
                Log.d("URL_Seguir data", compFollow.toString());
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
//                compFollow.put("idUsuario", 2);
                int idUsuario = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(getContext(), FILE_SHARED_DATA_USER, KEY_ID));
                compFollow.put("idUsuario", idUsuario);
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

        inscribirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLoginDialogo();

            }
        });
        return vista;
    }

    private void initElements() {
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
        compFollow = new HashMap<>();
        solicitud = new HashMap<>();

        nmb = vista.findViewById(R.id.txtNmbCompDet);
        cat = vista.findViewById(R.id.txtCatCompDet);
        org = vista.findViewById(R.id.txtOrgCompDet);
        ciudad = vista.findViewById(R.id.txtCityCompDet);
        genero = vista.findViewById(R.id.txtGenderCompDet);
        estado = vista.findViewById(R.id.tv_estado_infograll);
        follow = vista.findViewById(R.id.btnFollow);
        noFollow = vista.findViewById(R.id.btnNoFollow);
        inscribirse = vista.findViewById(R.id.inscribirse);
        btnEditCompetencia = vista.findViewById(R.id.btn_edit_competencia);

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
        List<String> roles = this.competition.getRol();

        for (int i = 0 ; i < roles.size(); i++) {
            if (roles.get(i).contains("SOLICITANTE")) {
                follow.setVisibility(View.INVISIBLE);
                noFollow.setVisibility(View.INVISIBLE);
                inscribirse.setVisibility(View.INVISIBLE);
                btnEditCompetencia.setVisibility(View.INVISIBLE);
            }
            if (roles.get(i).contains("ORGANIZADOR")) {
                follow.setVisibility(View.INVISIBLE);
                noFollow.setVisibility(View.INVISIBLE);
                inscribirse.setVisibility(View.VISIBLE);
                btnEditCompetencia.setVisibility(View.INVISIBLE);
            }
            if (roles.get(i).contains("COMPETIDOR")) {
                follow.setVisibility(View.INVISIBLE);
            //    noFollow.setVisibility(View.INVISIBLE);
                inscribirse.setVisibility(View.INVISIBLE);
                btnEditCompetencia.setVisibility(View.INVISIBLE);
            }
            if (roles.get(i).contains("SEGUIDOR")) {
                follow.setVisibility(View.INVISIBLE);
                noFollow.setVisibility(View.VISIBLE);
                //     inscribirse.setVisibility(View.VISIBLE);
                btnEditCompetencia.setVisibility(View.INVISIBLE);
            }
            if (roles.get(i).contains("ESPECTADOR")) {
                follow.setVisibility(View.VISIBLE);
                noFollow.setVisibility(View.INVISIBLE);
                inscribirse.setVisibility(View.VISIBLE);
                btnEditCompetencia.setVisibility(View.INVISIBLE);
            }
        }


    }


    private void createLoginDialogo() {
        final Dialog builder = new Dialog(vista.getContext());
        builder.setContentView(R.layout.alias_form);

        Button confirmar = builder.findViewById(R.id.confirmar);
        Button cancelar =  builder.findViewById(R.id.cancelar);
        final EditText alias = builder.findViewById(R.id.etAlias);

        confirmar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alia = alias.getText().toString();
                        if(alia.isEmpty()){
                            Toast toast = Toast.makeText(builder.getContext(), "Por favor agreagar un Alias", Toast.LENGTH_SHORT);
                            toast.show();
                        }else {
                            if(comprobarAlias(alia)){
                                Toast toast = Toast.makeText(builder.getContext(), "Solicitud registrada!!", Toast.LENGTH_SHORT);
                                toast.show();
                                builder.dismiss();
                            }
                        }
                        builder.dismiss();

                    }
                }
        );

        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                    builder.dismiss();
                    }
                }

        );

        builder.show();
    }

    private boolean comprobarAlias(String alia) {
        solicitud.clear();

        solicitud.put("idUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(getContext(), FILE_SHARED_DATA_USER, KEY_ID));
        solicitud.put("idCompetencia", Integer.toString(competition.getId()));
        solicitud.put("alias",alia);

        Call<MsgRequest> call = competitionSrv.solicitudCompetition(solicitud);
        Log.d("body", solicitud.toString());

        Log.d("response Dialog", call.request().url().toString());

        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
               try {
                   Log.d("response Dialog", Integer.toString(response.code()));

                   if(response.code() == 200){
                       MsgRequest msj = response.body();
                       Log.d("msj", msj.toString());
                       Toast toast = Toast.makeText(vista.getContext(),msj.toString() , Toast.LENGTH_SHORT);
                       toast.show();
                       inscribirse.setVisibility(View.INVISIBLE);
                       comprobado =  true;

                   } else{

                       Toast toast = Toast.makeText(vista.getContext(), "Alias en Uso", Toast.LENGTH_SHORT);
                       toast.show();
                       comprobado = false;
                   }

               } catch (Exception e) {
                   e.printStackTrace();
               }
            }

            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return comprobado;
    }

}
