package com.leefo.budgetapplication.controller;

import android.content.Context;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryModel;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.TransactionModel;
import com.leefo.budgetapplication.model.ObserverHandler;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.ModelObserver;
import com.leefo.budgetapplication.model.TransactionRequest;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The Controller class represents the Controller in the Model-View-Controller pattern.
 * The class responsibility is to listen to the View and respond by modifying the model and updating
 * the view.
 *
 * @author Felix Edholm, Linus Lundgren, Emelie Edberg
 */
public class Controller {

    /**
     * Object handling logic for transactions.
     */
    private static TransactionModel transactionModel;

    /**
     * Object handling logic for categories
     */
    private static CategoryModel categoryModel;



    /**
     * Initializes database, transactionModel and categoryModel.
     * @param context Application context for database.
     */
    public static void InitializeBackend(Context context)
    {
        DataBaseManager database = new DataBaseManager(context);

        categoryModel = new CategoryModel(database, transactionModel);

        transactionModel = new TransactionModel(database);

    }

    /**
     * Adds observer to list of observers to be updated when the model changes.
     * @param observer Observer to be added.
     */
    public static void addObserver(ModelObserver observer)
    {
        ObserverHandler.addObserver(observer);
    }

    /**
     * Edits the name and color of a category with the given id. Color must be a String of a hexadecimal
     * color code with the format: #XXXXXX.
     *
     * @param oldCategory Object of category to be changed.
     */
    public static void editCategoryInfo(Category oldCategory, String newName, String newColor, boolean isIncome) {
        Category newCategory = new Category(newName, newColor, isIncome);

        categoryModel.editCategory(oldCategory, newCategory);
    }

    /**
     * Adds a new category to the database. Color must be a String of a hexadecimal color code with
     * the format: #XXXXXX.
     *
     * @param name  The name of the new category.
     * @param color The color of the new category.
     */
    public static void addNewCategory(String name, String color, boolean isIncome) {
        Category newCategory = new Category(name, color, isIncome);

        categoryModel.addCategory(newCategory);
    }


    /**
     * Removes category with the given id from the database. Transactions under the removed category
     * are automatically moved to the Other category.
     *
     * @param category category to removed
     */
    public static void removeCategory(Category category) {
        categoryModel.deleteCategory(category);
    }

    /**
     * Returns a list of all the categories in the database.
     *
     * @return a list of all the categories in the database.
     */
    public static ArrayList<Category> getAllCategories() {
        return categoryModel.getCategoryList();
    }

    /**
     * Adds new transaction to database.
     *
     * @param amount      The amount of the transaction.
     * @param description A description of the transaction
     * @param date        The date the transaction was made.
     * @param category    Category of the new transaction.
     */
    public static void addNewTransaction(float amount, String description, LocalDate date, Category category) {
        FinancialTransaction newTransaction = new FinancialTransaction(amount, description, date, category);

        transactionModel.addTransaction(newTransaction);
    }

    /**
     * Edits the information of a transaction with the given Id.
     *
     * @param oldTransaction Object of transaction to be changed.
     */
    public static void editTransaction(FinancialTransaction oldTransaction, float newAmount, String newDesc, LocalDate newDate, Category newCategory){
        FinancialTransaction newTransaction = new FinancialTransaction(newAmount, newDesc, newDate, newCategory);

        transactionModel.editTransaction(oldTransaction, newTransaction);
    }

    /**
     * Removes transaction with the given id from the database.
     *
     * @param transaction Transaction to be removed.
     */
    public static void removeTransaction(FinancialTransaction transaction){
        transactionModel.deleteTransaction(transaction);
    }


    /**
     * Retrieves all transactions within the parameters of the TransactionRequest.
     *
     * All parameters being null means all transactions will be retrieved.
     *
     * @param month Month transactions were made, optional.
     * @param year Year transactions were made, optional.
     * @return A list of transactions specified by request.
     */
    public static ArrayList<FinancialTransaction> getTransactions(int month, int year)
    {
        TransactionRequest request = new TransactionRequest((Category)null, month, year);

        return transactionModel.searchTransactions(request);
    }



    /**
     * Retrieves all transactions within the parameters of the TransactionRequest.
     *
     * All parameters being null means all transactions will be retrieved.
     *
     * @param category Category that the transactions belong to, optional.
     * @param month Month transactions were made, optional.
     * @param year Year transactions were made, optional.
     * @return A list of transactions specified by request.
     */
    public static ArrayList<FinancialTransaction> getTransactions(Category category, int month, int year)
    {
        TransactionRequest request = new TransactionRequest(category, month, year);

        return transactionModel.searchTransactions(request);
    }

