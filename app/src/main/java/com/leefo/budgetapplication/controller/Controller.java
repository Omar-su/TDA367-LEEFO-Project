package com.leefo.budgetapplication.controller;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryHandler;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.model.TransactionHandler;

import java.util.List;

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
    private final TransactionHandler transactionHandler;

    /**
     * The handler for category modification and retrieval.
     */
    private final CategoryHandler categoryHandler;


    //View view;?

    /**
     * Constructor to create instance of Controller. The constructor creates instances of
     * CategoryHandler and TransactionHandler.
     */
    public Controller() {
        this.transactionHandler = new TransactionHandler();
        this.categoryHandler = new CategoryHandler();
    }

    /**
     * Edits the name and color of a category with the given id. Color must be a String of a hexadecimal
     * color code with the format: #XXXXXX.
     *
     * @param id    The id of the category to be edited.
     * @param name  The new name of the category.
     * @param color The new color of the category.
     */
    public void editCategoryInfo(int id, String name, String color) {
        categoryHandler.editCategory(id, name, color);
    }

    /**
     * Adds a new category to the database. Color must be a String of a hexadecimal color code with
     * the format: #XXXXXX.
     *
     * @param name  The name of the new category.
     * @param color The color of the new category.
     */
    public void addNewCategory(String name, String color) {
        categoryHandler.addCategory(name, color);
    }


    /**
     * Removes category with the given id from the database. Transactions under the removed category
     * are automatically moved to the Other category.
     *
     * @param id The id of the category to be removed.
     */
    public void removeCategory(int id) {
        categoryHandler.removeCategory(id);
    }

    /**
     * Returns a list of all the categories in the database.
     *
     * @return a list of all the categories in the database.
     */
    public List<Category> getAllCategories() {
        return categoryHandler.getCategories();
    }

    /**
     * Returns a list of transactions made in a given year and month.
     *
     * @param year  The year the transactions were made.
     * @param month The month the transactions were made.
     * @return A list with transactions made in the given year and month.
     */
    public List<Transaction> searchTransactionsByMonth(int year, int month) {
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
    public List<Transaction> searchTransactionsByMonthAndCategory(int year, int month, int categoryId) {
        return transactionHandler.searchByMonthAndCategory(year, month, categoryId);
    }


}
