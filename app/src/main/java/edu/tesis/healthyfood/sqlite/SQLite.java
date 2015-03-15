package edu.tesis.healthyfood.sqlite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	public boolean addDiario(String usuario,String receta, double calorias){
		if(usuario!=null){
			ContentValues cv=new ContentValues();
			cv.put(sqlh.username, usuario);
			cv.put(sqlh.receta, receta);
			cv.put(sqlh.calorias, calorias);
			cv.put(sqlh.fecha_Diario, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			Calendar c=Calendar.getInstance();
			int hora=c.get(Calendar.HOUR_OF_DAY);
			int minuto=c.get(Calendar.MINUTE);
			int segundo=c.get(Calendar.SECOND);
			cv.put(sqlh.hora_Diario, hora);
			cv.put(sqlh.minuto_diario, minuto);
			cv.put(sqlh.segundo_diario, segundo);
			return(db.insert(sqlh.tablaDiario, null, cv)!=-1)?true:false;
		}
		return false;
	}
	
	public ArrayList<EntradaDiario> getEntradasDia(String user,Date d){
		ArrayList<EntradaDiario> lista=null;
		if(user!=null){
			Cursor cursor = db.query(sqlh.tablaDiario,
					new String[]{sqlh.receta,sqlh.calorias,sqlh.fecha_Diario,sqlh.hora_Diario,sqlh.minuto_diario,sqlh.segundo_diario},
						sqlh.username+"=? AND "+sqlh.fecha_Diario+" LIKE ? ",
						new String[]{user,"%"+new SimpleDateFormat("yyyy-MM-dd").format(d)+"%"},
						null,null,
						sqlh.idDiario+" ASC ");
			if(cursor.moveToFirst()){
				lista = new ArrayList<EntradaDiario>();
				do{
					try{
						String rec = cursor.getString(0);
						double cal = cursor.getDouble(1);
						String date=cursor.getString(2);
						Date fecha =new SimpleDateFormat("yyyy-MM-dd").parse(date);
						int hora = cursor.getInt(3);
						int minuto = cursor.getInt(4);
						int segundo = cursor.getInt(5);
						
						EntradaDiario ed = new EntradaDiario();
						ed.receta=rec;
						ed.calorias=cal;
						ed.fecha=fecha;
						ed.hora=hora;
						ed.minuto=minuto;
						ed.segundo=segundo;
						ed.usuario=user;
						
						lista.add(ed);
					}catch(NumberFormatException nfe){
						nfe.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(cursor.moveToNext());
			}
		}
		return lista;
	}
	
	public boolean addTMB(String username,double tmb){
		if(username!=null){
			ContentValues cv = new ContentValues();
			cv.put(sqlh.username, username);
			cv.put(sqlh.tmb, tmb);
			cv.put(sqlh.fecha_tmb, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			return(db.insert(sqlh.tablaTMB, null, cv)!=-1)?true:false;
		}
		return false;
	}
	
	public TMB getLastTMB(String username){
		TMB t = null;
		if(username!=null){
			Cursor cursor = db.query(sqlh.tablaTMB,
				new String[]{sqlh.tmb,sqlh.fecha_tmb},
					sqlh.username+"=?",
					new String[]{username},
					null,null,
					sqlh.id_tmb+" DESC ","1");
			if(cursor.moveToFirst()){
				do{
					t=new TMB();
					try{
						t.value = Double.parseDouble(cursor.getString(0));
						t.fecha_tomado=cursor.getString(1);
						//return t;
					}catch(NumberFormatException nfe){
						return null;
					}
				}while(cursor.moveToNext());
			}
		}
		return t;
	}
	
	public boolean addReg(String username,String sex,String birth){
		if(username!=null){
			ContentValues cv = new ContentValues();
			cv.put(sqlh.username, username);
			cv.put(sqlh.sex,sex);
			cv.put(sqlh.birth, birth);
			cv.put(sqlh.fecha_inicio, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			return(db.insert(sqlh.tabla, null, cv)!=-1)?true:false;
		}
		return false;
	}
	
	public boolean addObjetivo(String username,String newOb){
		if(username!=null){
			ContentValues cv = new ContentValues();
			cv.put(sqlh.username, username);
			cv.put(sqlh.objetivo, newOb);
			cv.put(sqlh.fecha_ob, new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()));
			return(db.insert(sqlh.tablaObjetivo, null, cv)!=-1)?true:false;
		}
		return false;
	}
	
	public boolean addMedicion(String username,double peso,double altura,double imc){
		if(username!=null){
			ContentValues cv = new ContentValues();
			cv.put(sqlh.username, username);
			cv.put(sqlh.altura, altura);
			cv.put(sqlh.peso, peso);
			cv.put(sqlh.imc, imc);
			cv.put(sqlh.fecha,new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			return(db.insert(sqlh.tablaMedicion, null,cv)!=-1)?true:false;
		}
		return false;
	}
	
	public String getLastObjetivo(String usr){
		String res=null;
		Cursor cursor = db.query(sqlh.tablaObjetivo,
				new String[]{sqlh.objetivo},
							sqlh.username+"=?",
				new String[]{usr},
							null,null,
							sqlh.fecha_ob+" DESC ","1");
		if(cursor.moveToFirst()){
			do{
				res = cursor.getString(0);
			}while(cursor.moveToNext());
		}
		return res;
	}
	
	public Medicion getLastMedicion(String usr){
		Medicion med = null;
		Cursor cursor = db.query(sqlh.tablaMedicion,
			new String[]{sqlh.fecha,sqlh.imc,sqlh.altura,sqlh.peso},
				sqlh.username+"=?",
				new String[]{usr},
				null,null,
				sqlh.fecha+" DESC ","1");
		if(cursor.moveToFirst()){
			do{
				med = new Medicion();
				try{
					med.setFecha(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(0)));
				}catch(ParseException e){}
				med.setImc(cursor.getDouble(1));
				med.setAltura(cursor.getDouble(2));
				med.setPeso(cursor.getDouble(3));
				
			}while(cursor.moveToNext());
		}
		return med;
	}
	
	public ArrayList<Medicion> getMediciones(String usr){
		ArrayList<Medicion> med = new ArrayList<Medicion>();
		Cursor cursor = db.query(sqlh.tablaMedicion,
				new String[]{sqlh.fecha,sqlh.imc,sqlh.altura,sqlh.peso},
					sqlh.username+"=?",
				new String[]{usr},
					null,null,
					sqlh.fecha+" ASC ");
		if(cursor.moveToFirst()){
			do{
				Medicion m = new Medicion();
				try{
					m.setFecha(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(0)));
				}catch(ParseException e){}
				m.setImc(cursor.getDouble(1));
				m.setAltura(cursor.getDouble(2));
				m.setPeso(cursor.getDouble(3));
				med.add(m);
			}while(cursor.moveToNext());
		}
		
		return med;
	}

    public boolean updateSession(String username,int id){
        ContentValues cv = new ContentValues();
        cv.put(sqlh.username,username);
        return (db.update(sqlh.tabla,cv,sqlh.id_sesion+"=?",new String[]{id+""})!=-1)?true:false;
    }

	public Sesion getLastSesion(){
		Sesion ses=null;
		Cursor cursor = db.query(sqlh.tabla,
				new String[]{sqlh.id_sesion,sqlh.username,sqlh.fecha_inicio,sqlh.fecha_fin
							,sqlh.sex,sqlh.birth},
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
				
				ses.setSex(cursor.getString(4));
				ses.setBirth(cursor.getString(5));
				
			}while(cursor.moveToNext());
		}
		return ses;
	}
	
	public boolean deleteSesion(int id){
		return (db.delete(sqlh.tabla, sqlh.id_sesion+"="+id, null)>0)?true:false;
	}
}
