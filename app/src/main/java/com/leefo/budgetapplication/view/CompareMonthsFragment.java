package com.leefo.budgetapplication.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.leefo.budgetapplication.R;

import java.util.ArrayList;

/**
 * The class that represents the fragment for compare months
 */
public class CompareMonthsFragment extends Fragment {

    BarChart mpBarChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare_months, container, false);
        mpBarChart = view.findViewById(R.id.barChart);
        compareTwoMonths();
        return view;
    }

    private void compareTwoMonths() {
        BarDataSet barDataSet1 = new BarDataSet(foodEntries(), "DataSet 1");
        barDataSet1.setColor(Color.RED);
        BarDataSet barDataSet2 = new BarDataSet(restaurantEntries(), "DataSet 2");
        barDataSet2.setColor(Color.BLUE);
        BarDataSet barDataSet3 = new BarDataSet(houseRentEntries(), "DataSet 3");
        barDataSet3.setColor(Color.MAGENTA);
        BarDataSet barDataSet4 = new BarDataSet(alcoholEntries(), "DataSet 4");
        barDataSet4.setColor(Color.GREEN);

        BarData data = new BarData(barDataSet1, barDataSet2, barDataSet3, barDataSet4);
        mpBarChart.setData(data);
        mpBarChart.getDescription().setEnabled(false);
        mpBarChart.getLegend().setEnabled(false);

        String[] days = new String[]{
                "January", "February"
        };
        XAxis xAxis = mpBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        mpBarChart.setDragEnabled(true);
        mpBarChart.setVisibleXRangeMaximum(3);

        float barSpace = 0.05f;
        float groupSpace = 0.16f;
        data.setBarWidth(0.16f);

        //(barwidth + barspace)* no of bars + groupspace = 1
        // no of bars = 4

        mpBarChart.getXAxis().setAxisMinimum(0);
        mpBarChart.getXAxis().setAxisMaximum(0+mpBarChart.getBarData().getGroupWidth(groupSpace, barSpace)*2);
        mpBarChart.getAxisLeft().setAxisMinimum(0);

        mpBarChart.groupBars(0, groupSpace, barSpace);

        mpBarChart.invalidate();
    }

    private ArrayList<BarEntry> foodEntries(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,2000));
        barEntries.add(new BarEntry(2,791));
        return barEntries;
    }

    private ArrayList<BarEntry> restaurantEntries(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,900));
        barEntries.add(new BarEntry(2,631));

        return barEntries;
    }

    private ArrayList<BarEntry> houseRentEntries(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 1; i <= 2; i++){
            barEntries.add(new BarEntry(i, 5900));
        }
        return barEntries;
    }

    private ArrayList<BarEntry> alcoholEntries(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,100));
        barEntries.add(new BarEntry(2,291));

        return barEntries;
    }

}