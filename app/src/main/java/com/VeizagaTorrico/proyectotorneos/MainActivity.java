package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.activities.CrearCompetenciaDeporteActivity;
import com.VeizagaTorrico.proyectotorneos.activities.DetalleCompListActivity;
import com.VeizagaTorrico.proyectotorneos.adapters.SportsAdapter;
import com.VeizagaTorrico.proyectotorneos.fragments.CrearCompetencia1Fragment;
import com.VeizagaTorrico.proyectotorneos.fragments.CrearCompetencia2Fragment;
import com.VeizagaTorrico.proyectotorneos.list_view_adapters.AdapterListCompetencias;
import com.VeizagaTorrico.proyectotorneos.models.Sport;
import com.VeizagaTorrico.proyectotorneos.services.SportsSrv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CrearCompetencia1Fragment crearCompetencia1Fragment;
    private ListView listView;
    private SportsSrv sportsSrv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sportsSrv = new SportsAdapter().connectionEnable();

        listView = findViewById(R.id.listViewComp);

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
                    Log.d("RESPONSE CODE", Integer.toString(response.code()));
                }

                AdapterListCompetencias adapterListCompetencias = new AdapterListCompetencias(MainActivity.this, deportes);
                listView.setAdapter(adapterListCompetencias);
            }

            @Override
            public void onFailure(Call<List<Sport>> call, Throwable t) {
                Toast toast = Toast.makeText(MainActivity.this, "No anda una mierda", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onResponse", "no anda");

                }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sport deporte = (Sport)  adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this, DetalleCompListActivity.class);
                intent.putExtra("deporte", deporte);
                startActivity(intent);

            }
        });

    }



    /*
        crearCompetencia1Fragment = new CrearCompetencia1Fragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contentMain,crearCompetencia1Fragment, null);
        */
}
