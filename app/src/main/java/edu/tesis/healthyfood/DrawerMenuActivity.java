package edu.tesis.healthyfood;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
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
    String sex="";
    String birth="";
    String ambito="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i = this.getIntent();
        user = i.getExtras().getString("infoUser");
        sex = i.getExtras().getString("sex");
        birth = i.getExtras().getString("birth");
        
        try{
        	ambito = i.getExtras().getString("ambito");        	
        }
        catch(Exception e){
        	
        }
        //mTitle = mDrawerTitle = getTitle();
        mTitle = mDrawerTitle = "Men�";
        mPlanetTitles = getResources().getStringArray(R.array.option_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setIcon(android.R.color.transparent);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
     //           invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
       //         invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
            if(ambito!=null){
			if(ambito.equals("registro")){
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); 
                ft.replace(R.id.content_frame, Medidor.newInstance(user,sex,birth, "perfil"), "Medidor");
                ft.addToBackStack(null);
                ft.commit();
            }}

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
       if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }
       // Handle action buttons
       switch(item.getItemId()) {
	   case R.id.logout:
			Logout();
			return true;
       default:
           return super.onOptionsItemSelected(item);
       }
   }
    

	private void Logout(){
		SQLite sql = new SQLite(this);
		sql.abrir();
		Sesion s = sql.getLastSesion();
		if(sql.deleteSesion(s.getId())){
			sql.cerrar();
			this.finish();
			return;
		}
		sql.cerrar();
		this.finish();
	}

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment=new Fragment();
    	switch(position){
    		case 0:
    	        fragment = Perfil.newInstance(user,sex,birth);
    	        break;
    		case 1:
    			fragment = Recetas.newInstance(user);
    			break;
    		case 2:
    			fragment = Ejercicios.newInstance(user);
    	        break;
    		case 3:
    			Logout();
    			break;
    	
    	}
    	setTitle("HealthyFood");
    	//agregado el addToBackStack
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft =fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    

}
