package com.leefo.budgetapplication.model;

import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.controller.TransactionRequest;
import com.leefo.budgetapplication.view.SharedViewData;

import java.lang.reflect.Array;
import java.net.FileNameMap;
import java.time.LocalDate;
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
    private  ArrayList<FinancialTransaction> transactionList;

    /**
     * The list of Categories used in the application
     */
    private  ArrayList<Category> categoryList;

    //private  Category otherIncome = new Category( "Other income", "#13702A", true);
    //private  Category otherExpense = new Category( "Other expense", "701313", false);

   private final IDatabase database;


    /**
     * Constructor for creating a TransactionModel instance.
     */
    public TransactionModel(IDatabase database) {
        this.database = database;
        //transactionList = new ArrayList<>();
        //categoryList = new ArrayList<>();
        transactionList = getFinancialTransactions(); // not finished should be sorted first
        categoryList = getCategories();

        initDefaultCategories();
    }

    private void removeAllTransactions(){
        for (FinancialTransaction t : getTransactionList()){
            deleteTransaction(t);
        }
    }

    private void initDefaultCategories() {
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
     * Adds a financial transaction to the list of financial transactions at appropriate index by date (lower index --> more recent).
     * Also saves the changes to the persistence storage.
     * @param transaction The financial transaction to be added.
     */
    public void addTransaction(FinancialTransaction transaction) {

        ArrayList<FinancialTransaction> transactions = getTransactionList();

        // loops through transactions to find where new transaction should be inserted
        for(int i = 0; i < transactions.size(); i++)
        {
            // if the transaction to be added is NOT made before this iteration's transaction, then the index of the new transaction
            // should be the index of the current iteration's transaction
            if(!dateIsBefore(transaction.getDate(), transactions.get(i).getDate()))
            {
                transactionList.add(i, transaction); // adds transaction at index i (note: does NOT replace)
                break;
            }
        }

        saveTransactionToDatabase(transaction); // saves to database for persistence

        ObserverHandler.updateObservers(); // updates views
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
        //  finished i think, just wrote this quick.

        float sum = 0;
        for (FinancialTransaction t : searchTransactions(request)){
            sum = sum + t.getAmount();
        }
        return Math.abs(sum);
    }

    public ArrayList<FinancialTransaction> searchTransactions(TransactionRequest request)
    {
        ArrayList<FinancialTransaction> result = new ArrayList<>();

        // loops through every transaction
        for(FinancialTransaction transaction : getTransactionList())
        {
            // unnecessarily complicated method of retrieving year and month from LocalDate object
            // because older API versions don't support getMonthValue() and getYearValue()
            int transactionYear = Integer.parseInt(transaction.getDate().toString().substring(0, 4));
            int transactionMonth = Integer.parseInt(transaction.getDate().toString().substring(5, 7));

            // moves on to next transaction if current transaction does not match time specification
            if(request.timeIsSpecified())
                if(request.getYear() != transactionYear || request.getMonth() != transactionMonth)
                    continue;

            // moves on to next transaction if current transaction category does not match requested category
            if(request.categoryIsSpecified())
                if(!request.getCategory().Equals(transaction.getCategory()))
                    continue;

            result.add(transaction); // adds transaction to result if transaction passes checks
        }

        return result;
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

    public ArrayList<Category> removeEmptyCategories(ArrayList<Category> list, TransactionRequest request){
        ArrayList<Category> notEmpty = new ArrayList<>();
        for (Category c : list){
            request.setCategory(c);
            if (!searchTransactions(request).isEmpty()){
                notEmpty.add(c);
            }
        }
        return notEmpty;
    }

    // largest first
    public ArrayList<Category> sortCategoryListBySum(ArrayList<Category> list, TransactionRequest request){
        for (int x = 0; x < list.size() ; x++){
            for (int i = 0; i < list.size()-1; i++){
                float sum1 = getTransactionSum(new TransactionRequest(list.get(i), request.getMonth(), request.getYear()));
                float sum2 = getTransactionSum(new TransactionRequest(list.get(i+1), request.getMonth(), request.getYear()));
                if (sum1 < sum2){
                    swap(list, i, i+1);
                }
            }
        }
        return list;
    }


    // dataBase methods ----
    private void saveTransactionToDatabase(FinancialTransaction transaction){
        database.saveData(transaction);
    }

    private void saveCategoryToDatabase(Category category){
        database.saveData(category);
    }

    private void deleteTransactionFromDatabase(FinancialTransaction transaction){
        //database.removeData(transaction);
    }

    private void deleteCategoryFromDatabase(Category category){
        //database.removeData(category);
    }

    private ArrayList<FinancialTransaction> getFinancialTransactions(){

        ArrayList<FinancialTransaction> transactions = database.getFinancialTransactions();

        // transactions may not be in order when retrieved from database, so they must be sorted.
        // lower index means that the transaction has been made more recently.
        bubbleSortTransactions(transactions);

        return transactions; // should be sorted by date
    }

    private ArrayList<Category> getCategories(){
        return database.getCategories();
    }


    // sorting ----

    /**
     * Sorts an array list of transactions so that the most recent transactions will have a lower index.
     *
     * Uses the bubble sort algorithm. https://en.wikipedia.org/wiki/Bubble_sort
     *
     * @param transactions List to be sorted.
     */
    private void bubbleSortTransactions(ArrayList<FinancialTransaction> transactions)
    {
        boolean notCompleted = true; // will be set to false in the last loop through the list of transactions

        while (notCompleted)
        {
            notCompleted = false; // will be reset to true if a swap is made

            // will be iterated until the list is sorted
            for(int i = 0; i < transactions.size() - 2; i++)
            {
                FinancialTransaction first = transactions.get(i);
                FinancialTransaction second = transactions.get(i+1);

                // lower index transactions should be most recent
                if (dateIsBefore(first.getDate(), second.getDate())) {
                    swap(transactions, i, i + 1); // swaps transactions in array list
                    notCompleted = true;
                }
            }
        }
    }

    /**
     * If isBefore is a date before isAfter, then this method returns true.
     *
     * If isBefore is a date after isAfter, or if the dates are the same, then it returns false.
     *
     * This method is only needed because older API versions don't support isAfter() or isBefore()
     * methods in LocalDate.
     *
     * @param isBefore Date which should be before isAfter to return true.
     * @param isAfter Date which should be after isBefore to return true;
     * @return True if isBefore < isAfter.
     */
    private boolean dateIsBefore(LocalDate isBefore, LocalDate isAfter)
    {
        String before = isBefore.toString();
        String after = isAfter.toString();

        // unnecessarily complicated method of retrieving year and month from LocalDate object
        // because older API versions don't support getMonthValue() and getYearValue()
        int yearBefore = Integer.parseInt(before.substring(0, 4));
        int yearAfter = Integer.parseInt(after.substring(0, 4));

        if(yearBefore < yearAfter) return true;
        if(yearBefore > yearAfter) return false;

        int monthBefore = Integer.parseInt(before.substring(5, 7));
        int monthAfter = Integer.parseInt(after.substring(5, 7));

        if(monthBefore < monthAfter) return true;
        if(monthBefore > monthAfter) return false;

        int dayBefore = Integer.parseInt(before.substring(8, 10));
        int dayAfter = Integer.parseInt(after.substring(8, 10));

        return dayBefore < dayAfter; // if the day is the same, then the first is not before the other
    }

    /**
     * Swaps the position of two objects in a list.
     *
     * @param list List in which they will be swapped.
     * @param i1 Index of first object.
     * @param i2 Index of second object.
     */
    private <T> void swap(ArrayList<T> list, int i1, int i2)
    {
        T temp = list.get(i1); // stores i1 temporarily

        list.set(i1, list.get(i2)); // sets i1 to i2
        list.set(i2, temp); // sets i2 to temp
    }


}
