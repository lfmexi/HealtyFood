package edu.tesis.healthyfood.charts;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;

import edu.tesis.healthyfood.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class PieChartItem extends ChartItem {

	String descripcion;
    String center;
	@SuppressWarnings("rawtypes")
	public PieChartItem(ChartData cd,Context c,String desc,String center) {
		super(cd);
		descripcion=desc;
        this.center=center;
	}

	@Override
	public int getItemType() {
		return ChartItem.TYPE_PIECHART;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int pos, View convertView, Context c) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_piechart, null);
            holder.chart = (PieChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);
        }else{
        	holder = (ViewHolder) convertView.getTag();
        }
        
        holder.chart.setDescription(descripcion);
        holder.chart.setHoleRadius(40f);
        holder.chart.setTransparentCircleRadius(40f);
        holder.chart.setCenterText(center);
        holder.chart.setCenterTextSize(20f);
        holder.chart.setDrawXValues(true);
        holder.chart.setUsePercentValues(true);
        
        holder.chart.setData((PieData) chartData);
        
        Legend l = holder.chart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        holder.chart.animateXY(900, 900);

        return convertView;
	}

	private static class ViewHolder{
		PieChart chart;
	}
}
