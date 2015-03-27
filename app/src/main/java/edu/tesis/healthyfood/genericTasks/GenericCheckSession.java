package edu.tesis.healthyfood.genericTasks;

import java.util.Calendar;

import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;
import edu.tesis.healthyfood.sqlite.TMB;

/**
 * Created by luis on 26/03/15.
 */
public class GenericCheckSession {

    public static final int SESSION_STARTED = 1;
    public static final int SESSION_CLOSED = 0;

    public static int generateSessionStart(SQLite sql,Sesion s){
        if(s!=null){
            if(s.getFecha_fin()!=null){
                return SESSION_CLOSED;
            }else{
                String [] fecha = s.getBirth().split("-");
                int year = Integer.parseInt(fecha[0]);
                int month = Integer.parseInt(fecha[1]);
                int day = Integer.parseInt(fecha[2]);

                Calendar c = Calendar.getInstance();

                int year_today=c.get(Calendar.YEAR);
                int month_today=c.get(Calendar.MONTH);
                int day_today = c.get(Calendar.DAY_OF_MONTH);

                sql.abrir();
                TMB tmb = sql.getLastTMB(s.getUser());
                Medicion med = sql.getLastMedicion(s.getUser());
                sql.cerrar();

                if(tmb!=null && med!=null){
                    String[] lastFecha = tmb.fecha_tomado.split("-");

                    int yearlast = Integer.parseInt(lastFecha[0]);

                    boolean calcular = false;

                    if(yearlast<year_today){
                        if(year==yearlast+1){
                            if((month_today-month)>=0 && (day_today-day)>=0){
                                calcular = true;
                            }
                        }else{
                            calcular =false;
                        }
                    }
                    if(calcular){
                        int edad = year_today-year;
                        if((month_today-month)<0){
                            edad -=1;
                        }else if((day_today-day)<0){
                            edad-=1;
                        }
                        double tmb_val=(10*med.getPeso())+(6.25*med.getAltura()/100)-(5*edad);
                        if(s.getSex().equals("Hombre")){
                            tmb_val+=5;
                        }else{
                            tmb_val-=161;
                        }
                        sql.abrir();
                        sql.addTMB(s.getUser(), tmb_val);
                        sql.cerrar();
                    }
                }else if(med!=null){
                    int diferencia = year_today-year;
                    double tmb_val=(10*med.getPeso())+(6.25*med.getAltura()/100)-(5*diferencia);
                    if(s.getSex().equals("Hombre")){
                        tmb_val+=5;
                    }else{
                        tmb_val-=161;
                    }
                    sql.abrir();
                    sql.addTMB(s.getUser(), tmb_val);
                    sql.cerrar();
                }
                return SESSION_STARTED;
            }
        }
        return SESSION_CLOSED;
    }
}
