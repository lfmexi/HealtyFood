package edu.tesis.healthyfood.settingsTasks;

import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.tesis.healthyfood.Login;
import edu.tesis.healthyfood.UserSetting;
import edu.tesis.healthyfood.genericTasks.GenericJson;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;

/**
 * Created by luis on 15/03/15.
 */
public class UserAsyncTask extends SettingAsyncTask {

    private int action;

    public UserAsyncTask(Object object,int act) {
        super(object);
        action=act;
    }

    @Override
    protected String[]getArrayFromJSON(HttpClient client, String[] args) {

        String[]regs = null;

        String url = Login.url+"/update.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user",args[0]));

        switch (action){
            case LOAD_ACTION:
                params.add(new BasicNameValuePair("action","load"));
                break;
            case UPDATE_ACTION:
                params.add(new BasicNameValuePair("action","update"));
                params.add(new BasicNameValuePair("name",args[1]));
                params.add(new BasicNameValuePair("email",args[2]));
                break;
        }

        HttpPost post = new HttpPost(url);
        try{
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(post);
            String json = GenericJson.inputStreamToString(response.getEntity().getContent()).toString();
            if(json!=null){
                if(action==LOAD_ACTION){
                    JSONArray array = new JSONArray(json);
                    regs=new String[3];
                    regs[0]=(array.getJSONObject(0).getString("user"));
                    regs[1]=(array.getJSONObject(0).getString("name"));
                    regs[2]=(array.getJSONObject(0).getString("email"));

                }else if(action==UPDATE_ACTION){
                    JSONArray array = new JSONArray(json);
                    regs=new String[2];
                    regs[0]=(array.getJSONObject(0).getString("success"));
                    regs[1]=args[0];
                }
            }
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.getConnectionManager().shutdown();
        return regs;
    }

    @Override
    protected void onPostExecute(String[]result){
        if(result!=null){
            if(action==LOAD_ACTION){
                UserSetting us = (UserSetting)getObject();
                us.setTextUsername(result[0]);
                us.setTextUser(result[1]);
                us.setTextEmail(result[2]);
            }else if(action==UPDATE_ACTION){
                if(result[0].equals("success")){
                    SQLite sqlite=new SQLite(((UserSetting)getObject()).getContext());
                    sqlite.abrir();
                    Sesion ses = sqlite.getLastSesion();
                    if(sqlite.updateSession(result[1],ses.getId())){
                        Toast.makeText(((UserSetting)getObject()).getContext(),"Datos actualizados",Toast.LENGTH_LONG);
                    }else{
                        Toast.makeText(((UserSetting)getObject()).getContext(),"Error al guardar los cambios, sincronice por favor",Toast.LENGTH_LONG);
                    }
                }
            }
        }
    }
}
