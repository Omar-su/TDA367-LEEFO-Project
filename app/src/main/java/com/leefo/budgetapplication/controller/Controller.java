package com.leefo.budgetapplication.controller;

import android.content.Context;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryHandler;
import com.leefo.budgetapplication.model.DatabaseInitializer;
import com.leefo.budgetapplication.model.ObserverHandler;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.model.TransactionHandler;
import com.leefo.budgetapplication.view.ModelObserver;

import java.util.ArrayList;

/**
 * The Controller class represents the Controller in the Model-View-Controller pattern.
 * The class responsibility is to listen to the View and respond by modifying the model and updating
 * the view.
 *
 * @author Felix Edholm
 */
public class Controller {

    /**
     * The handler for transaction modification and retrieval.
     */
    private static TransactionHandler transactionHandler;

    /**
     * The handler for category modification and retrieval.
     */
    private static CategoryHandler categoryHandler;



    /**
     * Initializes database as well as the TransactionHandler and CategoryHandler.
     * @param context Application context for database.
     */
    public static void InitializeBackend(Context context)
    {
        DatabaseInitializer.InitializeDatabase(context);

        transactionHandler = new TransactionHandler();
        categoryHandler = new CategoryHandler();
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
     * @param id    The id of the category to be edited.
     * @param name  The new name of the category.
     * @param color The new color of the category.
     */
    public static void editCategoryInfo(int id, String name, String color) {
        categoryHandler.editCategory(id, name, color);
    }

    /**
     * Adds a new category to the database. Color must be a String of a hexadecimal color code with
     * the format: #XXXXXX.
     *
     * @param name  The name of the new category.
     * @param color The color of the new category.
     */
    public static void addNewCategory(String name, String color) {
        categoryHandler.addCategory(name, color);
    }


    /**
     * Removes category with the given id from the database. Transactions under the removed category
     * are automatically moved to the Other category.
     *
     * @param id The id of the category to be removed.
     */
    public static void removeCategory(int id) {
        categoryHandler.removeCategory(id);
    }

    /**
     * Returns a list of all the categories in the database.
     *
     * @return a list of all the categories in the database.
     */
    public static ArrayList<Category> getAllCategories() {
        return categoryHandler.getEveryCategory();
    }

    /**
     * Adds new transaction to database.
     *
     * @param amount      The amount of the transaction.
     * @param description A description of the transaction
     * @param date        The date the transaction was made.
     * @param categoryId  The id for the category of the added transaction.
     */
    public static void addNewTransaction(float amount, String description, String date, int categoryId) {
        transactionHandler.addTransaction(amount, description, date, categoryId);
    }

    /**
     * Edits the information of a transaction with the given Id.
     *
     * @param id The id of the transaction to be edited.
     * @param amount The new amount of the transaction.
     * @param description The new description of the transaction.
     * @param date The new date of the transaction.
     * @param CatId The new id for the transactions category.
     */
    public static void editTransactionInfo(int id, int amount, String description, String date, int CatId){
        transactionHandler.editTransaction(id, amount, description, date, CatId);
    }

    /**
     * Removes transaction with the given id from the database.
     *
     * @param transId The id of the transaction to be removed.
     */
    public static void removeTransaction(int transId){
        transactionHandler.removeTransaction(transId);
    }

    /**
     * Gets a list of every transaction in the database.
     * @return List of every transaction in the database.
     */
    public static ArrayList<Transaction> getAllTransactions(){
        return transactionHandler.getAllTransactions();
    }

    /**
     * Returns a list of transactions made in a given year and month.
     *
     * @param year  The year the transactions were made.
     * @param month The month the transactions were made.
     * @return A list with transactions made in the given year and month.
     */
    public static ArrayList<Transaction> searchTransactionsByMonth(String year, String month) {
        return transactionHandler.searchByMonth(year, month);
    }

    /**
     * Returns a list of transactions made in a given year and month filtered by a specific
     * category
     *
     * @param year       The year the transactions were made.
     * @param month      The month the transactions were made.
     * @param categoryId The id of the category to filter by.
     * @return A list with the transactions made in the given year and month filtered by category.
     */
    public static ArrayList<Transaction> searchTransactionsByMonthAndCategory(String year, String month, int categoryId) {
        return transactionHandler.searchByMonthAndCategory(year, month, categoryId);
    }

    /**
     * Gets the category corresponding to a given category id.
     * @param id id of the category wished to get.
     * @return the Category corresponding to the given id.
     */
    public static Category getCategoryFromId(int id){
        return categoryHandler.getCategoryFromId(id);
    }

    public static double getCategorySumByMonth(int id, String year, String month){
        return transactionHandler.getCategorySumByMonth(id, year, month);
    }

}
