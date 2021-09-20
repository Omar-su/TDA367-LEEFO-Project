package com.leefo.budgetapplication.model;


import androidx.annotation.NonNull;

/*
* Immutable container class for category data
* */
public class Category {

    private final int id; // unique id assigned by db
    private final String name;
    private final String color;


    public Category(int id, String name, String color)
    {
        this.id = id;
        this.name = name;
        this. color = color;
    }


    // ----------

    // returns true if given transaction is of this category
    public boolean transactionBelongs(Transaction transaction)
    {
        return id == transaction.getCategoryId();
    }

    // compares two categories
    public boolean Equals(Category cat)
    {
        return cat.getId() == id;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "" + id + " : " + name + ", color : " + color;
    }




    // GETTERS -------

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getColor()
    {
        return color;
    }


}
