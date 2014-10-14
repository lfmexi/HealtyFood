package edu.tesis.healthyfood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.tesis.healthyfood.sobj.ContenedorIngredientes;
import edu.tesis.healthyfood.sobj.Ingrediente_Receta;
import android.os.AsyncTask;
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
				
				
				final Ingrediente_Receta ir = ingredientes.get(nombre);
				if(ir.getTipoMedida().equals("u")){
					b.setMessage("Agregue las unidades necesarias del ingrediente");
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					b.setView(input);
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
				}else if(ir.getTipoMedida().equals("g")){
					b.setMessage("Agregue los gramos utilizados del ingrediente");
					input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
					b.setView(input);
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
				}else {
					b.setMessage("Agregue los litros utilizados del ingrediente");
					input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
					b.setView(input);
					b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							try{
								double entero = Double.parseDouble(input.getText().toString());
								ir.setLitros(entero);
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
			IngredienteAsync ia = new IngredienteAsync(this);
			ia.execute(str);
		}
	}
	
	private EditText campoBusqueda;
	private Button botonBusqueda;
	private ListView listaRes;
	
	private class IngredienteAsync extends AsyncTask<String,Void,String[]>{

		private Ingredientes padre;
		
		public IngredienteAsync(Ingredientes i){
			padre=i;
		}
		
		@Override
		protected String[] doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/getIngredientes.php");
			
	        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	        params.add(new BasicNameValuePair("patron",arg0[0]));
	        try {
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getValores(cliente,post);
		}
		
		private StringBuilder inputStreamToString(InputStream is) {
		    String rLine = "";
		    StringBuilder answer = new StringBuilder();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    try {
		     while ((rLine = rd.readLine()) != null) {
		      answer.append(rLine);
		       }
		    }
		    catch (IOException e) {
		        e.printStackTrace();
		     }
		    return answer;
		}
		
		private String []getValores(HttpClient cliente, HttpPost post){
			String []regs=null;
			try{
		    	HttpResponse response = cliente.execute(post);
		    	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    	JSONArray mArray = new JSONArray(jsonResult);
		    	int num_registros=mArray.length();
		    	if(num_registros>0){
		    		regs = new String[num_registros];
		    		for (int i = 0; i < num_registros; i++) {
			    	    JSONObject object = mArray.getJSONObject(i);
			    	    String campo1 = object.getString("nombre");
			    	    String campo2 = object.getString("tipoMedida");
			    	    regs[i] = campo1+","+campo2;
			    	}
		    	}
		    }catch(JSONException e){
		    	e.printStackTrace();
		    } catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	cliente.getConnectionManager().shutdown();
			return regs;
		}
		
		protected void onPostExecute(String[] result){
			if(result!=null){
				String adaptador[] = new String[result.length];
				for(int i = 0;i<result.length;i++){
					String linea_ingrediente = result[i];
					String [] attr_ingrediente = linea_ingrediente.split(",");
					padre.ingredientes.put(attr_ingrediente[0], new Ingrediente_Receta(attr_ingrediente[0],attr_ingrediente[1]));
					adaptador[i] = attr_ingrediente[0];
				}
				padre.listaRes.setAdapter(new ArrayAdapter<String>(padre,android.R.layout.simple_list_item_1,android.R.id.text1,adaptador));
			}
		}
		
	}
	
}
