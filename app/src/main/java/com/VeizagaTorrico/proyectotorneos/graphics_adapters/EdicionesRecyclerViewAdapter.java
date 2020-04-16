package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.Edition;

import java.util.List;

public class EdicionesRecyclerViewAdapter extends RecyclerView.Adapter<EdicionesRecyclerViewAdapter.Holder>
        implements View.OnClickListener {

    private Context context;
    private List<Edition> ediciones;
    private View.OnClickListener listener;

    public EdicionesRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setEdiciones(List<Edition> ediciones) {
        this.ediciones = ediciones;
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
    public EdicionesRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(this.context).inflate(R.layout.edicion_layout,parent,false);
        vista.setOnClickListener(this);
        return new EdicionesRecyclerViewAdapter.Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull EdicionesRecyclerViewAdapter.Holder holder, int position) {
        try {
            Edition edicion = this.ediciones.get(position);

//            Log.d("DATA_EDITION",encuentro.toString());
            holder.tvOperacionEdicion.setText(edicion.getOperacion());
            holder.tvFechaEdicion.setText(edicion.getFecha());
            holder.tvEditorEdicion.setText(edicion.getEditor());
            Log.d("DATA_EDITION", "( Aca iria la info de la edicon )");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(this.ediciones!= null){
            return this.ediciones.size();
        }
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvOperacionEdicion, tvFechaEdicion, tvEditorEdicion;

        public Holder(@NonNull View itemView) {
            super(itemView);
            try{
                tvOperacionEdicion = itemView.findViewById(R.id.tv_operacion_edicion);
                tvFechaEdicion = itemView.findViewById(R.id.tv_fecha_edicion);
                tvEditorEdicion = itemView.findViewById(R.id.tv_editor_edicion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
