package com.leefo.budgetapplication.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.SharedViewData;
import com.leefo.budgetapplication.view.adapters.CategoryViewListAdapter;

import java.util.ArrayList;

/**
 * The class that represents the fragment for the category view inside the HomeFragment
 */
public class HomeCategoryViewFragment extends Fragment {

    PieChart pieChart;
    ListView listView;
    CategoryViewListAdapter adapter;

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
        initList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).openSingleCategoryFragment();
                SharedViewData.singleCatgery = (Category) adapterView.getItemAtPosition(i);
            }
        });

        return view;
    }

    private void initList(){
        ArrayList<Category> notEmptyCategories = new ArrayList<>();
        for (Category c : Controller.getAllCategories()){
            if (!Controller.getTransactions(c).isEmpty()){
                notEmptyCategories.add(c);
            }
        }
        adapter = new CategoryViewListAdapter(getActivity().getApplicationContext(),notEmptyCategories);
        listView.setAdapter(adapter);
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

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> myColors = new ArrayList<>();

        double sum = 0;
        for(Category c :  Controller.getAllCategories()){
            /*
            sum = 0;
            for(Transaction t : Controller.getAllTransactions()){
                if(t.getCategoryId() == c.getId()){
                    sum += t.getAmount();
                }
            }
             */
            sum = Controller.getTransactionSum(c); // TODO dates
            if (sum != 0){
                entries.add(new PieEntry((float)(sum*-1),c.getName()));
                myColors.add(Color.parseColor(c.getColor()));
            }
        }



        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(myColors);

        PieData data = new PieData(dataSet);

        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // update

        pieChart.animateY(1000, Easing.EaseInOutQuad);
    }

}