package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Edition;
import com.VeizagaTorrico.proyectotorneos.models.PositionCompetitor;

import java.util.List;

public class PosicionesRecyclerViewAdapter extends RecyclerView.Adapter<PosicionesRecyclerViewAdapter.Holder>
        implements View.OnClickListener {

    private Context context;
    private List<PositionCompetitor> posiciones;
    private View.OnClickListener listener;

    public PosicionesRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setPosiciones(List<PositionCompetitor> posiciones) {
        this.posiciones = posiciones;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(this.listener != null){
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    @NonNull
    @Override
    public PosicionesRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.posicion_layout,parent,false);
        vista.setOnClickListener(this);
        return new PosicionesRecyclerViewAdapter.Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PosicionesRecyclerViewAdapter.Holder holder, int position) {
        try {
            PositionCompetitor posicion = this.posiciones.get(position);

            holder.tvCompetidor.setText(posicion.getCompetidor());
            holder.tvPj.setText(posicion.getJugados());
            holder.tvPg.setText(posicion.getGanados());
            holder.tvPp.setText(posicion.getPerdidos());
            holder.tvPts.setText(posicion.getPuntos());
            holder.tvDif.setText(posicion.getDiferencia());

            if(posicion.getEmpatados() == null){
                holder.tvPe.setVisibility(View.GONE);
            }
            else{
                holder.tvPe.setText(posicion.getEmpatados());
            }
            Log.d("DATA_POSITION", "( Aca iria la info de la posicion )");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(this.posiciones!= null){
            return this.posiciones.size();
        }
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvCompetidor, tvPj, tvPg, tvPe, tvPp, tvPts, tvDif;

        public Holder(@NonNull View itemView) {
            super(itemView);
            try{
                tvCompetidor = itemView.findViewById(R.id.tv_competidor_posiciones);
                tvPj = itemView.findViewById(R.id.tv_pj_posiciones);
                tvPg = itemView.findViewById(R.id.tv_pg_posiciones);
                tvPe = itemView.findViewById(R.id.tv_pe_posiciones);
                tvPp = itemView.findViewById(R.id.tv_pp_posiciones);
                tvPts = itemView.findViewById(R.id.tv_pts_posiciones);
                tvDif = itemView.findViewById(R.id.tv_dif_posiciones);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
