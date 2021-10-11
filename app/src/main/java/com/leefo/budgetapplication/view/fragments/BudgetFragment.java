package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leefo.budgetapplication.R;

/**
 * Class that represents the fragment for the Budget page
 *
 * @author Eugene Dvoryankov
 */
public class BudgetFragment extends Fragment {

    private Button editBudget;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        editBudget = view.findViewById(R.id.edit_budget_button);

        initEditBudgetOnClickListener();

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


}