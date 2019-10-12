package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.User;

import java.util.List;

public class CompetidoresRecyclerViewAdapter extends RecyclerView.Adapter<CompetidoresRecyclerViewAdapter.ViewHolder>{

    private List<User> competidores;
    private Context context;

    public CompetidoresRecyclerViewAdapter(Context context,List<User> items) {
        this.competidores = items;
        this.context = context;
    }

    public void setCompetidores(List<User> competidores)
    {
        this.competidores = competidores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.competidores_list_layout, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User usuario = this.competidores.get(position);
        Log.d("COMPETIDORES",usuario.getApellido());

        holder.txtNombUsuario.setText(usuario.getNombreUsuario());
        holder.txtNombre.setText(usuario.getNombre());
        holder.txtApellido.setText(usuario.getApellido());

        //ACA TENGO QUE IMPLEMENTAR LA LOGICA DEL CHECKBOX
        holder.btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "Implementar logica para rechazar solicitudes", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        holder.btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "Implementar logica para acerptar solicitudes", Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.competidores != null) {
           return this.competidores.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombUsuario, txtNombre,txtApellido;
        ImageButton btnAceptar,btnRechazar;


        public ViewHolder(@NonNull View view) {
            super(view);
            txtNombUsuario = view.findViewById(R.id.nombUsuario);
            txtNombre = view.findViewById(R.id.nomb);
            txtApellido = view.findViewById(R.id.apelido);
            btnAceptar = view.findViewById(R.id.btnAceptar);
            btnRechazar = view.findViewById(R.id.btnRechazar);

        }
    }
}