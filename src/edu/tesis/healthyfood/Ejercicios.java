package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;

public class Ejercicios extends Fragment {

	String user="";
	
	public Ejercicios(String user){
		this.user=user;
	}
	
	@Override
	protected View onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ejercicios);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ejercicios, menu);
		return true;
	}

}
