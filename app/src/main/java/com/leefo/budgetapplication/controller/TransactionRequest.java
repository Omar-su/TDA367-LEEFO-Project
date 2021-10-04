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
    private Category category; // can be null

    /**
     * Specifies what time transaction was made.
     */
    private final int month, year; // can be 0

    public TransactionRequest(Category category, int month, int year)
    {
        this.category = category;
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
        return category != null;
    }




    //  GETTERS -----------

    public Category getCategory()
    {
        return category;
    }

    public int getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
