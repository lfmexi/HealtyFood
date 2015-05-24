package edu.tesis.healthyfood;

import java.io.File;
import java.io.FileOutputStream;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.tesis.healthyfood.sobj.ContenedorIngredientes;
import edu.tesis.healthyfood.sobj.ContenedorInstrucciones;
import edu.tesis.healthyfood.sobj.Ingrediente_Receta;

@SuppressWarnings("deprecation")
public class PublicaReceta extends Fragment {

	String user="";
	static ContenedorIngredientes contenedor;
	static ContenedorInstrucciones instrucciones;

    public PublicaReceta(){}

    public static PublicaReceta newInstance(String usr){
        PublicaReceta fragment=new PublicaReceta();
        Bundle args = new Bundle();
        args.putString("user",usr);
        fragment.setArguments(args);
        return fragment;
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_publica_receta, container, false);

        contenedor = new ContenedorIngredientes();
		instrucciones = new ContenedorInstrucciones();

		selector_categoria = (Spinner)view.findViewById(R.id.receta_spinner_cat);
		campo_nombre = (EditText)view.findViewById(R.id.receta_nombre);
		campo_instrucciones = (EditText)view.findViewById(R.id.receta_instrucciones);
		imagen = (ImageView)view.findViewById(R.id.receta_imagen);
		boton_registrar = (Button)view.findViewById(R.id.receta_publicar);
		boton_ingredientes = (Button)view.findViewById(R.id.receta_boton_agrega);
		boton_ver = (Button)view.findViewById(R.id.recetas_ver_ingredientes);
		boton_foto = (Button)view.findViewById(R.id.receta_photo);
		Button cal = (Button)view.findViewById(R.id.button1);
		
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this.getActivity(),
		        R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        user = getArguments().getString("user");
		
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

		boton_foto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickCamara();
			}
		});

		cal.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				calorias();
			}
			
		});
		
		return view;
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path",this.path_imagen);
        outState.putParcelable("lista",contenedor);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            path_imagen=savedInstanceState.getString("path");

            if(!path_imagen.equals("")){
                File image = new File(path_imagen);

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

                bitmap=getResizedBitmap(bitmap, 400, 400);
                imagen.setImageBitmap(bitmap);
            }
            contenedor=savedInstanceState.getParcelable("lista");
        }
    }

    private void calorias(){
		String ingredientes = "";
		double cal=0;
		for(Ingrediente_Receta ir :contenedor.lista.values()){
			ingredientes+=ir.getNombre_ingrediente();
			
			if(ir.getGramos()!=0){
				double cal_ir = ir.getCal_100g()/100 * ir.getGramos();
				ingredientes = ingredientes + " -> " +cal_ir + " cal";
				cal +=cal_ir;
			}
			ingredientes+="\n";
		}
		ingredientes = ingredientes+getResources().getString(R.string.calorias_receta)+": "+cal+" cal";
		AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
		b.setTitle(getResources().getString(R.string.calorias_receta));
		b.setMessage(ingredientes);
		b.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==REQUEST_CAMERA && (resultCode==Activity.RESULT_OK)){
			if(photoPath==null){
				Uri selectImageUri=data.getData();
				path_imagen = getPath(selectImageUri);
			}else{
				path_imagen = photoPath;
			}
			File image = new File(path_imagen);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();

			Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

			bitmap=getResizedBitmap(bitmap, 400, 400);
		    try{
		        File file = new File(path_imagen);
		        FileOutputStream fOut = new FileOutputStream(file);
		        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
		        fOut.flush();
		        fOut.close();}
		    catch (Exception e) {
		        e.printStackTrace();
		    }
			bitmap=BitmapFactory.decodeFile(path_imagen);
			imagen.setImageBitmap(bitmap);
		}
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
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
				ingredientes = ingredientes + " -> " +ir.getLitros() + " litros";
			}
			ingredientes+="\n";
		}
		AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
		b.setTitle("Ingredientes en la receta");
		b.setMessage(ingredientes);
		b.show();
	}
	
	private void agregarOnClick(){
		Intent i = new Intent(this.getActivity(),Ingredientes.class);
		i.putExtra("ambito", "publica");
		startActivity(i);
	}
	
	private String constructIngredientes(){
		String json="[";
		
		for(Ingrediente_Receta ir:contenedor.lista.values()){
			String ingrediente = "{\"nombre_ingrediente\":\""+ir.getNombre_ingrediente()+"\",\"unidades\":"+ir.getUnidades()+",\"gramos\":"+ir.getGramos()+",\"litros\":"+ir.getLitros()+"}";
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

            	AlertDialog.Builder b= new AlertDialog.Builder(getActivity());
                b.setTitle("Carga en progreso");
                b.setMessage("Espere mientras se cargan los datos al servidor");
                AlertDialog a = b.show();
        		UploaderTask ut = new UploaderTask(this,a);
        		String ingredientes = constructIngredientes();
        		ut.execute(path_imagen,user,campo_nombre.getText().toString(),campo_instrucciones.getText().toString(),selector_categoria.getSelectedItem().toString(),ingredientes);
        	}else {
            	AlertDialog.Builder b= new AlertDialog.Builder(getActivity());
                b.setTitle("Error en la carga");
                b.setMessage("Todos los campos son obligatorios");
                b.show();
            }
        }else {
        	AlertDialog.Builder b= new AlertDialog.Builder(getActivity());
            b.setTitle("Error en la carga");
            b.setMessage("Todos los campos son obligatorios");
            b.show();
        }
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	//METODO ONCREATE FOTO
	public void onClickCamara() 
	{
		String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) 
        {					
            long captureTime = System.currentTimeMillis();

            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN_MR1){
                photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/HealthyFood/Photo" + captureTime + ".jpg";
            }else{
                photoPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/Photo" + captureTime + ".jpg";
            }

            try
            {
            	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(photoPath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));	 
                startActivityForResult(Intent.createChooser(intent, "Capture Image"), REQUEST_CAMERA);
            } 
            catch (Exception e) 
            {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
		
    }

	private static final int REQUEST_CAMERA = 1;
	private static final int REQUEST_SELECT_PHOTO = 0;
	
	protected Uri imageUri;
	private String photoPath;

	
	
	
	private Spinner selector_categoria;
	private EditText campo_nombre;
	private EditText campo_instrucciones;
	private ImageView imagen;
	private Bitmap bitmap;
	private String path_imagen="";
	private Button boton_ingredientes;
	private Button boton_registrar;
	private Button boton_ver;
	private Button boton_foto;

	
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
	        
	        if(!filename.equals("")) me.addPart("file", new FileBody(new File(filename)));
	        
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
				AlertDialog.Builder b= new AlertDialog.Builder(padre.getActivity());
		        b.setTitle("Carga exitosa");
		        b.setMessage("La receta ha sido publicada con �xito");
		        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						getActivity().getSupportFragmentManager().popBackStack();
					}
		        });
		        b.show();
			}else{
				AlertDialog.Builder b= new AlertDialog.Builder(padre.getActivity());
		        b.setTitle("Error");
		        b.setMessage("La receta no ha sido publicada con exito");
		        b.show();
			}
		}
	}
}
