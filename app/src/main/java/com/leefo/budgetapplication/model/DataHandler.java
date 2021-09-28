package com.leefo.budgetapplication.model;

import java.util.ArrayList;
import java.util.List;

public class DataHandler extends ObserverHandler {

    private final List<Transaction> transactionList;

    private final List<Category> categoryList;

    private final Category otherIncome = new Category(0, "Other income", "#13702A", true);

    private final Category otherExpense = new Category(1, "Other expense", "701313", false);


    public DataHandler(List<Transaction> transactionList, List<Category> categoryList) {
        this.transactionList = transactionList;
        this.categoryList = categoryList;

        // When running for the first time, before database has saved default categories
        // we need to somehow add them to the list.
        // categoryList.add(otherIncome);
        // categoryList.add(otherExpense);
    }

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);

        updateObservers();
    }

    public void deleteTransaction(Transaction transaction) {
        transactionList.remove(transaction);

        updateObservers();
    }

    public void addCategory(Category category) {
        categoryList.add(category);

        updateObservers();
    }

    public void deleteCategory(Category category) {
        if (category.isIncome()) {
            for (Transaction t : transactionList) {
                if (t.getCategory().isEqual(category)) {
                    t.setCategory(otherIncome);
                }
            }
        } else {
            for (Transaction t : transactionList) {
                if (t.getCategory().isEqual(category)) {
                    t.setCategory(otherExpense);
                }
            }
        }
        categoryList.remove(category);
        updateObservers();
    }

    public void editTransaction(Transaction oldTransaction, Transaction editedTransaction){
        deleteTransaction(oldTransaction);
        addTransaction(editedTransaction);

        updateObservers();
    }

    public void editCategory(Category oldCategory, Category editedCategory){
        deleteCategory(oldCategory);
        addCategory(editedCategory);

        updateObservers();
    }


}
