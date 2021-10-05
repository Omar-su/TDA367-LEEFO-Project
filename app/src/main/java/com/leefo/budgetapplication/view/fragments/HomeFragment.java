package com.leefo.budgetapplication.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.view.HomeViewModel;
import com.leefo.budgetapplication.view.SharedTimePeriodViewModel;
import com.leefo.budgetapplication.view.TimePeriod;

import java.time.LocalDate;
import java.time.Month;

/**
 * Class that represents the fragment for the Home page
 */
public class HomeFragment extends Fragment {

    private TextView income, expense, balance;
    private ToggleButton view_toggle, time_period_toggle;
    private ImageButton back_arrow, forward_arrow;
    //private TimePeriod timePeriod;
    private HomeViewModel homeViewModel;
    private SharedTimePeriodViewModel sharedTimePeriodViewModel;
    private LiveData<TimePeriod> liveData;

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

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        sharedTimePeriodViewModel = new ViewModelProvider(requireActivity()).get(SharedTimePeriodViewModel.class);
        //timePeriod = sharedViewModel.getTimePeriodLiveData().getValue();
        liveData = sharedTimePeriodViewModel.getTimePeriodLiveData();

        // init
        initToggleButton(view);
        updateHeaderValues();
        initTimePeriod();
        openCorrectFragment();

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavigation);
        MenuItem item = bottomNav.getMenu().findItem(R.id.nav_home);
        item.setChecked(true);

        return view;
    }



    private void initTimePeriod() {
        if (!liveData.getValue().isTimeSpecified()){
            time_period_toggle.toggle();
            disabelArrowButtons();
        } else {
            updateTimePeriodButtonLabel();
        }

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedTimePeriodViewModel.decrementMonth();
                updateTimePeriodButtonLabel();
                updateHeaderValues();
            }
        });
        forward_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedTimePeriodViewModel.incrementMonth();
                updateTimePeriodButtonLabel();
                updateHeaderValues();
            }
        });

        time_period_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedTimePeriodViewModel.setNoSpecifiedTimePeriod();
                    disabelArrowButtons();
                    updateHeaderValues();
                } else {
                    sharedTimePeriodViewModel.setSpecifiedTimePeriod(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
                    updateTimePeriodButtonLabel();
                    back_arrow.setEnabled(true);
                    forward_arrow.setEnabled(true);
                    forward_arrow.clearColorFilter();
                    back_arrow.clearColorFilter();
                    updateHeaderValues();
                }
            }
        });
    }

    private void disabelArrowButtons() {
        back_arrow.setEnabled(false);
        forward_arrow.setEnabled(false);
        forward_arrow.setColorFilter(Color.WHITE);
        back_arrow.setColorFilter(Color.WHITE);
    }

    private void updateTimePeriodButtonLabel() {
        String text = Month.of(liveData.getValue().getMonth()) + " " + liveData.getValue().getYear();
        setTimePeriodText(text);
    }

    private void setTimePeriodText(String text) {
        time_period_toggle.setText(text);
        time_period_toggle.setTextOff(text);
    }

    private void openCorrectFragment() {
        // toggle is set to listView mode as default, if categoryView shall open first it needs to be toggled
        if (homeViewModel.lastOpenedViewWasCategoryView) {
            view_toggle.toggle();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_middleSection_Home, new HomeCategoryViewFragment()).commit();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_middleSection_Home, new HomeListViewFragment()).commit();
        }
    }

    private void updateHeaderValues(){
        float in = Controller.getTotalIncome(liveData.getValue().getMonth(), liveData.getValue().getYear());
        float ex = Controller.getTotalExpense(liveData.getValue().getMonth(), liveData.getValue().getYear());
        float ba = Controller.getTransactionBalance(liveData.getValue().getMonth(), liveData.getValue().getYear());
        income.setText(String.valueOf(in));
        expense.setText(String.valueOf(ex));
        balance.setText(String.valueOf(ba));
    }

    private void initToggleButton(View view) {
        view_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    homeViewModel.lastOpenedViewWasCategoryView = true;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_middleSection_Home, new HomeCategoryViewFragment()).commit();
                } else {
                    homeViewModel.lastOpenedViewWasCategoryView = false;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_middleSection_Home, new HomeListViewFragment()).commit();
                }
            }
        });
    }

}