package com.leefo.budgetapplication.model;

import java.util.List;

/**
 * Contains static methods for handling transactions in the database (getting, setting, modifying).
 */
public class TransactionHandler {

    /**
     * Gets a list of transactions from a given year and month.
     * @param year Year transactions were made.
     * @param month Month transactions were made.
     * @return Returns a list of transactions.
     */
    public List<Transaction> searchByMonth(int year, int month)
    {
        return DataBaseManager.getTransactionsByMonth(year, month);
    }

    /**
     * Gets a list of transactions from a given year and month. Also filters out any transactions that are not of a specific category.
     * @param year Year transactions were made.
     * @param month Month transactions were made.
     * @param categoryId Id of category to filter by.
     * @return A list of transactions.
     */
    public List<Transaction> searchByMonthAndCategory(int year, int month, int categoryId)
    {
        return DataBaseManager.getTransactionsByMonthAndCat(year, month, categoryId);
    }



}
