package com.VeizagaTorrico.proyectotorneos.graphics_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.News;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoticiasRecyclerViewAdapter extends RecyclerView.Adapter<NoticiasRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<News> noticias;
    private View.OnClickListener listener;

    public NoticiasRecyclerViewAdapter(Context context) {
        this.context = context;
        noticias = new ArrayList<>();
    }

    public void setNoticias(List<News> noticias) {
        Log.d("NOTICIAS RECIBIDAS", noticias.toString());
        this.noticias = noticias;
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

    @NonNull
    @Override
    public NoticiasRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_noticias,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiasRecyclerViewAdapter.ViewHolder holder, int position) {
        try {
            News noticia = this.noticias.get(position);
            holder.competencia.setText(this.noticias.get(position).getCompetencia());
            holder.titulo.setText(noticia.getTitulo());
            holder.subtitulo.setText(noticia.getSubtitulo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(this.noticias.size() != 0){
            return this.noticias.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView competencia, titulo, subtitulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tv_titulo_noticia);
            subtitulo = itemView.findViewById(R.id.tv_subtitulo_noticia);
            competencia = itemView.findViewById(R.id.tv_competencia_noticia);

        }
    }
}
