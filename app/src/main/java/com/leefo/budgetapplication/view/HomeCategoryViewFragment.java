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
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.model.TransactionFake;

import java.util.ArrayList;
import java.util.List;


public class HomeCategoryViewFragment extends Fragment {

    PieChart pieChart;
    ListView listView;
    ArrayList<TransactionFake> list;
    List<Transaction> transactionList;
    ListViewAdapterHomeList adapter;

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


        list = new ArrayList<>();
        // TODO hardcoded for now
        list.add(new TransactionFake(50, "Danslektioner", "#55F979"));
        list.add(new TransactionFake(100, "Mat", "#558DF9"));
        list.add(new TransactionFake(100, "Musik", "#F95555"));
        adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(), list);
        listView.setAdapter(adapter);

        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> myColorz = new ArrayList<>();
        fillCategorySums();

        for(Category c : DataBaseManager.getEveryCategory()) {
            entries.add(new PieEntry(c.getSum(), c.getName()));
            myColorz.add(Color.parseColor("#558DF9"));
        }

        /*
        entries.add(new PieEntry(50,"Danslektioner"));
        entries.add(new PieEntry(100,"Mat"));
        entries.add(new PieEntry(100,"Musik"));
        ArrayList<Integer> myColors = new ArrayList<>();
        myColors.add(Color.parseColor("#558DF9"));
        myColors.add(Color.parseColor("#F95555"));
        myColors.add(Color.parseColor("#55F979"));
         */
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(myColorz);

        PieData data = new PieData(dataSet);

        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(24f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // update

        pieChart.animateY(1000, Easing.EaseInOutQuad);
    }

    private void fillCategorySums() {
        for(Category c : DataBaseManager.getEveryCategory()){
            for(Transaction t : DataBaseManager.getAllTransactions()){
                if(c.getId() == t.getCategoryId()){
                    c.addToSum(t.getAmount());;
                }
            }
        }
    }
}