package com.leefo.budgetapplication.model;

import java.util.List;

public class DataHandler {

    private final List<Transaction> transactionList;

    private final List<Category> categoryList;


    public DataHandler(List<Transaction> transactionList, List<Category> categoryList) {
        this.transactionList = transactionList;
        this.categoryList = categoryList;
    }

    public void addTransaction(Transaction transaction){
        transactionList.add(transaction);
    }

    public void deleteTransaction(Transaction transaction){
        transactionList.remove(transaction);
    }

    public void addCategory(Category category){
        categoryList.add(category);
    }

    public void deleteCategory(Category category){
    }
}
