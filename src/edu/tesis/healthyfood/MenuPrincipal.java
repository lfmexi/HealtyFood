package edu.tesis.healthyfood;

import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

public class MenuPrincipal extends ActionBarActivity implements TabListener {

	String user="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		
		ActionBar Bar=getSupportActionBar();
		Bar.setDisplayHomeAsUpEnabled(true);
		Bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);;
		Tab p=Bar.newTab().setText(R.string.title_tab_profile).setIcon(R.drawable.ic_action_action_perm_identity).setTabListener(this);
		Bar.addTab(p);
		p=Bar.newTab().setText(R.string.title_tab_recipes).setIcon(R.drawable.ic_action_action_assignment).setTabListener(this);
		Bar.addTab(p);
		p=Bar.newTab().setText(R.string.title_tab_settings).setIcon(R.drawable.ic_action_action_settings).setTabListener(this);
		Bar.addTab(p);
		
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
		}else if(nombreMenu.equals("Medidor de calorÃ­as")){
			Intent i = new Intent(this,Medidor.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}
	}
	
	private ListView listaMenu;
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
		
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static class DummySectionFragment extends Fragment {
	    public static final String ARG_SECTION_NUMBER = "placeholder_text";

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	       Bundle savedInstanceState) {
	      TextView textView = new TextView(getActivity());
	      textView.setGravity(Gravity.CENTER);
	      textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
	      return textView;
	    }
	  }

}
