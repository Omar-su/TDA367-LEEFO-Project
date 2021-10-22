package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.CategoryController;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.adapters.EditBudgetListAdapter;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents the editbudget fragment page
 *
 * @author Omar Sulaiman
 */
public class EditBudgetFragment extends Fragment {

    private ListView editBudgetLV;
    private Button saveBudgetButton;
    EditBudgetListAdapter adapter;
    private ImageButton cross;


    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     *
     * @return the view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_budget, container, false);

        saveBudgetButton = view.findViewById(R.id.saveButtonBudget);
        editBudgetLV = view.findViewById(R.id.editBudgetListV);
        cross = view.findViewById(R.id.cross_edit_budget);

        initList();

        initSaveButtonOnClickListener();
        initCross();
        return view;
    }

    /**
     * populates the listView with the expense categories
     */
    private void initList() {
        List<Category> categoryList = CategoryController.getExpenseCategories();
        categoryList = CategoryController.sortCategoriesByAlphabet(categoryList);
        categoryList = CategoryController.sortCategoriesByBudget(categoryList);
        adapter = new EditBudgetListAdapter(requireActivity().getApplicationContext(), categoryList);
        editBudgetLV.setAdapter(adapter);

    }


    private void initCross(){
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new BudgetFragment()).commit();
            }
        });
    }


    private void initSaveButtonOnClickListener() {
        saveBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBudget();
            }
        });
    }

    /**
     * Updates the budget attribute of the categories depending on the input from the user that
     * when pressing the save button
     */
    private void editBudget(){

        Map<Category,Integer> editBudgetHashMap = adapter.getEditBudgetHashMap();
        editBudgetHashMap.forEach((key, value) -> CategoryController.editCategoryInfo((Category) key, (Integer) value));
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new BudgetFragment()).commit();

    }


}
