package edu.tesis.healthyfood;

import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuPrincipal extends Activity {

	String user="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
		
		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		
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
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.logout:
				Logout();
				return true;
		}
		return false;
	}

	private void Logout(){
		SQLite sql = new SQLite(this);
		sql.abrir();
		Sesion s = sql.getLastSesion();
		if(sql.deleteSesion(s.getId())){
			sql.cerrar();
			this.finish();
			return;
		}
		sql.cerrar();
		this.finish();
	}
	
	private void listaOnClick(View view){
		String nombreMenu=((TextView) view).getText().toString();
		if(nombreMenu.equals("Publicar receta")){
			Intent i = new Intent(this,PublicaReceta.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Buscar recetas")){
			Intent i = new Intent(this,BuscaRecetas.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Mis recetas")){
			Intent i = new Intent(this,MisRecetas.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Ejercicios recomendados")){
			Intent i = new Intent(this,Ejercicios.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Mi Perfil")){
			Intent i = new Intent(this,Perfil.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Medidor de calorías")){
			Intent i = new Intent(this,Medidor.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}
	}
	
	private ListView listaMenu;

}
