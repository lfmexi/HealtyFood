package edu.tesis.healthyfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
		TextView txt=(TextView)view.findViewById(R.id.textUser);
		txt.setText(user);
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
        final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
        ft.replace(R.id.content_frame, new Medidor(user, "perfil",getActivity()), "Medidor"); 
        ft.addToBackStack(null);
        ft.commit();
	}
	
	private void progreso(){
        final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
        ft.replace(R.id.content_frame, new ProgresoIMC(user), "Progreso IMC"); 
        ft.addToBackStack(null);
        ft.commit();
	}
	

	private Button botonMedir;
	private Button botonProgreso;
}
