package com.VeizagaTorrico.proyectotorneos.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.adapters.TypesOrganizationAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.TypesOrganization;
import com.VeizagaTorrico.proyectotorneos.services.TypesOrganizationSrv;

import java.util.ArrayList;
import java.util.List;

public class CrearCompetenciaOrgActivity extends AppCompatActivity {

    //Widgets
    private Button btnCrear,btnVlvr;
    private Spinner spinner;
    private TextView txtView;
    private Competition competition;
    private TypesOrganizationSrv orgSrv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia_org);

        inicializar();

        // BOTONES!
        btnVlvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrearCompetenciaOrgActivity.this,CrearCompetenciaDeporteActivity.class);
                intent.putExtra("competition", competition);
                startActivity(intent);
            }
        });
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hacer peticion al servidor
                Toast toast = Toast.makeText(CrearCompetenciaOrgActivity.this, "Implementar servicio de creacion", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        Call<List<TypesOrganization>> call = orgSrv.getTypesOrganization();
        call.enqueue(new Callback<List<TypesOrganization>>() {
            @Override
            public void onResponse(Call<List<TypesOrganization>> call, Response<List<TypesOrganization>> response) {
            List<TypesOrganization> tipos = new ArrayList<TypesOrganization>();

            if(response.code() == 200){
                tipos = response.body();
                Log.d("RESPONSECODE OrgActvty",  Integer.toString(response.code()) );
            }

                ArrayAdapter<TypesOrganization> adapter = new ArrayAdapter<TypesOrganization>(CrearCompetenciaOrgActivity.this,android.R.layout.simple_spinner_item,tipos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        TypesOrganization org = (TypesOrganization) spinner.getSelectedItem();
                        txtView.setText(org.getDescription());
                        competition =(Competition) getIntent().getSerializableExtra("competition");
                        competition.setTypesOrganization(org);
                        Log.d("A ver que trajo",org.getName());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }

                });
            }

            @Override
            public void onFailure(Call<List<TypesOrganization>> call, Throwable t) {
                Toast toast = Toast.makeText(CrearCompetenciaOrgActivity.this, "No anda una mierda", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onResponse", "no anda");

            }
        });
    }

    public void inicializar(){

        txtView = findViewById(R.id.descripcionTipoOrg);
        btnCrear = findViewById(R.id.btnCCSig_3);
        btnVlvr = findViewById(R.id.btnCCvlvr_2);
        spinner = findViewById(R.id.spinnerTipoOrg);
        orgSrv = new TypesOrganizationAdapter().connectionEnable();
    }
}
