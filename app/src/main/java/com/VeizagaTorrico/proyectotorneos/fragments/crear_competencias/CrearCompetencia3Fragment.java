package com.VeizagaTorrico.proyectotorneos.fragments.crear_competencias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.TypesOrganization;
import com.VeizagaTorrico.proyectotorneos.services.TypesOrganizationSrv;

import java.util.List;

public class CrearCompetencia3Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //Widgets
    private Button btnCrear;
    private Spinner spinner;
    private TextView txtView;
    private Competition competition;
    private TypesOrganizationSrv orgSrv;
    private View vista;

    public CrearCompetencia3Fragment() {
        // Required empty public constructor
    }
    public static CrearCompetencia3Fragment newInstance(String param1, String param2) {
        CrearCompetencia3Fragment fragment = new CrearCompetencia3Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_competencia3, container, false);

        txtView = vista.findViewById(R.id.descripcionTipoOrg);
        btnCrear = vista.findViewById(R.id.btnCCSig_3);
        spinner = vista.findViewById(R.id.spinnerTipoOrg);
        orgSrv = new RetrofitAdapter().connectionEnable().create(TypesOrganizationSrv.class);

        //LISTENER BOTONES
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hacer peticion al servidor
                Toast toast = Toast.makeText(getContext(), "Implementar servicio de creacion", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        Call<List<TypesOrganization>> call = orgSrv.getTypesOrganization();
        call.enqueue(new Callback<List<TypesOrganization>>() {
            @Override
            public void onResponse(Call<List<TypesOrganization>> call, Response<List<TypesOrganization>> response) {
                List<TypesOrganization> tipos;

                if(response.code() == 200){
                    tipos = response.body();
                    Log.d("RESPONSECODE OrgActvty",  Integer.toString(response.code()) );
                    ArrayAdapter<TypesOrganization> adapter = new ArrayAdapter<>(vista.getContext(),android.R.layout.simple_spinner_item,tipos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            TypesOrganization org = (TypesOrganization) spinner.getSelectedItem();
                            txtView.setText(org.getDescription());
                            competition =(Competition) getArguments().getSerializable("competition");
                            //  competition.setTypesOrganization(org);
                            Log.d("ItemSelected",org.getName());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<TypesOrganization>> call, Throwable t) {
                Toast toast = Toast.makeText(getContext(), "Por favor recargar pestaña", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onFailure", t.getMessage());
            }
        });


        return vista;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
