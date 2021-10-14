package com.leefo.budgetapplication.view.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.TimePeriodViewModel;

import java.time.Month;

/**
 * Class that represents the fragment for the Budget page
 *
 * @author Eugene Dvoryankov
 */
public class BudgetFragment extends Fragment {

    private Button editBudget;
    private RatingBar averageRatingBar;
    private TextView monthText;
    private TimePeriod timePeriod;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        editBudget = view.findViewById(R.id.edit_budget_button);
        averageRatingBar = view.findViewById(R.id.averageRatingBar);
        monthText = view.findViewById(R.id.month_budget);

        TimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(TimePeriodViewModel.class);
        timePeriod = viewModel.getTimePeriodLiveData().getValue();

        initEditBudgetOnClickListener();
        initGradeListFragment();
        initHeader();

        return view;
    }

    private void initEditBudgetOnClickListener() {
        editBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new EditBudgetFragment()).commit();
            }
        });
    }

    private void initGradeListFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_Categories_budget, new GradedBudgetFragment()).commit();
    }

    private void initHeader() {
        String month = Month.of(timePeriod.getMonth()) + " " + timePeriod.getYear();
        averageRatingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#6200ed")));
        monthText.setText(month);
        averageRatingBar.setRating(Controller.getAverageGradeForMonth(timePeriod.getMonth(), timePeriod.getYear()));
    }




}