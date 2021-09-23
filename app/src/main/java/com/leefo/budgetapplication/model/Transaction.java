package com.leefo.budgetapplication.model;


import androidx.annotation.NonNull;

/**
* Immutable container class for transaction data.
* */
public class Transaction {

    /**
     * Unique id assigned in database.
     */
    private final int id;
    /**
     * Id of the category that the transaction belongs to.
     */
    private final int categoryId;
    /**
     * Date that the transaction occurred.
     */
    private final String date;
    /**
     * Total currency amount handled by the transaction.
     */
    private final double amount;

    /**
     * Description for transaction, optional.
     */
    private final String description;

    public Transaction(int id, double amount, String description, String date, int categoryId)
    {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.categoryId = categoryId;
    }



    // ---------

    /**
     * Compares two transactions.
     * @param compare Transaction to compare to.
     * @return True if the transaction id is the same.
     */
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

    public String getDate()
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
