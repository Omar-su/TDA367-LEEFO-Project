package com.leefo.budgetapplication.model;

import com.leefo.budgetapplication.controller.TransactionRequest;

import java.util.ArrayList;

public class TransactionModel {

    private final ArrayList<FinancialTransaction> transactionList = new ArrayList<>();

    private final ArrayList<Category> categoryList = new ArrayList<>();

    private  Category otherIncome = new Category( "Other income", "#13702A", true);

    private  Category otherExpense = new Category( "Other expense", "701313", false);

   // private final DataSaver datasaver;


    public TransactionModel() {
       // loadTransactionList();
       // loadCategoryList();
        // When running for the first time, before database has saved default categories
        // we need to somehow add them to the list.
        // categoryList.add(otherIncome);
        // categoryList.add(otherExpense);
    }

    public void addTransaction(FinancialTransaction transaction) {
        transactionList.add(transaction);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    public void deleteTransaction(FinancialTransaction transaction) {
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
            for (FinancialTransaction t : transactionList) {
                if (category.transactionBelongs(t)) {
                    editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(),
                            t.getDate(), otherIncome));
                }
            }
        } else {
            for (FinancialTransaction t : transactionList) {
                if (category.transactionBelongs(t)) {
                    editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(),
                            t.getDate(), otherExpense));
                }
            }
        }
        categoryList.remove(category);
        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    public void editTransaction(FinancialTransaction oldTransaction, FinancialTransaction editedTransaction){
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
        for(FinancialTransaction t : transactionList){
            if(oldCategory.transactionBelongs(t)){
                editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(), t.getDate(),
                        newCategory));
            }
        }
    }
    /**
     * Returns a copy of the transactionList
     * @return copy if transactionList
     */
    public ArrayList<FinancialTransaction> getTransactionList() {
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

    public float getTransactionSum(TransactionRequest request){
        // not finished i think, just wrote this quick.

        double sum = 0;
        for (FinancialTransaction t : searchTransactions(request)){
            sum = sum + t.getAmount();
        }
        return (float)sum;
    }

    public ArrayList<FinancialTransaction> searchTransactions(TransactionRequest request){
        if (!request.timeIsSpecified() && !request.categoryIsSpecified()){ // get all, no condition for category or time
            return getTransactionList();
        }
        if (!request.timeIsSpecified() && request.categoryIsSpecified()){ // get based on condition, no term for time
            Category category = request.getCategory();
            ArrayList<FinancialTransaction> transactions = new ArrayList<>();
            for (FinancialTransaction t : getTransactionList()){
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


    public double getTotalIncome(TransactionRequest request){ // month, year. category irrelevant
        double income = 0;
        for (Category c : getIncomeCategories()){
            request.setCategory(c);
            income = income + getTransactionSum(request);
        }
        return income;
    }

    public double getTotalExpense(TransactionRequest request){
        double expense = 0;
        for (Category c : getIncomeCategories()){
            request.setCategory(c);
            expense = expense + getTransactionSum(request);
        }
        return expense;
    }

    public double getTransactionBalance(TransactionRequest request){
        return getTotalIncome(request) + getTotalExpense(request);
    }


}
