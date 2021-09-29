package com.leefo.budgetapplication.controller;

import com.leefo.budgetapplication.model.Category;

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
    private final Category category; // can be null

    /**
     * Specifies what time transaction was made.
     */
    private final String month, year; // can be null

    public TransactionRequest(Category category, String month, String year)
    {
        this.category = category;
        this.month = month;
        this.year = year;

        if(month == null || year == null)
            month = year = null;
    }

    /**
     * For checking whether time is specified.
     * @return True if time is specified.
     */
    public boolean timeIsSpecified()
    {
        return month != null && year != null;
    }

    /**
     * For checking whether category is specified.
     * @return True if category is specified.
     */
    public boolean categoryIsSpecified()
    {
        return category != null;
    }




    //  GETTERS -----------

    public Category getCategory()
    {
        return category;
    }

    public String getMonth()
    {
        return month;
    }

    public String getYear()
    {
        return year;
    }

}