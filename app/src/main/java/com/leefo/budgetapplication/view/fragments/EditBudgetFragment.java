package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.ParcelableCategory;
import com.leefo.budgetapplication.view.adapters.EditCategoryListAdapter;
import com.leefo.budgetapplication.view.adapters.TransactionListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

public class EditBudgetFragment extends Fragment {

    private ListView editBudgetLV;
    private Button saveBudgetButton;
    EditCategoryListAdapter adapter;
    private ImageButton cross;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_budget, container, false);

        saveBudgetButton = view.findViewById(R.id.saveButtonBudget);
        editBudgetLV = view.findViewById(R.id.editBudgetListV);
        cross = view.findViewById(R.id.cross_edit_budget);

        initList();


        initSaveBudgetOnClickListener();
        initCross();
        return view;
    }

    private void initList() {
        ArrayList<Category> categoryList = Controller.getExpenseCategories();
        adapter = new EditCategoryListAdapter(requireActivity().getApplicationContext(), categoryList);
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


    private void initSaveBudgetOnClickListener() {
        saveBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBudget();
            }
        });
    }

    private void editBudget(){

        HashMap<Category,Integer> editBudgetHashMap = adapter.getEditBudgetHashMap();
        editBudgetHashMap.forEach((key, value) -> Controller.editCategoryInfo((Category) key, (Integer) value));
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new BudgetFragment()).commit();

    }


    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(requireActivity().getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}
