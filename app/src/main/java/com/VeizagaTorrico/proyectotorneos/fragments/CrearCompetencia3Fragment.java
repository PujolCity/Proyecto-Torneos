package com.VeizagaTorrico.proyectotorneos.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.VeizagaTorrico.proyectotorneos.adapters.TypesOrganizationAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.TypesOrganization;
import com.VeizagaTorrico.proyectotorneos.services.TypesOrganizationSrv;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrearCompetencia3Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrearCompetencia3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearCompetencia3Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearCompetencia3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearCompetencia3Fragment newInstance(String param1, String param2) {
        CrearCompetencia3Fragment fragment = new CrearCompetencia3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        orgSrv = new TypesOrganizationAdapter().connectionEnable();

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
                List<TypesOrganization> tipos = new ArrayList<TypesOrganization>();

                if(response.code() == 200){
                    tipos = response.body();
                    Log.d("RESPONSECODE OrgActvty",  Integer.toString(response.code()) );
                }

                ArrayAdapter<TypesOrganization> adapter = new ArrayAdapter<TypesOrganization>(getContext(),android.R.layout.simple_spinner_item,tipos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        TypesOrganization org = (TypesOrganization) spinner.getSelectedItem();
                        txtView.setText(org.getDescription());
                        competition =(Competition) getArguments().getSerializable("competition");
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
                Toast toast = Toast.makeText(getContext(), "No anda una mierda", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("onResponse", "no anda");

            }
        });


        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
