package com.leefo.budgetapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;
import com.leefo.budgetapplication.view.adapters.ManageCategoriesListAdapter;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;

import java.util.ArrayList;


public class ManageCategoriesFragment extends Fragment {

    private Button newCategoryButton;
    private ListView listView;
    private RadioGroup radioGroup;
    private ManageCategoriesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);
        radioGroup = view.findViewById(R.id.manage_categories_radioGroup);
        listView = view.findViewById(R.id.listView_manage_categories);
        adapter = new ManageCategoriesListAdapter(getActivity().getApplicationContext(), Controller.getExpenseCategories());
        listView.setAdapter(adapter);
        initRadioGroup();
        return view;
    }

    public void initRadioGroup(){

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.manage_categories_radio_expense){
                    adapter = new ManageCategoriesListAdapter(getActivity().getApplicationContext(), Controller.getExpenseCategories());
                } else {
                    adapter = new ManageCategoriesListAdapter(getActivity().getApplicationContext(), Controller.getIncomeCategories());
                }
                listView.setAdapter(adapter);
            }
        });
    }


}