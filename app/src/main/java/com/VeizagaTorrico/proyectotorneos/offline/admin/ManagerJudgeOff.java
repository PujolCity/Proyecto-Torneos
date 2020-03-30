package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.offline.model.JudgeOff;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

import java.util.Arrays;
import java.util.List;

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

        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_JUEZ+" where id="+judge.getId(), null);
        // si existe el campo lo actualizamos con los nuevos valores
        if(cursor.getCount() > 0){
            // actualizamos el campo
            ContentValues camposUpdate = new ContentValues();
            camposUpdate.put("nombre", judge.getNombre());
            camposUpdate.put("apellido", judge.getApellido());
            camposUpdate.put("dni", judge.getDni());
            instanceDb.update(DbContract.TABLE_JUEZ, camposUpdate, "id="+judge.getId(),null);
            Log.d("DB_LOCAL_JUEZ", "Actualiza un registro en Juez");
        }
        else{
            Log.d("DB_LOCAL_JUEZ", "Agrega un registro en Juez");
            instanceDb.insert(DbContract.TABLE_JUEZ, null, registro);
        }

        // actualizamos los ids de campos de la competencia
        updateJudgesOfCompetition(instanceDb, judge.getCompetencia(), judge.getId());

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

    // actualiza la lista de campos de la competencia
    private void updateJudgesOfCompetition(SQLiteDatabase instanceDb, int idCompetition, int idJudge){

        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA+" where id="+idCompetition, null);

        if(cursor.moveToFirst()){
            String idJueces = cursor.getString(10);
            // traemos los datos de la competencia
            if(!existJudge(idJueces, idJudge)){
                // agregamos el id a la lista de la competencia y persistimos el cambio
                idJueces += " "+idJudge;
                ContentValues newValues = new ContentValues();
                newValues.put("jueces", idJueces);
                instanceDb.update(DbContract.TABLE_COMPETENCIA, newValues, "id="+idCompetition,null);
                Log.d("UPD_LOCAL_DB", "Id jueces de competencia actualizada: "+idCompetition);
            }
        }

        instanceDb.close();

        return;
    }

    // determina si el 2do parametro se encuentra en el string recibido
    private boolean existJudge(String idJueces, int idJudge){
        String[] arrayJuecesId = idJueces.split("\\s+");
        // vemos si el campo ya se encuentra en la lista de los campos de la competencia
        List<String> listJuecesId = Arrays.asList(arrayJuecesId);
        if(listJuecesId.contains(String.valueOf(idJudge))){
            return true;
        }
        return false;
    }

    // elimina los jueces de una compoetencia
//    public void deleteByCompetition(int idCompetition){
//        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();
//        // recuperamos los competidores de la competencia
//        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_JUEZ+" where competencia="+idCompetition, null);
//        if(cursor != null && cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            do {
//                String idJuez = cursor.getString(0);
//                if(!haveForeignKey(instanceDb, Integer.valueOf(idJuez), idCompetition)){
//                    instanceDb.delete(DbContract.TABLE_JUEZ, "id="+idJuez, null);
//                }
//            } while (cursor.moveToNext());
//        }
//
//        Log.d("ROWS_DEL_DB", "Cant de jueces eliminados: "+cursor.getCount());
//        instanceDb.close();
//
//        return;
//    }

//    private boolean haveForeignKey(SQLiteDatabase instanceDb, int idJuez, int idCompetition){
//        // recuperamos las filas con el juez y sin la competencia indicada
//        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_JUEZ+
//                        " where id=" + idJuez +
//                        " AND competencia!=" + idCompetition,
//                null);
//
//        Log.d("ROWS_DEL_DB", "Cant de jueces con FK: "+cursor.getCount());
//
//        if(cursor.getCount() > 0){
//            return true;
//        }
//
//        return false;
//    }
}
