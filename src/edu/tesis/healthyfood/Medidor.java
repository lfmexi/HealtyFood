package edu.tesis.healthyfood;

import edu.tesis.healthyfood.sqlite.SQLite;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Medidor extends Fragment {
	private String user;
	private String ambito="";
	private Activity act;
	
	public Medidor(String usuario, String ambit, Activity acto){
		user=usuario;
		ambito=ambit;
		act=acto;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_medidor, container, false);
		botonRegistrar=(Button)view.findViewById(R.id.botonRegistrarMedidas);
		campoPeso = (EditText)view.findViewById(R.id.campoPeso);
		campoAltura = (EditText)view.findViewById(R.id.campoAltura);
				selectObjetivo = (Spinner)view.findViewById(R.id.selectObjetivo);
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(act,
		        R.array.objective_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectObjetivo.setAdapter(adapter);
		botonRegistrar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		return view;
	}

	private void botonOnClick(){
		if(!campoPeso.getText().toString().equals("") && !campoAltura.getText().toString().equals("")){
			
			double peso = Double.parseDouble(campoPeso.getText().toString());
			double altura = Double.parseDouble(campoAltura.getText().toString());
			double imc = peso/Math.pow(altura, 2);
			
			SQLite sql = new SQLite(getActivity());
			sql.abrir();
			sql.addMedicion(user, peso, altura, imc);
			sql.cerrar();
			
			sql.abrir();
			sql.addObjetivo(user, this.selectObjetivo.getSelectedItem().toString());
			sql.cerrar();

			if(ambito.equals("registro")){
				Intent i = new Intent(act,MenuPrincipal.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("infoUser", user);
				this.startActivity(i);
			}else{
				getActivity().getSupportFragmentManager().popBackStack();

			}
		}
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		act.getMenuInflater().inflate(R.menu.medidor, menu);
//		return true;
//	}

	private Button botonRegistrar;
	private EditText campoPeso;
	private EditText campoAltura;
	private Spinner selectObjetivo;
}
