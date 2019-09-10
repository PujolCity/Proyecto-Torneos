package com.VeizagaTorrico.proyectotorneos.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.logica.Categoria;
import com.VeizagaTorrico.proyectotorneos.logica.Deporte;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CrearCompetenciaDeporteActivity extends AppCompatActivity {
    private Deporte deporte1,deporte2,deporte3;
    private Categoria categoria1,categoria2,categoria3,categoria4;
    private List<Deporte> deportes;
    private List<Categoria> categorias;
    private Spinner spinnerDeporte, spinnerCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia_deporte);


        this.deportes = new ArrayList<Deporte>();
        this.categorias = new ArrayList<Categoria>();

        this.categoria1 = new Categoria(1,"cat1","categoria de prueba 1",15);
        this.categoria2 = new Categoria(2,"cat2","categoria de prueba 2",10);
        this.categoria3 = new Categoria(3,"cat3","categoria de prueba 3",12);
        this.categoria4 = new Categoria(4,"cat4","categoria de prueba 4",17);

        this.deporte1 = new Deporte(1,"dep1");
        this.deporte2 = new Deporte(2,"dep2");
        this.deporte3 = new Deporte(3,"dep3");

        this.categorias.add(categoria1);
        this.categorias.add(categoria1);

        this.deporte1.addCategoria(categoria1);
        this.deporte1.addCategoria(categoria4);

        this.deporte2.addCategoria(categoria2);
        this.deporte2.addCategoria(categoria3);

        this.deporte3.addCategoria(categoria4);
        this.deporte3.addCategoria(categoria2);

        this.deportes.add(deporte1);
        this.deportes.add(deporte2);
        this.deportes.add(deporte3);

        spinnerDeporte = findViewById(R.id.spinnerDeporte);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);

        // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
        ArrayAdapter<Deporte> adapterDeporte = new ArrayAdapter<Deporte>(this,android.R.layout.simple_spinner_item,deportes);
        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
        adapterDeporte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // seteo el adapter
        spinnerDeporte.setAdapter(adapterDeporte);
        // manejador del evento OnItemSelected
        spinnerDeporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Deporte dep;
                //elemento obtenido del spinner lo asigno a deporte y traigo la lista de categorias que tiene
                dep = (Deporte) spinnerDeporte.getSelectedItem();
                categorias = dep.getCategorias();
                //creo el adapter para el spinnerCategorias
                ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<Categoria>(CrearCompetenciaDeporteActivity.this,android.R.layout.simple_spinner_item,categorias);
                //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategoria.setAdapter(adapterCategoria);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
