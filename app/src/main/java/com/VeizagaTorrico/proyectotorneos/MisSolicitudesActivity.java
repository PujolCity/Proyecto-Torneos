package com.VeizagaTorrico.proyectotorneos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CompetidoresListFragment;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.SolicitudesRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.RespSrvUser;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.services.NotificationSrv;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_TOKEN_FIREBASE;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_EMAIL;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_LASTNAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_NAME;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_SESSION;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_TOKEN;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_USERNAME;

public class MisSolicitudesActivity extends AppCompatActivity implements CompetidoresListFragment.OnFragmentInteractionListener {

    private ImageButton btnBackIniti;
    CompetidoresListFragment misSolicituesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_solicitudes_activity);
        misSolicituesFragment = new CompetidoresListFragment();
        // recuperamos el id de la competencia recibido
        String idCompetenciaNotif = getIntent().getStringExtra(Constants.EXTRA_KEY_ID_COMPETENCIA);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_KEY_ID_COMPETENCIA, idCompetenciaNotif);
        misSolicituesFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, misSolicituesFragment ).commit();
        listenBackInit();
    }

    // ponemos a la escucha el boton de retroceder de la baraa de navegacion
    private void listenBackInit(){
        btnBackIniti = findViewById(R.id.btn_back_ini);
        btnBackIniti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToInitApp();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: no se bien que hacer aca
    }

    private void passToInitApp() {
        Intent toInitApp = new Intent(this, NavigationMainActivity.class);
        toInitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toInitApp);
        finish();
    }

}
