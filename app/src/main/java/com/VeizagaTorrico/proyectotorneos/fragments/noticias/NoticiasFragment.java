package com.VeizagaTorrico.proyectotorneos.fragments.noticias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.NoticiasRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.News;
import com.VeizagaTorrico.proyectotorneos.services.NewsSrv;

import java.util.ArrayList;
import java.util.List;


public class NoticiasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private RecyclerView recyclerNoticias;
    private RecyclerView.LayoutManager manager;
    private NoticiasRecyclerViewAdapter newsAdapter;
    private List<News> noticias;
    private NewsSrv newsSrv;
    private TextView textNoticias;


    public NoticiasFragment() {
        // Required empty public constructor
    }

    public static NoticiasFragment newInstance() {
        NoticiasFragment fragment = new NoticiasFragment();
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
        vista = inflater.inflate(R.layout.fragment_noticias, container, false);
        initElement();
        if(this.noticias.size() == 0){
            sinNoticias();
        }
        inflarRecycler();
        return vista;
    }

    private void inflarRecycler() {


    }

    private void initElement() {
        recyclerNoticias = vista.findViewById(R.id.recyclerNoticias);
        manager = new LinearLayoutManager(vista.getContext());
        recyclerNoticias.setLayoutManager(manager);
        newsAdapter = new NoticiasRecyclerViewAdapter(vista.getContext());
        textNoticias = vista.findViewById(R.id.tv_noticias);
        this.noticias = new ArrayList<>();
        noticias.add(new News(1,"Manejo De Fechas En Java I","Las clases java.util.Date y java.sql.Date.","java.util.Date: Según la documentación \"La clase java.util.Date representa un instante de tiempo específico, con precisión de milisegundos\"; esto más que ser una especie de \"autoadulación\" para la clase, quiere decir que no solo se trata de una simple cadena al estilo yyyy/MM/dd, sino que almacena hasta milisegundos y que es posible trabajar con ellos.\n"));

        noticias.add(new News(1,"Manejo De Fechas En Java I","Las clases java.util.Date y java.sql.Date.","java.util.Date: Según la documentación \"La clase java.util.Date representa un instante de tiempo específico, con precisión de milisegundos\"; esto más que ser una especie de \"autoadulación\" para la clase, quiere decir que no solo se trata de una simple cadena al estilo yyyy/MM/dd, sino que almacena hasta milisegundos y que es posible trabajar con ellos.\n" +
                "Antes del jdk1.1 la clase java.util.Date tenía dos funciones adicionales a la que conocemos ahora, una de ellas era la interpretación de datos que tenían que ver con fechas, como años, días, segundos, entre otros. La otra era el formateo (la forma como se muestra) y parseo (convertir un String a java.util.Date). Pero debido a las dificultades que presentaban estas funcionalidades a la hora de internacionalizar los programas, esos métodos ya está obsoletos y la clase java.util.Calendar se encargó de esto; así que en este momento esta clase, sólo hace lo que se mencionó al principio: \"representa un instante de tiempo específico, con precisión de milisegundos\"; más adelante veremos como ampliar esta funcionalidad. Por ahora veamos las convenciones que sigue esta clase:\n" +
                "* El año \"y\" está representado por un entero igual a (\"y\" - 1900). Por ejemplo el año 2004 se representa como 104 (2004 - 1900).\n" ));

        noticias.add(new News(1,"Manejo De Fechas En Java I","Las clases java.util.Date y java.sql.Date.","java.util.Date: Según la documentación \"La clase java.util.Date representa un instante de tiempo específico, con precisión de milisegundos\"; esto más que ser una especie de \"autoadulación\" para la clase, quiere decir que no solo se trata de una simple cadena al estilo yyyy/MM/dd, sino que almacena hasta milisegundos y que es posible trabajar con ellos.\n" +
                "Antes del jdk1.1 la clase java.util.Date tenía dos funciones adicionales a la que conocemos ahora, una de ellas era la interpretación de datos que tenían que ver con fechas, como años, días, segundos, entre otros. La otra era el formateo (la forma como se muestra) y parseo (convertir un String a java.util.Date). Pero debido a las dificultades que presentaban estas funcionalidades a la hora de internacionalizar los programas, esos métodos ya está obsoletos y la clase java.util.Calendar se encargó de esto; así que en este momento esta clase, sólo hace lo que se mencionó al principio: \"representa un instante de tiempo específico, con precisión de milisegundos\"; más adelante veremos como ampliar esta funcionalidad. Por ahora veamos las convenciones que sigue esta clase:\n" +
                "* El año \"y\" está representado por un entero igual a (\"y\" - 1900). Por ejemplo el año 2004 se representa como 104 (2004 - 1900).\n" ));


        noticias.add(new News(1,"Manejo De Fechas En Java I","Las clases java.util.Date y java.sql.Date.","java.util.Date: Según la documentación \"La clase java.util.Date representa un instante de tiempo específico, con precisión de milisegundos\"; esto más que ser una especie de \"autoadulación\" para la clase, quiere decir que no solo se trata de una simple cadena al estilo yyyy/MM/dd, sino que almacena hasta milisegundos y que es posible trabajar con ellos.\n" +
                "Antes del jdk1.1 la clase java.util.Date tenía dos funciones adicionales a la que conocemos ahora, una de ellas era la interpretación de datos que tenían que ver con fechas, como años, días, segundos, entre otros. La otra era el formateo (la forma como se muestra) y parseo (convertir un String a java.util.Date). Pero debido a las dificultades que presentaban estas funcionalidades a la hora de internacionalizar los programas, esos métodos ya está obsoletos y la clase java.util.Calendar se encargó de esto; así que en este momento esta clase, sólo hace lo que se mencionó al principio: \"representa un instante de tiempo específico, con precisión de milisegundos\"; más adelante veremos como ampliar esta funcionalidad. Por ahora veamos las convenciones que sigue esta clase:\n" +
                "* El año \"y\" está representado por un entero igual a (\"y\" - 1900). Por ejemplo el año 2004 se representa como 104 (2004 - 1900).\n" +
                "* Las horas van entre 0 y 23, donde la medianoche es 0 y el medio día 12.\n"));

        noticias.add(new News(1,"Manejo De Fechas En Java I","Las clases java.util.Date y java.sql.Date.","java.util.Date: Según la documentación \"La clase java.util.Date representa un instante de tiempo específico, con precisión de milisegundos\"; esto más que ser una especie de \"autoadulación\" para la clase, quiere decir que no solo se trata de una simple cadena al estilo yyyy/MM/dd, sino que almacena hasta milisegundos y que es posible trabajar con ellos.\n" +
                "Antes del jdk1.1 la clase java.util.Date tenía dos funciones adicionales a la que conocemos ahora, una de ellas era la interpretación de datos que tenían que ver con fechas, como años, días, segundos, entre otros. La otra era el formateo (la forma como se muestra) y parseo (convertir un String a java.util.Date). Pero debido a las dificultades que presentaban estas funcionalidades a la hora de internacionalizar los programas, esos métodos ya está obsoletos y la clase java.util.Calendar se encargó de esto; así que en este momento esta clase, sólo hace lo que se mencionó al principio: \"representa un instante de tiempo específico, con precisión de milisegundos\"; más adelante veremos como ampliar esta funcionalidad. Por ahora veamos las convenciones que sigue esta clase:\n"));

        noticias.add(new News(1,"Manejo De Fechas En Java I","Las clases java.util.Date y java.sql.Date.","java.util.Date: Según la documentación \"La clase java.util.Date representa un instante de tiempo específico, con precisión de milisegundos\"; esto más que ser una especie de \"autoadulación\" para la clase, quiere decir que no solo se trata de una simple cadena al estilo yyyy/MM/dd, sino que almacena hasta milisegundos y que es posible trabajar con ellos.\n" +
                "Antes del jdk1.1 la clase java.util.Date tenía dos funciones adicionales a la que conocemos ahora, una de ellas era la interpretación de datos que tenían que ver con fechas, como años, días, segundos, entre otros. La otra era el formateo (la forma como se muestra) y parseo (convertir un String a java.util.Date). Pero debido a las dificultades que presentaban estas funcionalidades a la hora de internacionalizar los programas, esos métodos ya está obsoletos y la clase java.util.Calendar se encargó de esto; así que en este momento esta clase, sólo hace lo que se mencionó al principio: \"representa un instante de tiempo específico, con precisión de milisegundos\"; más adelante veremos como ampliar esta funcionalidad. Por ahora veamos las convenciones que sigue esta clase:\n" +
                "* El año \"y\" está representado por un entero igual a (\"y\" - 1900). Por ejemplo el año 2004 se representa como 104 (2004 - 1900).\n" +
                "* Los meses son representados por números entre 0 y 11, donde enero es 0 y diciembre es 11.\n" +
                "* Los días y minutos se representan de forma corriente. Entre 1 - 31 y 0 - 59 respectivamente.\n" +
                "* Las horas van entre 0 y 23, donde la medianoche es 0 y el medio día 12ajshdASGFhasoufgsaiudhfiuasgfuyasihdkusayfoiuhasjkdgsauydksabd.\n"));

        newsAdapter.setNoticias(noticias);
        recyclerNoticias.setAdapter(newsAdapter);

    }

    private void sinNoticias() {
        recyclerNoticias.setVisibility(View.INVISIBLE);
        textNoticias.setVisibility(View.VISIBLE);
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
