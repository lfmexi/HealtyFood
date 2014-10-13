package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RecetasPropias extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recetas_propias);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recetas_propias, menu);
		return true;
	}
	
}
