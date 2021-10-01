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

    //private  Category otherIncome = new Category( "Other income", "#13702A", true);
    //private  Category otherExpense = new Category( "Other expense", "701313", false);

   private final IDatabase database;


    /**
     * Constructor for creating a TransactionModel instance.
     */
    public TransactionModel(IDatabase database) {
        this.database = database;


        initCategories();
    }

    private void initCategories() {
        for (Category c : getCategoryList()){
            if (c.getName().equals("Other income")){
                return;
            }
        }
        setDefaultCategories();
    }

    private void setDefaultCategories(){
        // Other
        addCategory(new Category("Other income", "#C4C4C4", true));
        addCategory(new Category("Other expense", "#C4C4C4", false));

        // Expenses
        addCategory(new Category("Home", "#FF6464", false));
        addCategory(new Category("Food", "#64FF7D", false));
        addCategory(new Category("Transportation", "#64BEFF", false));
        addCategory(new Category("Clothes", "#FF64DD", false));
        addCategory(new Category("Entertainment", "#FFAE64", false));
        addCategory(new Category("Electronics", "#64FFEC",false));

        //Income
        addCategory(new Category("Salary", "#FCFF64", true));
        addCategory(new Category("Gift", "#6473FF", true));
    }

    /**
     * Adds a financial transaction to the list of financial transactions.
     * Also saves the changes to the persistence storage.
     * @param transaction The financial transaction to be added.
     */
    public void addTransaction(FinancialTransaction transaction) {
        transactionList.add(transaction);

        saveTransactionToDatabase(transaction);

        ObserverHandler.updateObservers();
    }

    /**
     * Deletes a financial transaction from the list of financial transactions.
     * Also saves the changes to the persistence storage.
     * @param transaction The financial transaction to be deleted.
     */
    public void deleteTransaction(FinancialTransaction transaction) {
        transactionList.remove(transaction);

        deleteTransactionFromDatabase(transaction);

        ObserverHandler.updateObservers();
    }

    /**
     * Adds a category to the list of categories. Also saves the changes to the persistence storage.
     * @param category The category to be added.
     */
    public void addCategory(Category category) {
        categoryList.add(category);

        saveCategoryToDatabase(category);

        ObserverHandler.updateObservers();
    }

    /**
     * Deletes a category from the list of categories. If a FinancialTransaction is of the deleted
     * category, that FinancialTransactions category is switched to a default category.
     * Also saves the changes to the persistence storage.
     * @param category The category to be deleted.
     */
    public void deleteCategory(Category category) {
        if (category == getOtherExpenseCategory()) return; // not allowed tp remove that one
        if (category == getOtherIncomeCategory()) return;; // not allowed to remove that one

        if (category.isIncome()) {
            for (FinancialTransaction t : transactionList) {
                if (category.transactionBelongs(t)) {
                    editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(),
                            t.getDate(), getOtherIncomeCategory()));
                }
            }
        } else {
            for (FinancialTransaction t : transactionList) {
                if (category.transactionBelongs(t)) {
                    editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(),
                            t.getDate(), getOtherExpenseCategory()));
                }
            }
        }
        categoryList.remove(category);

        deleteCategoryFromDatabase(category);

        ObserverHandler.updateObservers();
    }

    private Category getOtherIncomeCategory(){
        for (Category c : getCategoryList()){
            if (c.getName().equals("Other income")){
                return c;
            }
        }
        return null; // will never happen
    }

    private Category getOtherExpenseCategory(){
        for (Category c : getCategoryList()){
            if (c.getName().equals("Other expense")){
                return c;
            }
        }
        return null; // will never happen
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

        deleteTransactionFromDatabase(oldTransaction);
        saveTransactionToDatabase(editedTransaction);

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

        deleteCategoryFromDatabase(oldCategory);
        saveCategoryToDatabase(editedCategory);

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
        return Math.abs(sum);
    }

    public ArrayList<FinancialTransaction> searchTransactions(TransactionRequest request){
        if (!request.timeIsSpecified() && !request.categoryIsSpecified()){ // get all, no condition for category or time
            return getTransactionList();
        }
        if (!request.timeIsSpecified() && request.categoryIsSpecified()){ // get based on category, no term for time
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
        return Math.abs(expense);
    }

    /**
     * Returns the balance between income and expense for a specific TransactionRequest.
     * @param request The object with the request to calculate balance from.
     * @return The calculated balance.
     */
    public float getTransactionBalance(TransactionRequest request){
        return getTotalIncome(request) - getTotalExpense(request);
    }

    // dataBase methods ----
    private void saveTransactionToDatabase(FinancialTransaction transaction){
        //database.saveData(transaction);
    }

    private void saveCategoryToDatabase(Category category){
        //database.saveData(category);
    }

    private void deleteTransactionFromDatabase(FinancialTransaction transaction){
        //database.removeData(transaction);
    }

    private void deleteCategoryFromDatabase(Category category){
        //database.removeData(category);
    }


}
