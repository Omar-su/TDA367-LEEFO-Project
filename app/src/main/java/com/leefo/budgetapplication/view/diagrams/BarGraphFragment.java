package com.leefo.budgetapplication.view.diagrams;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leefo.budgetapplication.R;

import java.util.ArrayList;

/**
 * The class that represents the fragment for the bar graph inside the budget fragment
 */
public class BarGraphFragment extends Fragment {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        //barChart = view.findViewById(R.id.barChart);

        getEntries();

        barDataSet = new BarDataSet(barEntries,"Data Set");
        barData = new BarData(barDataSet);

        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        return view;
    }

    private void getEntries(){
        //TODO: Create BarGraphAdapter
        //TODO: Create a Listview to show each element of the bargraph

        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f,2));
        barEntries.add(new BarEntry(2f,4));
        barEntries.add(new BarEntry(3f,1));
        barEntries.add(new BarEntry(5f,5));
        barEntries.add(new BarEntry(6f,3));
        barEntries.add(new BarEntry(7f,2));
    }
}