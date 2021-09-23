package com.leefo.budgetapplication.model;


import androidx.annotation.NonNull;

/**
* Immutable container class for category data.
* */
public class Category {

    /**
     * Represents category id in database.
     */
    private final int id; // unique id assigned by db
    /**
     * Name of category
     */
    private final String name;
    /**
     * Category color.
     */
    private final String color;


    public Category(int id, String name, String color)
    {
        this.id = id;
        this.name = name;
        this.color = color;
    }


    // ----------

    /**
     * Method for checking if a transaction belongs to this category.
     * @param transaction Transaction to be checked.
     * @return True if transaction belongs to category.
     */
    public boolean transactionBelongs(Transaction transaction)
    {
        return id == transaction.getCategoryId();
    }

    /**
     * Compares two categories
     * @param cat Category to compare to
     * @return True if the category id's are the same
     */
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
