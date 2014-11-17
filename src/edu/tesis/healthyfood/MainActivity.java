package edu.tesis.healthyfood;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;
import edu.tesis.healthyfood.sqlite.TMB;


public class MainActivity extends ActionBarActivity implements TabListener  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageButton ingreso = (ImageButton)this.findViewById(R.id.main_boton_entrar);
		ingreso.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
	}

	private void botonOnClick(){
		SQLite sql = new SQLite(this);
		sql.abrir();
		Sesion s = sql.getLastSesion();
		sql.cerrar();
		if(s!=null){
			if(s.getFecha_fin()!=null){
				Intent i = new Intent(this,Login.class);
				this.startActivity(i);
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
				
				Intent i = new Intent(this,DrawerMenuActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("infoUser", s.getUser());
				i.putExtra("sex",s.getSex());
				i.putExtra("birth", s.getBirth());
				this.startActivity(i);
			}
		}else{
			Intent i = new Intent(this,Login.class);
			this.startActivity(i);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

}
