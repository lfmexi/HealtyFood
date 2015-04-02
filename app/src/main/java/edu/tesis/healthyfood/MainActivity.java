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
import android.widget.ProgressBar;

import edu.tesis.healthyfood.genericTasks.GenericCheckSession;
import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;
import edu.tesis.healthyfood.sqlite.TMB;


public class MainActivity extends ActionBarActivity implements TabListener  {

    private ProgressBar progressBar;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        if(savedInstanceState==null){
            Thread thread = new Thread(){
                @Override
                public void run() {
                    progress();
                }
            };
            thread.start();
        }
	}

	private void progress(){
		SQLite sql = new SQLite(this);
		sql.abrir();
		Sesion s = sql.getLastSesion();
		sql.cerrar();

        int decision = GenericCheckSession.generateSessionStart(sql,s);

        if(decision==GenericCheckSession.SESSION_STARTED){
            Intent i = new Intent(this,DrawerMenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("infoUser", s.getUser());
            i.putExtra("sex",s.getSex());
            i.putExtra("birth", s.getBirth());
            i.putExtra("fb",s.isFb());
            this.startActivity(i);
        }else{
            int progress = 0;
            while(progress<100){
                progress++;
                progressBar.setProgress(progress);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Intent i = new Intent(this,Login.class);
            this.startActivity(i);
        }
        finish();
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
