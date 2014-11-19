package edu.tesis.healthyfood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.LimitLine;
import com.github.mikephil.charting.utils.LimitLine.LimitLabelPosition;

import edu.tesis.healthyfood.charts.ChartItem;
import edu.tesis.healthyfood.charts.LineChartItem;
import edu.tesis.healthyfood.charts.PieChartItem;
import edu.tesis.healthyfood.sqlite.EntradaDiario;
import edu.tesis.healthyfood.sqlite.SQLite;

public class ConsumoDiario extends Fragment {

	private String user;
	
	public ConsumoDiario (String usr){
		user =usr;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_consumo_diario,container, false);
		
		ListView lista = (ListView)view.findViewById(R.id.listView1);
		
		ArrayList<ChartItem> charts = new ArrayList<ChartItem>();
		
		charts.add(new LineChartItem(getDiario(),getActivity(),"Ingesta diaria recomendada"));
		charts.add(new PieChartItem(this.getDiarioRecetas(),getActivity(),"Porcentaje consumido por receta"));
		
		ChartDataAdapter cda = new ChartDataAdapter(this.getActivity(),charts);
		lista.setAdapter(cda);
		return view;
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
	        return getItem(position).getItemType();
	    }
	        
	    @Override
	    public int getViewTypeCount() {
	        return 3;
	    }
	}

	private LineData getDiario(){
		ArrayList<Entry> yVals = new ArrayList<Entry>();
		Date d = new Date();
		SQLite sql = new SQLite(this.getActivity());
		sql.abrir();
		ArrayList<EntradaDiario> mediciones=sql.getEntradasDia(user, d);
		double idr=sql.getLastTMB(user).value*1.2;
		sql.cerrar();
		
		int count= 0;
		
		if(mediciones!=null) count = mediciones.size();
		
		ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mediciones.get(i).hora+":"+mediciones.get(i).minuto+":"+mediciones.get(i).segundo);
        }
        
        float acumulado = 0f;
        
        for (int i = 0; i < count; i++) {
        	acumulado +=(float)mediciones.get(i).calorias;
            yVals.add(new Entry(acumulado, i));
        }
        
        LineDataSet d1 = new LineDataSet(yVals, "Calorías consumidas el día de hoy: "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(d));
        d1.setLineWidth(3f);
        d1.setCircleSize(5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
		
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(d1); 
        
        LimitLine limiteCalorico = new LimitLine((float)idr);
        limiteCalorico.setLineWidth(1f);
        limiteCalorico.enableDashedLine(10f, 2f, 0f);
        limiteCalorico.setDrawValue(true);
        limiteCalorico.setLabelPosition(LimitLabelPosition.RIGHT);
        limiteCalorico.setLineColor(Color.RED);
        
        LineData data = new LineData(xVals,dataSets);
        data.addLimitLine(limiteCalorico);
        
		return data;
	}
	
	private PieData getDiarioRecetas() {
		
		Date d = new Date();
		SQLite sql = new SQLite(this.getActivity());
		sql.abrir();
		ArrayList<EntradaDiario> mediciones=sql.getEntradasDia(user, d);
		sql.cerrar();
		
		int count =0;
		if(mediciones!=null) count =mediciones.size();
		
		
		ArrayList<String> recetas = new ArrayList<String>();
		
		for(int i =0;i<count;i++){
			recetas.add(mediciones.get(i).receta+"->"+mediciones.get(i).calorias+" cal");
		}
		
		ArrayList<Entry> entradas = new ArrayList<Entry>();
		
		for(int i=0;i<count;i++){
			entradas.add(new Entry((int)mediciones.get(i).calorias,i));
		}
		
		PieDataSet data = new PieDataSet(entradas,"");
		
		data.setSliceSpace(5f);
        data.setColors(ColorTemplate.COLORFUL_COLORS);
        
        PieData cd = new PieData(recetas, data);
        return cd;
	}
}

