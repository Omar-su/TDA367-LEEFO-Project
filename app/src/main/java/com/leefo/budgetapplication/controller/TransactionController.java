package com.leefo.budgetapplication.controller;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.TransactionModel;
import com.leefo.budgetapplication.model.TransactionRequest;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The TransactionController class represents a Controller in the Model-View-Controller pattern.
 * The class responsibility is to listen to the View and respond by modifying transaction data and
 * updating the view.
 *
 * @author Felix Edholm, Linus Lundgren, Emelie Edberg
 */
public class TransactionController {

    /**
     * Object handling logic for transactions.
     */
    private static TransactionModel transactionModel;

    public TransactionController(TransactionModel transactionModel) {
        TransactionController.transactionModel = transactionModel;
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
    public static void editTransaction(FinancialTransaction oldTransaction, float newAmount, String newDesc, LocalDate newDate, Category newCategory) {
        FinancialTransaction newTransaction = new FinancialTransaction(newAmount, newDesc, newDate, newCategory);

        transactionModel.editTransaction(oldTransaction, newTransaction);
    }

    /**
     * Removes transaction with the given id from the database.
     *
     * @param transaction Transaction to be removed.
     */
    public static void removeTransaction(FinancialTransaction transaction) {
        transactionModel.deleteTransaction(transaction);
    }


    /**
     * Retrieves all transactions within the parameters of the TransactionRequest.
     * <p>
     * All parameters being null means all transactions will be retrieved.
     *
     * @param month Month transactions were made, optional.
     * @param year  Year transactions were made, optional.
     * @return A list of transactions specified by request.
     */
    public static ArrayList<FinancialTransaction> getTransactions(int month, int year) {
        TransactionRequest request = new TransactionRequest((Category) null, month, year);

        return transactionModel.searchTransactions(request);
    }


    /**
     * Retrieves all transactions within the parameters of the TransactionRequest.
     * <p>
     * All parameters being null means all transactions will be retrieved.
     *
     * @param category Category that the transactions belong to, optional.
     * @param month    Month transactions were made, optional.
     * @param year     Year transactions were made, optional.
     * @return A list of transactions specified by request.
     */
    public static ArrayList<FinancialTransaction> getTransactions(Category category, int month, int year) {
        TransactionRequest request = new TransactionRequest(category, month, year);

        return transactionModel.searchTransactions(request);
    }

    /**
     * Gets sum of all transactions within parameters of the TransactionRequest.
     *
     * @param category Category that the transactions belong to, optional.
     * @param month    Month transactions were made.
     * @param year     Year transactions were made.
     * @return Sum of transactions.
     */
    public static float getTransactionSum(Category category, int month, int year) {
        TransactionRequest request = new TransactionRequest(category, month, year);

        return transactionModel.getTransactionSum(request);
    }

    /**
     * Returns the total income amount for a specific month and year.
     *
     * @param month The month to calculate income amount for.
     * @param year  The year to calculate income amount for.
     * @return The total income amount for the specified time period.
     */
    public static float getTotalIncome(int month, int year) {
        // requests sum of all transactions in the income categories during specified month and year
        TransactionRequest request = new TransactionRequest(null, month, year);
        return transactionModel.getTotalIncome(request);
    }

    /**
     * Returns the total expense amount for a specific month and year.
     *
     * @param month The month to calculate expense amount for.
     * @param year  The year to calculate expense amount for.
     * @return The total expense amount for the specified time period.
     */
    public static float getTotalExpense(int month, int year) {
        // requests sum of all transactions in the expense categories during specified month and year
        TransactionRequest request = new TransactionRequest(null, month, year);
        return transactionModel.getTotalExpense(request);
    }


    /**
     * Returns the balance between income amount and expense amount for a specific month and year.
     *
     * @param month The month to calculate the balance for.
     * @param year  The year to calculate the balance for.
     * @return The calculated balance.
     */
    public static float getTransactionBalance(int month, int year) {

        TransactionRequest request = new TransactionRequest(null, month, year);
        return transactionModel.getTransactionBalance(request);
    }


    /**
     * Removes categories from list which have zero transactions in a given time period.
     *
     * @param list  List to be worked on.
     * @param month The month checked for if there is any transactions. (can be null, meaning all months).
     * @param year  The year checked for if there is any transactions. (can be null, meaning all years).
     */
    public static void removeEmptyCategories(ArrayList<Category> list, int month, int year) {
        TransactionRequest request = new TransactionRequest((Category) null, month, year);
        transactionModel.removeEmptyCategories(list, request);
    }

    /**
     * Sorts a given category list based on the sum of transactions belonging to the category i a specific time period.
     * Categories with largest sum gets the lowest index in the list.
     *
     * @param list  The list to be sorted.
     * @param month The month in which the sum will be calculated. (can be 0, meaning all months)
     * @param year  The year in which the sum will be calculated. (can be 0, meaning all years)
     */
    public static void sortCategoryListBySum(ArrayList<Category> list, int month, int year) {
        TransactionRequest request = new TransactionRequest((Category) null, month, year);
        transactionModel.sortCategoryListBySum(list, request);
    }

    /**
     * Sorts a category list based on how many times it was used in the latest 20 transactions.
     * Larger amount means lower list index.
     *
     * @param categoryList Category list to be sorted
     */
    public static void sortCategoryListByPopularity(ArrayList<Category> categoryList) {
        transactionModel.sortCategoryListByPopularity(categoryList);
    }


    /**
     * Gets amount of days in a row where user has spent less than they do on average daily.
     *
     * @return amount of days streak has been ongoing
     */
    public static int getCurrentStreak() {
        return transactionModel.getCurrentStreak();
    }

    /**
     * Gets the longest amount of time in days where the user has spent less than they do on average daily.
     *
     * @return record length streak
     */
    public static int getRecordStreak() {
        return transactionModel.getRecordStreak();
    }

    /**
     * Gets average amount spent per day.
     *
     * @return average spending
     */
    public static float getAverageSpending() {
        return transactionModel.getAverageSpending();
    }

    /**
     * Gets all expense transactions made today.
     *
     * @return sum of all of todays expenses
     */
    public static float getTodaysExpenses() {
        return transactionModel.getTodaysExpenses();
    }
}
