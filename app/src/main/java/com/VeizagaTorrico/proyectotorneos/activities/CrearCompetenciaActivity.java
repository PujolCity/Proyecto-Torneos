package com.VeizagaTorrico.proyectotorneos.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;
import com.VeizagaTorrico.proyectotorneos.adapters.CompetitionAdapter;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.services.CompetitionSrv;

import java.util.Calendar;

public class  CrearCompetenciaActivity extends AppCompatActivity {
    private static final String CERO = "0";
    private static final String BARRA = "/";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    //Widgets
    private TextView txtView;
    private ImageButton ibObtenerFecha;
    private EditText txtNmbComp, txtFecha;
    private Button btnSig;
    private String nombreComp;
    private Competition competition;
    private CompetitionSrv competitionSrv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia);
        competition =  new Competition();
        competitionSrv = new CompetitionAdapter().connectionEnable();
        //Widget TextView donde se mostrara el nombre de la competencia
        txtView = findViewById(R.id.txtNmbComp);
        //Wdget de donde tomo el posible nombre de la competencia
        txtNmbComp = findViewById(R.id.txtNmbComp);
        //Widget TextView donde se mostrara la fecha obtenida
        txtView = findViewById(R.id.dateInicioCrearComp);
        txtFecha = findViewById(R.id.txtfechaComp);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = findViewById(R.id.ib_obtener_fecha);
        //Evento setOnClickListener - clic
        ibObtenerFecha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });
        btnSig = findViewById(R.id.btnCCSig_1);
        btnSig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreComp = txtNmbComp.getText().toString();
                Log.d("dato enviado",nombreComp);

                Call<Success> call = competitionSrv.comprobar(nombreComp);
                call.enqueue(new Callback<Success>() {
                    @Override
                    public void onResponse(Call<Success> call, Response<Success> response) {
                        Log.d("onResponse", Integer.toString(response.code()));
                        if (!response.body().isExiste()) {
                            sigActivity();
                        }else {
                            Toast toast = Toast.makeText(CrearCompetenciaActivity.this, "Competencia Existente", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Success> call, Throwable t) {
                        Toast toast = Toast.makeText(CrearCompetenciaActivity.this, "No anda una mierda", Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d("onResponse", "no anda");

                    }
                });
                }
            }
        );
    }

    private void obtenerFecha(){
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                c.set(year,mesActual ,dayOfMonth);
                txtFecha.setText(diaFormateado+ BARRA + mesFormateado + BARRA + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        datePicker.show();

    }

    private void sigActivity(){
        Intent intent = new Intent(CrearCompetenciaActivity.this, CrearCompetenciaDeporteActivity.class);
        competition.setName(txtNmbComp.getText().toString());
        competition.setInitDate(c);
        intent.putExtra("competition", competition);
        startActivity(intent);}


}
