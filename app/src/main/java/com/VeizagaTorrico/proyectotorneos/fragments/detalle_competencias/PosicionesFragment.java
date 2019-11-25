package com.VeizagaTorrico.proyectotorneos.fragments.detalle_competencias;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;

public class PosicionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;

    private TableLayout tablaPosiciones;

    public PosicionesFragment() {
        // Required empty public constructor
    }
public static PosicionesFragment newInstance() {
        PosicionesFragment fragment = new PosicionesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_posiciones, container, false);
        tablaPosiciones = vista.findViewById(R.id.TablaPosiciones);
        showTablePositions();
        return vista;
    }

    private void showTablePositions() {

        Log.d("LOG - ADD", "Entramos a la funcion para agregar la fila");

        TableRow tbrow = new TableRow(getContext());
        // agregamos los valores de las columnas
        TextView tvCompetidor = new TextView(getContext());
        tvCompetidor.setText("CompX");
        tvCompetidor.setTextColor(Color.BLACK);
        tvCompetidor.setGravity(Gravity.CENTER);
        tbrow.addView(tvCompetidor);
        TextView tvPj = new TextView(getContext());
        tvPj.setText("X");
        tvPj.setTextColor(Color.BLACK);
        tvPj.setGravity(Gravity.CENTER);
        tbrow.addView(tvPj);
        TextView tvPg = new TextView(getContext());
        tvPg.setText("X");
        tvPg.setTextColor(Color.BLACK);
        tvPg.setGravity(Gravity.CENTER);
        tbrow.addView(tvPg);
        TextView tvPe = new TextView(getContext());
        tvPe.setText("X");
        tvPe.setTextColor(Color.BLACK);
        tvPe.setGravity(Gravity.CENTER);
        tbrow.addView(tvPe);
        TextView tvPp = new TextView(getContext());
        tvPp.setText("X");
        tvPp.setTextColor(Color.BLACK);
        tvPp.setGravity(Gravity.CENTER);
        tbrow.addView(tvPp);
        TextView tvPts = new TextView(getContext());
        tvPts.setText("X");
        tvPts.setTextColor(Color.BLACK);
        tvPts.setGravity(Gravity.CENTER);
        tbrow.addView(tvPts);

        tablaPosiciones.addView(tbrow);
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
