package edu.tesis.healthyfood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
			ft.replace(R.id.content_frame, new PublicaReceta(user,getActivity()), "Medidor"); 
	        ft.addToBackStack(null);
	        ft.commit();
		}else if(nombreMenu.equals("Buscar recetas")){
			ft.replace(R.id.content_frame, new BuscaRecetas(user,getActivity()), "Medidor"); 
	        ft.addToBackStack(null);
	        ft.commit();
		}else if(nombreMenu.equals("Mis recetas")){
			ft.replace(R.id.content_frame, new MisRecetas(user,getActivity()), "Medidor"); 
	        ft.addToBackStack(null);
	        ft.commit();
		}else if(nombreMenu.equals("Recetas favoritas")){
			ft.replace(R.id.content_frame, new Favoritas(user,getActivity()), "Medidor"); 
	        ft.addToBackStack(null);
	        ft.commit();
		}
	}

}

