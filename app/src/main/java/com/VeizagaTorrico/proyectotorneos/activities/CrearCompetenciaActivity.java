package com.VeizagaTorrico.proyectotorneos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VeizagaTorrico.proyectotorneos.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia);
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
                getText();
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
                txtFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        datePicker.show();
    }


    private void getText(){
        //Con esto pruebo que anda el EditText lo que saco de aca deberia mandarlo a un
        //servicio para ver si el nombre de la competencia es unica

        String nombreComp;
        nombreComp = txtNmbComp.getText().toString();
        Toast.makeText(getApplicationContext(),nombreComp,Toast.LENGTH_LONG).show();
    }
}
