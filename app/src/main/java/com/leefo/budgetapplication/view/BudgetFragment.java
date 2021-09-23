package com.leefo.budgetapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leefo.budgetapplication.R;

/**
 * Class that represents the fragment for the Budget page
 */
public class BudgetFragment extends Fragment {

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }
}