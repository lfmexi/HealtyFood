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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

@SuppressWarnings("deprecation")
public class ForgotData extends Activity {
	EditText editable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_data);
		
		Button b = (Button)this.findViewById(R.id.button1);
		editable = (EditText)this.findViewById(R.id.editText1);
		
		b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		
	}

	private void botonOnClick(){
		if(editable.getText()!=null){
			if(!editable.getText().toString().equals("")){
				CharSequence c_email=editable.getText().toString();
				if(android.util.Patterns.EMAIL_ADDRESS.matcher(c_email).matches()){
					AsyncForgot as =new AsyncForgot(this);
					as.execute(c_email.toString());
				}else{
					AlertDialog.Builder alert=new AlertDialog.Builder(this);
					alert.setTitle("Error");
					alert.setMessage("El email ingresado no es válido");
					alert.show();
				}
			}else{
				AlertDialog.Builder alert=new AlertDialog.Builder(this);
				alert.setTitle("Error");
				alert.setMessage("El email ingresado no es válido");
				alert.show();
			}
		}else{
			AlertDialog.Builder alert=new AlertDialog.Builder(this);
			alert.setTitle("Error");
			alert.setMessage("El email ingresado no es válido");
			alert.show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_data, menu);
		return true;
	}
	
	private class AsyncForgot extends AsyncTask<String,Void,String>{
		ForgotData padre;
		public AsyncForgot(ForgotData f){
			padre=f;
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			HttpClient cliente = new DefaultHttpClient();
			HttpPost post = new HttpPost(Login.url+"/getData.php");
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	        params.add(new BasicNameValuePair("email",arg0[0]));
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
				AlertDialog.Builder alert=new AlertDialog.Builder(padre);
				alert.setTitle("Información solicitada");
				alert.setMessage("Se le enviará la información al correo electrónico si es válido");
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						padre.finish();
					}
		        });
				alert.show();
			}else{
				AlertDialog.Builder alert=new AlertDialog.Builder(padre);
				alert.setTitle("Error");
				alert.setMessage("Información no enviada");
				alert.show();
			}
		}
	}

}
