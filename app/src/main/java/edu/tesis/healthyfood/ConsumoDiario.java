package edu.tesis.healthyfood;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

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
import edu.tesis.healthyfood.sqlite.TMB;

public class ConsumoDiario extends Fragment implements  OnDateSetListener{

	private String user;
	private TextView textFecha;
	private int year;
	private int month;
	private int day;
	private ListView lista;

    public ConsumoDiario(){}

    public static ConsumoDiario newInstance(String usr){
        ConsumoDiario fragment=new ConsumoDiario();
        Bundle args = new Bundle();
        args.putString("user",usr);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_consumo_diario,container,false);

        user = getArguments().getString("user");

		lista = (ListView)v.findViewById(R.id.listView1);
		Button b = (Button)v.findViewById(R.id.button1);
		Button b2 = (Button)v.findViewById(R.id.button2);
		textFecha = (TextView)v.findViewById(R.id.textView2);
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH)+1;
		day = c.get(Calendar.DAY_OF_MONTH);
		
		String fecha  = year+"-"+month+"-"+day;
		
		textFecha.setText(fecha);
		
		ArrayList<ChartItem> charts = new ArrayList<ChartItem>();
		
		Date d= new Date();
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		charts.add(new LineChartItem(getDiario(d),this.getActivity(),"Ingesta diaria recomendada"));
		charts.add(new PieChartItem(this.getDiarioRecetas(d),this.getActivity(),"Porcentaje consumido por receta",
                getResources().getString(R.string.calorias_totales)));
		
		ChartDataAdapter cda = new ChartDataAdapter(this.getActivity(),charts);
		lista.setAdapter(cda);
		
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		b2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick2();
			}
		});
		
		return v;
	}

	
	private void onDateChange(int y,int m,int d){
		year  = y;
        month = m;
        day   = d;
        textFecha.setText(year+"-"+month+"-"+day);
        String fecha  = year+"-"+month+"-"+day;
		
		ArrayList<ChartItem> charts = new ArrayList<ChartItem>();
		
		Date dt= new Date();
		try {
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		charts.add(new LineChartItem(getDiario(dt),this.getActivity(),"Ingesta diaria recomendada"));
		charts.add(new PieChartItem(this.getDiarioRecetas(dt),this.getActivity(),"Porcentaje consumido por receta",
                getResources().getString(R.string.calorias_totales)));
		
		ChartDataAdapter cda = new ChartDataAdapter(this.getActivity(),charts);
		lista.setAdapter(cda);
		
	}
	
	private void botonOnClick(){
		//this.showDialog(DATE_PICKER_ID);
		
		DatePickerDialog dp=new DatePickerDialog(this.getActivity(),this,year,month-1,day);
		dp.show();
	}
	private void botonOnClick2(){
		//this.showDialog(DATE_PICKER_ID);
        final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
		ft.replace(R.id.content_frame, BuscaRecetas.newInstance(user), "Buscar Receta");
        ft.addToBackStack(null);
        ft.commit();
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

	private LineData getDiario(Date d){
		ArrayList<Entry> yVals = new ArrayList<Entry>();
		SQLite sql = new SQLite(this.getActivity());
		sql.abrir();
		ArrayList<EntradaDiario> mediciones=sql.getEntradasDia(user, d);
		TMB last = sql.getLastTMB(user);
		double idr=0;
		if(last!=null)
			idr=last.value*1.2;
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
        
        LineDataSet d1 = new LineDataSet(yVals,getResources().getString(R.string.calorias_consumidas_dia)+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(d));
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
	
	private PieData getDiarioRecetas(Date d) {
		
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
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		onDateChange(year,monthOfYear+1,dayOfMonth);
	}
}

