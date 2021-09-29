package com.leefo.budgetapplication.controller;

import android.content.Context;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryHandler;
import com.leefo.budgetapplication.model.DataHandler;
import com.leefo.budgetapplication.model.DatabaseInitializer;
import com.leefo.budgetapplication.model.ObserverHandler;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.model.TransactionHandler;
import com.leefo.budgetapplication.view.ModelObserver;

import java.util.ArrayList;
import java.util.List;

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
    private static DataHandler dataHandler;



    /**
     * Initializes database as well as the TransactionHandler and CategoryHandler.
     * @param context Application context for database.
     */
    public static void InitializeBackend(Context context)
    {
        DatabaseInitializer.InitializeDatabase(context);

        dataHandler = new DataHandler();
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
     * @param newCategory Updated category.
     */
    public static void editCategoryInfo(Category oldCategory, Category newCategory) {
        dataHandler.editCategory(oldCategory, newCategory);
    }

    /**
     * Adds a new category to the database. Color must be a String of a hexadecimal color code with
     * the format: #XXXXXX.
     *
     * @param name  The name of the new category.
     * @param color The color of the new category.
     */
    public static void addNewCategory(String name, String color) {
        Category newCategory = new Category(name, color);

        dataHandler.addCategory(newCategory);
    }


    /**
     * Removes category with the given id from the database. Transactions under the removed category
     * are automatically moved to the Other category.
     *
     * @param category category to removed
     */
    public static void removeCategory(Category category) {
        dataHandler.deleteCategory(category);
    }

    /**
     * Returns a list of all the categories in the database.
     *
     * @return a list of all the categories in the database.
     */
    public static ArrayList<Category> getAllCategories() {
        return (ArrayList<Category>) dataHandler.getCategoryList();
    }

    /**
     * Adds new transaction to database.
     *
     * @param amount      The amount of the transaction.
     * @param description A description of the transaction
     * @param date        The date the transaction was made.
     * @param category    Category of the new transaction.
     */
    public static void addNewTransaction(float amount, String description, String date, Category category) {
        Transaction newTransaction = new Transaction(amount, description, date, category);

        dataHandler.addTransaction(newTransaction);
    }

    /**
     * Edits the information of a transaction with the given Id.
     *
     * @param oldTransaction Object of transaction to be changed.
     * @param newTransaction Updated transaction.
     */
    public static void editTransaction(Transaction oldTransaction, Transaction newTransaction){
        dataHandler.editTransaction(oldTransaction, newTransaction);
    }

    /**
     * Removes transaction with the given id from the database.
     *
     * @param transaction Transaction to be removed.
     */
    public static void removeTransaction(Transaction transaction){
        dataHandler.deleteTransaction(transaction);
    }

    /**
     * Retrieves all transactions within the parameters of the TransactionRequest.
     * @param request Specifies what transactions should be returned.
     * @return A list of transactions specified by request.
     */
    public List<Transaction> getTransactions(TransactionRequest request)
    {
        return dataHandler.searchTransactions(request);
    }

    /**
     * Gets sum of all transactions within parameters of the TransactionRequest.
     * @param request Specifies what transactions should be added to sum.
     * @return Sum of transactions.
     */
    public float getTransactionSum(TransactionRequest request)
    {
        return dataHandler.getSum(request);
    }

    /**
     * Getter for all categories in model.
     * @return A list of categories.
     */
    public List<Category> getCategories()
    {
        return dataHandler.getCategoryList();
    }

}
