package edu.tesis.healthyfood;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

import edu.tesis.healthyfood.genericTasks.GenericCheckSession;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

    }

    @Override
    public void onBuildHeaders(List<Header> target){
        loadHeadersFromResource(R.xml.pref_headers,target);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static class AccountSettingFrag extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstance){
            super.onCreate(savedInstance);
            addPreferencesFromResource(R.xml.pref_account_setting);
        }
    }

    public static class SyncSettingFrag extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstance){
            super.onCreate(savedInstance);
            addPreferencesFromResource(R.xml.pref_sync_setting);
        }
    }

}
