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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class BuscaRecetas extends Fragment {

	
	private String user="";
    private String[] adapter;
    public BuscaRecetas(){}

    public static BuscaRecetas newInstance(String usr){
        BuscaRecetas fragment=new BuscaRecetas();
        Bundle args = new Bundle();
        args.putString("user",usr);
        fragment.setArguments(args);
        return fragment;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_busca_recetas, container, false);
		campoReceta = (EditText)view.findViewById(R.id.receta_instrucciones);
		botonBusqueda =(Button)view.findViewById(R.id.recetas_buscar);
		resultado = (ListView)view.findViewById(R.id.busca_receta_result);

        user = getArguments().getString("user");
        adapter = new String[]{};
		botonBusqueda.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		
		resultado.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaOnItemClick(arg1);
			}
		});
        if(savedInstanceState!=null){
            adapter=savedInstanceState.getStringArray("adapter");
            resultado.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,adapter));
        }
		return view;
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("adapter",adapter);
    }

	private void botonOnClick(){
		String patron = campoReceta.getText().toString();
		BuscaRecetasAsync b = new BuscaRecetasAsync(this);
		b.execute(patron);
	}
	
	private void listaOnItemClick(View v){
		String nombre = ((TextView) v).getText().toString();
		Intent i = new Intent(this.getActivity(),VisualizaReceta.class);
		i.putExtra("infoUser", user);
		i.putExtra("receta", nombre);
		this.startActivity(i);
	}
	
	private EditText campoReceta;
	private Button botonBusqueda;
	private ListView resultado;

	private class BuscaRecetasAsync extends AsyncTask<String,Void,String[]>{

		BuscaRecetas padre;
		
		BuscaRecetasAsync(BuscaRecetas p){
			padre = p;
		}
		
		@Override
		protected String[] doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/getRecetas.php");
			
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
				String adaptador[] = new String[result.length];
				for(int i = 0;i<result.length;i++){
					adaptador[i] = result[i];
				}
                padre.adapter=adaptador;
				padre.resultado.setAdapter(new ArrayAdapter<String>(padre.getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,adaptador));
			}
		}
	}
}
