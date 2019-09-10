package com.VeizagaTorrico.proyectotorneos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;

public class CrearCompetenciaOrgActivity extends AppCompatActivity {

    //Widgets
    private Button btnSig;
    private Spinner spinner;
    private String lista[] = {"Seleccione un item",
            "Holis","Soy","Un","Ejemplo","Para","Probar","Que","Ando"
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia_org);

        btnSig = findViewById(R.id.btnCCSig_3);

        spinner = findViewById(R.id.spinnerTipoOrg);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                           @Override
                                           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                           }
                                           @Override
                                           public void onNothingSelected(AdapterView<?> adapterView) {
                                           }

                                       });
    }

}
