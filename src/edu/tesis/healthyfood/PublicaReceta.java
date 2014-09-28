package edu.tesis.healthyfood;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publica_receta);
		
		selector_categoria = (Spinner)this.findViewById(R.id.receta_spinner_cat);
		imagen = (ImageView)this.findViewById(R.id.receta_imagen);
		
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
		        R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		imagen.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imagenOnClick();
			}
		});
		
		selector_categoria.setAdapter(adapter);
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

}
