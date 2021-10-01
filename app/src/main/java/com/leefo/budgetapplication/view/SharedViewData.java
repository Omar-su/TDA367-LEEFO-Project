package com.leefo.budgetapplication.view;

import androidx.fragment.app.FragmentTransaction;

import com.leefo.budgetapplication.controller.TransactionRequest;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;

public class SharedViewData {

    static public Category singleCategory;
    static public FinancialTransaction singleTransaction;
    static public boolean lastOpenedViewWasCategoryView;
    static public TransactionRequest timePeriod;
}
