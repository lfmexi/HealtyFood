package edu.tesis.healthyfood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.LimitLine;
import com.github.mikephil.charting.utils.LimitLine.LimitLabelPosition;

import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ProgresoIMC extends ActionBarActivity {

	private LineChart pChart;
	private String user="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progreso_imc);
		
		Intent i=this.getIntent();
		user = i.getExtras().getString("infoUser");
		
		pChart = (LineChart)this.findViewById(R.id.chartProgresoIMC);
		
		pChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener(){

			@Override
			public void onValueSelected(Entry e, int dataSetIndex) {
				// TODO Auto-generated method stub
				onSelected(e);
			}

			@Override
			public void onNothingSelected() {
				// TODO Auto-generated method stub
				
			}
		});
		
		pChart.setStartAtZero(false);
		
		pChart.setDescription("Historial de IMC del usuario "+user);
		
		pChart.setUnit(" kg/m^2");
		pChart.setDrawUnitsInChart(true);
		pChart.setDrawXLabels(true);
		
		pChart.setDrawBorder(true);
		pChart.setBorderPositions(new BorderPosition[] {
                BorderPosition.BOTTOM
        });
		
		pChart.setHighlightEnabled(true);
		pChart.setTouchEnabled(true);
		
		pChart.setDragEnabled(true);
		pChart.setScaleEnabled(true);
		pChart.setPinchZoom(true);
		
		pChart.setHighlightIndicatorEnabled(true);
		setData();
		pChart.animateX(2500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progreso_imc, menu);
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
	
	private void onSelected(Entry e){
		float val = e.getVal();
		if(val<16f){
			Toast.makeText(this, "Se encuentra en delgadez severa", Toast.LENGTH_SHORT).show();
		}else if(val<18.5f){
			Toast.makeText(this, "Se encuentra bajo peso", Toast.LENGTH_SHORT).show();	
		}else if(val<25f){
			Toast.makeText(this, "Se encuentra normal", Toast.LENGTH_SHORT).show();
		}else if(val<30f){
			Toast.makeText(this, "Se encuentra en sobrepeso", Toast.LENGTH_SHORT).show();
		}else if(val<40f){
			Toast.makeText(this, "Se encuentra obeso", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "Se encuentra en obesidad mórbida", Toast.LENGTH_SHORT).show();		
		}
	}
	
	private void setData() {
		
		SQLite sql = new SQLite(this);
		sql.abrir();
		ArrayList<Medicion> mediciones=sql.getMediciones(user);
		sql.cerrar();
		
		int count = mediciones.size();
		
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(
            		new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(mediciones.get(i).getFecha()));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
                                // 0.1) / 10);
            yVals.add(new Entry((float) mediciones.get(i).getImc(), i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "IMC por medición");

        // set the line to be drawn like this "- - - - - -"
        
        set1.enableDashedLine(15f, 1f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(5f);
        set1.setCircleSize(10f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        LimitLine infraPeso = new LimitLine(18.49f);
        infraPeso.setLineWidth(1f);
        infraPeso.enableDashedLine(10f, 2f, 0f);
        infraPeso.setDrawValue(true);
        infraPeso.setLabelPosition(LimitLabelPosition.RIGHT);
        infraPeso.setLineColor(Color.BLUE);
        
        LimitLine delgadezSevera = new LimitLine(15.99f);
        delgadezSevera.setLineWidth(1f);
        delgadezSevera.enableDashedLine(10f, 2f, 0f);
        delgadezSevera.setDrawValue(true);
        delgadezSevera.setLabelPosition(LimitLabelPosition.RIGHT);
        delgadezSevera.setLineColor(Color.GRAY);
        
        LimitLine sobrePeso =new LimitLine(25f);
        sobrePeso.setLineWidth(1f);
        sobrePeso.enableDashedLine(10f, 2f, 0f);
        sobrePeso.setDrawValue(true);
        sobrePeso.setLabelPosition(LimitLabelPosition.RIGHT);
        sobrePeso.setLineColor(Color.RED);

        LimitLine obesidad =new LimitLine(30f);
        obesidad.setLineWidth(1f);
        obesidad.enableDashedLine(10f, 2f, 0f);
        obesidad.setDrawValue(true);
        obesidad.setLabelPosition(LimitLabelPosition.RIGHT);
        obesidad.setLineColor(Color.MAGENTA);
        
        LimitLine obesidadM =new LimitLine(40f);
        obesidadM.setLineWidth(1f);
        obesidadM.enableDashedLine(10f, 2f, 0f);
        obesidadM.setDrawValue(true);
        obesidadM.setLabelPosition(LimitLabelPosition.RIGHT);
        obesidadM.setLineColor(Color.DKGRAY);
        
        data.addLimitLine(delgadezSevera);
        data.addLimitLine(infraPeso);
        data.addLimitLine(sobrePeso);
        data.addLimitLine(obesidad);
        data.addLimitLine(obesidadM);
        
        pChart.setData(data);
    }

}
