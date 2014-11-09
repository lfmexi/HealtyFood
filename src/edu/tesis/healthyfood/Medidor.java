package edu.tesis.healthyfood;

import edu.tesis.healthyfood.sqlite.SQLite;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Medidor extends Activity {
	private String user;
	private String ambito="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medidor);

		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		ambito = i.getExtras().getString("ambito");

		botonRegistrar = (Button)this.findViewById(R.id.botonRegistrarMedidas);
		campoPeso = (EditText)this.findViewById(R.id.campoPeso);
		campoAltura = (EditText)this.findViewById(R.id.campoAltura);
		selectObjetivo = (Spinner)this.findViewById(R.id.selectObjetivo);
		
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
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
	}

	private void botonOnClick(){
		if(!campoPeso.getText().toString().equals("") && !campoAltura.getText().toString().equals("")){
			
			double peso = Double.parseDouble(campoPeso.getText().toString());
			double altura = Double.parseDouble(campoAltura.getText().toString());
			double imc = peso/Math.pow(altura, 2);
			
			SQLite sql = new SQLite(this);
			sql.abrir();
			sql.addMedicion(user, peso, altura, imc);
			sql.cerrar();
			
			sql.abrir();
			sql.addObjetivo(user, this.selectObjetivo.getSelectedItem().toString());
			sql.cerrar();
			
			if(ambito.equals("registro")){
				Intent i = new Intent(this,MenuPrincipal.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("infoUser", user);
				this.startActivity(i);
			}
			this.finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.medidor, menu);
		return true;
	}

	private Button botonRegistrar;
	private EditText campoPeso;
	private EditText campoAltura;
	private Spinner selectObjetivo;
}
