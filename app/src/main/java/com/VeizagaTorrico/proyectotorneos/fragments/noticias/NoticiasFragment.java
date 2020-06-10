package com.VeizagaTorrico.proyectotorneos.fragments.noticias;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.RetrofitAdapter;
import com.VeizagaTorrico.proyectotorneos.graphics_adapters.NoticiasRecyclerViewAdapter;
import com.VeizagaTorrico.proyectotorneos.models.News;
import com.VeizagaTorrico.proyectotorneos.services.NewsSrv;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerMsgView;
import com.VeizagaTorrico.proyectotorneos.utils.ManagerSharedPreferences;
import com.VeizagaTorrico.proyectotorneos.utils.MensajeSinInternet;
import com.VeizagaTorrico.proyectotorneos.utils.NetworkReceiver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.VeizagaTorrico.proyectotorneos.Constants.FILE_SHARED_DATA_USER;
import static com.VeizagaTorrico.proyectotorneos.Constants.KEY_ID;


public class NoticiasFragment extends Fragment implements MensajeSinInternet {

    private OnFragmentInteractionListener mListener;

    private View vista;
    private RecyclerView recyclerNoticias;
    private RecyclerView.LayoutManager manager;
    private NoticiasRecyclerViewAdapter newsAdapter;
    private List<News> noticias;
    private NewsSrv newsSrv;
    private TextView textNoticias;
    private int usuario;
    private SwipeRefreshLayout refreshLayout;
    private TextView sinConexion;
    private AlertDialog alertDialog;

    public NoticiasFragment() {
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
        refresh();
        if(NetworkReceiver.existConnection(vista.getContext())) {
            sinConexion.setVisibility(View.GONE);
            inflarRecycler();
            refresh();
        }
        else{
            sinConexion.setVisibility(View.VISIBLE);
            sinInternet();
            refresh();
        }
        return vista;
    }

    private void refresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkReceiver.existConnection(vista.getContext())) {
                    sinConexion.setVisibility(View.GONE);
                    inflarRecycler();
                }
                else{
                    sinInternet();
                    sinConexion.setVisibility(View.VISIBLE);
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void inflarRecycler() {
        recyclerNoticias.setVisibility(View.VISIBLE);
        Call<List<News>> call = newsSrv.getNews(usuario);
        Log.d("URL NOTICIAS", call.request().url().toString());
        alertDialog.show();
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                Log.d("RESPONSE NOTICIAS",Integer.toString(response.code()));
                    if(response.code() == 200){
                        alertDialog.dismiss();
                        noticias = response.body();
                        Log.d("DATOS NOTICIAS",noticias.toString());
                        if(!noticias.isEmpty()){
                            newsAdapter.setNoticias(noticias);
                            recyclerNoticias.setAdapter(newsAdapter);
                            newsAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    News news = noticias.get(recyclerNoticias.getChildAdapterPosition(view));
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("noticia",news);
                                    Navigation.findNavController(vista).navigate(R.id.detalleNoticiasFragment,bundle);
                                }
                            });
                        } else {
                            alertDialog.dismiss();
                            sinNoticias();
                        }
                    }
                    if (response.code() == 400) {
                        alertDialog.dismiss();
                        Log.d("RESP_RECOVERY_ERROR", "PETICION MAL FORMADA: "+response.errorBody());
                        //JSONObject jsonObject = null;
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String userMessage = jsonObject.getString("messaging");
                            Log.d("RESP_RECOVERY_ERROR", "Msg de la repuesta: "+userMessage);
                            Toast.makeText(vista.getContext(), "Hubo un problema :  << "+userMessage+" >>", Toast.LENGTH_SHORT).show();
                            sinNoticias();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(response.code() == 204){
                        alertDialog.dismiss();
                        sinNoticias();
                    }
            }
            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                try {
                    alertDialog.dismiss();
                    Log.d("onFailure", t.getMessage());
                    Toast toast = Toast.makeText(vista.getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT);
                    toast.show();
                    sinNoticias();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initElement() {
        refreshLayout = vista.findViewById(R.id.refreshNoticias);
        sinConexion = vista.findViewById(R.id.tv_sin_conexion_noticias);
        newsSrv = new RetrofitAdapter().connectionEnable().create(NewsSrv.class);
        recyclerNoticias = vista.findViewById(R.id.recyclerNoticias);
        textNoticias = vista.findViewById(R.id.tv_noticias);
        manager = new LinearLayoutManager(vista.getContext());
        recyclerNoticias.setLayoutManager(manager);
        recyclerNoticias.setHasFixedSize(true);
        newsAdapter = new NoticiasRecyclerViewAdapter(vista.getContext());
        recyclerNoticias.setAdapter(newsAdapter);
        this.noticias = new ArrayList<>();
        usuario = Integer.valueOf(ManagerSharedPreferences.getInstance().getDataFromSharedPreferences(vista.getContext(), FILE_SHARED_DATA_USER, KEY_ID));
        alertDialog = ManagerMsgView.getMsgLoading(vista.getContext(), "Espere un momento..");
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

    @Override
    public void sinInternet() {
        Toast toast = Toast.makeText(vista.getContext(), "Sin Conexion a Internet, por el momento no se podran ver las noticias. Por favor intente mas tarde", Toast.LENGTH_LONG);
        toast.show();
        sinNoticias();
        textNoticias.setText("Sin conexion a internet");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
