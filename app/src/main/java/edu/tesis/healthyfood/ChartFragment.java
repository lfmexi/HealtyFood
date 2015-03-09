package edu.tesis.healthyfood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.LimitLine;
import com.github.mikephil.charting.utils.LimitLine.LimitLabelPosition;

import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ChartFragment extends Fragment {
	
	private String user="";
	private LineChart pChart;

    public ChartFragment(){}

    public static ChartFragment newInstance(String usr){
        ChartFragment fragment=new ChartFragment();
        Bundle args = new Bundle();
        args.putString("user",usr);
        fragment.setArguments(args);
        return fragment;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.frag_chart_progreso, container,false);

        this.user = getArguments().getString("user");

        pChart = (LineChart)v.findViewById(R.id.chartProgresoIMC);
		
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
		return v;
	}
	
	private void onSelected(Entry e){
		float val = e.getVal();
		if(val<16f){
			Toast.makeText(getActivity(), getResources().getString(R.string.delgadez_servera), Toast.LENGTH_SHORT).show();
		}else if(val<18.5f){
			Toast.makeText(getActivity(), getResources().getString(R.string.bajo_peso), Toast.LENGTH_SHORT).show();
		}else if(val<25f){
			Toast.makeText(getActivity(), getResources().getString(R.string.normal), Toast.LENGTH_SHORT).show();
		}else if(val<30f){
			Toast.makeText(getActivity(), getResources().getString(R.string.sobrepeso), Toast.LENGTH_SHORT).show();
		}else if(val<40f){
			Toast.makeText(getActivity(), getResources().getString(R.string.obeso), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getActivity(), getResources().getString(R.string.obesidad_morbidad), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setData() {
		
		SQLite sql = new SQLite(getActivity());
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

        LineDataSet set1 = new LineDataSet(yVals, getResources().getString(R.string.imc_por_medicion));

        
        set1.enableDashedLine(15f, 1f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(5f);
        set1.setCircleSize(10f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); 

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
