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
import androidx.lifecycle.Observer;
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
import com.leefo.budgetapplication.view.ParcelableCategory;
import com.leefo.budgetapplication.view.TimePeriodViewModel;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.adapters.CategoryListAdapter;

import java.util.ArrayList;

/**
 * This class represents the fragment for the pie chart view inside the HomeFragment
 * The fragment consists of
 *      - a pie chart
 *      - list of the expense categories
 * The list shows either all expense categories or categories for a chosen month
 * The pie chart shows what percentage, of 100 %, each category is equivalent too.
 * Opens SingleCategoryFragment, when a category (from the list) is clicked
 * Used by (opened from) HomeFragment
 * @author Emelie Edberg, Eugene Dvoryankov
 */
public class HomeCategoryViewFragment extends Fragment {

    PieChart pieChart;
    ListView listView;
    CategoryListAdapter adapter;
    TextView noTransactions1, noTransactions2;
    TimePeriod timePeriod;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category_view, container, false);

        TimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(TimePeriodViewModel.class);
        timePeriod = viewModel.getTimePeriodLiveData().getValue();

        viewModel.getTimePeriodLiveData().observe(getViewLifecycleOwner(), new Observer<TimePeriod>() {
            @Override
            public void onChanged(TimePeriod newTimePeriod) {
                //timePeriod = newTimePeriod; // not needed apparently. the data is ALIVE :)
                updateData();
            }
        });

        pieChart = view.findViewById(R.id.pie_chart);
        listView = view.findViewById(R.id.listOfPieChart);

        setupPieChart();
        initList();

        noTransactions1 = view.findViewById(R.id.noTransactionsYetText1);
        noTransactions2 = view.findViewById(R.id.noTransactionsYetText2);

        updateData();

        return view;
    }

    private void updateData() {
        ArrayList<Category> list = Controller.getExpenseCategories();
        Controller.removeEmptyCategories(list, timePeriod.getMonth(), timePeriod.getYear());
        Controller.sortCategoryListBySum(list, timePeriod.getMonth(), timePeriod.getYear());

        if (list.isEmpty()){
            noTransactions1.setVisibility(View.VISIBLE);
            noTransactions2.setVisibility(View.VISIBLE);
            pieChart.clear();
        } else {
            loadPieChartData(list);
            noTransactions1.setVisibility(View.INVISIBLE);
            noTransactions2.setVisibility(View.INVISIBLE);
        }

        updateList(list);
    }


    private void initList(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) adapterView.getItemAtPosition(i);
                Fragment fragment = new SingleCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CHOSEN_CATEGORY", new ParcelableCategory(category));
                fragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
            }
        });
    }

    private void updateList(ArrayList<Category> list) {
        if (getActivity() != null){
            adapter = new CategoryListAdapter(getActivity().getApplicationContext(), list, timePeriod);
            listView.setAdapter(adapter);
        }

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

        float sum;
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

}