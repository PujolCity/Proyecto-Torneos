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

    public void addRowCompetitionFromObject(CompetitionOff competition, String[] fasesCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        // pasamos los roles a un string para poder persistirlo
        String[] rolesComp = competition.getRol();
        String rolesString = "";
        for (int i = 0; i < rolesComp.length; i++) {
            rolesString += rolesComp[i]+" ";
        }

        // pasamos las fases a un string para poder persistirlo
        String fasesString = "";
        for (int i = 0; i < fasesCompetition.length; i++) {
            fasesString += fasesCompetition[i]+" ";
        }

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
        registroComp.put("rol", rolesString);
        registroComp.put("campos", "");
        registroComp.put("fases", fasesString);
        registroComp.put("fase_actual", competition.getFase_actual());
        registroComp.put("jueces", "");

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Competencia");
        Log.d("DB_LOCAL_INSERT", "Fases guardadas: "+fasesString);

        instanceDb.insert(DbContract.TABLE_COMPETENCIA, null, registroComp);
        // instanceDb.update(DbContract.TABLE_COMPETENCIA, registroComp,"id=" + id, null);

        instanceDb.close();
    }

    public CompetitionOff getObjectCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA+" where id="+idCompetition, null);
        CompetitionOff competition = null;
        if(filaCompUser.moveToFirst()){
            String rolesString = filaCompUser.getString(9);
            String[] roles = rolesString.split("\\s+");
            String fasesString = filaCompUser.getString(11);
            String[] fases = fasesString.split("\\s+");
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
                    roles,
                    fases
            );
        }
        instanceDb.close();
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
                Log.d("DB_LOCAL_COMP!", "Roles competencia leidos local:  "+rolesString);
                if(rolesString.contains(rol)){
                    //String[] roles = rolesString.split(" ");
                    String[] roles = rolesString.split("\\s+");
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
                    competition.setFaseActual(cursor.getString(11));

                    Log.d("DB_LOCAL_READ_!", "Nombre competencia: "+competition.getName());
                    competencias.add(competition);
                }

            } while (cursor.moveToNext());
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Cant de competencias "+rol+" => "+competencias.size());

        return competencias;
    }

    // devuelve una lista de las copetencias en las que se cuenta con el rol recibido
    public int cantGroupByCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor cursor = instanceDb.rawQuery("select max(grupo) as grupo from "+ DbContract.TABLE_ENCUENTRO+" where competencia="+idCompetition, null);

        int cantGrupos = -1;

        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            // traemos la cant de grupos de la competencia
            cantGrupos = Integer.valueOf(cursor.getString(0));
        }

        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Cant de grupos de la competencia "+cantGrupos);

        return cantGrupos;
    }

    // devuelve una lista de las copetencias en las que se cuenta con el rol recibido
    public int cantJornadaByCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor cursor = instanceDb.rawQuery("select MAX(jornada) from "+ DbContract.TABLE_ENCUENTRO+" where competencia="+idCompetition, null);

        int cantJornadas = -1;
        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            // traemos la cant de jornadas de la competencia
            cantJornadas = Integer.valueOf(cursor.getString(0));
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Cant de jornadas de la competencia "+cantJornadas);

        return cantJornadas;
    }

    // devuelve una lista de las copetencias en las que se cuenta con el rol recibido
    public String[] phasesByCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor cursor = instanceDb.rawQuery("select fases from "+ DbContract.TABLE_COMPETENCIA+" where id="+idCompetition, null);


        String[] fases = {};

        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            // traemos las fases de la competencia
            String fasesStringDb = cursor.getString(0);
            Log.d("DB_LOCAL_READ", "Fases string leidas "+fasesStringDb);
            fases = fasesStringDb.split("\\s+");
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Cant d fases de la competencia "+fases.length);

        return fases;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de competencias:"+cantRows);
        instanceDb.close();

        return cantRows;
    }

    public boolean existCompetition(int idcompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA +" where id="+idcompetition, null);
        int cantRows = cursor.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de competencias encontradas:"+cantRows);
        instanceDb.close();

        if(cantRows > 0){
            return true;
        }
        return false;
    }

    public void deleteCompetition(int idcompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();
        instanceDb.delete(DbContract.TABLE_COMPETENCIA, "id="+idcompetition, null);
        Log.d("ROWS_DEL_DB", "Competencia eliminada: "+idcompetition);
        instanceDb.close();

        return;
    }
}
