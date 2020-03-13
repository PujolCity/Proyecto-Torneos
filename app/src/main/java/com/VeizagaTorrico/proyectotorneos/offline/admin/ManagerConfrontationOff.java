package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerConfrontationOff {

    private DbHelper adminDB;

    public ManagerConfrontationOff(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowConfrontationFromObject(ConfrontationOff confrontation){
        Log.d("DB_LOCAL_INSERT_INSC", "Encuentro: "+confrontation);
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        // insertamos datos en la tabla competencia
        ContentValues registro = new ContentValues();
        registro.put("id", confrontation.getId());
        registro.put("grupo", confrontation.getGrupo());
        registro.put("rdo1", confrontation.getRdo1());
        registro.put("rdo2", confrontation.getRdo2());
        registro.put("jornada", confrontation.getJornada());
        registro.put("fase", confrontation.getFase());
        registro.put("juez", confrontation.getIdJuez());
        registro.put("campo", confrontation.getIdCampo());
        registro.put("turno", confrontation.getTurno());
        registro.put("competidor1", confrontation.getComp1());
        registro.put("competidor2", confrontation.getComp2());
        registro.put("fecha", confrontation.getFecha());
        registro.put("competencia", confrontation.getCompetencia());

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Encuentro");
//        Log.d("DB_LOCAL_INSERT", "Datos:"+registro);

        instanceDb.insert(DbContract.TABLE_ENCUENTRO, null, registro);

        instanceDb.close();
    }

    public ConfrontationOff getObjectConfrontation(int idConfrontation){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_ENCUENTRO+" where id="+idConfrontation, null);
        ConfrontationOff encuentro = null;
        if(cursor.moveToFirst()){
            Log.d("DB_LOCAL_GET: ", cursor.getString(1));
            // traemos los datos de la competencia
            encuentro = new ConfrontationOff(
                    Integer.valueOf(cursor.getString(0)),
                    Integer.valueOf(cursor.getString(1)),
                    Integer.valueOf(cursor.getString(2)),
                    Integer.valueOf(cursor.getString(3)),
                    Integer.valueOf(cursor.getString(4)),
                    Integer.valueOf(cursor.getString(5)),
                    Integer.valueOf(cursor.getString(6)),
                    Integer.valueOf(cursor.getString(7)),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    Integer.valueOf(cursor.getString(12))
            );
        }
        Log.d("DB_LOCAL_READ", "Fecha del encuentro: "+encuentro.getFecha());
        instanceDb.close();

        return encuentro;
    }

    // recupera los encuentros almacenados localmente de la competencia recibida
    public List<Confrontation> confrontationByCompetition(int idCompeition, Map<String,String> fecha_grupo){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        String jornada = fecha_grupo.get("fase");
        String grupo = fecha_grupo.get("grupo");

        String queryBase = "select * from "+ DbContract.TABLE_ENCUENTRO+" where competencia="+idCompeition;
        if(jornada != null){
            // agregamos la condicion a la query
            queryBase += " AND jornada="+jornada;
        }
        if(grupo != null){
            // agregamos la condicion a la query
            queryBase += " AND grupo="+grupo;
        }

//        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_ENCUENTRO+" where competencia="+idCompeition, null);
        Cursor cursor = instanceDb.rawQuery(queryBase, null);
        List<Confrontation> encuentros = new ArrayList<>();

        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                Confrontation encuentro = new Confrontation(
                        Integer.valueOf(cursor.getString(0)),
                        cursor.getString(9),
                        cursor.getString(10),
                        Integer.valueOf(cursor.getString(2)),
                        Integer.valueOf(cursor.getString(3)),
                        null, null, null);
                Log.d("DB_LOCAL_READ_!", "Comp1: "+encuentro.getCompetidor1());
                encuentros.add(encuentro);
            } while (cursor.moveToNext());
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Cant de encuentros recuperados: "+encuentros.size());

        return encuentros;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_ENCUENTRO, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de encuentros:"+cantRows);
        instanceDb.close();

        return cantRows;
    }

    public void deleteByCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();
        // recuperamos los competidores de la competencia
        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_ENCUENTRO+" where competencia="+idCompetition, null);
        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                String idEncuentro = cursor.getString(0);
                instanceDb.delete(DbContract.TABLE_ENCUENTRO, "id="+idEncuentro, null);
            } while (cursor.moveToNext());
        }

        Log.d("ROWS_DEL_DB", "Cant de encuentros eliminados: "+cursor.getCount());
        instanceDb.close();

        return;
    }

    public void updateByCompetition(int idConfrontation, int idCompetition, int rdo1, int rdo2){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rdo1", rdo1);
        contentValues.put("rdo2", rdo2);
        instanceDb.update(DbContract.TABLE_ENCUENTRO,contentValues, "id="+idConfrontation +" and competencia="+idCompetition,null);

        instanceDb.close();
    }
}
