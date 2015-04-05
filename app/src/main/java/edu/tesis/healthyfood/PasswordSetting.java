package edu.tesis.healthyfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.List;

import edu.tesis.healthyfood.genericTasks.GenericJson;
import edu.tesis.healthyfood.settingsTasks.PasswordAsyncTask;
import edu.tesis.healthyfood.settingsTasks.SettingAsyncTask;
import edu.tesis.healthyfood.settingsTasks.UserAsyncTask;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;

/**
 * Created by luis on 15/03/15.
 */
public class PasswordSetting extends DialogPreference {

    private EditText password;
    private EditText newpass;
    private EditText confirm;
    private String usuario;

    public PasswordSetting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPositiveButtonText(context.getResources().getString(R.string.editar_button));
        setNegativeButtonText(context.getResources().getString(R.string.cancelar_button));
    }

    public PasswordSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(context.getResources().getString(R.string.editar_button));
        setNegativeButtonText(context.getResources().getString(R.string.cancelar_button));
    }

    @Override
    protected View onCreateDialogView(){
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_password_layout,null);
        return v;
    }

    @Override
    protected void onBindDialogView(View v){
        password=(EditText)v.findViewById(R.id.editPassword);
        newpass =(EditText)v.findViewById(R.id.editNuevoPassword);
        confirm=(EditText)v.findViewById(R.id.editConfirmNuevo);
        SQLite sql = new SQLite(this.getContext());
        sql.abrir();
        Sesion s = sql.getLastSesion();
        sql.cerrar();
        if(s!=null){
            usuario=s.getUser();
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult){
        if(positiveResult){

            if(password!=null){
                String npass=newpass.getText().toString();
                String conf = confirm.getText().toString();
                if(npass.equals(conf)){
                    PasswordAsyncTask passwordAsyncTask=new PasswordAsyncTask(this, SettingAsyncTask.UPDATE_ACTION);
                    passwordAsyncTask.execute(usuario,password.getText().toString(),npass);
                }else{
                    Toast.makeText(this.getContext(),"Las claves no coinciden",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class PasswordAsyncTask extends AsyncTask<String,Void,String> {

        private PasswordSetting padre;
        private int action;

        public PasswordAsyncTask(PasswordSetting r,int ac){
            padre = r;
            action=ac;
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            HttpClient cliente = new DefaultHttpClient();
            HttpPost post = new HttpPost(Login.url+"/changepass.php");

            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("user",arg0[0]));
            params.add(new BasicNameValuePair("newpass",arg0[2]));
            params.add(new BasicNameValuePair("oldpass",arg0[1]));
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
                    AlertDialog.Builder alert=new AlertDialog.Builder(padre.getContext());
                    alert.setTitle("Mensaje");
                    alert.setMessage(result);
                    alert.show();
            }
        }
    }


}
