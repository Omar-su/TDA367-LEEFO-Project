package com.leefo.budgetapplication.model;

import java.util.List;

/**
 * Contains static methods for handling transactions in the database (getting, setting, modifying).
 */
public class TransactionHandler {

    // TODO MAKE METHODS NOT STATIC AND CREATE INSTANCES OF TRANSACTIONHANDLER IN CONTROLLER

    /**
     * Gets a list of transactions from a given year and month.
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
     * @param year Year transactions were made.
     * @param month Month transactions were made.
     * @param categoryId Id of category to filter by.
     * @return A list of transactions.
     */
    public List<Transaction> searchByMonthAndCategory(String year, String month, int categoryId)
    {
        return DataBaseManager.getTransactionsByMonthAndCat(year, month, categoryId);
    }

    public void addTransaction(double amount, String description, int date,
                                  boolean isExpense, int categoryId) {
        DataBaseManager.addTransaction(description,amount,String.valueOf(date),categoryId);
    }

    public void editTransaction(int id, int amount, String description, String date, int CatId){
        DataBaseManager.editTransaction(id,amount,description,date,CatId);
    }

    public void removeTransaction(int transId){
        DataBaseManager.deleteTransaction(transId);
    }





}
