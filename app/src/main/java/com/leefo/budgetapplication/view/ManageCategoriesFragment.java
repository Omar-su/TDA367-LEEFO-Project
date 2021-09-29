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
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;


public class ManageCategoriesFragment extends Fragment {

    private Button newCategoryButton;
    private ListView listView;
    private RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

        //adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(),transactions);
        //listView.setAdapter(adapter);
        return view;
    }


}