package edu.tesis.healthyfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuPrincipal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
		String listado[]=new String[]{
				"Publicar receta","Buscar recetas","Mis recetas","Ejercicios recomendados","Mi Perfil","Medidor de calorías"
		};
		listaMenu=(ListView)this.findViewById(R.id.listaMenu);
		listaMenu.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,listado));
		
		listaMenu.setOnItemClickListener(new OnItemClickListener(){
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
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}
	
	private void listaOnClick(View view){
		String nombreMenu=((TextView) view).getText().toString();
		if(nombreMenu.equals("Publicar receta")){
			Intent i = new Intent(this,PublicaReceta.class);
			this.startActivity(i);
		}else if(nombreMenu.equals("Buscar recetas")){
			Intent i = new Intent(this,BuscaRecetas.class);
			this.startActivity(i);
		}else if(nombreMenu.equals("Mis recetas")){
			Intent i = new Intent(this,MisRecetas.class);
			this.startActivity(i);
		}else if(nombreMenu.equals("Ejercicios recomendados")){
			Intent i = new Intent(this,Ejercicios.class);
			this.startActivity(i);
		}else if(nombreMenu.equals("Mi Perfil")){
			Intent i = new Intent(this,Perfil.class);
			this.startActivity(i);
		}else if(nombreMenu.equals("Medidor de calorías")){
			Intent i = new Intent(this,Medidor.class);
			this.startActivity(i);
		}
	}
	
	private ListView listaMenu;

}
