package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Perfila extends Activity {

	String user="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		
		botonMedir=(Button)this.findViewById(R.id.botonNuevaMedida);
		botonProgreso=(Button)this.findViewById(R.id.botonGetProgreso);
		
		botonMedir.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				medir();
			}
		});
		
		botonProgreso.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				progreso();
			}
		});
	}

	
	private void medir(){
		Intent i = new Intent(this,Medidor.class);
		i.putExtra("infoUser", user);
		i.putExtra("ambito","perfil");
		this.startActivity(i);
	}
	
	private void progreso(){
		Intent i = new Intent(this,ProgresoIMC.class);
		i.putExtra("infoUser", user);
		this.startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perfil, menu);
		return true;
	}

	private Button botonMedir;
	private Button botonProgreso;
}
