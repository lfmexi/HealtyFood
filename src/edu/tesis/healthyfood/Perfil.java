package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Perfil extends Fragment {
	String user="";

	public Perfil(String user) {
		this.user=user;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_perfil, container, false);
		botonMedir=(Button)view.findViewById(R.id.botonNuevaMedida);
		botonProgreso=(Button)view.findViewById(R.id.botonGetProgreso);
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

		return view;
	}

	
	private void medir(){
		Intent i = new Intent(getActivity(),Medidor.class);
		i.putExtra("infoUser", user);
		i.putExtra("ambito","perfil");
		this.startActivity(i);
	}
	
	private void progreso(){
		Intent i = new Intent(getActivity(),ProgresoIMC.class);
		i.putExtra("infoUser", user);
		this.startActivity(i);
	}
	

	private Button botonMedir;
	private Button botonProgreso;
}
