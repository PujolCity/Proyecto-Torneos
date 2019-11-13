package com.VeizagaTorrico.proyectotorneos;

import android.net.Uri;
import android.os.Bundle;

import com.VeizagaTorrico.proyectotorneos.fragments.competencias.CompetenciasListFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CompetidoresListFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.competencias.FiltroFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.encuentros.DetalleEncuentroFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.MisCompetenciasFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias.CrearCompetencia1Fragment;
import com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias.CrearCompetencia2Fragment;
import com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias.CrearCompetencia3Fragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias.DetalleCompetenciaFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias.EncuentrosFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias.InfoGeneralCompetenciaFragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.VeizagaTorrico.proyectotorneos.fragments.InicioFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias.PosicionesFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CargasDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CompetidoresDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.DetalleOrganizandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.EncuentrosDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.GeneralDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.OrganizandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.ParticipandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.SiguiendoFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class NavigationMainActivity extends AppCompatActivity
        implements CrearCompetencia1Fragment.OnFragmentInteractionListener ,
        CrearCompetencia2Fragment.OnFragmentInteractionListener ,
        CrearCompetencia3Fragment.OnFragmentInteractionListener ,
        CompetenciasListFragment.OnFragmentInteractionListener ,
        InfoGeneralCompetenciaFragment.OnFragmentInteractionListener ,
        InicioFragment.OnFragmentInteractionListener,
        CompetidoresListFragment.OnFragmentInteractionListener,
        MisCompetenciasFragment.OnFragmentInteractionListener,
        ParticipandoFragment.OnFragmentInteractionListener,
        SiguiendoFragment.OnFragmentInteractionListener,
        OrganizandoFragment.OnFragmentInteractionListener,
        FiltroFragment.OnFragmentInteractionListener,
        CargasDetalleFragment.OnFragmentInteractionListener,
        CompetidoresDetalleFragment.OnFragmentInteractionListener,
        DetalleOrganizandoFragment.OnFragmentInteractionListener,
        EncuentrosDetalleFragment.OnFragmentInteractionListener,
        GeneralDetalleFragment.OnFragmentInteractionListener,
        DetalleCompetenciaFragment.OnFragmentInteractionListener,
        EncuentrosFragment.OnFragmentInteractionListener,
        PosicionesFragment.OnFragmentInteractionListener,
        DetalleEncuentroFragment.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
     //   Log.d("Prob SHAREDPREFERENCES", ManagerToken.getInstance().getTokenInternal(this));

        //aca se declaran los elementos del menu desplegable
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.inicioFragment, R.id.crearCompetencia1Fragment, R.id.filtroFragment, R.id.misCompetencias)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
