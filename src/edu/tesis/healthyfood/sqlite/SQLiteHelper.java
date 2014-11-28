package edu.tesis.healthyfood.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String database="HealthyFoodDB";
	private static final int version = 14;
	
	public final String tabla = "Sesion";
	public final String id_sesion = "idSesion";
	public final String username = "username";
	public final String sex = "sex";
	public final String birth = "birth";
	public final String fecha_inicio = "fecha_inicio";
	public final String fecha_fin = "fecha_fin";
	
	public final String tablaMedicion = "Medicion";
	public final String peso = "peso";
	public final String altura = "altura";
	public final String fecha = "fecha";
	public final String imc = "imc";
	
	public final String tablaObjetivo = "Objetivo";
	public final String fecha_ob = "fecha_ob";
	public final String objetivo = "obj";
	
	public final String tablaTMB="TasaMetabolica";
	public final String id_tmb="id";
	public final String fecha_tmb = "fecha_tmb";
	public final String tmb = "tmb";
	
	public final String tablaDiario="Diario";
	public final String fecha_Diario = "fecha_diario";
	public final String hora_Diario = "hora_diario";
	public final String minuto_diario= "minuto_diario";
	public final String segundo_diario = "segundo_diario";
	public final String receta="receta";
	public final String calorias="calorias";
	public final String idDiario = "idDiario";
	
	private final String ddl ="CREATE TABLE "+tabla+" ( "
			+id_sesion+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
			username +" TEXT NOT NULL, "+
			sex +" TEXT NOT NULL, "+
			birth+" TEXT NOT NULL, "+
			fecha_inicio +" DATETIME NULL, "+
			fecha_fin+ " DATETIME NULL)";
	
	private final String ddlMedicion ="CREATE TABLE "+tablaMedicion+" ( "
			+username +" TEXT NOT NULL, "+
			fecha +" DATETIME NOT NULL, "+
			peso+" REAL NOT NULL, "+
			altura+" REAL NOT NULL, "+
			imc+" REAL NOT NULL)";
	

	private final String ddlObjetivo="CREATE TABLE "+tablaObjetivo+" ( "
			+username+ " TEXT NOT NULL, "+
			fecha_ob+" DATETIME NOT NULL, "+
			objetivo+" TEXT NOT NULL)";
	
	private final String ddlTMB = "CREATE TABLE "+tablaTMB+" ( "
			+id_tmb+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
			username+" TEXT NOT NULL, "+
			fecha_tmb+" DATE NOT NULL, "+
			tmb+" REAL NOT NULL)";
	
	private final String ddlDiario = "CREATE TABLE "+tablaDiario+" ( "
			+idDiario+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
			username+" TEXT NOT NULL, "+
			fecha_Diario+" DATE NOT NULL, "+
			hora_Diario+" INTEGER NOT NULL, "+
			minuto_diario+" INTEGER NOT NULL, "+
			segundo_diario+" INTEGER NOT NULL, "+
			receta+" TEXT NOT NULL, "+
			calorias+" REAL NOT NULL)";
	
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
		db.execSQL(ddlMedicion);
		db.execSQL(ddlObjetivo);
		db.execSQL(ddlTMB);
		db.execSQL(ddlDiario);
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
			

			//elimina tabla
			db.execSQL( "DROP TABLE IF EXISTS " + tablaMedicion );
			//y luego creamos la nueva tabla
			db.execSQL( ddlMedicion);
			

			//elimina tabla
			db.execSQL( "DROP TABLE IF EXISTS " + tablaObjetivo );
			//y luego creamos la nueva tabla
			db.execSQL( ddlObjetivo);
			

			//elimina tabla
			db.execSQL( "DROP TABLE IF EXISTS " + tablaTMB );
			//y luego creamos la nueva tabla
			db.execSQL( ddlTMB);
			
			//elimina tabla
			db.execSQL( "DROP TABLE IF EXISTS " + tablaDiario );
			//y luego creamos la nueva tabla
			db.execSQL( ddlDiario);
		}
	}

}
