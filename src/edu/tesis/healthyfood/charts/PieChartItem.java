package edu.tesis.healthyfood.charts;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;

import edu.tesis.healthyfood.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class PieChartItem extends ChartItem {

	String descripcion;
	public PieChartItem(ChartData cd,Context c,String desc) {
		super(cd);
		descripcion=desc;
	}

	@Override
	public int getItemType() {
		return ChartItem.TYPE_PIECHART;
	}

	@Override
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
        holder.chart.setCenterText("Calorías totales\nconsumidas");
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
