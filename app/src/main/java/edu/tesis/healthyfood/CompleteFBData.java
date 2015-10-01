package edu.tesis.healthyfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.tesis.healthyfood.genericTasks.GenericJson;
import edu.tesis.healthyfood.sqlite.SQLite;


public class CompleteFBData extends Activity {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean fb;
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_fbdata);
        campoemail = (EditText)this.findViewById(R.id.editEmail);
        selectSexo = (Spinner)this.findViewById(R.id.selectorSexo);
        textoFecha = (TextView) this.findViewById(R.id.textFecha);
        botonRegistro = (Button)this.findViewById(R.id.botonRegistrar);
        Button getFecha = (Button)this.findViewById(R.id.botonSelector);

        id = getIntent().getExtras().getString("id");
        username=getIntent().getExtras().getString("username");
        firstName=getIntent().getExtras().getString("firstName");
        lastName = getIntent().getExtras().getString("lastName");
        fb = getIntent().getExtras().getBoolean("fb");

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

        botonRegistro.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                botonOnClick();
            }
        });

        getFecha.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showDialog(DATE_PICKER_ID);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_PICKER_ID:
                return new DatePickerDialog(this,pickerListener,year,month-1,day);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete_fbdata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void botonOnClick(){
        if(!campoemail.getText().toString().equals("")){
            CharSequence c_email=campoemail.getText().toString();
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(c_email).matches()){
                String birth = year+"-"+month+"-"+day;
                String sex=selectSexo.getSelectedItem().toString();
                RegistroAsyncTask r = new RegistroAsyncTask(this,id,username);
                r.execute(
                        id,
                        firstName+" "+lastName,
                        "fromFB",
                        campoemail.getText().toString(),
                        sex,
                        birth
                );
            }
        }
    }

    private EditText campoemail;
    private TextView textoFecha;
    private Spinner selectSexo;
    private Button botonRegistro;

    private DatePickerDialog.OnDateSetListener pickerListener;

    private class RegistroAsyncTask extends AsyncTask<String,Void,String> {
        private CompleteFBData padre;
        private String user;
        private String name;

        public RegistroAsyncTask(CompleteFBData padre,String user,String name){
            this.user=user;
            this.padre=padre;
            this.name=name;
        }

        @Override
        protected String doInBackground(String... arg0) {
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

        private String getValores(HttpClient cliente, HttpPost post){
            String regs=null;
            try{
                HttpResponse response = cliente.execute(post);
                return GenericJson.inputStreamToString(response.getEntity().getContent()).toString();
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
                    sql.addReg(user,sex,birth,fb);
                    sql.cerrar();

                    Intent i = new Intent(padre,DrawerMenuActivity.class);
                    i.putExtra("infoUser", user);
                    i.putExtra("nombre",name);
                    i.putExtra("sex",sex);
                    i.putExtra("birth", birth);
                    i.putExtra("ambito", "registro");
                    i.putExtra("fb",true);
                    padre.startActivity(i);
                    padre.finish();
                }else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(padre);
                    alert.setTitle(padre.getResources().getString(R.string.error_auth));
                    alert.setMessage(result);
                    alert.show();
                }
            }
        }
    }
}
