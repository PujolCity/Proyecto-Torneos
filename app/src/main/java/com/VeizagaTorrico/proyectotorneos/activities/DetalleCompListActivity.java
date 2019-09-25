package com.VeizagaTorrico.proyectotorneos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Sport;

public class DetalleCompListActivity extends AppCompatActivity {

    private TextView nmb, dep, cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_comp_list);

        nmb = findViewById(R.id.txtNmbCompDet);
        dep = findViewById(R.id.txtDepCompDet);
        cat = findViewById(R.id.txtCatCompDet);

        Sport deporte = (Sport) getIntent().getExtras().getSerializable("deporte");

        nmb.setText(deporte.getNombre());
        cat.setText(deporte.getCategories().get(0).getNombreCat());
        dep.setText(deporte.getCategories().get(0).getDescripcion());

    }
}
