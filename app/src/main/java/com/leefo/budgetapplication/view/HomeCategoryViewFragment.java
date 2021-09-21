package com.leefo.budgetapplication.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leefo.budgetapplication.R;

import java.util.ArrayList;


public class HomeCategoryViewFragment extends Fragment {

    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category_view, container, false);

        pieChart = view.findViewById(R.id.pie_chart);
        setupPieChart();
        loadPieChartData();
        return view;
    }

    private void setupPieChart(){
       pieChart.setDrawHoleEnabled(false); // donut
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setCenterText("Spending by category");
        //pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        /*

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);

         */
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(100,"Mat"));
        entries.add(new PieEntry(100,"Musik"));
        entries.add(new PieEntry(50,"Danslektioner"));


        ArrayList<Integer> myColors = new ArrayList<>();
        myColors.add(Color.parseColor("#558DF9"));
        myColors.add(Color.parseColor("#F95555"));
        myColors.add(Color.parseColor("#55F979"));

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(myColors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // update

        pieChart.animateY(1000, Easing.EaseInOutQuad);
    }
}