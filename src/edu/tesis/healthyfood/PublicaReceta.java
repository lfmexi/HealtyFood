package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class PublicaReceta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publica_receta);
		
		selector_categoria = (Spinner)this.findViewById(R.id.receta_spinner_cat);
		
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
		        R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		selector_categoria.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.publica_receta, menu);
		return true;
	}
	
	private Spinner selector_categoria;
	private EditText campo_nombre;
	private EditText campo_instrucciones;
	private ImageView imagen;
	private Button boton_ingredientes;
	private Button boton_registrar;

}
