package com.VeizagaTorrico.proyectotorneos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.VeizagaTorrico.proyectotorneos.fragments.CerrarSesionFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CrearInscripcionFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil.MiPerfilFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.competencias.CompetenciasListFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CompetidoresListFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.competencias.FiltroFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.EditCompetenciaFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.encuentros.DetalleEncuentroFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil.NotificacionesFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil.TabPerfilFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.MisCompetenciasFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias.CrearCompetencia1Fragment;
import com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias.CrearCompetencia2Fragment;
import com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias.CrearCompetencia3Fragment;
import com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias.DetalleCompetenciaFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias.EncuentrosFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias.InfoGeneralCompetenciaFragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.VeizagaTorrico.proyectotorneos.fragments.InicioFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.competencias.detalle_competencias.PosicionesFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CargasDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CompetidoresDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.DetalleOrganizandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.EncuentrosDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.GeneralDetalleFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.OrganizandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.ParticipandoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mis_competencias.SiguiendoFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.noticias.DetalleNoticiasFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.noticias.NoticiasFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga.CargaFaseFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga.CargarJuezFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga.CargarNoticiaFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga.CargarPredioFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga.CargarTurnosFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.pantalla_carga.CoOrganizadorFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil.InvitacionesFragment;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_EMAIL;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_SESSION;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;

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
        DetalleEncuentroFragment.OnFragmentInteractionListener,
        CargarPredioFragment.OnFragmentInteractionListener,
        CargarTurnosFragment.OnFragmentInteractionListener,
        CargarJuezFragment.OnFragmentInteractionListener,
        CoOrganizadorFragment.OnFragmentInteractionListener,
        InvitacionesFragment.OnFragmentInteractionListener,
        CargaFaseFragment.OnFragmentInteractionListener,
        MiPerfilFragment.OnFragmentInteractionListener,
        NotificacionesFragment.OnFragmentInteractionListener,
        EditCompetenciaFragment.OnFragmentInteractionListener,
        TabPerfilFragment.OnFragmentInteractionListener,
        NoticiasFragment.OnFragmentInteractionListener,
        CargarNoticiaFragment.OnFragmentInteractionListener,
        DetalleNoticiasFragment.OnFragmentInteractionListener,
        CerrarSesionFragment.OnFragmentInteractionListener,
        CrearInscripcionFragment.OnFragmentInteractionListener {

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

        //aca se declaran los elementos del menu desplegable
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.noticiasFragment, R.id.crearCompetencia1Fragment, R.id.filtroFragment, R.id.misCompetencias, R.id.tabPerfilFragment, R.id.cerrarSesionFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateDataNavMainMenu(navigationView);
    }

    // actualizamos los datos que se mueatran en la barra principal
    private void updateDataNavMainMenu(NavigationView navbar) {
        View vistaNavMain = navbar.getHeaderView(0);
        TextView tvTitle = vistaNavMain.findViewById(R.id.tv_user_navbar_main);
        TextView tvSubtitle = vistaNavMain.findViewById(R.id.tv_correo_navbar_main);

        // actualizamos los datos
        tvTitle.setText(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this, FILE_SHARED_DATA_USER, KEY_USERNAME));
        tvSubtitle.setText(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(this, FILE_SHARED_DATA_USER, KEY_EMAIL));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            if(!obtenerEstadoButton()){
                passToHome();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean obtenerEstadoButton() {
        return ManagerSharedPreferences.getInstance().getSessionFromSharedPreferences(this.getApplicationContext(), FILE_SHARED_DATA_USER, KEY_SESSION);
    }

    private void passToHome() {
        ManagerSharedPreferences.getInstance().setSessionFromSharedPreferences(this.getApplicationContext(),FILE_SHARED_DATA_USER, KEY_SESSION, false);
        Intent toInitApp = new Intent(this, HomeActivity.class);
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toInitApp);
        finish();
    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        Bundle extras = intent.getExtras();
//        if(extras != null){
//            if(extras.containsKey("misSolicitudes"))
//            {
//                String dataNotificationPush = extras.getString(Constants.EXTRA_KEY_ID_COMPETENCIA);
//                Log.d("NOTIF_FORGROUND", "Reconoce la clave recibida. IdComp recibido: "+dataNotificationPush);
//                Intent toMisSolicitudes = new Intent(this, MisSolicitudesActivity.class);
//                toMisSolicitudes.putExtra(Constants.EXTRA_KEY_ID_COMPETENCIA, dataNotificationPush);
//                toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                toMisSolicitudes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(toMisSolicitudes);
//            }
//
//        }
//    }
}
