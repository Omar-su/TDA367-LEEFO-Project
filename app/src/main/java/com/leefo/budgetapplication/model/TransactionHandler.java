package com.leefo.budgetapplication.model;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Contains methods for handling transactions in the database (getting, setting, modifying).
 */
public class TransactionHandler extends ObserverHandler {

    final private DataBaseManager database;

    public TransactionHandler()
    {
        database = DataBaseManager.getInstance();
    }

    /**
     * Gets a list of every transaction in the database.
     * @return List of every transaction in the database.
     */
    public ArrayList<Transaction> getAllTransactions(){
        return database.getAllTransactions();
    }

    /**
     * Gets a list of transactions from a given year and month.
     *
     * @param year Year transactions were made.
     * @param month Month transactions were made.
     * @return Returns a list of transactions.
     */
    public ArrayList<Transaction> searchByMonth(String year, String month)
    {
        return database.getTransactionsByMonth(year, month);
    }

    /**
     * Gets a list of transactions from a given year and month. Also filters out any transactions that are not of a specific category.
     *
     * @param year Year transactions were made.
     * @param month Month transactions were made.
     * @param categoryId Id of category to filter by.
     * @return A list of transactions.
     */
    public ArrayList<Transaction> searchByMonthAndCategory(String year, String month, int categoryId)
    {
        return database.getTransactionsByMonthAndCat("" + year, "" + month, categoryId);
    }

    /**
     * Adds transaction to the database.
     *
     * @param amount Total transaction value.
     * @param description Transaction description, optional.
     * @param date Date transaction was made
     * @param categoryId Category that transaction belongs to.
     */
    public void addTransaction(float amount, String description, String date, int categoryId)
    {
        database.addTransaction(description, amount, date, categoryId);

        updateObservers(); // updates views
    }

    /**
     * Changes the information of a given transaction.
     *
     * @param id Id of transaction to be changed.
     * @param amount New transaction value.
     * @param description New description of transaction.
     * @param date Date transaction was made.
     * @param catId Category transaction belongs to.
     */
    public void editTransaction(int id, float amount, String description, String date, int catId)
    {
        database.editTransaction(id, amount, description, date, catId);

        updateObservers(); // updates views
    }

    /**
     * Removes specified transaction.
     *
     * @param id Id of specified transaction to be deleted.
     */
    public void removeTransaction(int id)
    {
        database.deleteTransaction(id);

        updateObservers(); // updates views
    }



}
