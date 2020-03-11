package com.VeizagaTorrico.proyectotorneos.offline.setup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_COMPETENCIA =
            "CREATE TABLE IF NOT EXISTS "+DbContract.TABLE_COMPETENCIA+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "nombre TEXT, "+
                "categoria TEXT, "+
                "organizacion TEXT, "+
                "fecha_ini TEXT, "+
                "genero TEXT, "+
                    "ciudad TEXT, "+
                "frecuencia TEXT, "+
                "estado TEXT, "+
                "rol TEXT"+
                ")";

    private static final String CREATE_TABLE_INSCRIPCION =
            "CREATE TABLE IF NOT EXISTS "+DbContract.TABLE_INSCRIPCION+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "finicio TEXT, "+
                "fcierre TEXT, "+
                "monto INTEGER, "+
                "requisitos TEXT, "+
                "competencia INTEGER, "+
                "FOREIGN KEY(competencia) REFERENCES "+DbContract.TABLE_COMPETENCIA+"(competencia_id)"+
                ")";

    private static final String CREATE_TABLE_CAMPO =
            "CREATE TABLE IF NOT EXISTS "+DbContract.TABLE_CAMPO+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "nombre TEXT, "+
                    "predio TEXT, "+
                    "competencia INTEGER, "+
                    "FOREIGN KEY(competencia) REFERENCES "+DbContract.TABLE_COMPETENCIA+"(competencia_id)"+
                    ")";

    private static final String CREATE_TABLE_JUEZ =
            "CREATE TABLE IF NOT EXISTS "+DbContract.TABLE_JUEZ+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "nombre TEXT, "+
                    "apellido TEXT, "+
                    "dni TEXT, "+
                    "competencia INTEGER, "+
                    "FOREIGN KEY(competencia) REFERENCES "+DbContract.TABLE_COMPETENCIA+"(competencia_id)"+
                    ")";

    private static final String CREATE_TABLE_COMPETIDORES =
            "CREATE TABLE IF NOT EXISTS "+DbContract.TABLE_COMPETIDOR+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "alias TEXT, "+
                    "usuario TEXT, "+
                    "competencia INTEGER, "+
                    "FOREIGN KEY(competencia) REFERENCES "+DbContract.TABLE_COMPETENCIA+"(competencia_id)"+
                    ")";

    private static final String CREATE_TABLE_ENCUENTROS =
            "CREATE TABLE IF NOT EXISTS "+DbContract.TABLE_ENCUENTRO+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "grupo INTEGER, "+
                    "rdo1 INTEGER, "+
                    "rdo2 INTEGER, "+
                    "jornada INTEGER, "+
                    "fase INTEGER, "+
                    "juez INTEGER, "+
                    "campo INTEGER, "+
                    "turno TEXT, "+
                    "competidor1 TEXT, "+
                    "competidor2 TEXT, "+
                    "fecha TEXT, "+
                    "competencia INTEGER, "+
                    "FOREIGN KEY(competencia) REFERENCES "+DbContract.TABLE_COMPETENCIA+"(id), "+
                    "FOREIGN KEY(juez) REFERENCES "+DbContract.TABLE_JUEZ+"(id), "+
                    "FOREIGN KEY(campo) REFERENCES "+DbContract.TABLE_CAMPO+"(id)"+
                    ")";

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
        super(context, DbContract.DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creaos las tablas de la DB
        db.execSQL(CREATE_TABLE_COMPETENCIA);
        db.execSQL(CREATE_TABLE_INSCRIPCION);
        db.execSQL(CREATE_TABLE_CAMPO);
        db.execSQL(CREATE_TABLE_JUEZ);
        db.execSQL(CREATE_TABLE_COMPETIDORES);
        db.execSQL(CREATE_TABLE_ENCUENTROS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteRegister(SQLiteDatabase db){
        db.execSQL("DELETE FROM user");
    }
}
