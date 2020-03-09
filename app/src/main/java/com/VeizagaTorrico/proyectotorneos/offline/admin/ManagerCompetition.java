package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.offline.model.Competition;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

public class ManagerCompetition {

    private DbHelper adminDB;

    public ManagerCompetition(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowCompetitionFromObject(Competition competition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        String[] rolesComp = competition.getRol();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < rolesComp.length; i++) {
            stringBuilder.append(rolesComp[i]);
        }
        String rolesString = stringBuilder.toString();

        // insertamos datos en la tabla competencia
        ContentValues registroComp = new ContentValues();
        registroComp.put("id", competition.getId());
        registroComp.put("nombre", competition.getNombre());
        registroComp.put("categoria", competition.getCategoria());
        registroComp.put("organizacion", competition.getOrganizacion());
        registroComp.put("fecha_ini", competition.getFecha_ini());
        registroComp.put("genero", competition.getGenero());
        registroComp.put("ciudad", competition.getCiudad());
        registroComp.put("frecuencia", competition.getFrecuencia());
        registroComp.put("estado", competition.getEstado());
//        registroComp.put("rol", competition.getRol());
        registroComp.put("rol", rolesString);

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Competencia");

        instanceDb.insert(DbContract.TABLE_COMPETENCIA, null, registroComp);

        instanceDb.close();
    }

    public Competition getObjectCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA+" where id="+idCompetition, null);
        Competition competition = null;
        if(filaCompUser.moveToFirst()){
            String rolesString = filaCompUser.getString(8);
            String[] roles = rolesString.split(" ");
            // traemos los datos de la competencia
            competition = new Competition(
                    Integer.valueOf(filaCompUser.getString(0)),
                    filaCompUser.getString(1),
                    filaCompUser.getString(2),
                    filaCompUser.getString(3),
                    filaCompUser.getString(4),
                    filaCompUser.getString(5),
                    filaCompUser.getString(6),
                    filaCompUser.getString(7),
                    filaCompUser.getString(8),
                    roles
//                    filaCompUser.getString(8)
            );
        }
        Log.d("DB_LOCAL_READ", "Competencia: "+competition.getNombre());

        return competition;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de competencias:"+cantRows);
        instanceDb.close();

        return cantRows;
    }
}
