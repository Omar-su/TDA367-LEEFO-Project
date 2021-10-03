package com.leefo.budgetapplication.view;

import android.content.Context;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.controller.TransactionRequest;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;

public class SharedViewData {

    static public Category singleCategory;
    static public FinancialTransaction singleTransaction;
    static public boolean lastOpenedViewWasCategoryView;
    static public TimePeriod timePeriod;
    static public Context mainActivityContext;
    static public BottomNavigationView bottomNavigationView;
}
