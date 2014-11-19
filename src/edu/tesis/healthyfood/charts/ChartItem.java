package edu.tesis.healthyfood.charts;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

public abstract class ChartItem {
	protected static final int TYPE_BARCHART = 0;
    protected static final int TYPE_LINECHART = 1;
    protected static final int TYPE_PIECHART = 2;
    
    protected ChartData chartData;
    
    public ChartItem(ChartData cd) {
        this.chartData = cd;      
    }
    
    public abstract int getItemType();
    
    public abstract View getView(int pos, View convertView, Context c);
    
}
