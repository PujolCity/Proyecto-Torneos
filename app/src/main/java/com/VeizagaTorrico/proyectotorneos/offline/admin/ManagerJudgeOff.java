package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.offline.model.JudgeOff;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

public class ManagerJudgeOff {

    private DbHelper adminDB;

    public ManagerJudgeOff(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowJudgeFromObject(JudgeOff judge){
        Log.d("DB_LOCAL_INSERT_INSC", "Juez: "+judge);
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        // insertamos datos en la tabla competencia
        ContentValues registro = new ContentValues();
        registro.put("id", judge.getId());
        registro.put("nombre", judge.getNombre());
        registro.put("apellido", judge.getApellido());
        registro.put("dni", judge.getDni());
        registro.put("competencia", judge.getCompetencia());

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Juez");

        instanceDb.insert(DbContract.TABLE_JUEZ, null, registro);

        instanceDb.close();
    }

    public JudgeOff getObjectJudge(int idJudge){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_JUEZ+" where id="+idJudge, null);
        JudgeOff juez = null;
        if(filaCompUser.moveToFirst()){
            Log.d("DB_LOCAL_GET: ", filaCompUser.getString(1));
            // traemos los datos de la competencia
            juez = new JudgeOff(
                    Integer.valueOf(filaCompUser.getString(0)),
                    filaCompUser.getString(1),
                    filaCompUser.getString(2),
                    filaCompUser.getString(3),
                    Integer.valueOf(filaCompUser.getString(3))
            );
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Juez: "+juez.getNombre());

        return juez;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_JUEZ, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de jueces:"+cantRows);
        instanceDb.close();

        return cantRows;
    }

    public void deleteByCompetition(int idCompetition){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();
        // recuperamos los competidores de la competencia
        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_JUEZ+" where competencia="+idCompetition, null);
        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                String idJuez = cursor.getString(0);
                if(!haveForeignKey(instanceDb, Integer.valueOf(idJuez), idCompetition)){
                    instanceDb.delete(DbContract.TABLE_JUEZ, "id="+idJuez, null);
                }
            } while (cursor.moveToNext());
        }

        Log.d("ROWS_DEL_DB", "Cant de jueces eliminados: "+cursor.getCount());
        instanceDb.close();

        return;
    }

    private boolean haveForeignKey(SQLiteDatabase instanceDb, int idJuez, int idCompetition){
        // recuperamos los competidores de la competencia
        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_JUEZ+
                        " where id=" + idJuez +
                        " AND competencia!=" + idCompetition,
                null);

        Log.d("ROWS_DEL_DB", "Cant de jueces con FK: "+cursor.getCount());

        if(cursor.getCount() > 0){
            return true;
        }

        return false;
    }
}
