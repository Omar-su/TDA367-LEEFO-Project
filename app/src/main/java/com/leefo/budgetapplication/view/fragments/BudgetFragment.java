package com.leefo.budgetapplication.view.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.TimePeriodViewModel;

import java.time.LocalDate;
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
    private ImageButton arrow_back_budget;
    private ImageButton arrow_forward_budget;

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
        arrow_back_budget = view.findViewById(R.id.arrow_back_budget);
        arrow_forward_budget = view.findViewById(R.id.arrow_forward_budget);

        timePeriod = new TimePeriod(LocalDate.now().getYear(), LocalDate.now().getMonthValue());

        initEditBudgetOnClickListener();
        initGradeListFragment();
        initHeader();
        initTimePeriod();

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
        requireActivity().getSupportFragmentManager().beginTransaction().
                add(R.id.FrameLayout_Categories_budget, new GradedBudgetFragment(), "GradedBudgetFragment").commit();
    }

    private void initHeader() {

        averageRatingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#6200ed")));

        averageRatingBar.setRating(Controller.getAverageGradeForMonth(timePeriod.getMonth(), timePeriod.getYear()));
    }

    private void initTimePeriod() {
            updateTimePeriodButtonLabel();

        FragmentManager fm = requireActivity().getSupportFragmentManager();
        GradedBudgetFragment gbf = (GradedBudgetFragment) fm.findFragmentByTag("GradedBudgetFragment");

        arrow_back_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod.decrementMonth();
                gbf.updateMonth(timePeriod);
                updateTimePeriodButtonLabel();
            }
        });
        arrow_forward_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod.incrementMonth();
                gbf.updateMonth(timePeriod);
                updateTimePeriodButtonLabel();
            }
        });

    }

    private void updateTimePeriodButtonLabel() {
        String month = Month.of(timePeriod.getMonth()) + " " + timePeriod.getYear();
        monthText.setText(month);
    }




}