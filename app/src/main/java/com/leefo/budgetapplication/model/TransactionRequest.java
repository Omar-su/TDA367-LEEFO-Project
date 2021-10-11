package com.leefo.budgetapplication.model;

import java.util.ArrayList;

/**
 * Implementation of 'command' design pattern.
 *
 * Specifies what transactions are requested when sending requests for transactions to the model.
 *
 * @author Linus Lundgren
 */
public class TransactionRequest {

    /**
     * Specifies what category of transactions.
     */
    private ArrayList<Category> categories; // can be null

    /**
     * Specifies what time transaction was made.
     */
    private final int month, year; // can be 0

    public TransactionRequest(Category category, int month, int year)
    {
        categories = new ArrayList<>();

        categories.add(category);

        this.month = month;
        this.year = year;

        if(month == 0 || year == 0) {
            month = 0;
            year = 0;
        }
    }

    public TransactionRequest(ArrayList<Category> categories, int month, int year)
    {
        this.categories = new ArrayList<>(categories);

        this.month = month;
        this.year = year;

        if(month == 0 || year == 0)
            month = year = 0;
    }

    /**
     * For checking whether time is specified.
     * @return True if time is specified.
     */
    public boolean timeIsSpecified()
    {
        return month != 0 && year != 0;
    }

    /**
     * For checking whether category is specified.
     * @return True if category is specified.
     */
    public boolean categoryIsSpecified()
    {
        return categories != null && !categories.isEmpty();
    }

    public boolean belongsToCategories(FinancialTransaction transaction)
    {
        if(!categoryIsSpecified()) return true;

        for (Category category : categories) {
            if (category.transactionBelongs(transaction))
                return true; // returns true if it belongs to any of the categories
        }

        return false;
    }




    //  GETTERS -----------

    public ArrayList<Category> getCategories()
    {
        return new ArrayList<>(categories);
    }

    public int getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }
}
