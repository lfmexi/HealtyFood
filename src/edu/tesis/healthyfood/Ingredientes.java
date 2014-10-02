package edu.tesis.healthyfood;

import java.util.TreeMap;

import edu.tesis.healthyfood.sobj.ContenedorIngredientes;
import edu.tesis.healthyfood.sobj.Ingrediente_Receta;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Ingredientes extends Activity {

	private TreeMap<String,Ingrediente_Receta> ingredientes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingredientes);
		ingredientes = new TreeMap<String,Ingrediente_Receta>();
		
		campoBusqueda = (EditText)this.findViewById(R.id.ingredientes_busca);
		botonBusqueda = (Button)this.findViewById(R.id.ing_buscar);
		listaRes = (ListView)this.findViewById(R.id.ingredientes_result);
		
		botonBusqueda.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				busquedaOnClick();
			}
		});
		
		listaRes.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaOnItemClick(arg1);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingredientes, menu);
		return true;
	}

	private void listaOnItemClick(View v){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		final String nombre = ((TextView) v).getText().toString();
		if(!PublicaReceta.contenedor.lista.containsKey(nombre)){
			if(ingredientes.containsKey(nombre)){
				b.setTitle("Agregue el ingrediente");
				
				final EditText input = new EditText(this);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				b.setView(input);
				
				final Ingrediente_Receta ir = ingredientes.get(nombre);
				if(ir.getTipoMedida().equals("u")){
					b.setMessage("Agregue las unidades necesarias del ingrediente");
					b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							try{
								int entero = Integer.parseInt(input.getText().toString());
								ir.setUnidades(entero);
								PublicaReceta.contenedor.lista.put(nombre, ir);
							}catch(NumberFormatException nfe){
								//nada
							}
						}
					});
					b.setNegativeButton("Cancel", null);
				}else{
					b.setMessage("Agregue los gramos utilizados del ingrediente");
					b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							try{
								double entero = Double.parseDouble(input.getText().toString());
								ir.setGramos(entero);
								PublicaReceta.contenedor.lista.put(nombre, ir);
							}catch(NumberFormatException nfe){
								//nada
							}
						}
					});
					b.setNegativeButton("Cancel", null);
				}
				b.show();
			}
		}else{
			b.setTitle("Eliminar ingrediente");
			b.setMessage("El ingrediente ya ha sido agregado, ¿desea eliminarlo?");
			b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PublicaReceta.contenedor.lista.remove(nombre);
				}
			});
			b.setNegativeButton("No", null);
			b.show();
		}
	}
	
	private void busquedaOnClick(){
		if(campoBusqueda.getText()!=null){
			String str= campoBusqueda.getText().toString();
			if(!str.equals("")){
				String arreglo[] = new String[]{
						"Ingrediente 1","Ingrediente 2","Ingrediente 3"
				};
				listaRes.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,arreglo));
				ingredientes = new TreeMap<String,Ingrediente_Receta>();
				ingredientes.put("Ingrediente 1", new Ingrediente_Receta("Ingrediente 1","u"));
				ingredientes.put("Ingrediente 2", new Ingrediente_Receta("Ingrediente 2","g"));
				ingredientes.put("Ingrediente 3", new Ingrediente_Receta("Ingrediente 3","u"));
			}
		}
	}
	
	private EditText campoBusqueda;
	private Button botonBusqueda;
	private ListView listaRes;
}
