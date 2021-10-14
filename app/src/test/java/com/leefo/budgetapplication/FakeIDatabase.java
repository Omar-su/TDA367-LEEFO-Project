package com.leefo.budgetapplication;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.IDatabase;

import java.util.ArrayList;

public class FakeIDatabase implements IDatabase {
    @Override
    public ArrayList<FinancialTransaction> getFinancialTransactions() {
        return null;
    }

    @Override
    public ArrayList<Category> getCategories() {
        return null;
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


