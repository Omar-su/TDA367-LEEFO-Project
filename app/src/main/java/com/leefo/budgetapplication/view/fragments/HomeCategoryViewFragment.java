package com.leefo.budgetapplication.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.SharedViewModel;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.ViewObserver;
import com.leefo.budgetapplication.view.ViewObserverHandler;
import com.leefo.budgetapplication.view.adapters.CategoryViewListAdapter;

import java.util.ArrayList;

/**
 * The class that represents the fragment for the category view inside the HomeFragment
 */
public class HomeCategoryViewFragment extends Fragment implements ViewObserver {

    PieChart pieChart;
    ListView listView;
    CategoryViewListAdapter adapter;
    TextView noTransactoins1, noTransactoins2;
    TimePeriod timePeriod;
    SharedViewModel viewModel;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category_view, container, false);

        ViewObserverHandler.addObserver(this);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timePeriod = viewModel.getTimePeriod().getValue();

        pieChart = view.findViewById(R.id.pie_chart);
        listView = view.findViewById(R.id.listOfPieChart);

        setupPieChart();
        initList();

        noTransactoins1 = view.findViewById(R.id.noTransactionsYetText1);
        noTransactoins2 = view.findViewById(R.id.noTransactionsYetText2);

        updateData();

        return view;
    }

    private void updateData() {

        ArrayList<Category> list = Controller.removeEmptyCategories(Controller.getExpenseCategories(), timePeriod.getMonth(), timePeriod.getYear());
        list = Controller.sortCategoryListBySum(list, timePeriod.getMonth(), timePeriod.getYear());

        if (list.isEmpty()){
            noTransactoins1.setVisibility(View.VISIBLE);
            noTransactoins2.setVisibility(View.VISIBLE);
            pieChart.clear();
        } else {
            loadPieChartData(list);
            noTransactoins1.setVisibility(View.INVISIBLE);
            noTransactoins2.setVisibility(View.INVISIBLE);
        }

        updateList(list);
    }


    private void initList(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).openFragmentInMainFrameLayout(new SingleCategoryFragment());
                viewModel.singleCategory = (Category) adapterView.getItemAtPosition(i);
            }
        });
    }

    private void updateList(ArrayList<Category> list) {
        adapter = new CategoryViewListAdapter(viewModel.mainActivityContext, list, timePeriod);
        listView.setAdapter(adapter);
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(false); // false for pie chart, true for donut chart
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setNoDataText("");

        //Disable the auto generated list of pie chart
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
    }


    private void loadPieChartData(ArrayList<Category> list){

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> myColors = new ArrayList<>();

        float sum = 0;
        for(Category c :  list){
            sum = Controller.getTransactionSum(c, timePeriod.getMonth(), timePeriod.getYear());
            entries.add(new PieEntry(sum,""));
            myColors.add(Color.parseColor(c.getColor()));
        }

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(myColors);

        PieData data = new PieData(dataSet);

        data.setDrawValues(true);
        //data.setValueFormatter(new PercentFormatter(pieChart));

        data.setValueTextSize(18f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // update

        pieChart.animateY(800, Easing.EaseInOutQuad);
    }

    @Override
    public void update() {
        if (viewModel.lastOpenedViewWasCategoryView){
            updateData();
        }
    }
}