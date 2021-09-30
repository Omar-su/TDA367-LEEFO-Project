package com.leefo.budgetapplication.model;

import com.leefo.budgetapplication.controller.TransactionRequest;

import java.util.ArrayList;

/**
 * The TransactionModel class contains methods for manipulating, adding and removing categories and
 * transactions. The TransactionModel class also saves every change it does to the database for
 * persistance storage.
 *
 * @author Felix Edholm, Emelie Edberg
 */
public class TransactionModel {

    /**
     * The list of FinancialTransactions used in the application
     */
    private final ArrayList<FinancialTransaction> transactionList = new ArrayList<>();

    /**
     * The list of Categories used in the application
     */
    private final ArrayList<Category> categoryList = new ArrayList<>();

    private  Category otherIncome = new Category( "Other income", "#13702A", true);

    private  Category otherExpense = new Category( "Other expense", "701313", false);

   private final IDatabase database;


    /**
     * Constructor for creating a TransactionModel instance.
     */
    public TransactionModel(IDatabase database) {
        this.database = database;
       // loadTransactionList(); and sort the list
       // loadCategoryList(); and sort the list
        // When running for the first time, before database has saved default categories
        // we need to somehow add them to the list.
        // categoryList.add(otherIncome);
        // categoryList.add(otherExpense);
    }

    /**
     * Adds a financial transaction to the list of financial transactions.
     * Also saves the changes to the persistence storage.
     * @param transaction The financial transaction to be added.
     */
    public void addTransaction(FinancialTransaction transaction) {
        transactionList.add(transaction);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    /**
     * Deletes a financial transaction from the list of financial transactions.
     * Also saves the changes to the persistence storage.
     * @param transaction The financial transaction to be deleted.
     */
    public void deleteTransaction(FinancialTransaction transaction) {
        transactionList.remove(transaction);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    /**
     * Adds a category to the list of categories. Also saves the changes to the persistence storage.
     * @param category The category to be added.
     */
    public void addCategory(Category category) {
        categoryList.add(category);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    /**
     * Deletes a category from the list of categories. If a FinancialTransaction is of the deleted
     * category, that FinancialTransactions category is switched to a default category.
     * Also saves the changes to the persistence storage.
     * @param category The category to be deleted.
     */
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

    /**
     * Edits the information of a financial transaction.
     * Also saves the changes to the persistence storage.
     * @param oldTransaction The transaction with the information to be edited.
     * @param editedTransaction The transaction with the edited information.
     */
    public void editTransaction(FinancialTransaction oldTransaction, FinancialTransaction editedTransaction){
        deleteTransaction(oldTransaction);
        addTransaction(editedTransaction);

        //saveToDatabase();
        ObserverHandler.updateObservers();
    }

    /**
     * Edits the information of a category.
     * Also saves the changes to the persistence storage.
     * @param oldCategory The category with the information to be edited.
     * @param editedCategory The category with the edited information.
     */
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

        float sum = 0;
        for (FinancialTransaction t : searchTransactions(request)){
            sum = sum + t.getAmount();
        }
        return sum;
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
     * Returns a list of income categories.
     * @return a list of income categories.
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
     * Returns a list of expense categories.
     * @return a list of expense categories.
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

    /**
     * Returns the total income amount for a specific TransactionRequest.
     * @param request The object with the request to calculate total income from.
     * @return The total income amount. for the specific request.
     */
    public float getTotalIncome(TransactionRequest request){ // month, year. category irrelevant
        float income = 0;
        for (Category c : getIncomeCategories()){
            request.setCategory(c);
            income = income + getTransactionSum(request);
        }
        return income;
    }

    /**
     * Returns the total expense amount for a specific TransactionRequest.
     * @param request The object with the request to calculate total expense from.
     * @return The total expense amount for the specific request.
     */
    public float getTotalExpense(TransactionRequest request){
        float expense = 0;
        for (Category c : getExpenseCategories()){
            request.setCategory(c);
            expense = expense + getTransactionSum(request);
        }
        return expense;
    }

    /**
     * Returns the balance between income and expense for a specific TransactionRequest.
     * @param request The object with the request to calculate balance from.
     * @return The calculated balance.
     */
    public float getTransactionBalance(TransactionRequest request){
        return getTotalIncome(request) + getTotalExpense(request);
    }


}
