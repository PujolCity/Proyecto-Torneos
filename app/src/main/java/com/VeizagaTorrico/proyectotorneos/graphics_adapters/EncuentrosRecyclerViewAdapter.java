package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EncuentrosRecyclerViewAdapter extends RecyclerView.Adapter<EncuentrosRecyclerViewAdapter.Holder>
        implements View.OnClickListener {

    private Context context;
    private List<Confrontation> encuentros;
    private View.OnClickListener listener;

    public EncuentrosRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setEncuentros(List<Confrontation> encuentros) {
        this.encuentros = encuentros;
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
    public EncuentrosRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.encuentro_layout,null);
        vista.setOnClickListener(this);
        return new EncuentrosRecyclerViewAdapter.Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull EncuentrosRecyclerViewAdapter.Holder holder, int position) {
        try {
            Confrontation encuentro = this.encuentros.get(position);

            holder.comp1.setText(encuentro.getCompetidor1());
            holder.comp2.setText(encuentro.getCompetidor2());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(this.encuentros!= null){
            return this.encuentros.size();
        }
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView comp1, comp2;
        EditText edComp1, edComp2;

        public Holder(@NonNull View itemView) {
            super(itemView);
            try{
                comp1 = itemView.findViewById(R.id.txtComp1);
                comp2 = itemView.findViewById(R.id.txtComp2);
                edComp1 = itemView.findViewById(R.id.etResultadoC1);
                edComp2 = itemView.findViewById(R.id.etResultadoC2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
