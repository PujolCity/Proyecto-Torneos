package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.VeizagaTorrico.proyectotorneos.Constants.ACTUALIZANDO_DATOS;

public class SolicitudesRecyclerViewAdapter extends RecyclerView.Adapter<SolicitudesRecyclerViewAdapter.ViewHolder>{

    private List<User> competidores;
    private int idComptencia;
    private Map<String,String> solicitud;
    private Context context;
    private UserSrv userSrv;
    private AlertDialog alertDialog;

    public SolicitudesRecyclerViewAdapter(Context context) {
        this.context = context;
        userSrv = new RetrofitAdapter().connectionEnable().create(UserSrv.class);

    }

    public void setCompetidores(List<User> competidores)
    {
        this.competidores = competidores;
    }

    public void setIdComptencia(int idComptencia) {
        this.idComptencia = idComptencia;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_solicitudes_competidores,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User usuario = this.competidores.get(position);

        try {
            holder.txtNombUsuario.setText(usuario.getNombreUsuario());
            holder.txtNombre.setText(usuario.getNombre());
            holder.txtApellido.setText(usuario.getApellido());
            //ACA TENGO QUE IMPLEMENTAR LA LOGICA DEL CHECKBOX
            holder.btnRechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    solicitud = new HashMap<>();
                    solicitud.put("idUsuario",Integer.toString(usuario.getId()));
                    solicitud.put("idCompetencia",Integer.toString(idComptencia));
                    Log.d("BODY RECHAZO", solicitud.toString());

                    Call<Success> call = userSrv.refusePetitionerUser(solicitud);
                    Log.d("URL", call.request().url().toString());
                    showMsgWaitServer(context);
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            if(response.code() == 200){
                                endMsgWaitServer(context);
                                Log.d("response code", Integer.toString(response.code()));
                                Toast toast = Toast.makeText(context, "Solicitud rechazada", Toast.LENGTH_SHORT);
                                toast.show();
                                holder.btnAceptar.setVisibility(View.INVISIBLE);
                                holder.btnRechazar.setVisibility(View.INVISIBLE);
                            }
                        }
                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            endMsgWaitServer(context);
                            Toast toast = Toast.makeText(context, "Ver", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            });
            holder.btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    solicitud = new HashMap<>();
                    solicitud.put("idUsuario",Integer.toString(usuario.getId()));
                    solicitud.put("idCompetencia",Integer.toString(idComptencia));

                    Call<Success> call = userSrv.acceptPetitionUser(solicitud);
                    Log.d("URL", call.request().url().toString());
                    showMsgWaitServer(context);
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            endMsgWaitServer(context);
                            Log.d("response code", Integer.toString(response.code()));
                            Toast toast = Toast.makeText(context, "Solicitud aceptada", Toast.LENGTH_SHORT);
                            toast.show();
                            holder.btnAceptar.setVisibility(View.INVISIBLE);
                            holder.btnRechazar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            endMsgWaitServer(context);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (this.competidores != null) {
           return this.competidores.size();
        }
        return 0;
    }


    private void showMsgWaitServer(Context context) {
        alertDialog = ManagerMsgView.getMsgLoading(context, ACTUALIZANDO_DATOS);
        alertDialog.show();
    }

    private void endMsgWaitServer(Context context) {
        alertDialog.dismiss();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombUsuario, txtNombre,txtApellido;
        ImageButton btnAceptar,btnRechazar;

        public ViewHolder(@NonNull View view) {
            super(view);
            try {
                txtNombUsuario = view.findViewById(R.id.nombUsuario);
                txtNombre = view.findViewById(R.id.nomb);
                txtApellido = view.findViewById(R.id.apelido);
                btnAceptar = view.findViewById(R.id.btnAceptar);
                btnRechazar = view.findViewById(R.id.btnRechazar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}