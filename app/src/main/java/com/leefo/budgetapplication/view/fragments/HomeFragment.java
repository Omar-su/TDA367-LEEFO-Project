package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.view.SharedViewData;

/**
 * Class that represents the fragment for the Home page
 */
public class HomeFragment extends Fragment {

    private TextView income, expense, balance;
    private boolean openListViewFirst;
    ToggleButton toggle;
    boolean lastOpenedViewWasCategoryView;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toggle = view.findViewById(R.id.toggleButton);
        lastOpenedViewWasCategoryView = SharedViewData.lastOpenedViewWasCategoryView;

        // toggle is set to listView mode as default, if categoryView shall open first it needs to be toggled
        if (lastOpenedViewWasCategoryView) {
            toggle.toggle();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_middleSection_Home, new HomeCategoryViewFragment()).commit();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_middleSection_Home, new HomeListViewFragment()).commit();
        }

        // get views
        income = view.findViewById(R.id.income_text_view);
        expense = view.findViewById(R.id.expense_text_view);
        balance = view.findViewById(R.id.balance_text_view);

        // init
        initToggleButton(view);
        updateHeaderValues();

        return view;
    }

    private void updateHeaderValues(){
        income.setText(String.valueOf(Controller.getTotalIncome()));
        expense.setText(String.valueOf(Controller.getTotalExpense()));
        balance.setText(String.valueOf(Controller.getTransactionBalance()));
    }

    private void initToggleButton(View view) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_middleSection_Home, new HomeCategoryViewFragment()).commit();
                    SharedViewData.lastOpenedViewWasCategoryView = true;
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_middleSection_Home, new HomeListViewFragment()).commit();
                    SharedViewData.lastOpenedViewWasCategoryView = false;
                }
            }
        });
    }

}