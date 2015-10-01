package edu.tesis.healthyfood;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.tesis.healthyfood.genericTasks.GenericCheckSession;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;

public class DrawerMenuActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    String user="";
    String username="";
    String sex="";
    String birth="";
    String ambito="";
    boolean fb=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent i = this.getIntent();
        user = i.getExtras().getString("infoUser");
        username = i.getExtras().getString("nombre");
        sex = i.getExtras().getString("sex");
        birth = i.getExtras().getString("birth");
        fb = i.getExtras().getBoolean("fb");


        try{
        	ambito = i.getExtras().getString("ambito");        	
        }
        catch(Exception e){
        	
        }
        mTitle = mDrawerTitle = getResources().getString(R.string.menu);
        mPlanetTitles = getResources().getStringArray(R.array.option_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setIcon(android.R.color.transparent);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
                ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
             }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0,true);
            if(ambito!=null){
                if(ambito.equals("registro")){
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, Medidor.newInstance(user,username,sex,birth, "perfil"), "Medidor");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setTitle("HealthyFood");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
       if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }
       switch(item.getItemId()) {
           case R.id.logout:
                Logout();
                return true;
           default:
               return super.onOptionsItemSelected(item);
        }
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){

            SQLite sql = new SQLite(this);
            sql.abrir();
            Sesion s = sql.getLastSesion();
            sql.cerrar();

            int decision = GenericCheckSession.generateSessionStart(sql, s);

            if(decision==GenericCheckSession.SESSION_STARTED){
                Intent i = this.getIntent();
                i.putExtra("infoUser", s.getUser());
                i.putExtra("sex",s.getSex());
                i.putExtra("birth", s.getBirth());

                user = i.getExtras().getString("infoUser");
                sex = i.getExtras().getString("sex");
                birth = i.getExtras().getString("birth");
            }
        }
    }

    private void Logout(){
        if(fb){
            FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentTransaction ft =fragmentManager.beginTransaction();
            Fragment prev=fragmentManager.findFragmentByTag("dialog");
            if(prev!=null){
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment dialogFragment = LogoutFacebookFragment.newInstance();
            dialogFragment.show(ft,"dialog");
        }else{
            GenericCheckSession.Logout(this);
        }
	}

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position,false);
        }
    }
    private void selectItem(int position,boolean saved) {
        Fragment fragment=new Fragment();
    	switch(position){
    		case 0:
    	        fragment = Perfil.newInstance(user,username,sex,birth);
    	        break;
    		case 1:
    			fragment = Recetas.newInstance(user);
    			break;
    		case 2:
    			fragment = Ejercicios.newInstance(user);
    	        break;
    		case 3:
                this.startActivityForResult(new Intent(this, SettingsActivity.class), 0);
    			break;
            case 4:
                Logout();
                break;
    	}

    	setTitle("HealthyFood");
        if(position<3){
            FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentTransaction ft =fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            if(!saved)ft.addToBackStack(null);
            ft.commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
