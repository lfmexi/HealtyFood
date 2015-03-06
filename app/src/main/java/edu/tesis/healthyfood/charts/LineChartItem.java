package edu.tesis.healthyfood.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;

import edu.tesis.healthyfood.R;

public class LineChartItem extends ChartItem{

	private String descripcion;
	
	@SuppressWarnings("rawtypes")
	public LineChartItem(ChartData cd,Context c,String desc) {
		super(cd);
		descripcion=desc;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getItemType() {
		// TODO Auto-generated method stub
		return TYPE_LINECHART;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int pos, View convertView, Context c) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
			holder.chart = (LineChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.chart.setDrawYValues(true);
        holder.chart.setDescription(descripcion);
        holder.chart.setDrawVerticalGrid(false);
        holder.chart.setDrawGridBackground(false);

        XLabels xl = holder.chart.getXLabels();
        xl.setCenterXLabelText(true);
        xl.setPosition(XLabelPosition.BOTTOM);

        YLabels yl = holder.chart.getYLabels();
        yl.setLabelCount(5);

        // set data
        holder.chart.setData((LineData) chartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateX(1000);

        return convertView;
	}
	
	private static class ViewHolder{
		LineChart chart;
	}

}
