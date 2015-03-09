package edu.tesis.healthyfood;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ProgresoIMC extends FragmentActivity {

	private String user="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progreso_imc);

		Intent i = this.getIntent();
		user = i.getExtras().getString("infoUser");
		
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setOffscreenPageLimit(3);
		
		PageAdapter a = new PageAdapter(getSupportFragmentManager());
		pager.setAdapter(a);
	}

	private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm); 
        }

        @Override
        public Fragment getItem(int pos) {  
            Fragment f = null;
            
            switch(pos) {
            case 0:
                f = ChartFragment.newInstance(user);
                break;
            case 1:
                f=GridFragment.newInstance(user);
            	break;
            }

            return f;
        }

        @Override
        public int getCount() {
            return 2;
        }       
    }
}
