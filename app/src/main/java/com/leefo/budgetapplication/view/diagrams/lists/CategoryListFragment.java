package com.leefo.budgetapplication.view.diagrams.lists;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.view.ModelObserver;


import java.util.ArrayList;
import java.util.List;


public class CategoryListFragment extends Fragment implements ModelObserver {
    ListView listView;
    //ListViewAdapterCategoryList adapter;
    ArrayList<Transaction> transactions;
    ArrayList<Category> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list_view, container, false);

        Controller.addObserver(this);
        //listView = view.findViewById(R.id.category_list);
        transactions = Controller.getAllTransactions();
        categories = Controller.getAllCategories();

        //adapter = new ListViewAdapterCategoryList(getActivity().getApplicationContext(),categories, getCategorySumList(categories));
        //listView.setAdapter(adapter);

        return view;
    }

    private List<Double> getCategorySumList(ArrayList<Category> categoryArrayList){
        List<Double> categorySumArray = null;
        for(Category c : categoryArrayList){
            categorySumArray.add(getCategorySum(c));
        }
        return categorySumArray;
    }

    private double getCategorySum(Category cat){
        double sum = 0;
        for(Transaction t: transactions){
            if(t.getCategoryId() == cat.getId()){
                sum += t.getAmount();
            }
        }
        return sum;
    }

    @Override
    public void update() {

    }
}