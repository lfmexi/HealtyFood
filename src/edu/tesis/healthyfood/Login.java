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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
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
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

	public static final String url="http://healthylifeapp.esy.es";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		campo_username = (EditText)this.findViewById(R.id.loginUsername);
		campo_password = (EditText)this.findViewById(R.id.loginPassword);
		
		botonLogin = (Button)this.findViewById(R.id.main_boton_ingreso);
		botonForgot = (Button)this.findViewById(R.id.login_forgot);
		botonRegister = (Button)this.findViewById(R.id.login_boton_register);
		
		botonLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				loginOnClick();
			}
			
		});
		
		botonForgot.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginForgot();
			}
		});
		
		botonRegister.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginRegister();
			}}
		);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private void loginOnClick(){
		//en caso se logre realizar el login, ésto se realiza en la clase
		//que deriva de AsyncTask
		if(campo_username.getText()!=null && campo_password.getText()!=null){
			String nombre = campo_username.getText().toString();
			String pass = campo_password.getText().toString();
			
			if(nombre!=null && pass!=null){
				LoginAsyncTask lt = new LoginAsyncTask(this);
				lt.execute(nombre,pass);
			}
			
		}
	}
	
	private void loginForgot(){
		Intent i=new Intent(this,ForgotData.class);
		startActivity(i);
	}
	
	private void loginRegister(){
		Intent i= new Intent(this,Registrar.class);
		startActivity(i);
		finish();
	}
	
	private Button botonLogin;
	private Button botonForgot;
	private Button botonRegister;
	private EditText campo_username;
	private EditText campo_password;
	
	private class LoginAsyncTask extends AsyncTask<String,Void,String>{

		private Login padre;
		
		public LoginAsyncTask(Login p){
			padre = p;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(url+"/login.php");
			
	        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	        params.add(new BasicNameValuePair("user",arg0[0]));
	        params.add(new BasicNameValuePair("pass",arg0[1]));
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
		    	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    	JSONArray mArray = new JSONArray(jsonResult);
		    	int num_registros=mArray.length();
		    	for (int i = 0; i < num_registros; i++) {
		    	    JSONObject object = mArray.getJSONObject(i);
		    	    String campo1 = object.getString("nick");
		    	    regs=campo1;
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
		
		protected void onPostExecute(String result){
			if(result!=null){
				SQLite sql = new SQLite(padre);
				sql.abrir();
				sql.addReg(result);
				sql.cerrar();
				Intent i= new Intent(padre,MenuPrincipal.class);
				i.putExtra("infoUser", result);
				padre.startActivity(i);
				padre.finish();
			}else{
				AlertDialog.Builder alert=new AlertDialog.Builder(padre);
				alert.setTitle("Error de autenticación");
				alert.setMessage("Usuario o contraseña no válidos");
				alert.show();				
			}
		}
	}

}
