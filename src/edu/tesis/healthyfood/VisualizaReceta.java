package edu.tesis.healthyfood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VisualizaReceta extends Activity {

	String user="";
	String receta="";
	String[]info_receta;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualiza_receta);
		
		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		receta = i.getExtras().getString("receta");
		
		textNombreReceta = (TextView)this.findViewById(R.id.textoNombreReceta);
		textUsuario = (TextView)this.findViewById(R.id.textUsuario);
		textTipoReceta = (TextView)this.findViewById(R.id.textTipoReceta);
		textCalorias = (TextView)this.findViewById(R.id.textCalorias);
		verIngredientes = (TextView)this.findViewById(R.id.textVerIngredientes);
		campoInstrucciones = (EditText)this.findViewById(R.id.textoInstrucciones);
		botonFavorito = (Button)this.findViewById(R.id.botonFavorito);
		botonAgrega = (Button)this.findViewById(R.id.botonConsumir);
		
		textNombreReceta.setText(receta);
		textUsuario.setText("Cargando");
		textTipoReceta.setText("Cargando");
		textCalorias.setText("Cargando");
		campoInstrucciones.setText("Cargando");
		campoInstrucciones.setKeyListener(null);
		
		
		verIngredientes.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				verIngredientesOnClick();
			}
		});
		
		botonFavorito.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				favoritoOnClick();
			}
		});
		
		botonAgrega.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				agregaOnClick();
			}
		});
	
		LoaderAsync l = new LoaderAsync(this);
		l.execute(receta);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualiza_receta, menu);
		return true;
	}
	
	
	private void verIngredientesOnClick(){
		
	}

	private void favoritoOnClick(){
		
	}
	
	private void agregaOnClick(){
		
	}
	
	private TextView textNombreReceta;
	private TextView textUsuario;
	private TextView textTipoReceta;
	private TextView textCalorias;
	private TextView verIngredientes;
	private EditText campoInstrucciones;
	private Button botonFavorito;
	private Button botonAgrega;

	
	private class LoaderAsync extends AsyncTask<String,Void,String[]>{

		private VisualizaReceta padre;
		
		public LoaderAsync(VisualizaReceta v){
			padre = v;
		}
		
		@Override
		protected String[] doInBackground(String... arg0) {
			String[] response = null;
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/getDetailReceta.php");
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("receta",arg0[0]));
			try {
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = getValores(cliente,post);
			return response;
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
		    		JSONObject object = mArray.getJSONObject(0);
		    		regs = new String[6];
		    		regs[0] = object.getString("nombre");
		    		regs[1] = object.getString("username");
		    		regs[2] = object.getString("categoria");
		    		regs[3] = object.getString("calorias");
		    		regs[4] = object.getString("instrucciones");
		    		regs[5] = object.getString("url");
		    	}
		    }catch(JSONException e){
		    	e.printStackTrace();
		    } catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return regs;
		}
		
		protected void onPostExecute(String[] result){
			if(result!=null){
				padre.textNombreReceta.setText(result[0]);
				padre.textUsuario.setText(result[1]);
				padre.textTipoReceta.setText(result[2]);
				padre.textCalorias.setText(result[3]);
				padre.campoInstrucciones.setText(result[4]);
				padre.info_receta=result;
				//enviar a pedir la imagen
			}
		}
	}
}
