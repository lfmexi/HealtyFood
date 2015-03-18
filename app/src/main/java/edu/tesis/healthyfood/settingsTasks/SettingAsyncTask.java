package edu.tesis.healthyfood.settingsTasks;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.tesis.healthyfood.Login;

/**
 * Created by luis on 15/03/15.
 */
public abstract class SettingAsyncTask extends AsyncTask<String,Void,String[]> {


    public static final int LOAD_ACTION=0;
    public static final int UPDATE_ACTION=1;

    private Object object;

    public SettingAsyncTask(Object object){
        this.object=object;
    }

    @Override
    protected String[] doInBackground(String... arg0) {
        HttpClient client = new DefaultHttpClient();
        return getArrayFromJSON(client,arg0);
    }

    protected abstract String [] getArrayFromJSON(HttpClient client,String[]args);

    public Object getObject(){
        return object;
    }

    public void setObject(Object o){
        object = o;
    }

}
