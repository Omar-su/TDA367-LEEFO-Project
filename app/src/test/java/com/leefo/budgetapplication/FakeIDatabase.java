package com.leefo.budgetapplication;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.IDatabase;

import java.util.ArrayList;

//Fake IDatabase class created to test model without database implementation.
public class FakeIDatabase implements IDatabase {
    @Override
    public ArrayList<FinancialTransaction> getFinancialTransactions() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Category> getCategories() {
        return new ArrayList<>();
    }

    @Override
    public void saveData(FinancialTransaction transaction) {

    }

    @Override
    public void saveData(Category category) {

    }

    @Override
    public void removeData(FinancialTransaction transaction) {

    }

    @Override
    public void removeData(Category category) {

    }
}


