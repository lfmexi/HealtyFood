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

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import edu.tesis.healthyfood.sqlite.SQLite;

@SuppressWarnings("deprecation")
public class Ejercicios extends Fragment {

	String user="";
	
	ArrayList<String> listaEjercicios;
	
	private ListView lista;

    public Ejercicios(){}

    public static Ejercicios newInstance(String usr){
        Ejercicios fragment=new Ejercicios();
        Bundle args = new Bundle();
        args.putString("user",usr);
        fragment.setArguments(args);
        return fragment;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.activity_ejercicios, container, false);
		TextView tUser = (TextView)view.findViewById(R.id.textoUsuario);
		TextView tObj = (TextView)view.findViewById(R.id.textoObjetivo);
		lista = (ListView)view.findViewById(R.id.listView1);

        user = getArguments().getString("user");

		lista.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				botonOnClick(((TextView) arg1).getText().toString());
			}
		});
		
		tUser.setText(user);
		
		if(listaEjercicios==null){
			SQLite sql=new SQLite(this.getActivity());
			sql.abrir();
			String lastObj=sql.getLastObjetivo(user);
			sql.cerrar();
			if(lastObj!=null){
				tObj.setText("Objetivo: "+lastObj);
				EjerciciosAsync ej=new EjerciciosAsync(this);
				ej.execute(lastObj);
			}else{
				AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
				b.setTitle("Aviso");
				b.setMessage("Registre una medici�n con objetivo antes de ver ejercicios");
			}
		}
		
		return view;
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.ejercicios, menu);
		return true;
	}
	
	private void botonOnClick(String ejercicio){
		EjercicioSelect es=new EjercicioSelect(this);
		es.execute(ejercicio);
	}

	private class EjerciciosAsync extends AsyncTask<String,Void,String[]>{

		Ejercicios padre;
		
		public EjerciciosAsync(Ejercicios e){
			padre  = e;
		}
		
		@Override
		protected String[] doInBackground(String... arg0) {
			
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/getEjercicios.php");
			
	        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	        params.add(new BasicNameValuePair("objetivo",arg0[0]));
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
			    	    regs[i] = object.getString("nombre");
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
				padre.listaEjercicios=new ArrayList<String>();
				for(int i = 0;i<result.length;i++){
					padre.listaEjercicios.add(result[i]);
				}
				padre.lista.setAdapter(new ArrayAdapter<String>(padre.getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,padre.listaEjercicios));
			}
		}
		
	}
	
	private class EjercicioSelect extends AsyncTask<String,Void,String[]>{

		Ejercicios padre;
		
		public EjercicioSelect(Ejercicios e){
			padre = e;
		}
		@Override
		protected String[] doInBackground(String... arg0) {
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/selectEjercicio.php");
			
	        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	        params.add(new BasicNameValuePair("nombre",arg0[0]));
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
		    		regs = new String[3];
		    	    JSONObject object = mArray.getJSONObject(0);
			   	    regs[0] = object.getString("nombre");
			   	    regs[1] = object.getString("objetivo");
			   	    regs[2] = object.getString("idVideo");
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
				Intent i = new Intent(padre.getActivity(),VisualizaEjercicio.class);
				i.putExtra("infoUser", user);
				i.putExtra("ejercicio", result[0]);
				i.putExtra("objetivo", result[1]);
				i.putExtra("idVideo", result[2]);
				padre.startActivity(i);
			}
		}
	}
}
