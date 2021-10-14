package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.TimePeriodViewModel;
import com.leefo.budgetapplication.view.adapters.GradedBudgetListAdapter;
import com.leefo.budgetapplication.view.adapters.TransactionListAdapter;

import java.util.ArrayList;

public class GradedBudgetFragment extends Fragment {

    ListView listView;
    TextView noBudget1;
    TextView noBudget2;
    TimePeriod timePeriod;


    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graded_budget, container, false);

        // get views
        listView = view.findViewById(R.id.listView_gradedBudgets);
        noBudget1 = view.findViewById(R.id.noBudgetText1);
        noBudget2 = view.findViewById(R.id.noBudgetText2);

        TimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(TimePeriodViewModel.class);
        timePeriod = viewModel.getTimePeriodLiveData().getValue();

        viewModel.getTimePeriodLiveData().observe(getViewLifecycleOwner(), new Observer<TimePeriod>() {
            @Override
            public void onChanged(TimePeriod newTimePeriod) {
                updateList(Controller.getBudgetCategoriesByMonth(timePeriod.getMonth(), timePeriod.getYear()));
            }
        });


        updateList(Controller.getBudgetCategoriesByMonth(timePeriod.getMonth(), timePeriod.getYear()));

        return view;
    }

    private void updateList(ArrayList<Category> budgetCategories) {
        if (getActivity() != null) {

            if (budgetCategories.isEmpty()) {
                noBudget1.setVisibility(View.VISIBLE);
                noBudget2.setVisibility(View.VISIBLE);

                    noBudget1.setText("There are currently no set budgets for any category.");
                    noBudget2.setText("Edit a category to add budget.");

                }
            } else {
                noBudget1.setVisibility(View.INVISIBLE);
                noBudget2.setVisibility(View.INVISIBLE);
            }
            GradedBudgetListAdapter adapter = new GradedBudgetListAdapter(getActivity().
                    getApplicationContext(), budgetCategories, timePeriod);
            listView.setAdapter(adapter);
        }
    }



