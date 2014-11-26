package edu.tesis.healthyfood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
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

import edu.tesis.healthyfood.sqlite.SQLite;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Registro extends Activity {

	private int year;
	private int month;
	private int day;
	static final int DATE_PICKER_ID = 1111;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		campouser = (EditText)this.findViewById(R.id.registro_username);
		campoemail = (EditText)this.findViewById(R.id.registro_email);
		campopass= (EditText)this.findViewById(R.id.registro_pass);
		campoconfpass = (EditText)this.findViewById(R.id.registro_confirma);
		campoNombre = (EditText)this.findViewById(R.id.registro_nombre);
		textoFecha = (TextView) this.findViewById(R.id.textoFecha);
		botonRegistro = (Button)this.findViewById(R.id.registro_boton_confirma);
		selectSexo = (Spinner)this.findViewById(R.id.selectorSexo);
		Button getFecha = (Button)this.findViewById(R.id.botonSeleccionFecha);
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH)+1;
		day = c.get(Calendar.DAY_OF_MONTH);
		
		textoFecha.setText(year+"-"+month+"-"+day);
		
		pickerListener = new DatePickerDialog.OnDateSetListener() {
			    @Override
		        public void onDateSet(DatePicker view, int selectedYear,
		                int selectedMonth, int selectedDay) {
		            year  = selectedYear;
		            month = selectedMonth+1;
		            day   = selectedDay;
		            textoFecha.setText(year+"-"+month+"-"+day);
		        }
		};
		
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
		        R.array.gender_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		selectSexo.setAdapter(adapter);
		
		botonRegistro.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		
		getFecha.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_ID);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DATE_PICKER_ID:
			return new DatePickerDialog(this,pickerListener,year,month-1,day);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener pickerListener;
	
	private void botonOnClick(){
		if(!campouser.getText().toString().equals("")&&!campoemail.getText().toString().equals("")&&!campopass.getText().toString().equals("")
				&&!campoconfpass.getText().toString().equals("")&&!campoNombre.getText().toString().equals("")){
			if(campoconfpass.getText().toString().equals(campopass.getText().toString())){
				CharSequence c_email=campoemail.getText().toString();
				if(android.util.Patterns.EMAIL_ADDRESS.matcher(c_email).matches()){
					String birth = year+"-"+month+"-"+day;				
					String sex=selectSexo.getSelectedItem().toString();
					RegistroAsyncTask r = new RegistroAsyncTask(this,campouser.getText().toString());
					r.execute(campouser.getText().toString(),campoNombre.getText().toString(),
							campopass.getText().toString(),campoemail.getText().toString(),
							sex,birth);
				}else{
					AlertDialog.Builder alert=new AlertDialog.Builder(this);
					alert.setTitle("Email");
					alert.setMessage("El email ingresado no es válido");
					alert.show();
				}
			}
		}
	}
	
	private EditText campouser;
	private EditText campoemail;
	private EditText campopass;
	private EditText campoconfpass;
	private EditText campoNombre;
	private TextView textoFecha;
	private Spinner selectSexo;
	private Button botonRegistro;

	private class RegistroAsyncTask extends AsyncTask<String,Void,String>{

		private Registro padre;
		private String user;
		
		public RegistroAsyncTask(Registro r,String us){
			padre = r;
			user=us;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/registrar.php");
			
	        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	        params.add(new BasicNameValuePair("user",arg0[0]));
	        params.add(new BasicNameValuePair("name",arg0[1]));
	        params.add(new BasicNameValuePair("pass",arg0[2]));
	        params.add(new BasicNameValuePair("email",arg0[3]));
	        params.add(new BasicNameValuePair("sex",arg0[4]));
	        params.add(new BasicNameValuePair("fecha",arg0[5]));
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
		
		private String getValores(HttpClient cliente, HttpPost post){
			String regs=null;
			try{
		    	HttpResponse response = cliente.execute(post);
		    	return inputStreamToString(response.getEntity().getContent()).toString();	
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	cliente.getConnectionManager().shutdown();
			return regs;
		}
		
		protected void onPostExecute(String result){
			if(result!=null){
				if(result.indexOf("Insertado")!=-1){
					String birth = year+"-"+month+"-"+day;
					String sex=padre.selectSexo.getSelectedItem().toString();
					
					SQLite sql = new SQLite(padre);
					sql.abrir();
					sql.addReg(user,sex,birth);
					sql.cerrar();
					
					Intent i = new Intent(padre,DrawerMenuActivity.class);
					i.putExtra("infoUser", user);
					i.putExtra("sex",sex);
					i.putExtra("birth", birth);
					i.putExtra("ambito", "registro");
					padre.startActivity(i);
					padre.finish();
				}else{
					
					AlertDialog.Builder alert=new AlertDialog.Builder(padre);
					alert.setTitle("Error de autenticación");
					alert.setMessage(result);
					alert.show();
				}
			}
		}
	}
}
