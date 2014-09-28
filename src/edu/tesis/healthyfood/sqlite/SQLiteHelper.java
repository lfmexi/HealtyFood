package edu.tesis.healthyfood.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String database="HealthyFoodDB";
	private static final int version = 1;
	
	public final String tabla = "Sesion";
	public final String id_sesion = "idSesion";
	public final String username = "username";
	public final String fecha_inicio = "fecha_inicio";
	public final String fecha_fin = "fecha_fin";
	
	private final String ddl ="CREATE TABLE "+tabla+" ( "
			+id_sesion+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
			username +" TEXT NOT NULL, "+
			fecha_inicio +" DATETIME NULL, "+
			fecha_fin+ " DATETIME NULL)";
	
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);	
		// TODO Auto-generated constructor stub
	}
	
	public SQLiteHelper(Context context) {		
		super( context,database, null,version );		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(ddl);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if ( newVersion > oldVersion )
		{
			//elimina tabla
			db.execSQL( "DROP TABLE IF EXISTS " + tabla );
			//y luego creamos la nueva tabla
			db.execSQL( ddl);
		}
	}

}
