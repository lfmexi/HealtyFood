package edu.tesis.healthyfood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

public class ProgresoIMC extends Fragment {

	private LineChart pChart;
	private String user="";
	
	public ProgresoIMC (String us){
		user=us;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_progreso_imc, container, false);
		
		ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(3);
		
		PageAdapter a = new PageAdapter(getActivity().getSupportFragmentManager());
		pager.setAdapter(a);
		

		return view;
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
                f = new ChartFragment(user);
                break;
            case 1:
                f=new GridFragment(user);
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
