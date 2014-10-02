package edu.tesis.healthyfood.sqlite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLite {
	
	private SQLiteHelper sqlh;
	private SQLiteDatabase db;
	
	public SQLite(Context cont){
		sqlh = new SQLiteHelper(cont);
	}

	public void abrir(){
		Log.i("SQLite", "Se abre conexion a la base de datos " + sqlh.getDatabaseName() );
		db = sqlh.getWritableDatabase(); 		
	}
	
	public void cerrar()
	{
		Log.i("SQLite", "Se cierra conexion a la base de datos " + sqlh.getDatabaseName() );
		sqlh.close();		
	}
	
	public boolean addReg(String username){
		if(username!=null){
			ContentValues cv = new ContentValues();
			cv.put(sqlh.username, username);
			cv.put(sqlh.fecha_inicio, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			return(db.insert(sqlh.tabla, null, cv)!=-1)?true:false;
		}
		return false;
	}
	
	public Sesion getLastSesion(){
		Sesion ses=null;
		Cursor cursor = db.query(sqlh.tabla,
				new String[]{sqlh.id_sesion,sqlh.username,sqlh.fecha_inicio,sqlh.fecha_fin},
							null,null,null,null,
							sqlh.id_sesion+" DESC ","1");
		if(cursor.moveToFirst()){
			do{
				ses = new Sesion(null,null,null);
				ses.setId(cursor.getInt(0));
				ses.setUser(cursor.getString(1));
				if(cursor.getString(2)!=null){
					try {
						ses.setFecha_inicio(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(1)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(cursor.getString(3)!=null){
					try {
						ses.setFecha_inicio(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(2)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}while(cursor.moveToNext());
		}
		return ses;
	}
	
	public boolean deleteSesion(int id){
		return (db.delete(sqlh.tabla, sqlh.id_sesion+"="+id, null)>0)?true:false;
	}
}
