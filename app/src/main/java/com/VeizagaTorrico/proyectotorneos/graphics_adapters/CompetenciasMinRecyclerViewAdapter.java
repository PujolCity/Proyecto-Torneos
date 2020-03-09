package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompetenciasMinRecyclerViewAdapter extends RecyclerView.Adapter<CompetenciasMinRecyclerViewAdapter.Holder>
                                        implements View.OnClickListener {

    private Context context;
    private List<CompetitionMin> competencias;
    private View.OnClickListener listener;

    public CompetenciasMinRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setCompetencias(List<CompetitionMin> competencias) {
        try {
            this.competencias = competencias;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.card_competencias,parent,false);
        vista.setOnClickListener(this);
        return new Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            CompetitionMin competitionMin = competencias.get(position);
            holder.txtOrg.setText(competitionMin.getTypesOrganization());
            holder.txtCompetencia.setText(competitionMin.getName());
            holder.txtCat.setText(competitionMin.getCategory());

            if(!competitionMin.getRol().get(0).contains("ESPECTADOR")) {
                if (competitionMin.getRol().size() == 1) {
                    holder.txtRol1.setText(competitionMin.getRol().get(0));
                } else {
                    holder.txtRol1.setText(competitionMin.getRol().get(0));
                    holder.txtRol2.setText(competitionMin.getRol().get(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        TextView txtCompetencia, txtCat, txtOrg, txtRol1,txtRol2;


        public Holder(@NonNull View itemView) {
            super(itemView);
            try {
                txtCompetencia = itemView.findViewById(R.id.txtNmbCompList);
                txtCat = itemView.findViewById(R.id.txtCatCompList);
                txtOrg = itemView.findViewById(R.id.txtOrgCompList);
                txtRol1 = itemView.findViewById(R.id.competenciaRol1);
                txtRol2 = itemView.findViewById(R.id.competenciaRol2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
