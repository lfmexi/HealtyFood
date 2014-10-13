package edu.tesis.healthyfood;

import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {

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
				Intent i = new Intent(this,MenuPrincipal.class);
				i.putExtra("infoUser", s.getUser());
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
