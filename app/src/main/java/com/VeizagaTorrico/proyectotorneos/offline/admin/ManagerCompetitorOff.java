package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;
import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitorOff;

import java.util.ArrayList;
import java.util.List;

public class ManagerCompetitorOff {

    private DbHelper adminDB;

    public ManagerCompetitorOff(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowCompetitorFromObject(CompetitorOff competitor){
        Log.d("DB_LOCAL_INSERT_INSC", "Competidor: "+competitor);
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        // insertamos datos en la tabla competencia
        ContentValues registro = new ContentValues();
        registro.put("id", competitor.getId());
        registro.put("alias", competitor.getAlias());
        registro.put("usuario", competitor.getUsuario());
        registro.put("nombre", competitor.getNombre());
        registro.put("apellido", competitor.getApellido());
        registro.put("correo", competitor.getCorreo());
        registro.put("competencia", competitor.getCompetencia());

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Competidor");

        instanceDb.insert(DbContract.TABLE_COMPETIDOR, null, registro);

        instanceDb.close();
    }

    public CompetitorOff getObjectCompetitor(int idCompetitor){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETIDOR+" where id="+idCompetitor, null);
        CompetitorOff competitor = null;
        if(filaCompUser.moveToFirst()){
            Log.d("DB_LOCAL_GET: ", filaCompUser.getString(1));
            // traemos los datos de la competencia
            competitor = new CompetitorOff(
                    Integer.valueOf(filaCompUser.getString(0)),
                    filaCompUser.getString(1),
                    filaCompUser.getString(2),
                    filaCompUser.getString(3),
                    filaCompUser.getString(4),
                    filaCompUser.getString(5),
                    Integer.valueOf(filaCompUser.getString(6))
            );
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Competitor: "+competitor.getAlias());

        return competitor;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETIDOR, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de competidores:"+cantRows);
        instanceDb.close();

        return cantRows;
    }

    public void deleteByCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();
        // recuperamos los competidores de la competencia
        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETIDOR+" where competencia="+idCompetition, null);
        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                String idCompetidor = cursor.getString(0);
                instanceDb.delete(DbContract.TABLE_COMPETIDOR, "id="+idCompetidor, null);
            } while (cursor.moveToNext());
        }

        Log.d("ROWS_DEL_DB", "Cant de competidores eliminados: "+cursor.getCount());
        instanceDb.close();

        return;
    }

    // recupera los competidores de una competencia
    public List<User> getCompetitorsByCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETIDOR+" where competencia="+idCompetition, null);
        List<User> competitors = new ArrayList<>();

        if(filaCompUser != null && filaCompUser.getCount() != 0) {
            filaCompUser.moveToFirst();
            Log.d("DB_LOCAL_GET: ", "Alias comp DB local: "+filaCompUser.getString(1));
            do {
                // traemos los datos de la competencia
                User competitor = new User(
                        Integer.valueOf(filaCompUser.getString(0)),
                        filaCompUser.getString(2),
                        filaCompUser.getString(3),
                        filaCompUser.getString(4),
                        filaCompUser.getString(1),
                        filaCompUser.getString(5),
                        ""
                );
                competitors.add(competitor);
            } while (filaCompUser.moveToNext());
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Cant competitores de la competencia: "+competitors.size());

        return competitors;
    }
}
