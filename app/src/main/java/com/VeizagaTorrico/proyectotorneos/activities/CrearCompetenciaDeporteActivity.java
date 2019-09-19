package com.VeizagaTorrico.proyectotorneos.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.adapters.SportsAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Category;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.Sport;
import com.VeizagaTorrico.proyectotorneos.services.SportsSrv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CrearCompetenciaDeporteActivity extends AppCompatActivity {

    private List<Category> categories;
    private Spinner spinnerDeporte, spinnerCategoria;
    private TextView txtDescripcion;
    private SportsSrv sportsSrv;
    private Competition competition;
    private Button vlvrBtn, sigBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia_deporte);
        // inicializo algunos parametros
        inicializar();
        vlvrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrearCompetenciaDeporteActivity.this,  CrearCompetenciaActivity.class);
                intent.putExtra("competition", competition);
                startActivity(intent);
            }
        });

        sigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(CrearCompetenciaDeporteActivity.this, CrearCompetenciaOrgActivity.class);
                    intent.putExtra("competition", competition);
                    startActivity(intent);
            }

        });

        //En call viene el tipo de dato que espero del servidor
        Call<List<Sport>> call = sportsSrv.getSports();
        call.enqueue(new Callback<List<Sport>>() {
            @Override
            public void onResponse(Call<List<Sport>> call, Response<List<Sport>> response) {
                List<Sport> deportes = new ArrayList<Sport>();

                //codigo 200 si salio tdo bien
                if (response.code() == 200) {
                    //asigno a deportes lo que traje del servidor
                    deportes = response.body();
                    Log.d("RESPONSE CODE",  Integer.toString(response.code()) );
                }

                // creo el adapter para el spinnerDeporte y asigno el origen de los datos para el adaptador del spinner
                ArrayAdapter<Sport> adapterDeporte = new ArrayAdapter<Sport>(CrearCompetenciaDeporteActivity.this,android.R.layout.simple_spinner_item, deportes);
                //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                adapterDeporte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // seteo el adapter
                spinnerDeporte.setAdapter(adapterDeporte);
                // manejador del evento OnItemSelected


                spinnerDeporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Sport dep;
                        //elemento obtenido del spinner lo asigno a deporte y traigo la lista de categories que tiene
                        dep = (Sport) spinnerDeporte.getSelectedItem();
                        categories = dep.getCategories();

                        //creo el adapter para el spinnerCategorias
                        ArrayAdapter<Category> adapterCategoria = new ArrayAdapter<Category>(CrearCompetenciaDeporteActivity.this,android.R.layout.simple_spinner_item, categories);

                        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
                        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategoria.setAdapter(adapterCategoria);
                        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                competition =(Competition) getIntent().getSerializableExtra("competition");
                                Category cat;
                                // para mostrar la descripcion de la categoria
                                cat = (Category) spinnerCategoria.getSelectedItem();
                                txtDescripcion.setText(cat.getDescripcion());
                                if(cat != null)
                                competition.setCategory(cat);
                                Log.d("A ver que trajo", competition.toString());
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
            @Override
            public void onFailure(Call<List<Sport>> call, Throwable t) {
                Toast toast = Toast.makeText(CrearCompetenciaDeporteActivity.this, "No anda una mierda", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onResponse", "no anda");

            }
        });
    }
    private void inicializar(){
        spinnerDeporte = findViewById(R.id.spinnerDeporte);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        txtDescripcion = findViewById(R.id.descripcionCategoria);
        sportsSrv = new SportsAdapter().connectionEnable();
        sigBtn = findViewById(R.id.btnCCSig_2);
        vlvrBtn = findViewById(R.id.btnCCvlvr_1);

    }


}
