package com.VeizagaTorrico.proyectotorneos.recycle_view_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Sport;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRecyclerCompView extends RecyclerView.Adapter<AdapterRecyclerCompView.Holder>
                                        implements View.OnClickListener {

    private Context context;
    private List<Sport> deportes;
    private View.OnClickListener listener;

    public AdapterRecyclerCompView(Context context, List<Sport> deportes) {
        this.context = context;
        this.deportes = deportes;
    }

    public void setDeportes(List<Sport> deportes) {
        this.deportes = deportes;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.fragment_list__view__comp_,null);
        vista.setOnClickListener(this);
        return new Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Sport sport = deportes.get(position);
        holder.txtCategoria.setText(sport.getCategories().get(0).getNombreCat());
        holder.txtCompetencia.setText(sport.getCategories().get(0).getDescripcion());
        holder.txtDeporte.setText(deportes.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        if(this.deportes != null){
            return this.deportes.size();
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
