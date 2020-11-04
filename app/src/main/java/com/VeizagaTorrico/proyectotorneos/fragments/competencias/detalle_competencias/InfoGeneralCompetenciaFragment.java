package com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Inscription;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.offline.admin.AdminDataOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerCompetitionOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerCompetitorOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerFieldOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerInscriptionOff;
import com.VeizagaTorrico.proyectotorneos.offline.admin.ManagerJudgeOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.DataOffline;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;
import com.VeizagaTorrico.proyectotorneos.services.ConfrontationSrv;
import com.VeizagaTorrico.proyectotorneos.services.InscriptionSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;


public class InfoGeneralCompetenciaFragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private CompetitionMin competition;
    private Map<String,Integer> compFollow;
    private TextView nmb, cat, org, ciudad, genero, estado,monto, requisitos, fechaInicio,fechaCierre, fechaInicioCompetencia, frecuencia;
    private ImageButton follow, noFollow;
    private CompetitionSrv competitionSrv;
    private ConfrontationSrv confrontationSrv;
    private Button inscribirse;
    private ImageButton downloadOff;
    private TextView tvDescargarOff, tvDescDecargarOff;
    private View vista;
    private Map<String,String> solicitud;
    private boolean comprobado, isOtherFragment;
    private InscriptionSrv inscriptionSrv;
    private Inscription inscription;
    private LinearLayout linear;

    public InfoGeneralCompetenciaFragment() {
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
        vista = inflater.inflate(R.layout.fragment_info_general_competencia, container, false);
        initElements();

        if(NetworkReceiver.existConnection(vista.getContext())) {
            ocultarBotones();
            listeners();
        } else {
            sinInternet();
            elementosSinConexion();
        }

        return vista;
    }

    private void elementosSinConexion() {
        follow.setVisibility(View.INVISIBLE);
        noFollow.setVisibility(View.INVISIBLE);
        inscribirse.setVisibility(View.INVISIBLE);
        downloadOff.setVisibility(View.INVISIBLE);
        linear.setVisibility(View.INVISIBLE);
        tvDescargarOff.setVisibility(View.INVISIBLE);
        tvDescDecargarOff.setVisibility(View.INVISIBLE);
    }

    private void listeners() {
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUsuario = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
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
                            try {
                                Log.d("On failure", t.getMessage());
                                Toast toast = Toast.makeText(vista.getContext(), "Por favor recargue la pestaña", Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                int idUsuario = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
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

    }

    private void initElements() {
        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
        inscriptionSrv = new RetrofitAdapter().connectionEnable().create(InscriptionSrv.class);
        confrontationSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);

        compFollow = new HashMap<>();
        solicitud = new HashMap<>();

        linear = vista.findViewById(R.id.layout_include);
        monto = vista.findViewById(R.id.monto);
        requisitos = vista.findViewById(R.id.requisitos);
        fechaInicio = vista.findViewById(R.id.fecha_inicio);
        fechaCierre = vista.findViewById(R.id.fecha_cierre);
        fechaInicioCompetencia = vista.findViewById(R.id.tv_fecha_infograll);
        frecuencia = vista.findViewById(R.id.tv_frecuencia_infograll);
        nmb = vista.findViewById(R.id.txtNmbCompDet);
        cat = vista.findViewById(R.id.txtCatCompDet);
        org = vista.findViewById(R.id.txtOrgCompDet);
        ciudad = vista.findViewById(R.id.txtCityCompDet);
        genero = vista.findViewById(R.id.txtGenderCompDet);
        estado = vista.findViewById(R.id.tv_estado_infograll);
        follow = vista.findViewById(R.id.btnFollow);
        noFollow = vista.findViewById(R.id.btnNoFollow);
        inscribirse = vista.findViewById(R.id.inscribirse);
        downloadOff = vista.findViewById(R.id.btn_download_off);
        tvDescargarOff = vista.findViewById(R.id.tv_down_comp);
        tvDescDecargarOff = vista.findViewById(R.id.tv_down_descripcion);
        try{
            nmb.setText(competition.getName());
            cat.setText(competition.getCategory());
            org.setText(competition.getTypesOrganization());
            ciudad.setText(competition.getCiudad());
            genero.setText(competition.getGenero());
            estado.setText(competition.getEstado());
            fechaInicioCompetencia.setText(fechaInicioCompetencia.getText()+" "+competition.getFechaIni());
            frecuencia.setText(frecuencia.getText() + " "+competition.getFrecuencia());
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void setOtherFragment(boolean otherFragment) {
        isOtherFragment = otherFragment;
    }

    public boolean isOtherFragment() {
        return isOtherFragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void sinInternet() {
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, algunas funciones no estaran disponibles por el momento", Toast.LENGTH_LONG);
        toast.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void ocultarBotones(){
        try {
            List<String> roles = this.competition.getRol();
            Log.d("ROLES", roles.toString());
            for (int i = 0 ; i < roles.size(); i++) {
                Log.d("ROL EN FOR: ", roles.get(i));
                // ocultamos el boton de inscripcion si la inscripcion no esta abierta
                if (roles.get(i).contains("SOLICITANTE")) {
                    follow.setVisibility(View.INVISIBLE);
                    noFollow.setVisibility(View.INVISIBLE);
                    inscribirse.setVisibility(View.INVISIBLE);
                }
                if (roles.contains("ORGANIZADOR")) {
                    follow.setVisibility(View.INVISIBLE);
                    noFollow.setVisibility(View.INVISIBLE);
                    inscribirse.setVisibility(View.VISIBLE);
                }
                if (roles.contains("COMPETIDOR")) {
                    follow.setVisibility(View.INVISIBLE);
                    noFollow.setVisibility(View.INVISIBLE);
                    inscribirse.setVisibility(View.INVISIBLE);
                }
                if (roles.get(i).contains("SEGUIDOR")) {
                    follow.setVisibility(View.INVISIBLE);
                    noFollow.setVisibility(View.VISIBLE);
                    inscribirse.setVisibility(View.VISIBLE);
                }
                if (roles.get(i).contains("ESPECTADOR")) {
                    follow.setVisibility(View.VISIBLE);
                    noFollow.setVisibility(View.INVISIBLE);
                    inscribirse.setVisibility(View.VISIBLE);
                }
            }
            if(this.competition.getEstado().contains("COMPETENCIA_INSCRIPCION_ABIERTA") || this.competition.getEstado().contains("CON_INSCRIPCION")) {
                //inscribirse.setVisibility(View.VISIBLE);
                linear.setVisibility(View.VISIBLE);
                if(NetworkReceiver.existConnection(vista.getContext())){
                    llenarDatoInscripcion();
                }
            }
            else{
                linear.setVisibility(View.INVISIBLE);
                inscribirse.setVisibility(View.INVISIBLE);
            }
            // permite q cuando se llame desde menu diferentes se vea o no el boton
            if(isOtherFragment()){
                downloadOff.setVisibility(View.VISIBLE);
                tvDescargarOff.setVisibility(View.VISIBLE);
                tvDescDecargarOff.setVisibility(View.VISIBLE);
            }else {
                downloadOff.setVisibility(View.INVISIBLE);
                tvDescargarOff.setVisibility(View.INVISIBLE);
                tvDescDecargarOff.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void llenarDatoInscripcion() {
        Call<Inscription> call = inscriptionSrv.getInscripcion(competition.getId());
        Log.d("URL INSCRIPTION", call.request().url().toString());
        call.enqueue(new Callback<Inscription>() {
            @Override
            public void onResponse(Call<Inscription> call, Response<Inscription> response) {
                if (response.code() == 200) {
                    try {
                        inscription = response.body();
                        requisitos.setText(inscription.getRequisitos());
                        monto.setText(Integer.toString(inscription.getMonto()));
                        fechaInicio.setText(parsearFecha(inscription.getFechaInicio()));
                        fechaCierre.setText(parsearFecha(inscription.getFechaCierre()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == 400) {
                    Log.d("RESP_GROUND_ERROR", "PETICION MAL FORMADA: " + response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("messaging");
                        Log.d("RESP_GROUN_ERROR", "Msg de la repuesta: " + message);
                        Toast.makeText(vista.getContext(), "No se pudo asignar el predio:  << " + message + " >>", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            @Override
            public void onFailure(Call<Inscription> call, Throwable t) {
                try {
                    Log.d("OnFailure SETPREDIO",t.getMessage());
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String parsearFecha(String fechaServer) {
        List<String> token = new ArrayList<>();
        String fecha = fechaServer.substring(0,10);
        StringTokenizer st = new StringTokenizer(fecha, "-");
        while (st.hasMoreTokens()) {
            token.add(st.nextToken());
        }
        return token.get(2)+"/"+ token.get(1)+"/"+token.get(0);
    }

    private void createLoginDialogo() {
        LayoutInflater linf = LayoutInflater.from(vista.getContext());
        final View inflator = linf.inflate(R.layout.alias_form,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(vista.getContext());
        alert.setTitle("Ingrese nombre de Equipo o Participante!");
        alert.setView(inflator);

        final EditText alias = inflator.findViewById(R.id.etAlias);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String alia = alias.getText().toString();
                if(alia.isEmpty()){
                    Toast toast = Toast.makeText(inflator.getContext(), "Por favor agreagar un Alias", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    if(comprobarAlias(alia)){
                        Toast toast = Toast.makeText(inflator.getContext(), "Solicitud registrada!!", Toast.LENGTH_SHORT);
                        toast.show();
                        dialog.dismiss();
                    }
                }
                dialog.cancel();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });



        alert.show();
    }

    private boolean comprobarAlias(String alia) {
        solicitud.clear();
        solicitud.put("idUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
        solicitud.put("idCompetencia", Integer.toString(competition.getId()));
        solicitud.put("alias",alia);

        Call<MsgRequest> call = competitionSrv.solicitudCompetition(solicitud);
        Log.d("body", solicitud.toString());

        call.enqueue(new Callback<MsgRequest>() {
            @Override
            public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
               try {
                   Log.d("response Dialog", Integer.toString(response.code()));
                   if(response.code() == HttpURLConnection.HTTP_OK){
                       MsgRequest msj = response.body();
                       Log.d("msj", msj.toString());
                       Toast toast = Toast.makeText(vista.getContext(),msj.toString() , Toast.LENGTH_SHORT);
                       toast.show();
                       inscribirse.setVisibility(View.INVISIBLE);
                       comprobado =  true;
                   }
                   if(response.code() == HttpURLConnection.HTTP_BAD_REQUEST){
                       try {
                           JSONObject jsonObject = new JSONObject(response.errorBody().string());
                           String userMessage = jsonObject.getString("messaging");
                           Log.d("RESP_SOLIC_ERROR", "Msg de la repuesta: "+userMessage);
                           Toast toast = Toast.makeText(vista.getContext(), "Error: "+userMessage, Toast.LENGTH_LONG);
                           toast.show();
                           comprobado = false;
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
            }
            @Override
            public void onFailure(Call<MsgRequest> call, Throwable t) {
                try {
                    Log.d("OnFailure SETPREDIO",t.getMessage());
                    Toast toast = Toast.makeText(vista.getContext(), "Recargue la pestaña", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return comprobado;
    }

}
