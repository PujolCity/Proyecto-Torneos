package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.offline.model.FieldOff;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

import java.util.Arrays;
import java.util.List;

public class ManagerFieldOff {

    private DbHelper adminDB;

    public ManagerFieldOff(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowFieldFromObject(FieldOff field){
        Log.d("DB_LOCAL_INSERT_INSC", "Campo: "+field);
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        // insertamos datos en la tabla competencia
        ContentValues registro = new ContentValues();
        registro.put("id", field.getId());
        registro.put("nombre", field.getNombre());
        registro.put("predio", field.getPredio());

        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_CAMPO+" where id="+field.getId(), null);
        // si existe el campo lo actualizamos con los nuevos valores
        if(cursor.getCount() > 0){
            // actualizamos el campo
            ContentValues camposUpdate = new ContentValues();
            camposUpdate.put("nombre", field.getNombre());
            camposUpdate.put("predio", field.getPredio());
            instanceDb.update(DbContract.TABLE_CAMPO, camposUpdate, "id="+field.getId(),null);
            Log.d("DB_LOCAL_INSERT", "Actualiza un registro en Campo");
        }
        else{
            instanceDb.insert(DbContract.TABLE_CAMPO, null, registro);
            Log.d("DB_LOCAL_INSERT", "Agrega un registro en Campo");
        }

        // actualizamos los ids de campos de la competencia
        updateFieldsOfCompetetion(instanceDb, field.getCompetencia(), field.getId());

        instanceDb.close();
    }

    public FieldOff getObjectField(int idField){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_CAMPO+" where id="+idField, null);
        FieldOff campo = null;
        if(filaCompUser.moveToFirst()){
            Log.d("DB_LOCAL_GET: ", filaCompUser.getString(1));
            // traemos los datos de la competencia
            campo = new FieldOff(
                    Integer.valueOf(filaCompUser.getString(0)),
                    filaCompUser.getString(1),
                    filaCompUser.getString(2),
                    Integer.valueOf(filaCompUser.getString(3))
            );
        }
        instanceDb.close();
        Log.d("DB_LOCAL_READ", "Campo: "+campo.getNombre());

        return campo;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_CAMPO, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de campos:"+cantRows);
        instanceDb.close();

        return cantRows;
    }

    // recupera el juez del encuentro
    public Field getFieldConfrontation(int idEncuentro){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor cursor = instanceDb.rawQuery("select campo.id, campo.nombre, campo.predio"+
                        " from "+ DbContract.TABLE_CAMPO +
                        " INNER JOIN "+DbContract.TABLE_ENCUENTRO+" ON "+ DbContract.TABLE_CAMPO+".id = "+DbContract.TABLE_ENCUENTRO+".campo" +
                        " WHERE "+ DbContract.TABLE_ENCUENTRO+".id ="+idEncuentro,
                null);
        Field campo = null;
        if(cursor.moveToFirst()){
            Log.d("DB_LOCAL_ENCCAMPO: ", cursor.getString(1));
            // traemos los datos de la competencia
            campo = new Field(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    -1, -1, null
            );
        }
        Log.d("CAMPO_LOCAL_DB", "Nombre campo del encuentro:"+campo.getNombre());
        instanceDb.close();

        return campo;
    }

    // actualiza la lista de campos de la competencia
    private void updateFieldsOfCompetetion(SQLiteDatabase instanceDb, int idCompetition, int idField){

        Cursor cursor = instanceDb.rawQuery("select * from "+ DbContract.TABLE_COMPETENCIA+" where id="+idCompetition, null);

        if(cursor.moveToFirst()){
            String idCampos = cursor.getString(10);
            // traemos los datos de la competencia
            if(!existField(idCampos, idField)){
                // agregamos el id a la lista de la competencia y persistimos el cambio
                idCampos += " "+idField;
                ContentValues newValues = new ContentValues();
                newValues.put("campos", idCampos);
                instanceDb.update(DbContract.TABLE_COMPETENCIA, newValues, "id="+idCompetition,null);
                Log.d("UPD_LOCAL_DB", "Id campos de competencia actualizada: "+idCompetition);
            }
        }

        instanceDb.close();

        return;
    }

    // determina si el 2do parametro se encuentra en el string recibido
    private boolean existField(String idCampos, int idField){
        String[] arrayCamposId = idCampos.split("\\s+");
        // vemos si el campo ya se encuentra en la lista de los campos de la competencia
        List<String> listCamposId = Arrays.asList(arrayCamposId);
        if(listCamposId.contains(String.valueOf(idField))){
            return true;
        }
        return false;
    }
}
