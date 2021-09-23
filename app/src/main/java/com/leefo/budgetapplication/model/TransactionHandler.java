package com.leefo.budgetapplication.model;

import android.provider.ContactsContract;

import java.util.List;

/**
 * Contains static methods for handling transactions in the database (getting, setting, modifying).
 */
public class TransactionHandler {


    /**
     * Gets a list of transactions from a given year and month.
     *
     * @param year Year transactions were made.
     * @param month Month transactions were made.
     * @return Returns a list of transactions.
     */
    public List<Transaction> searchByMonth(String year, String month)
    {
        return DataBaseManager.getTransactionsByMonth(year, month);
    }

    /**
     * Gets a list of transactions from a given year and month. Also filters out any transactions that are not of a specific category.
     *
     * @param year Year transactions were made.
     * @param month Month transactions were made.
     * @param categoryId Id of category to filter by.
     * @return A list of transactions.
     */
    public List<Transaction> searchByMonthAndCategory(String year, String month, int categoryId)
    {
        return DataBaseManager.getTransactionsByMonthAndCat("" + year, "" + month, categoryId);
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
        DataBaseManager.addTransaction(description, amount, date, categoryId);
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
        DataBaseManager.editTransaction(id, amount, description, date, catId);
    }

    /**
     * Removes specified transaction.
     *
     * @param id Id of specified transaction to be deleted.
     */
    public void removeTransaction(int id)
    {
        DataBaseManager.deleteTransaction(id);
    }



}
