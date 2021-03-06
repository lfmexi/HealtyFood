package edu.tesis.healthyfood;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.media.ThumbnailUtils;
import edu.tesis.healthyfood.sobj.ContenedorIngredientes;
import edu.tesis.healthyfood.sobj.Ingrediente_Receta;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.TMB;

@SuppressWarnings("deprecation")
public class VisualizaReceta extends Activity {

	private String user="";
	private String receta="";
	private String[]info_receta;
	private String path="";

	private ContenedorIngredientes ingredientes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualiza_receta);

		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		receta = i.getExtras().getString("receta");
		
		SQLite sql=new SQLite(this);
		sql.abrir();
		TMB last =sql.getLastTMB(user);
		sql.cerrar();

		textNombreReceta = (TextView)this.findViewById(R.id.textoNombreReceta);
		textUsuario = (TextView)this.findViewById(R.id.textUsuario);
		textTipoReceta = (TextView)this.findViewById(R.id.textTipoReceta);
		textCalorias = (TextView)this.findViewById(R.id.textCalorias);
		verIngredientes = (Button)this.findViewById(R.id.verIngredientes);
		campoInstrucciones = (EditText)this.findViewById(R.id.textoInstrucciones);
		botonFavorito = (Button)this.findViewById(R.id.botonFavorito);
		botonAgrega = (Button)this.findViewById(R.id.botonConsumir);
		imagen = (ImageView)this.findViewById(R.id.visualizaImagen);
		bar = (RatingBar)this.findViewById(R.id.ratingReceta);
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
		
		bar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
				RateOnClick(rating);
			}
		});
		
		botonFavorito.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				favoritoOnClick();
			}
		});
        if(savedInstanceState==null){

            if(last!=null){
                double calorias = last.value*1.2;
                Toast.makeText(this, "Usted necesita "+calorias+" cal", Toast.LENGTH_SHORT).show();
            }

            LoaderAsync l = new LoaderAsync(this);
            l.execute(receta);
            RatingAsync l1 = new RatingAsync(this);
            l1.execute(receta,user);

        }else{
            textUsuario.setText(savedInstanceState.getString("usuario"));
            textNombreReceta.setText(savedInstanceState.getString("nombre_receta"));
            textCalorias.setText(savedInstanceState.getString("calorias"));
            textTipoReceta.setText(savedInstanceState.getString("tipo_receta"));
            campoInstrucciones.setText(savedInstanceState.getString("instrucciones"));
            info_receta=savedInstanceState.getStringArray("info_receta");
            botonAgrega.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    agregaOnClick();
                }
            });
            ingredientes=savedInstanceState.getParcelable("ingredientes");
            path = savedInstanceState.getString("path");

            File file=new File(path);
            if(file.exists()){
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
                imagen.setImageBitmap(bitmap);
            }
        }
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("usuario",textUsuario.getText().toString());
        outState.putString("nombre_receta",textNombreReceta.getText().toString());
        outState.putString("tipo_receta",textTipoReceta.getText().toString());
        outState.putString("calorias",textCalorias.getText().toString());
        outState.putString("instrucciones",campoInstrucciones.getText().toString());
        outState.putStringArray("info_receta",info_receta);
        outState.putString("path",path);
        outState.putParcelable("ingredientes",ingredientes);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualiza_receta, menu);
		return true;
	}
	
	
	private void verIngredientesOnClick(){
		if(ingredientes==null){
			IngredientesAsync ing = new IngredientesAsync(this);
			ing.execute(receta);
		}else{
			String ingr="";
			for(Ingrediente_Receta ir :ingredientes.lista.values()){
				ingr+=ir.getNombre_ingrediente();
				
				if(ir.getGramos()!=0){
					ingr = ingr + " -> " +ir.getGramos() + "g";
				}
				if(ir.getUnidades()!=0){
					ingr = ingr + " -> " +ir.getUnidades() + " unidades";
				}
				if(ir.getLitros()!=0){
					ingr = ingr + " -> " +ir.getLitros() + " litros";
				}
				ingr+="\n";
			}
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("Ingredientes en la receta");
			b.setMessage(ingr);
			b.show();
		}
	}

	private void favoritoOnClick(){
		FaveTask ut = new FaveTask(this);
		ut.execute(user,receta);		
		
	}
	
	private void RateOnClick(Float val){
		RatingTask ut = new RatingTask(this);
		ut.execute(user,receta,String.valueOf(val));		
	}
	
	private void agregaOnClick(){
		SQLite sql=new SQLite(this);
		double caloria=Double.parseDouble(info_receta[3]);
		sql.abrir();
		boolean agregado=sql.addDiario(user, receta, caloria);
		sql.cerrar();
		if(agregado){
			AlertDialog.Builder b=new AlertDialog.Builder(this);
			b.setTitle(getResources().getString(R.string.exito));
			b.setMessage("Receta agregada al diario");
			b.show();
		}
	}
	
	private TextView textNombreReceta;
	private TextView textUsuario;
	private TextView textTipoReceta;
	private TextView textCalorias;
	private Button verIngredientes;
	private ImageView imagen; 
	private EditText campoInstrucciones;
	private Button botonFavorito;
	private Button botonAgrega;
	private RatingBar bar;


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
				padre.botonAgrega.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						agregaOnClick();
					}
				});

                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN_MR1){
                        padre.path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/HealthyFood/" + result[0] + ".jpg";
                    }else{
                        padre.path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + result[0] + ".jpg";
                    }
                }
                File file=new File(padre.path);
                if(file.exists()){
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
                    padre.imagen.setImageBitmap(bitmap);
                }else{
                    AlertDialog.Builder b= new AlertDialog.Builder(padre);
                    b.setTitle("Carga en progreso");
                    b.setMessage("Espere mientras carga la imagen de la receta");
                    AlertDialog a = b.show();
                    new DownloadImage(padre,a).execute(Login.url+"/"+result[5]);
                }
			}
		}
	}

	private class RatingAsync extends AsyncTask<String,Void,String[]>{

		private VisualizaReceta padre;
		
		public RatingAsync(VisualizaReceta v){
			padre = v;
		}
		
		@Override
		protected String[] doInBackground(String... arg0) {
			String[] response = null;
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/getRating.php");
			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			params.add(new BasicNameValuePair("receta",arg0[0]));
			params.add(new BasicNameValuePair("user",arg0[1]));
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
		    		regs = new String[1];
		    		regs[0] = object.getString("rating");
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
				if (result[0]!=null){
					padre.bar.setRating(Float.parseFloat(result[0]));
				}
			}
		}
	}

	
	private class RatingTask extends AsyncTask<String,Void,String>{

		private  VisualizaReceta padre;
		public RatingTask(VisualizaReceta p){
			padre = p;
		}
		
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				postFile(arg0[0],arg0[1],arg0[2]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return "ok";
		}
		
		private void postFile(String username, String nombre, String rating) throws ClientProtocolException, IOException{
			String url=Login.url+"/raterecipe.php";
			HttpPost post = new HttpPost(url);
            HttpClient cliente = new DefaultHttpClient();

            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("user",username));
            params.add(new BasicNameValuePair("receta",nombre));
            params.add(new BasicNameValuePair("rating",rating));
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cliente.execute(post);
		}
		
		protected void onPostExecute(String result){

		}
	}

	private class FaveTask extends AsyncTask<String,Void,String>{

		private  VisualizaReceta padre;
		public FaveTask(VisualizaReceta p){
			padre = p;
		}
		
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				postFile(arg0[0],arg0[1]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return "ok";
		}
		
		private void postFile(String username, String nombre) throws ClientProtocolException, IOException{
			String url=Login.url+"/faverecipe.php";
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("user",username));
            params.add(new BasicNameValuePair("receta",nombre));
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cliente.execute(post);
            cliente.getConnectionManager().shutdown();
		}
		
		protected void onPostExecute(String result){
			if(result!=null){
				AlertDialog.Builder b= new AlertDialog.Builder(padre);
		        b.setTitle("Has marcado una receta como favorita");
		        b.setMessage(result);
		        b.show();
			}else{
				AlertDialog.Builder b= new AlertDialog.Builder(padre);
		        b.setTitle("Error");
		        b.setMessage(getResources().getString(R.string.receta_no_fav));
		        b.show();
			}
		}
	}

	private class IngredientesAsync extends AsyncTask<String,Void,String[]>{

		private VisualizaReceta padre;
		
		public IngredientesAsync(VisualizaReceta v){
			padre = v;
		}
		
		@Override
		protected String[] doInBackground(String... arg0) {
			String[] response = null;
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/getIngRec.php");
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
		    	regs=new String[num_registros];
		    	for (int i = 0; i < num_registros; i++) {
		    	    JSONObject object = mArray.getJSONObject(i);
		    	    
		    	    regs[i] = object.getString("ingrediente")+","+object.getString("gramos")+','
		    	    		+object.getString("litros")+","+object.getString("unidades");
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
				
				padre.ingredientes = new ContenedorIngredientes();
				
				for(int i = 0;i<result.length;i++){
					String res = result[i];
					String attr[]=res.split(",");
					Ingrediente_Receta ir = new Ingrediente_Receta(attr[0],Integer.parseInt(attr[3]),Double.parseDouble(attr[1]));
					ir.setLitros(Double.parseDouble(attr[2]));
					padre.ingredientes.lista.put(attr[0], ir);
				}
				
				String ingr="";
				
				for(Ingrediente_Receta ir :padre.ingredientes.lista.values()){
					ingr+=ir.getNombre_ingrediente();
					
					if(ir.getGramos()!=0){
						ingr = ingr + " -> " +ir.getGramos() + "g";
					}
					if(ir.getUnidades()!=0){
						ingr = ingr + " -> " +ir.getUnidades() + " unidades";
					}
					if(ir.getLitros()!=0){
						ingr = ingr + " -> " +ir.getLitros() + " litros";
					}
					ingr+="\n";
				}
				AlertDialog.Builder b = new AlertDialog.Builder(padre);
				b.setTitle("Ingredientes en la receta");
				b.setMessage(ingr);
				b.show();
			}
		}
		
	}

	private class DownloadImage extends AsyncTask<String,Void,Bitmap>{

		VisualizaReceta padre;
		AlertDialog alert;
		
		public DownloadImage(VisualizaReceta p,AlertDialog a){
			padre = p;
			alert = a;
		}
		
		@Override
		protected Bitmap doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String urldisplay = arg0[0];
	        Bitmap mIcon11 = null;
	        try{
	        	InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        }catch(Exception e){
	        	 Log.e("Error", e.getMessage());
	             e.printStackTrace();
	        }
	        return mIcon11;
		}
		
		protected void onPostExecute(Bitmap result) {
			alert.dismiss();
			if(result!=null) {
                padre.imagen.setImageBitmap(result);
                if(padre.path.equals("")){
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN_MR1){
                            padre.path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/HealthyFood/" + padre.info_receta[0] + ".jpg";
                        }else{
                            padre.path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + padre.info_receta[0] + ".jpg";
                        }
                    }
                }
                File file = new File(padre.path);
                if(!file.exists()){
                    try{
                        FileOutputStream fOut = new FileOutputStream(file);
                        result.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
	    }
	}
}
