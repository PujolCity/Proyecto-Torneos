package com.VeizagaTorrico.proyectotorneos.list_view_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Sport;

import java.util.List;

public class AdapterListCompetencias extends BaseAdapter {

    private Context context;
    private List<Sport> deportes;
    private TextView txtCompetencia, txtDeporte, txtCategoria;

    public AdapterListCompetencias(Context context, List<Sport> deportes) {
        this.context = context;
        this.deportes = deportes;
    }

    @Override
    public int getCount() {
        return this.deportes.size();
    }

    @Override
    public Object getItem(int i) {
        return this.deportes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.deportes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflater =  LayoutInflater.from(this.context);
        vista = inflater.inflate(R.layout.list_view_competencias,null);
        txtCompetencia = vista.findViewById(R.id.txtNmbCompList);
        txtDeporte = vista.findViewById(R.id.txtDepCompList);
        txtCategoria = vista.findViewById(R.id.txtCatCompList);

        txtCompetencia.setText(this.deportes.get(i).getNombre());
        txtCategoria.setText(this.deportes.get(i).getCategories().get(0).getNombreCat());
        txtDeporte.setText(this.deportes.get(i).getCategories().get(0).getDescripcion());

        return vista;
    }
}
