package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.model.CategoryFake;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;

import java.util.ArrayList;

/**
 * The class that represents the fragment for adding a new transaction
 */
public class NewTransactionFragment extends Fragment {

    private EditText amount, description;
    private Spinner spinner;


    /**
     * Method that runs when the fragment is being created. It initializes the fragment and its components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_transaction, container, false);


        ArrayList<CategoryFake> categories = new ArrayList<>();
        categories.add(new CategoryFake("Mat", "#558DF9")); // TODO replace hardcoded data
        categories.add(new CategoryFake("Musik", "#F95555"));
        categories.add(new CategoryFake("Danslektioner", "#55F979"));

        spinner = (Spinner)view.findViewById(R.id.spinner_categoty);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), categories);
        spinner.setAdapter(spinnerAdapter);

        return view;
    }


}