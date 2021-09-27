package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.view.adapters.CategoryListAdapter;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

public class SingleCategoryFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_category, container, false);

        listView = view.findViewById(R.id.listview_single_category);
        //ListViewAdapterHomeList adapter = new CategoryListAdapter(getActivity().getApplicationContext(),notEmptyCategories);
        //listView.setAdapter(adapter);

        return view;
    }
}
