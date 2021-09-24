package com.leefo.budgetapplication.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.util.ArrayList;

/**
 * The class that represents the fragment for the category view inside the HomeFragment
 */
public class HomeCategoryViewFragment extends Fragment {

    PieChart pieChart;
    ListView listView;
    ListViewAdapterHomeList adapter;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category_view, container, false);

        pieChart = view.findViewById(R.id.pie_chart);
        listView = view.findViewById(R.id.listOfPieChart);
        setupPieChart();
        loadPieChartData();

        return view;
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(false); // false for pie chart, true for donut chart
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);

        //Disable the auto generated list of pie chart
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
    }

    private void loadPieChartData(){






        //adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(), something);
        listView.setAdapter(adapter);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(50,"Danslektioner"));
        entries.add(new PieEntry(100,"Mat"));
        entries.add(new PieEntry(100,"Musik"));

        ArrayList<Integer> myColors = new ArrayList<>();
        myColors.add(Color.parseColor("#558DF9"));
        myColors.add(Color.parseColor("#F95555"));
        myColors.add(Color.parseColor("#55F979"));

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(myColors);

        PieData data = new PieData(dataSet);

        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(24f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // update

        pieChart.animateY(1000, Easing.EaseInOutQuad);
    }

    private void getCategorySum(int id, String year, String month){
        Controller.getCategorySumByMonth(id, year, month);
    }

}