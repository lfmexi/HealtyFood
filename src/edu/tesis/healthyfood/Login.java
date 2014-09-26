package edu.tesis.healthyfood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

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
		this.finish();
		Intent i = new Intent(this,MenuPrincipal.class);
		this.startActivity(i);
	}
	
	private void loginForgot(){
		
	}
	
	private void loginRegister(){
		
		
	}
	
	private Button botonLogin;
	private Button botonForgot;
	private Button botonRegister;
	private EditText campo_username;
	private EditText campo_password;
	
	private class LoginAsyncTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
