package edu.tesis.healthyfood.genericTasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.tesis.healthyfood.DrawerMenuActivity;
import edu.tesis.healthyfood.Login;
import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.TMB;

/**
 * Created by luis on 27/03/15.
 */
public class LoginAsyncTask extends AsyncTask<String,Void,String[]> {

    private Login padre;

    public LoginAsyncTask(Login p){
        padre = p;
    }

    @Override
    protected String[] doInBackground(String... arg0) {
        // TODO Auto-generated method stub
        HttpClient cliente = new DefaultHttpClient();
        HttpPost post = new HttpPost(Login.url+"/login.php");

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
            sql.addReg(result[0],result[1],result[2]);
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
            padre.startActivity(i);
            padre.finish();
        }else{
            AlertDialog.Builder alert=new AlertDialog.Builder(padre);
            alert.setTitle("Error de autenticaci�n");
            alert.setMessage("Usuario o contrase�a no v�lidos");
            alert.show();
        }
    }
}
