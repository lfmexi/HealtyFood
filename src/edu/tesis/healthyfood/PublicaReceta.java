package edu.tesis.healthyfood;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.tesis.healthyfood.sobj.ContenedorIngredientes;
import edu.tesis.healthyfood.sobj.Ingrediente_Receta;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class PublicaReceta extends Activity {

	String user="";

	static ContenedorIngredientes contenedor = new ContenedorIngredientes();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publica_receta);
		contenedor = new ContenedorIngredientes();
		selector_categoria = (Spinner)this.findViewById(R.id.receta_spinner_cat);
		campo_nombre = (EditText)this.findViewById(R.id.receta_nombre);
		campo_instrucciones = (EditText)this.findViewById(R.id.receta_intrucciones);
		imagen = (ImageView)this.findViewById(R.id.receta_imagen);
		boton_registrar = (Button)this.findViewById(R.id.receta_publicar);
		boton_ingredientes = (Button)this.findViewById(R.id.receta_boton_agrega);
		boton_ver = (Button)this.findViewById(R.id.recetas_ver_ingredientes);
		
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
		        R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		
		imagen.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imagenOnClick();
			}
		});
		
		selector_categoria.setAdapter(adapter);

		boton_registrar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				registrarOnClick();
			}
		});
		
		boton_ingredientes.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				agregarOnClick();
			}
		});
		
		boton_ver.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				verOnClick();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.publica_receta, menu);
		return true;
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==1 && resultCode==Activity.RESULT_OK){
			Uri selectImageUri=data.getData();
			path_imagen = getPath(selectImageUri);
			bitmap=BitmapFactory.decodeFile(path_imagen);
			imagen.setImageBitmap(bitmap);
		}
	}

	private void verOnClick(){
		String ingredientes = "";
		for(Ingrediente_Receta ir :contenedor.lista.values()){
			ingredientes+=ir.getNombre_ingrediente();
			
			if(ir.getGramos()!=0){
				ingredientes = ingredientes + " -> " +ir.getGramos() + "g";
			}
			if(ir.getUnidades()!=0){
				ingredientes = ingredientes + " -> " +ir.getUnidades() + " unidades";
			}
			if(ir.getLitros()!=0){
				ingredientes = ingredientes + " -> " +ir.getLitros() + " unidades";
			}
			ingredientes+="\n";
		}
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Ingredientes en la receta");
		b.setMessage(ingredientes);
		b.show();
	}
	
	private void agregarOnClick(){
		Intent i = new Intent(this,Ingredientes.class);
		this.startActivity(i);
	}
	
	private String constructIngredientes(){
		String json="[";
		
		for(Ingrediente_Receta ir:contenedor.lista.values()){
			String ingrediente = "{\"nombre_ingrediente\":\""+ir.getNombre_ingrediente()+"\",\"unidades\":"+ir.getUnidades()+",\"gramos\":"+ir.getGramos()+"}";
			if(json.equals("[")){
				json+=ingrediente;
			}else{
				json+=","+ingrediente;
			}
		}
		json+="]";
		return json;
	}
	
	private void registrarOnClick(){
        if(campo_nombre.getText()!=null && campo_instrucciones.getText()!=null && selector_categoria.getSelectedItem()!=null){
        	
        	if(!campo_nombre.getText().toString().equals("")&& !campo_instrucciones.getText().toString().equals("") && !contenedor.lista.isEmpty()){

            	AlertDialog.Builder b= new AlertDialog.Builder(this);
                b.setTitle("Carga en progreso");
                b.setMessage("Espere mientras se cargan los datos al servidor");
                AlertDialog a = b.show();
        		UploaderTask ut = new UploaderTask(this,a);
        		String ingredientes = constructIngredientes();
        		ut.execute(path_imagen,user,campo_nombre.getText().toString(),campo_instrucciones.getText().toString(),selector_categoria.getSelectedItem().toString(),ingredientes);
        	}else {
            	AlertDialog.Builder b= new AlertDialog.Builder(this);
                b.setTitle("Error en la carga");
                b.setMessage("Todos los campos son obligatorios");
                b.show();
            }
        }else {
        	AlertDialog.Builder b= new AlertDialog.Builder(this);
            b.setTitle("Error en la carga");
            b.setMessage("Todos los campos son obligatorios");
            b.show();
        }
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	private void imagenOnClick(){
		Intent intent=new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		this.startActivityForResult(Intent.createChooser(intent, "Completar seleccionando"), 1);
	}
	
	
	private Spinner selector_categoria;
	private EditText campo_nombre;
	private EditText campo_instrucciones;
	private ImageView imagen;
	private Bitmap bitmap;
	private String path_imagen;
	private Button boton_ingredientes;
	private Button boton_registrar;
	private Button boton_ver;

	
	private class UploaderTask extends AsyncTask<String,Void,String>{

		private  PublicaReceta padre;
		private AlertDialog alerta;
		public UploaderTask(PublicaReceta p,AlertDialog a){
			padre = p;
			alerta=a;
		}
		
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				postFile(arg0[0],arg0[1],arg0[2],arg0[3],arg0[4],arg0[5]);
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
		
		private void postFile(String filename,String username, String nombre, String instruct,String cat,String ingr) throws ClientProtocolException, IOException{
			String url=Login.url+"/recipes.php";
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
	        MultipartEntityBuilder me = MultipartEntityBuilder.create();
	        
	        me.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	        me.addPart("file", new FileBody(new File(filename)));
	        me.addPart("user", new StringBody(username));
	        me.addPart("name", new StringBody(nombre));
	        me.addPart("ins", new StringBody(instruct));
	        me.addPart("categoria", new StringBody(cat));
	        me.addPart("ingredientes", new StringBody(ingr));
	        post.setEntity(me.build());
	        HttpResponse response=cliente.execute(post);
	        HttpEntity entidad = response.getEntity();
	        
	        entidad.consumeContent();
	        cliente.getConnectionManager().shutdown();
		}
		
		protected void onPostExecute(String result){
			alerta.dismiss();
			if(result!=null){
				AlertDialog.Builder b= new AlertDialog.Builder(padre);
		        b.setTitle("Carga exitosa");
		        b.setMessage("La receta ha sido publicada con éxito");
		        b.show();
			}else{
				AlertDialog.Builder b= new AlertDialog.Builder(padre);
		        b.setTitle("Error");
		        b.setMessage("La receta no ha sido publicada con éxito");
		        b.show();
			}
		}
	}
}
