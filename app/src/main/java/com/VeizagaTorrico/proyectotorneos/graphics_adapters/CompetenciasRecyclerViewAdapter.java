package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Competition;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompetenciasRecyclerViewAdapter extends RecyclerView.Adapter<CompetenciasRecyclerViewAdapter.Holder>
                                        implements View.OnClickListener {

    private Context context;
    private List<Competition> competencias;
    private View.OnClickListener listener;

    public CompetenciasRecyclerViewAdapter(Context context, List<Competition> competencias) {
        this.context = context;
        this.competencias = competencias;
    }

    public void setCompetencias(List<Competition> competencias) {
        try {
            this.competencias = competencias;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            Competition competition= competencias.get(position);
            holder.txtCategoria.setText(competition.getCategory().getNombreCat());
            holder.txtCompetencia.setText(competition.getName());
            //holder.txtDeporte.setText(competition.getCategory().getSport());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.fragment_competencias,parent,false);
        vista.setOnClickListener(this);
        return new Holder(vista);
    }

    @Override
    public int getItemCount() {
        if(this.competencias != null){
            return this.competencias.size();
        }
        return 0;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {
        if(this.listener != null){
            listener.onClick(view);
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtCompetencia, txtDeporte, txtCategoria;

        public Holder(@NonNull View itemView) {
            super(itemView);
            try {
                txtCompetencia = itemView.findViewById(R.id.txtNmbCompList);
                //txtDeporte = itemView.findViewById(R.id.txtDepCompList);
                txtCategoria = itemView.findViewById(R.id.txtCatCompList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
    }
}
