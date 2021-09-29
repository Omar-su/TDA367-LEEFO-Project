package com.leefo.budgetapplication.model;

import com.leefo.budgetapplication.controller.TransactionRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private final ArrayList<Transaction> transactionList = new ArrayList<>();

    private final ArrayList<Category> categoryList = new ArrayList<>();

    private  Category otherIncome = new Category( "Other income", "#13702A", true);

    private  Category otherExpense = new Category( "Other expense", "701313", false);

   // private final DataSaver datasaver;


    public DataHandler() {
       // loadTransactionList();
       // loadCategoryList();
        // When running for the first time, before database has saved default categories
        // we need to somehow add them to the list.
        // categoryList.add(otherIncome);
        // categoryList.add(otherExpense);
    }

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    public void deleteTransaction(Transaction transaction) {
        transactionList.remove(transaction);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    public void addCategory(Category category) {
        categoryList.add(category);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    public void deleteCategory(Category category) {
        if (category.isIncome()) {
            for (Transaction t : transactionList) {
                if (category.transactionBelongs(t)) {
                    editTransaction(t, new Transaction(t.getAmount(), t.getDescription(),
                            t.getDate(), otherIncome));
                }
            }
        } else {
            for (Transaction t : transactionList) {
                if (category.transactionBelongs(t)) {
                    editTransaction(t, new Transaction(t.getAmount(), t.getDescription(),
                            t.getDate(), otherExpense));
                }
            }
        }
        categoryList.remove(category);
        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    public void editTransaction(Transaction oldTransaction, Transaction editedTransaction){
        deleteTransaction(oldTransaction);
        addTransaction(editedTransaction);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    public void editCategory(Category oldCategory, Category editedCategory){
        replaceTransactionsCategory(oldCategory, editedCategory);
        deleteCategory(oldCategory);
        addCategory(editedCategory);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    private void replaceTransactionsCategory(Category oldCategory, Category newCategory){
        for(Transaction t : transactionList){
            if(oldCategory.transactionBelongs(t)){
                editTransaction(t, new Transaction(t.getAmount(), t.getDescription(), t.getDate(),
                        newCategory));
            }
        }
    }
    /**
     * Returns a copy of the transactionList
     * @return copy if transactionList
     */
    public ArrayList<Transaction> getTransactionList() {
        return new ArrayList<>(transactionList);
    }

    /**
     * Returns a copy of the categoryList
     * @return copy if categoryList
     */
    public ArrayList<Category> getCategoryList() {
        return new ArrayList<>(categoryList);
    }

    //TODO implement these methods

    public float getSum(TransactionRequest request){
        // not finished, just quick. for one category, every transaction. someone can redo this better
        Category category = request.getCategory();
        double sum = 0;
        for (Transaction t : searchTransactions(new TransactionRequest(category, null, null))){
            sum = sum + t.getAmount();
        }
        return (float)sum;
    }

    public ArrayList<Transaction> searchTransactions(TransactionRequest request){
        if (!request.timeIsSpecified() && !request.categoryIsSpecified()){
            return getTransactionList();
        }
        if (!request.timeIsSpecified() && request.categoryIsSpecified()){
            Category category = request.getCategory();
            ArrayList<Transaction> transactions = new ArrayList<>();
            for (Transaction t : getTransactionList()){
                if (t.getCategory() == category){
                    transactions.add(t);
                }
            }
            return transactions;
        }
        return getTransactionList();
    }
    /*
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

    /**
     * Get the income categories
     * @return income categories
     */
    public ArrayList<Category> getIncomeCategories(){
        ArrayList<Category> list = new ArrayList<>();
        for (Category c : getCategoryList()){
            if (c.isIncome()){
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Get the expense categories
     * @return expense categories
     */
    public ArrayList<Category> getExpenseCategories(){
        ArrayList<Category> list = new ArrayList<>();
        for (Category c : getCategoryList()){
            if (!c.isIncome()){
                list.add(c);
            }
        }
        return list;
    }




}
