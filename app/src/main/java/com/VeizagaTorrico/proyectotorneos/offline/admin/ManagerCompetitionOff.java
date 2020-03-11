package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitionOff;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerCompetitionOff {

    private DbHelper adminDB;

    public ManagerCompetitionOff(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowCompetitionFromObject(CompetitionOff competition){
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

    public CompetitionOff getObjectCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA+" where id="+idCompetition, null);
        CompetitionOff competition = null;
        if(filaCompUser.moveToFirst()){
            String rolesString = filaCompUser.getString(9);
            String[] roles = rolesString.split(" ");
            // traemos los datos de la competencia
            competition = new CompetitionOff(
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
            );
        }
        Log.d("DB_LOCAL_READ", "Competencia: "+competition.getNombre());

        return competition;
    }

    // devuelve una lista de las copetencias en las que se cuenta con el rol recibido
    public List<CompetitionMin> competitionByRol(String rol){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA, null);
        List<CompetitionMin> competencias = new ArrayList<>();

        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                // analizamos la lista de roles
                String rolesString = cursor.getString(9);
                Log.d("DB_LOCAL_COMP!", "Roles competencia "+rolesString);
                if(rolesString.contains(rol)){
                    String[] roles = rolesString.split(" ");
//                    CompetitionOff aux = new CompetitionOff(1,)
//                    CompetitionOff competitionAux = new CompetitionOff(
//                            Integer.valueOf(cursor.getString(0)),
//                            cursor.getString(1),
//                            cursor.getString(2),
//                            cursor.getString(3),
//                            cursor.getString(4),
//                            cursor.getString(5),
//                            cursor.getString(6),
//                            cursor.getString(7),
//                            cursor.getString(8),
//                            roles
//                    );
                    CompetitionMin competition = new CompetitionMin(
                            Integer.valueOf(cursor.getString(0)),
                            cursor.getString(1),
                            null,
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(5),
                            Arrays.asList(roles),
                            0
                            );

                    Log.d("DB_LOCAL_READ_!", "Nombre competencia: "+competition.getName());
                    competencias.add(competition);
                }

            } while (cursor.moveToNext());
        }
        Log.d("DB_LOCAL_READ", "Cant de competencias "+rol+" => "+competencias.size());

        return competencias;
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
