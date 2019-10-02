package com.VeizagaTorrico.proyectotorneos.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.models.Competition;



public class DetalleCompListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView nmb, dep, cat;
    private View vista;

    public DetalleCompListFragment() {
        // Required empty public constructor
    }
// TODO: Rename and change types and number of parameters
    public static DetalleCompListFragment newInstance(String param1, String param2) {
        DetalleCompListFragment fragment = new DetalleCompListFragment();
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
        vista = inflater.inflate(R.layout.fragment_detalle_comp_list, container, false);

        //vista.setContentView(R.layout.fragment_detalle_comp_list);

        nmb = vista.findViewById(R.id.txtNmbCompDet);
        dep = vista.findViewById(R.id.txtDepCompDet);
        cat = vista.findViewById(R.id.txtCatCompDet);

        Competition competition = (Competition) getArguments().getSerializable("deporte");

        nmb.setText(competition.getName());
        cat.setText(competition.getCategory().getNombreCat());
        dep.setText(competition.getCategory().getSport());

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
