package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.SharedViewData;
import com.leefo.budgetapplication.view.ViewObserverHandler;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

/**
 * Class that represents the fragment for the Home page
 */
public class HomeFragment extends Fragment {

    private TextView income, expense, balance;
    private ToggleButton view_toggle, time_period_toggle;
    private ImageButton back_arrow, forward_arrow;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // get views
        income = view.findViewById(R.id.income_text_view);
        expense = view.findViewById(R.id.expense_text_view);
        balance = view.findViewById(R.id.balance_text_view);
        time_period_toggle = view.findViewById(R.id.time_period_toggle);
        view_toggle = view.findViewById(R.id.toggleButton);
        back_arrow = view.findViewById(R.id.Arrow_back);
        forward_arrow = view.findViewById(R.id.Arrow_forward);

        // init

        initToggleButton(view);
        updateHeaderValues();
        initTimePeriod();
        openCorrectFragment();

        ArrayList<FinancialTransaction> list = Controller.getTransactions();

        return view;
    }



    private void initTimePeriod() {
        updateTimePeriodButtonLabel();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedViewData.timePeriod.decrementMonth();
                updateTimePeriodButtonLabel();
            }
        });
        forward_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedViewData.timePeriod.incrementMonth();
                updateTimePeriodButtonLabel();
                ViewObserverHandler.updateObservers();
            }
        });

        time_period_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedViewData.timePeriod.setNoSpecifiedTimePeriod();
                    back_arrow.setEnabled(false);
                    forward_arrow.setEnabled(false);
                    ViewObserverHandler.updateObservers();
                } else {
                    SharedViewData.timePeriod.setSpecifiedTimePeriod(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
                    updateTimePeriodButtonLabel();
                    back_arrow.setEnabled(true);
                    forward_arrow.setEnabled(true);
                    ViewObserverHandler.updateObservers();
                }
            }
        });
    }

    private void updateTimePeriodButtonLabel() {
        String text = Month.of(SharedViewData.timePeriod.getMonth()) + " " + SharedViewData.timePeriod.getYear();
        setTimePeriodText(text);
    }

    private void setTimePeriodText(String text) {
        time_period_toggle.setText(text);
        time_period_toggle.setTextOff(text);
    }

    private void openCorrectFragment() {
        // toggle is set to listView mode as default, if categoryView shall open first it needs to be toggled
        if (SharedViewData.lastOpenedViewWasCategoryView) {
            view_toggle.toggle();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_middleSection_Home, new HomeCategoryViewFragment()).commit();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_middleSection_Home, new HomeListViewFragment()).commit();
        }
    }

    private void updateHeaderValues(){
        float in = Controller.getTotalIncome(SharedViewData.timePeriod.getMonth(), SharedViewData.timePeriod.getYear());
        float ex = Controller.getTotalExpense(SharedViewData.timePeriod.getMonth(), SharedViewData.timePeriod.getYear());
        float ba = Controller.getTransactionBalance(SharedViewData.timePeriod.getMonth(), SharedViewData.timePeriod.getYear());
        income.setText(String.valueOf(in));
        expense.setText(String.valueOf(ex));
        balance.setText(String.valueOf(ba));
    }

    private void initToggleButton(View view) {
        view_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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