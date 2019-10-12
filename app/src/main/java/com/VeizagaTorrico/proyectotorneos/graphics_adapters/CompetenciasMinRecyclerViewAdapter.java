package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
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

    public CompetenciasMinRecyclerViewAdapter(Context context, List<CompetitionMin> competencias) {
        this.context = context;
        this.competencias = competencias;
    }

    public void setCompetencias(List<CompetitionMin> competencias) {
        this.competencias = competencias;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.fragment_competencias,null);
        vista.setOnClickListener(this);
        return new Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CompetitionMin competitionMin = competencias.get(position);
        holder.txtCategoria.setText(competitionMin.getTypesOrganization());
        holder.txtCompetencia.setText(competitionMin.getName());
        holder.txtDeporte.setText(competitionMin.getName());
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
            txtCompetencia = itemView.findViewById(R.id.txtNmbCompList);
            txtDeporte = itemView.findViewById(R.id.txtDepCompList);
            txtCategoria = itemView.findViewById(R.id.txtCatCompList);


        }
    }
}
