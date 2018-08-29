package com.worldskills.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    private static final String NOMBRE="registros.db";
    private static final int VERSION=1;
    private static final String TABLA_JUEGO="CREATE TABLE PARTIDA(ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT, PUNTAJE TEXT)";

    DataBase (Context context){
        super(context,NOMBRE,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_JUEGO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CREATE"+TABLA_JUEGO);
        db.execSQL(TABLA_JUEGO);
    }
    /*Metodo para guardar los campor que este reciba*/
    public void save(String nombre, int puntaje){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues valores=new ContentValues();

        valores.put("NOMBRE",nombre);
        valores.put("PUNTAJE",puntaje);

        db.insert("PARTIDA",null,valores);
    }

    /*Metodo que devuelve un cursor con 4 posiciones para los puntaje*/
    public Cursor load(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=null;

        try{
            String[] colunms={"NOMBRE","PUNTAJE"};
            String limit="4";
            String orderBy="PUNTAJE DESC";

            cursor=db.query("PARTIDA",colunms,null,null,null,orderBy,limit);

        }catch (Exception e){}

        return cursor;
    }
}
