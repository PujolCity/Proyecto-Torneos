package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

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
import com.VeizagaTorrico.proyectotorneos.models.Invitation;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.services.InvitationSrv;

import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudesOrganizadorRecyclerViewAdapter extends RecyclerView.Adapter<SolicitudesOrganizadorRecyclerViewAdapter.ViewHolder>
                                                        implements View.OnClickListener {

    private Context context;
    private List<Invitation> invitaciones;
    private View.OnClickListener listener;
    private InvitationSrv invitationSrv;

    public SolicitudesOrganizadorRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.card_invitaciones,null);
        vista.setOnClickListener(this);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try{
            invitationSrv = new RetrofitAdapter().connectionEnable().create(InvitationSrv.class);

            final Invitation invitacion = this.invitaciones.get(position);
            holder.txtCompetencia.setText(invitacion.getCompetencia());
            holder.txtOrganizador.setText(invitacion.getOrganizador());
            holder.txtCategoria.setText(invitacion.getCategoria());
            holder.btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<MsgRequest> call = invitationSrv.aceptarInvitacion(invitacion.getId());
                    call.enqueue(new Callback<MsgRequest>() {
                        @Override
                        public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                            if(response.code() == 200) {
                                Log.d("response code", Integer.toString(response.code()));
                                Toast toast = Toast.makeText(context, "Invitacion Aceptada", Toast.LENGTH_SHORT);
                                toast.show();
                                holder.btnAceptar.setVisibility(View.INVISIBLE);
                                holder.btnRechazar.setVisibility(View.INVISIBLE);
                            }
                            if (response.code() == 400) {
                                Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String userMessage = jsonObject.getString("messaging");
                                    Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                                    Toast.makeText(context, "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MsgRequest> call, Throwable t) {
                            Log.d("RESP_RECOVERY_ERROR", "error: "+t.getMessage());
                            Toast.makeText(context, "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            holder.btnRechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<MsgRequest> call = invitationSrv.rechazarInvitacion(invitacion.getId());
                    Log.d("Url invitacion",call.request().url().toString());
                    call.enqueue(new Callback<MsgRequest>() {
                        @Override
                        public void onResponse(Call<MsgRequest> call, Response<MsgRequest> response) {
                            if(response.code() == 200) {
                                Log.d("response code", Integer.toString(response.code()));
                                Toast toast = Toast.makeText(context, "Invitacion Rechazada", Toast.LENGTH_SHORT);
                                toast.show();
                                holder.btnAceptar.setVisibility(View.INVISIBLE);
                                holder.btnRechazar.setVisibility(View.INVISIBLE);
                            }
                            if (response.code() == 400) {
                                Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String userMessage = jsonObject.getString("messaging");
                                    Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                                    Toast.makeText(context, "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MsgRequest> call, Throwable t) {
                            Log.d("RESP_RECOVERY_ERROR", "error: "+t.getMessage());
                            Toast.makeText(context, "Existen problemas con el servidor ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setInvitaciones(List<Invitation> invitaciones) {
        this.invitaciones = invitaciones;
    }


    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if(this.invitaciones != null){
            return invitaciones.size();
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        if(this.listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCompetencia, txtCategoria, txtOrganizador;
        ImageButton btnAceptar, btnRechazar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try{
                btnAceptar = itemView.findViewById(R.id.btn_Aceptar_invitacion);
                btnRechazar = itemView.findViewById(R.id.btn_Rechazar_invitacion);

                txtCompetencia = itemView.findViewById(R.id.tv_competencia_invitacion);
                txtCategoria = itemView.findViewById(R.id.tv_categoria_invitacion);
                txtOrganizador = itemView.findViewById(R.id.tv_nombreOrg_invitacion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
