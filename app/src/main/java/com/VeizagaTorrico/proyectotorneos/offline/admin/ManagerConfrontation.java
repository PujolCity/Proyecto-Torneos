package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.offline.model.Confrontation;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

public class ManagerConfrontation {

    private DbHelper adminDB;

    public ManagerConfrontation(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowConfrontationFromObject(Confrontation confrontation){
        Log.d("DB_LOCAL_INSERT_INSC", "Encuentro: "+confrontation);
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        // insertamos datos en la tabla competencia
        ContentValues registro = new ContentValues();
        registro.put("id", confrontation.getId());
        registro.put("jornada", confrontation.getJornada());
        registro.put("fase", confrontation.getFase());
        registro.put("turno", confrontation.getTurno());
        registro.put("grupo", confrontation.getGrupo());
        registro.put("rdo1", confrontation.getComp1());
        registro.put("rdo2", confrontation.getComp2());
        registro.put("fecha", confrontation.getFecha());
        registro.put("competencia", confrontation.getCompetencia());
        registro.put("juez", confrontation.getIdJuez());
        registro.put("campo", confrontation.getIdCampo());
        registro.put("competidor1", confrontation.getComp1());
        registro.put("competidor2", confrontation.getComp2());

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Encuentro");

        instanceDb.insert(DbContract.TABLE_ENCUENTRO, null, registro);

        instanceDb.close();
    }

    public Confrontation getObjectConfrontation(int idConfrontation){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_ENCUENTRO+" where id="+idConfrontation, null);
        Confrontation encuentro = null;
        if(filaCompUser.moveToFirst()){
            Log.d("DB_LOCAL_GET: ", filaCompUser.getString(1));
            // traemos los datos de la competencia
            encuentro = new Confrontation(
                    Integer.valueOf(filaCompUser.getString(0)),
                    filaCompUser.getString(1),
                    Integer.valueOf(filaCompUser.getString(2)),
                    Integer.valueOf(filaCompUser.getString(3)),
                    filaCompUser.getString(4),
                    Integer.valueOf(filaCompUser.getString(5)),
                            Integer.valueOf(filaCompUser.getString(6)),
                    filaCompUser.getString(7),
                    filaCompUser.getString(8),
                    filaCompUser.getString(9),
                    Integer.valueOf(filaCompUser.getString(10)),
                    filaCompUser.getString(11),
                    Integer.valueOf(filaCompUser.getString(12))
            );
        }
        Log.d("DB_LOCAL_READ", "Fecha del encuentro: "+encuentro.getFecha());

        return encuentro;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_ENCUENTRO, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de encuentros:"+cantRows);
        instanceDb.close();

        return cantRows;
    }
}
