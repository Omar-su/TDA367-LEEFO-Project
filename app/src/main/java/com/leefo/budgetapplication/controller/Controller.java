package com.leefo.budgetapplication.controller;

import android.content.Context;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.TransactionModel;
import com.leefo.budgetapplication.model.DatabaseInitializer;
import com.leefo.budgetapplication.model.ObserverHandler;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.ModelObserver;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The Controller class represents the Controller in the Model-View-Controller pattern.
 * The class responsibility is to listen to the View and respond by modifying the model and updating
 * the view.
 *
 * @author Felix Edholm, Linus Lundgren
 */
public class Controller {

    /**
     * Object handling logic for all transactions and categories.
     */
    private static TransactionModel transactionModel;



    /**
     * Initializes database as well as the TransactionHandler and CategoryHandler.
     * @param context Application context for database.
     */
    public static void InitializeBackend(Context context)
    {
        DatabaseInitializer.InitializeDatabase(context);

        transactionModel = new TransactionModel();
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

        transactionModel.editCategory(oldCategory, newCategory);
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

        transactionModel.addCategory(newCategory);
    }


    /**
     * Removes category with the given id from the database. Transactions under the removed category
     * are automatically moved to the Other category.
     *
     * @param category category to removed
     */
    public static void removeCategory(Category category) {
        transactionModel.deleteCategory(category);
    }

    /**
     * Returns a list of all the categories in the database.
     *
     * @return a list of all the categories in the database.
     */
    public static ArrayList<Category> getAllCategories() {
        return (ArrayList<Category>) transactionModel.getCategoryList();
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
     * @param category Category that the transactions belong to, optional.
     * @return A list of transactions specified by request.
     */
    public static ArrayList<FinancialTransaction> getTransactions(Category category)
    {
        TransactionRequest request = new TransactionRequest(category, null, null);

        return transactionModel.searchTransactions(request);
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
    public static ArrayList<FinancialTransaction> getTransactions(String month, String year)
    {
        TransactionRequest request = new TransactionRequest(null, month, year);

        return transactionModel.searchTransactions(request);
    }

    /**
     * Retrieves all transactions within the parameters of the TransactionRequest.
     *
     * All parameters being null means all transactions will be retrieved.
     *
     * @return A list of transactions specified by request.
     */
    public static ArrayList<FinancialTransaction> getTransactions()
    {
        TransactionRequest request = new TransactionRequest(null, null, null);

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
    public static ArrayList<FinancialTransaction> getTransactions(Category category, String month, String year)
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
    public static float getTransactionSum(Category category, String month, String year)
    {
        TransactionRequest request = new TransactionRequest(category, month, year);

        return transactionModel.getTransactionSum(request);
    }

    /**
     * Gets sum of all transactions within parameters of the TransactionRequest.
     * @return Sum of transactions.
     */
    public static float getTransactionSum()
    {
        TransactionRequest request = new TransactionRequest(null, null, null);

        return transactionModel.getTransactionSum(request);
    }

    /**
     * Gets sum of all transactions within parameters of the TransactionRequest.
     * @param category Category that the transactions belong to, optional.
     * @return Sum of transactions.
     */
    public static float getTransactionSum(Category category)
    {
        TransactionRequest request = new TransactionRequest(category, null, null);

        return transactionModel.getTransactionSum(request);
    }

    /**
     * Gets sum of all transactions within parameters of the TransactionRequest.
     * @param month Month transactions were made.
     * @param year Year transactions were made.
     * @return Sum of transactions.
     */
    public static float getTransactionSum(String month, String year)
    {
        TransactionRequest request = new TransactionRequest(null, month, year);

        return transactionModel.getTransactionSum(request);
    }

    /**
     * Getter for all categories in model.
     * @return A list of categories.
     */
    public static ArrayList<Category> getCategories()
    {
        return transactionModel.getCategoryList();
    }

    public static ArrayList<Category> getIncomeCategories(){
        return transactionModel.getIncomeCategories();
    }

    public static ArrayList<Category> getExpenseCategories(){
        return transactionModel.getExpenseCategories();
    }

    public static double getTotalIncome(String month, String year){
        TransactionRequest request = new TransactionRequest(null, month, year);
        return transactionModel.getTotalIncome(request);
    }

    public static double getTotalIncome(){
        TransactionRequest request = new TransactionRequest(null, null, null);
        return transactionModel.getTotalIncome(request);
    }

    public static double getTotalExpense(String month, String year){
        TransactionRequest request = new TransactionRequest(null, month, year);
        return transactionModel.getTotalExpense(request);
    }

    public static double getTotalExpense(){
        TransactionRequest request = new TransactionRequest(null, null, null);
        return transactionModel.getTotalExpense(request);
    }

    public static double getTransactionBalance(String month, String year){
        TransactionRequest request = new TransactionRequest(null, month, year);
        return transactionModel.getTransactionBalance(request);
    }

    public static double getTransactionBalance(){
        TransactionRequest request = new TransactionRequest(null, null, null);
        return transactionModel.getTransactionBalance(request);
    }

}
