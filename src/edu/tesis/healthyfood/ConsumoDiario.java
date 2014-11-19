package edu.tesis.healthyfood;

import java.util.List;

import edu.tesis.healthyfood.charts.ChartItem;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ConsumoDiario extends ActionBarActivity {

	private String user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumo_diario);
		
		Intent i = this.getIntent();
		user= i.getExtras().getString("infoUser");
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consumo_diario, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class ChartDataAdapter extends ArrayAdapter<ChartItem>{

		public ChartDataAdapter(Context context, List<ChartItem> objects) {
	           super(context, 0, objects);
		}
		
		 @Override
	     public View getView(int position, View convertView, ViewGroup parent) {
			 return getItem(position).getView(position, convertView, getContext());
	     }
	        
	     @Override
	     public int getItemViewType(int position) {           
	         // return the views type
	         return getItem(position).getItemType();
	     }
	        
	     @Override
	     public int getViewTypeCount() {
	         return 3; // we have 3 different item-types
	     }
	}
}

