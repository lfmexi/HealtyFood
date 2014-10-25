package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class VisualizaReceta extends Activity {

	String user="";
	String receta="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualiza_receta);
		
		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		receta = i.getExtras().getString("receta");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualiza_receta, menu);
		return true;
	}

}
