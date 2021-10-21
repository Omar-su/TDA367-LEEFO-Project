package com.leefo.budgetapplication.view.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.BudgetGradeController;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.data.TimePeriod;
import com.leefo.budgetapplication.view.adapters.GradedBudgetListAdapter;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

/**
 * Class that represents the fragment for the Budget page
 *
 * @author Eugene Dvoryankov
 * @author Omar Sulaiman
 * @author Felix Edholm
 */
public class BudgetFragment extends Fragment {

    private Button editBudget;
    private RatingBar averageRatingBar;
    private TextView monthText;
    private TextView noBudget1;
    private TextView noBudget2;
    private TimePeriod timePeriod;
    private ImageButton arrow_back_budget;
    private ImageButton arrow_forward_budget;
    private ListView listView;


    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     *
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
        listView = view.findViewById(R.id.listView_gradedBudgets);
        noBudget1 = view.findViewById(R.id.noBudgetText1);
        noBudget2 = view.findViewById(R.id.noBudgetText2);

        timePeriod = new TimePeriod(LocalDate.now().getYear(), LocalDate.now().getMonthValue());

        updateList(timePeriod);
        initEditBudgetOnClickListener();
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

    private void initHeader(){
        averageRatingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#6200ed")));
        updateHeader();
    }

    private void updateHeader() {
        if (noBudget1.getVisibility() == View.VISIBLE){
            averageRatingBar.setRating(0);
        } else {
            averageRatingBar.setRating(BudgetGradeController.getAverageGradeForMonth(timePeriod.getMonth(), timePeriod.getYear()));
        }
    }

    private void initTimePeriod() {
        updateTimePeriodButtonLabel();


        arrow_back_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod.decrementMonth();
                updateList(timePeriod);
                updateTimePeriodButtonLabel();
                updateHeader();
            }
        });
        arrow_forward_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod.incrementMonth();
                updateList(timePeriod);
                updateTimePeriodButtonLabel();
                updateHeader();
            }
        });

    }

    private void updateTimePeriodButtonLabel() {
        String month = Month.of(timePeriod.getMonth()) + " " + timePeriod.getYear();
        monthText.setText(month);
    }

    private void updateList(TimePeriod timePeriod) {
        if (getActivity() != null) {

            if (isCurrentMonthOrAfter(timePeriod)) {
                //Displays all categories with budget goal even if no transactions made for current month
                ArrayList<Category> allBudgetCategories = BudgetGradeController.getAllBudgetCategories();
                if (allBudgetCategories.isEmpty()) {
                    noBudget1.setVisibility(View.VISIBLE);
                    noBudget2.setVisibility(View.VISIBLE);

                    noBudget1.setText("There are currently no set budgets for any category this month.");
                    noBudget2.setText("Press Edit to add a budget goal.");

                } else {
                    noBudget1.setVisibility(View.INVISIBLE);
                    noBudget2.setVisibility(View.INVISIBLE);
                }
                GradedBudgetListAdapter adapter = new GradedBudgetListAdapter(getActivity().
                        getApplicationContext(), allBudgetCategories, timePeriod);
                listView.setAdapter(adapter);
            } else {
                //If not current mont => earlier month. Displays only budget categories with transactions made
                ArrayList<Category> budgetCatsForMonth =
                        BudgetGradeController.getBudgetCategoriesByMonth(timePeriod.getMonth(), timePeriod.getYear());

                if (budgetCatsForMonth.isEmpty()) {
                    noBudget1.setVisibility(View.VISIBLE);
                    noBudget2.setVisibility(View.VISIBLE);

                    noBudget1.setText("There are currently no set budgets for any category.");
                    noBudget2.setText("Press Edit to add a budget goal.");
                } else {
                    noBudget1.setVisibility(View.INVISIBLE);
                    noBudget2.setVisibility(View.INVISIBLE);
                }
                GradedBudgetListAdapter adapter = new GradedBudgetListAdapter(getActivity().
                        getApplicationContext(), budgetCatsForMonth, timePeriod);
                listView.setAdapter(adapter);
            }


        }

    }

    private boolean isCurrentMonthOrAfter(TimePeriod timePeriod) {

        LocalDate monthChosen = LocalDate.of(timePeriod.getYear(), timePeriod.getMonth(), 1);
        LocalDate monthToday = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        return monthToday.isEqual(monthChosen) || monthToday.isBefore(monthChosen);
    }
}