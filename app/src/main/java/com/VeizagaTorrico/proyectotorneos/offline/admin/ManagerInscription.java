package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.offline.model.Inscription;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbContract;
import com.VeizagaTorrico.proyectotorneos.offline.setup.DbHelper;

public class ManagerInscription {

    private DbHelper adminDB;

    public ManagerInscription(Context context) {
        adminDB = new DbHelper(context, null, null, 1);
    }

    public void addRowInscriptionFromObject(Inscription inscripcion){
        Log.d("DB_LOCAL_INSERT_INSC", "Inscripcion: "+inscripcion);
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        // insertamos datos en la tabla competencia
        ContentValues registro = new ContentValues();
        registro.put("id", inscripcion.getId());
        registro.put("finicio", inscripcion.getFinicio());
        registro.put("fcierre", inscripcion.getFcierre());
        registro.put("monto", inscripcion.getMonto());
        registro.put("requisitos", inscripcion.getRequisitos());
        registro.put("competencia", inscripcion.getCompetencia());
//        registro.put("competencia", inscripcion.getCompetencia().getId());

        Log.d("DB_LOCAL_INSERT", "Agrega un registro en Inscripcion");

        instanceDb.insert(DbContract.TABLE_INSCRIPCION, null, registro);

        instanceDb.close();
    }

    public Inscription getObjectInscription(int idInscription){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaCompUser = instanceDb.rawQuery("select * from "+ DbContract.TABLE_INSCRIPCION+" where id="+idInscription, null);
        Inscription inscripcion = null;
        if(filaCompUser.moveToFirst()){
            Log.d("DB_LOCAL_GET: ", filaCompUser.getString(5));
            // traemos los datos de la competencia
            inscripcion = new Inscription(
                    Integer.valueOf(filaCompUser.getString(0)),
                    filaCompUser.getString(1),
                    filaCompUser.getString(2),
                    Integer.valueOf(filaCompUser.getString(3)),
                    filaCompUser.getString(4),
//                    new Competition()
                    Integer.valueOf(filaCompUser.getString(5))
            );
        }
        Log.d("DB_LOCAL_READ", "Competencia: "+inscripcion.getFinicio());

        return inscripcion;
    }

    public int getCantRows(){
        SQLiteDatabase instanceDb = adminDB.getWritableDatabase();

        Cursor filaComp = instanceDb.rawQuery("select * from "+ DbContract.TABLE_INSCRIPCION, null);
        int cantRows = filaComp.getCount();
        Log.d("ROWS_LOCAL_DB", "Cant de inscripciones:"+cantRows);
        instanceDb.close();

        return cantRows;
    }
}
