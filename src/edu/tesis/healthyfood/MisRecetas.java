package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MisRecetas extends Activity {

	String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mis_recetas);
		Intent i = this.getIntent();
		user = i.getExtras().getString("infoUser");
		
		lista = (ListView)this.findViewById(R.id.mis_recetas_lista);
		
		
		String listado[]=new String[]{
				"Recetas propias","Favoritas"
		};
		
		lista.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,listado));
		lista.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaOnClick(arg1);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mis_recetas, menu);
		return true;
	}

	private void listaOnClick(View v){
		String nombreMenu=((TextView) v).getText().toString();
		if(nombreMenu.equals("Recetas propias")){
			Intent i = new Intent(this,RecetasPropias.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else{
			Intent i = new Intent(this,Favoritas.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}
	}
	
	private ListView lista;
}
