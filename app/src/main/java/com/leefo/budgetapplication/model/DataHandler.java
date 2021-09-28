package com.leefo.budgetapplication.model;

import java.util.ArrayList;
import java.util.List;

public class DataHandler extends ObserverHandler {

    private final List<Transaction> transactionList;

    private final List<Category> categoryList;

    private  Category otherIncome = new Category(0, "Other income", "#13702A", true);

    private  Category otherExpense = new Category(1, "Other expense", "701313", false);

   // private final DataSaver datasaver;


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

        //saveToDatabase();
        updateObservers();
    }

    public void deleteTransaction(Transaction transaction) {
        transactionList.remove(transaction);

        //saveToDatabase();
        updateObservers();
    }

    public void addCategory(Category category) {
        categoryList.add(category);

        //saveToDatabase();
        updateObservers();
    }

    public void deleteCategory(Category category) {
        if (category.isIncome()) {
            for (Transaction t : transactionList) {
                if (t.getCategory().isEqual(category)) {
                    editTransaction(t, new Transaction(t.getAmount(), t.getDescription(),
                            t.getDate(), otherIncome));
                }
            }
        } else {
            for (Transaction t : transactionList) {
                if (t.getCategory().isEqual(category)) {
                    editTransaction(t, new Transaction(t.getAmount(), t.getDescription(),
                            t.getDate(), otherExpense));
                }
            }
        }
        categoryList.remove(category);
        //saveToDatabase();
        updateObservers();
    }

    public void editTransaction(Transaction oldTransaction, Transaction editedTransaction){
        deleteTransaction(oldTransaction);
        addTransaction(editedTransaction);

        //saveToDatabase();
        updateObservers();
    }

    public void editCategory(Category oldCategory, Category editedCategory){
        deleteCategory(oldCategory);
        addCategory(editedCategory);

        //saveToDatabase();
        updateObservers();
    }


     //TODO implement these methods
    /*
    public float getSum(Command command){
    }

    public List<Transaction> searchTransactions(Command command){

    }

    private void loadTransactionList(){
        transactionList.addAll(datasaver.getTransactionList());
    }

    private void loadCategoryList(){
        categoryList.addAll(datasaver.getCategoryList());
    }

    private void saveToDatabase(){
        datasaver.saveData(transactionList, categoryList);
    }
*/






}
