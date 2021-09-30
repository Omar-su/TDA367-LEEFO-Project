package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.view.MainActivity;

/**
 * Class that represents the fragment for the More page
 */
public class MoreFragment extends Fragment {

    private Button btnCompareMonths;
    private Button btnManageCategory;
    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        btnCompareMonths = view.findViewById(R.id.compare_month_button);
        btnManageCategory = view.findViewById(R.id.manage_categories_button);
        init_btnCompareMonths_OnClickListener();
        init_btnManageCategory_OnClickListener();
        return view;
    }

    private void init_btnManageCategory_OnClickListener(){
        btnManageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openFragmentInMainFrameLayout(new ManageCategoriesFragment());
            }
        });
    }

    private void init_btnCompareMonths_OnClickListener() {
        btnCompareMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openFragmentInMainFrameLayout(new CompareMonthsFragment());
            }
        });
    }


}