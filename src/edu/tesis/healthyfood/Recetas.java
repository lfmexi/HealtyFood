package edu.tesis.healthyfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.TabListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Recetas extends Fragment{
	String user="";
	private ListView listaMenu;
	
	public Recetas(String user2) {
		user=user2;
		// TODO Auto-generated constructor stub
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.activity_recetas, container, false);
		String listado[]=new String[]{
				"Publicar receta","Buscar recetas","Mis recetas","Recetas favoritas"};
		//,"Ejercicios recomendados","Mi Perfil","Medidor de calorías"
		listaMenu=(ListView)view.findViewById(R.id.listaMenu);
		listaMenu.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, listado));
		
		listaMenu.setOnItemClickListener(new OnItemClickListener(){
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaOnClick(arg1);
			}
		});

		return view;
	}
	
	private void listaOnClick(View view){
        final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
        String nombreMenu=((TextView) view).getText().toString();
		if(nombreMenu.equals("Publicar receta")){
			Intent i = new Intent(getActivity(),PublicaReceta.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Buscar recetas")){
			Intent i = new Intent(getActivity(),BuscaRecetas.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Mis recetas")){
			Intent i = new Intent(getActivity(),MisRecetas.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Ejercicios recomendados")){
			Intent i = new Intent(getActivity(),Ejercicios.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Mi Perfil")){
			Intent i = new Intent(getActivity(),Perfila.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Medidor de calorías")){
			Intent i = new Intent(getActivity(),Medidor.class);
			i.putExtra("infoUser", user);
			this.startActivity(i);
		}else if(nombreMenu.equals("Recetas favoritas")){
			ft.replace(R.id.content_frame, new Favoritas(user,getActivity()), "Medidor"); 
	        ft.addToBackStack(null);
	        ft.commit();
		}
	}

}

