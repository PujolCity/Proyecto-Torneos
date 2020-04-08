package com.VeizagaTorrico.proyectotorneos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;

import com.VeizagaTorrico.proyectotorneos.fragments.detalle_organizando.CompetidoresListFragment;
import com.VeizagaTorrico.proyectotorneos.fragments.mi_perfil.InvitacionesFragment;

public class MisInvitacionesActivity extends AppCompatActivity implements InvitacionesFragment.OnFragmentInteractionListener {

    InvitacionesFragment misInvitacionesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_invitaciones);

        misInvitacionesFragment = new InvitacionesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_invitacion, misInvitacionesFragment ).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: no se bien que hacer aca
    }
}
