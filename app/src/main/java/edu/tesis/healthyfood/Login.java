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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.Profile;

import edu.tesis.healthyfood.conn.Connection;
import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.TMB;

@SuppressWarnings("deprecation")
public class Login extends FragmentActivity {

	public static final String url=Connection.url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        Log.i("Hash",Connection.printKeyHash(this));

        campo_username = (EditText)this.findViewById(R.id.loginUsername);
		campo_password = (EditText)this.findViewById(R.id.loginPassword);
		
		botonLogin = (Button)this.findViewById(R.id.main_boton_ingreso);
		botonForgot = (Button)this.findViewById(R.id.login_forgot);
		botonRegister = (Button)this.findViewById(R.id.login_boton_register);
        social = (Button)this.findViewById(R.id.socialButton);
		
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

        social.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                socialOnClick();
            }
        });
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

    private void socialOnClick(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft =fragmentManager.beginTransaction();
        Fragment prev=fragmentManager.findFragmentByTag("dialog");
        if(prev!=null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = SocialDialogFragment.newInstance();
        dialogFragment.show(ft,"dialog");
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
		Intent i= new Intent(this,Registro.class);
		startActivity(i);
		finish();
	}

    public void loginFacebook(Profile profile){
        new LoginAsyncTask(this,true,profile).execute(
                profile.getName(),
                "fromFB"
        );
    }

	private Button botonLogin;
	private Button botonForgot;
	private Button botonRegister;
	private EditText campo_username;
	private EditText campo_password;
    private Button social;
	
	private class LoginAsyncTask extends AsyncTask<String,Void,String[]>{

		private Login padre;
        private boolean fb;
		private Profile profile;
		public LoginAsyncTask(Login p){
			padre = p;
		}

        public LoginAsyncTask(Login p,boolean fromFB,Profile profile){
            padre = p;
            fb=fromFB;
            this.profile = profile;
        }

		@Override
		protected String[] doInBackground(String... arg0) {
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
		
		private String[] getValores(HttpClient cliente, HttpPost post){
			String [] regs=null;
			try{
		    	HttpResponse response = cliente.execute(post);
		    	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    	JSONArray mArray = new JSONArray(jsonResult);
		    	int num_registros=mArray.length();
		    	if(num_registros>0)regs = new String[3];
		    	for (int i = 0; i < num_registros; i++) {
		    	    JSONObject object = mArray.getJSONObject(i);
		    	    String campo1 = object.getString("nick");
		    	    String campo2 = object.getString("sex");
		    	    String campo3 = object.getString("birth");
		    	    regs[0]=campo1;
		    	    regs[1]=campo2;
		    	    regs[2]=campo3;
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
				SQLite sql = new SQLite(padre);
				sql.abrir();
                if(!fb)
				    sql.addReg(result[0],result[1],result[2]);
                else
                    sql.addReg(result[0],result[1],result[2],fb);
				sql.cerrar();
				
				String [] fecha = result[2].split("-");
				int year = Integer.parseInt(fecha[0]);
				int month = Integer.parseInt(fecha[1]);
				int day = Integer.parseInt(fecha[2]);
				
				Calendar c = Calendar.getInstance();
				
				int year_today=c.get(Calendar.YEAR);
				int month_today=c.get(Calendar.MONTH);
				int day_today = c.get(Calendar.DAY_OF_MONTH);
				
				sql.abrir();
				TMB tmb = sql.getLastTMB(result[0]);
				Medicion med = sql.getLastMedicion(result[0]);
				sql.cerrar();
				
				if(tmb!=null && med!=null){
					String[] lastFecha = tmb.fecha_tomado.split("-");
					
					int yearlast = Integer.parseInt(lastFecha[0]);
					
					boolean calcular = false;
					
					if(yearlast<year_today){
						if(year==yearlast+1){
							if((month_today-month)>=0 && (day_today-day)>=0){
								calcular = true;
							}
						}else{
							calcular =false;
						}
					}
					if(calcular){
						int edad = year_today-year;
						if((month_today-month)<0){
							edad -=1;
						}else if((day_today-day)<0){
							edad-=1;
						}
						double tmb_val=(10*med.getPeso())+(6.25*med.getAltura()*100)-(5*edad);
						if(result[1].equals("Hombre")){
							tmb_val+=5;
						}else{
							tmb_val-=161;
						}
						sql.abrir();
						sql.addTMB(result[0], tmb_val);
						sql.cerrar();
					}
				}else if(med!=null){
					int diferencia = year_today-year;
					double tmb_val=(10*med.getPeso())+(6.25*med.getAltura()*100)-(5*diferencia);
					if(result[1].equals("Hombre")){
						tmb_val+=5;
					}else{
						tmb_val-=161;
					}
					sql.abrir();
					sql.addTMB(result[0], tmb_val);
					sql.cerrar();
				}
				
				Intent i= new Intent(padre,DrawerMenuActivity.class);
				i.putExtra("infoUser", result[0]);
				i.putExtra("sex",result[1]);
				i.putExtra("birth", result[2]);
                i.putExtra("fb",fb);
				padre.startActivity(i);
				padre.finish();
			}else{
                if(!fb){
                    AlertDialog.Builder alert=new AlertDialog.Builder(padre);
                    alert.setTitle(padre.getResources().getString(R.string.error_auth));
                    alert.setMessage(padre.getResources().getString(R.string.user_o_pass));
                    alert.show();
                }else if(profile!=null){
                    String id = profile.getId();
                    String username = profile.getName();
                    String firstName = profile.getFirstName();
                    String lastName = profile.getLastName();

                    Intent intent=new Intent(padre,CompleteFBData.class);
                    intent.putExtra("id",id);
                    intent.putExtra("username",username);
                    intent.putExtra("firstName",firstName);
                    intent.putExtra("lastName",lastName);
                    intent.putExtra("fb",true);

                    padre.startActivity(intent);
                    padre.finish();
                }
			}
		}
	}

}
