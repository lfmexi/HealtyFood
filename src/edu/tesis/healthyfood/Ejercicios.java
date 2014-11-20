package edu.tesis.healthyfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Ejercicios extends Fragment {

	String user="";
	
	public Ejercicios(String user){
		this.user=user;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.activity_ejercicios, container, false);
		Button b = (Button)view.findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		return view;
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.ejercicios, menu);
		return true;
	}
	
	private void botonOnClick(){
		Intent i = new Intent(this.getActivity(),VisualizaEjercicio.class);
		i.putExtra("infoUser", user);
		this.startActivity(i);
		
	}

}
