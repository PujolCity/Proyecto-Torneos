package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.offline.model.FieldOff;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

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
        registro.put("competencia", field.getCompetencia());

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Campo");

        instanceDb.insert(DbContract.TABLE_CAMPO, null, registro);

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
}
