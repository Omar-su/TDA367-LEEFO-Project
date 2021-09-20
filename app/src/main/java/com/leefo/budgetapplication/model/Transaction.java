package com.leefo.budgetapplication.model;


import androidx.annotation.NonNull;

/*
* Immutable container class for transaction data
* */
public class Transaction {

    private final int id, categoryId; // each transaction has a unique id, same applies for categories (assigned in db)
    private final int date;
    private final double amount; // currency amount handled by transaction
    private final String description;

    public Transaction(int id, double amount, String description, int date, int categoryId)
    {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.categoryId = categoryId;
    }



    // ---------

    // compares two categories
    public boolean Equals(Transaction compare)
    {
        return compare.getId() == id;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "" + id + " : " + amount + ", date : " + date;
    }




    // GETTERS ------

    public int getId()
    {
        return id;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public int getDate()
    {
        return date;
    }

    public double getAmount()
    {
        return amount;
    }

    public String getDescription()
    {
        return description;
    }


    //  --------


}
