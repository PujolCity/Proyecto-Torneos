package com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.Inscription;
import com.VeizagaTorrico.proyectotorneos.offline.admin.AdminDataOff;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;

public class GeneralDetalleFragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private CompetitionMin competencia;
    private Button btnEditar,btnInscripcion;
    private InscriptionSrv inscriptionSrv;
    private LinearLayout linear;
    private Inscription inscription;
    private ImageButton downloadOff;
    private TextView nmb, cat, org, ciudad, genero, estado,monto, requisitos, fechaInicio,fechaCierre, fechaInicioCompetencia, frecuencia;
    private TextView tvDownloadMje, tvDownloadDescripcion;
    private CompetitionSrv competitionSrv;
    private ConfrontationSrv confrontationSrv;
    private UserSrv usersSrv;
    private List<ConfrontationOff> encuentrosServer;
    private AdminDataOff adminDataOff;
    private Map<String,String> userComp = new HashMap<>();
    private DataOffline dataServer;

    public GeneralDetalleFragment() {
    }

    public static GeneralDetalleFragment newInstance() {
        GeneralDetalleFragment fragment = new GeneralDetalleFragment();
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
        vista = inflater.inflate(R.layout.info_general_organizando_competencia, container, false);
        initElements();

        Log.d("competencia",this.competencia.toString());
        try{
            nmb.setText(competencia.getName());
            cat.setText(competencia.getCategory());
            org.setText(competencia.getTypesOrganization());
            ciudad.setText(competencia.getCiudad());
            genero.setText(competencia.getGenero());
            estado.setText(competencia.getEstado());
            frecuencia.setText(frecuencia.getText() + " "+competencia.getFrecuencia());
            if(NetworkReceiver.existConnection(vista.getContext())){
                fechaInicioCompetencia.setText(fechaInicioCompetencia.getText()+" "+competencia.getFechaIni());
            }
            else{
                fechaInicioCompetencia.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(NetworkReceiver.existConnection(vista.getContext())) {
            ocultarBotones();
            llenarDatoInscripcion();
            listenerDownload();
            listenButtonEdit();
            listenButtonInscripcion();
        } else {
            llenarDatosInscripcionOffline();
            elementosSinConexion();
            sinInternet();
        }

        return vista;
    }

    private void elementosSinConexion() {
        btnInscripcion.setVisibility(View.INVISIBLE);
        btnEditar.setVisibility(View.INVISIBLE);
        downloadOff.setVisibility(View.INVISIBLE);
        tvDownloadMje.setVisibility(View.INVISIBLE);
        tvDownloadDescripcion.setVisibility(View.INVISIBLE);
    }

    private void listenerDownload() {
        downloadOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =new AlertDialog.Builder(vista.getContext());
                builder.setTitle("Descargar datos de la competencia?");
                builder.setMessage("Se guardarán localmente los datos y encuentros de la competencia." +
                        "\n ¿Está seguro?");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadConfrontationServer();
                        dowloadDataCompettition();
                    }
                });

                builder.setNegativeButton("Rechazar", null);

                Dialog dialog = builder.create();
                dialog.show();
            }
        });

    }


    // realiza la descarga de los encuentros de la competencia
    private void downloadConfrontationServer() {

        Call<List<ConfrontationOff>> call = confrontationSrv.confrontationsOffline(competencia.getId());
        Log.d("GET_DATA_OFF", call.request().url().toString());
        call.enqueue(new Callback<List<ConfrontationOff>>() {
            @Override
            public void onResponse(Call<List<ConfrontationOff>> call, Response<List<ConfrontationOff>> response) {
                if (response.code() == 200) {
                    encuentrosServer = response.body();
                    Log.d("DATA_OFF", "cant de encuentros recuperados: "+encuentrosServer.size());

                    // guardamos los datos en la DB local
                    adminDataOff.loadConfrontations(encuentrosServer);
                    Toast.makeText(vista.getContext(), "La descarga y almacenamiento de datos de los encuentros de la competencia se ha realizado.", Toast.LENGTH_LONG).show();
                }
                if (response.code() == 400) {
                    Log.d("RESP_CONF_NOTIF", "PETICION MAL FORMADA: " + response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_CONF_NOTIF", "Msg de la repuesta: " + userMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<ConfrontationOff>> call, Throwable t) {
                Log.d("SERVER_ERROR", "Problemas de conecion con el servidor");
            }
        });
    }

    private void dowloadDataCompettition(){
        usersSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

        userComp.put("idUsuario", ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
        userComp.put("idCompetencia", String.valueOf(competencia.getId()));

//        Log.d("GET_DATA_OFF", "Body peticion: "+userComp);

        Call<DataOffline> call = usersSrv.getDataOffline(userComp);
        Log.d("GET_DATA_OFF", call.request().url().toString());
        call.enqueue(new Callback<DataOffline>() {
            @Override
            public void onResponse(Call<DataOffline> call, Response<DataOffline> response) {
                if (response.code() == 200) {
                    dataServer = response.body();
                    Log.d("GRAL_DET_OFF", "Nombre comp: "+dataServer.getCompetencia().getNombre());
                    // guardamos los datos en la DB local
                    adminDataOff.loadCompetition(dataServer.getCompetencia(), dataServer.getFases());

                    // controlamos que existan datos antes de guardarlos en la DB local
                    if(dataServer.getCompetidores() != null){
                        adminDataOff.loadCompetitors(dataServer.getCompetidores());
                        Log.d("GRAL_DET_OFF", "Cant comp: "+dataServer.getCompetidores().size());
                    }
                    else{
                        Log.d("GRAL_DET_OFF", "La comp aun no cuenta con competidores ");
                    }
                    if(dataServer.getCampos() != null){
                        adminDataOff.loadFields(dataServer.getCampos());
                        Log.d("GRAL_DET_OFF", "Cant campos: "+dataServer.getCampos().size());
                    }
                    else{
                        Log.d("GRAL_DET_OFF", "La comp aun no cuenta con campos ");
                    }
                    if(dataServer.getJueces() != null){
                        adminDataOff.loadJudges(dataServer.getJueces());
                        Log.d("GRAL_DET_OFF", "Cant Jueces: "+dataServer.getJueces().size());
                    }
                    else{
                        Log.d("GRAL_DET_OFF", "La comp aun no cuenta con jueces ");
                    }
                    if(dataServer.getInscripcion() != null){
                        adminDataOff.loadInscription(dataServer.getInscripcion());
                        Log.d("GRAL_DET_OFF", "Inicio inscripcion: "+dataServer.getInscripcion().getFinicio());
                    }
                    else{
                        Log.d("GRAL_DET_OFF", "La comp aun no cuenta con una inscripcion ");
                    }
                    Toast.makeText(vista.getContext(), "La descarga y almacenamiento de la competencia(competidores, campos, jueces, inscripcion) se ha realizado.", Toast.LENGTH_LONG).show();
                }
                if (response.code() == 400) {
                    Log.d("RESP_CONF_NOTIF", "PETICION MAL FORMADA: " + response.errorBody());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String userMessage = jsonObject.getString("messaging");
                        Log.d("RESP_CONF_NOTIF", "Msg de la repuesta: " + userMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<DataOffline> call, Throwable t) {
                Log.d(" GET_DATA_OFF", t.getMessage());
            }
        });
    }


    private void initElements() {

        inscriptionSrv = new RetrofitAdapter().connectionEnable().create(InscriptionSrv.class);
        adminDataOff = new AdminDataOff(vista.getContext());

        competitionSrv = new RetrofitAdapter().connectionEnable().create(CompetitionSrv.class);
        inscriptionSrv = new RetrofitAdapter().connectionEnable().create(InscriptionSrv.class);
        confrontationSrv = new RetrofitAdapter().connectionEnable().create(ConfrontationSrv.class);

        linear = vista.findViewById(R.id.layout_include_organizando);
        monto = vista.findViewById(R.id.monto);
        requisitos = vista.findViewById(R.id.requisitos);
        fechaInicio = vista.findViewById(R.id.fecha_inicio);
        fechaCierre = vista.findViewById(R.id.fecha_cierre);

        tvDownloadMje = vista.findViewById(R.id.tv_download_comp);
        tvDownloadDescripcion = vista.findViewById(R.id.tv_download_descripcion);

        nmb = vista.findViewById(R.id.txtNmbCompDet_organizando);
        cat = vista.findViewById(R.id.txtCatCompDet_organizando);
        org = vista.findViewById(R.id.txtOrgCompDet_organizando);
        ciudad = vista.findViewById(R.id.txtCityCompDet_organizando);
        genero = vista.findViewById(R.id.txtGenderCompDet_organizando);
        estado = vista.findViewById(R.id.tv_estado_infograll_organizando);
        fechaInicioCompetencia = vista.findViewById(R.id.tv_fecha_infograll_organizando);
        frecuencia = vista.findViewById(R.id.tv_frecuencia_infograll_organizando);
        btnEditar = vista.findViewById(R.id.btn_edit_competencia_organizando);
        btnInscripcion = vista.findViewById(R.id.generar_inscripcion_organizando);
        downloadOff = vista.findViewById(R.id.btn_download_off_organizando);

    }

    private void listenButtonInscripcion() {
        btnInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(competencia.getEstado().contains("SIN_INSCRIPCION")) {
                    passToCrearInscripcion();
                } else{
                    btnInscripcion.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void ocultarBotones(){
        try {
            btnEditar.setVisibility(View.INVISIBLE);
            List<String> roles = this.competencia.getRol();
            for (int i = 0 ; i < roles.size(); i++) {
                if (roles.get(i).contains("ORGANIZADOR")) {
                    btnEditar.setVisibility(View.VISIBLE);
                }
            }
            if(!competencia.getEstado().contains("SIN_INSCRIPCION")) {
                btnInscripcion.setVisibility(View.INVISIBLE);
                btnEditar.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void listenButtonEdit() {
        // ponemos a la escucha el boton de cargar Predio y campos
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(competencia.getEstado().contains("SIN_INSCRIPCION")){
                passToEditCompetencia();
            }
            else{
                Toast.makeText(vista.getContext(), "ACCION NO PERMITIDA: no se pueden editar los datos de una competencia con inscripcion abierta.", Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    private void passToEditCompetencia(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", this.competencia);
        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
        Navigation.findNavController(vista).navigate(R.id.editCompetenciaFragment, bundle);
    }

    private void passToCrearInscripcion(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("competencia", this.competencia);
        // ACA ES DONDE PUEDO PASAR A OTRO FRAGMENT Y DE PASO MANDAR UN OBJETO QUE CREE CON EL BUNDLE
        Navigation.findNavController(vista).navigate(R.id.crearInscripcionFragment, bundle);
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
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, algunas funciones no estaran disponibles por el momento", Toast.LENGTH_LONG);
        toast.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
    }

    private void llenarDatoInscripcion() {
        if(this.competencia.getEstado().contains("COMPETENCIA_INSCRIPCION_ABIERTA") || this.competencia.getEstado().contains("CON_INSCRIPCION")) {
            linear.setVisibility(View.VISIBLE);
            Call<Inscription> call = inscriptionSrv.getInscripcion(competencia.getId());
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
        else{
            linear.setVisibility(View.INVISIBLE);
        }
    }

     private void llenarDatosInscripcionOffline(){
         Log.d("INSC_OFF_SHOW","Entra");
         if(this.competencia.getEstado().contains("COMPETENCIA_INSCRIPCION_ABIERTA") || this.competencia.getEstado().contains("CON_INSCRIPCION")) {
             linear.setVisibility(View.VISIBLE);
//             adminInscripcionOff = new ManagerInscriptionOff(vista.getContext());
//             Inscription inscripcionLocal = adminInscripcionOff.inscriptionByCompetition(competencia.getId());
             Inscription inscripcionLocal = adminDataOff.inscriptionByCompetition(competencia.getId());
//             Log.d("INSC_OFF_SHOW", inscripcionLocal.toString());
             requisitos.setText(inscripcionLocal.getRequisitos());
             monto.setText(Integer.toString(inscripcionLocal.getMonto()));
             fechaInicio.setText(parsearFecha(inscripcionLocal.getFechaInicio()));
             fechaCierre.setText(parsearFecha(inscripcionLocal.getFechaCierre()));
         }
         else{
             linear.setVisibility(View.INVISIBLE);
         }
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
}
