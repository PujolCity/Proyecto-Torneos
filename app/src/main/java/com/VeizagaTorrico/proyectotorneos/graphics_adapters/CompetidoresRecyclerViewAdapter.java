package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompetidoresRecyclerViewAdapter extends RecyclerView.Adapter<CompetidoresRecyclerViewAdapter.ViewHolder> {
    private List<User> competidores;
    private Context context;

    public CompetidoresRecyclerViewAdapter(Context context) {
        this.context = context;
        this.competidores = new ArrayList<>();
    }


    public void setCompetidores(List<User> competidores)
    {
        this.competidores = competidores;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.competidores_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            User usuario = this.competidores.get(position);

            holder.txtNombUsuario.setText(usuario.getNombreUsuario());
            holder.txtNombre.setText(usuario.getNombre());
            holder.txtApellido.setText(usuario.getApellido());
            holder.txtAlias.setText(usuario.getAlias());
            holder.txtCorreo.setText(usuario.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(this.competidores.size() != 0)
            return this.competidores.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombUsuario, txtNombre,txtApellido, txtAlias, txtCorreo;


        public ViewHolder(@NonNull View view) {
            super(view);
            try {
                txtNombUsuario = view.findViewById(R.id.usrNombreUsuario);
                txtNombre = view.findViewById(R.id.usrNombre);
                txtApellido = view.findViewById(R.id.usrApellido);
                txtAlias = view.findViewById(R.id.usrAlias);
                txtCorreo = view.findViewById(R.id.usrCorreo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
    }
}