    /**
     * Gets sum of all transactions within parameters of the TransactionRequest.
     * @param category Category that the transactions belong to, optional.
     * @param month Month transactions were made.
     * @param year Year transactions were made.
     * @return Sum of transactions.
     */
    public static float getTransactionSum(Category category, int month, int year)
    {
        TransactionRequest request = new TransactionRequest(category, month, year);

        return transactionModel.getTransactionSum(request);
    }


    /**
     * Returns a list of all categories in the model.
     * @return A list of all categories in the model.
     */
    public static ArrayList<Category> getCategories()
    {
        return categoryModel.getCategoryList();
    }

    /**
     * Returns a list of all income categories in the model.
     * @return A list of all income categories in the model.
     */
    public static ArrayList<Category> getIncomeCategories(){
        return categoryModel.getIncomeCategories();
    }

    /**
     * Returns a list of a all expense categories in the model.
     * @return A list of all expense categories in the model.
     */
    public static ArrayList<Category> getExpenseCategories(){
        return categoryModel.getExpenseCategories();
    }

    /**
     * Returns the total income amount for a specific month and year.
     * @param month The month to calculate income amount for.
     * @param year The year to calculate income amount for.
     * @return The total income amount for the specified time period.
     */
    public static float getTotalIncome(int month, int year){
        // requests sum of all transactions in the income categories during specified month and year
        TransactionRequest request = new TransactionRequest(categoryModel.getIncomeCategories(), month, year);
        return transactionModel.getTransactionSum(request);
    }

    /**
     * Returns the total expense amount for a specific month and year.
     * @param month The month to calculate expense amount for.
     * @param year The year to calculate expense amount for.
     * @return The total expense amount for the specified time period.
     */
    public static float getTotalExpense(int month, int year){
        // requests sum of all transactions in the expense categories during specified month and year
        TransactionRequest request = new TransactionRequest(categoryModel.getExpenseCategories(), month, year);
        return transactionModel.getTransactionSum(request);
    }


    /**
     * Returns the balance between income amount and expense amount for a specific month and year.
     * @param month The month to calculate the balance for.
     * @param year The year to calculate the balance for.
     * @return The calculated balance.
     */
    public static float getTransactionBalance(int month, int year){
        return getTotalIncome(month, year) - getTotalExpense(month, year);
    }


    /**
     * Removes categories from list which have zero transactions in a given time period.
     * @param list List to be worked on.
     * @param month The month checked for if there is any transactions. (can be null, meaning all months).
     * @param year The year checked for if there is any transactions. (can be null, meaning all years).
     */
    public static void removeEmptyCategories(ArrayList<Category> list, int month, int year){
        TransactionRequest request = new TransactionRequest((Category)null, month, year);
        transactionModel.removeEmptyCategories(list, request);
    }

    /**
     * Sorts a given category list based on the sum of transactions belonging to the category i a specific time period.
     * Categories with largest sum gets the lowest index in the list.
     *
     * @param list The list to be sorted.
     * @param month The month in which the sum will be calculated. (can be 0, meaning all months)
     * @param year The year in which the sum will be calculated. (can be 0, meaning all years)
     */
    public static void sortCategoryListBySum(ArrayList<Category> list, int month, int year){
        TransactionRequest request = new TransactionRequest((Category)null, month, year);
        transactionModel.sortCategoryListBySum(list, request);
    }

    /**
     * Sorts a category list based on how many times it was used in the latest 20 transactions.
     * Larger amount means lower list index.
     * @param categoryList Category list to be sorted
     */
    public static void sortCategoryListByPopularity(ArrayList<Category> categoryList){
        transactionModel.sortCategoryListByPopularity(categoryList);
    }

    /**
     * Sorts a transaction list based on the transaction amount.
     * Larger amount means lower list index.
     * @param list List to be sorted
     */
    public static void sortByAmount(ArrayList<FinancialTransaction> list) {
        transactionModel.sortByAmount(list);
    }

    /**
     * Searches the transaction list to find transactions that match the searched text by note description
     * @param list the the list of transactions to check
     * @param note the searched note description
     */
    public static ArrayList<FinancialTransaction> searchTransactionByNote(ArrayList<FinancialTransaction> list, String note){
        return transactionModel.searchTransactionByNote(list, note);
    }

    /**
     * Searches the transaction list to find transactions that match the searched text by amount
     * @param list the the list of transactions to check
     * @param amount the searched amount
     */
    public static ArrayList<FinancialTransaction> searchTransactionByAmount(ArrayList<FinancialTransaction> list, Float amount){
        return transactionModel.searchTransactionByAmount(list, amount);
    }

}
