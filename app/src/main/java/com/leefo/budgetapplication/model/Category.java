package com.leefo.budgetapplication.model;


import androidx.annotation.NonNull;

/**
* Immutable container class for category data.
 *
 * @author Linus Lundgren
* */
public class Category {

    /**
     * Name of category
     */
    private final String name;
    /**
     * Category color. Hex color system
     */
    private final String color;

    /**
     * True if transactions of this category goes out of or into the account balance.
     */
    private final boolean isIncome;


    private final int budgetGoal;


    public Category(String name, String color, boolean isIncome, int budgetGoal )
    {
        this.name = name;
        this.color = color;
        this.isIncome = isIncome;
        this.budgetGoal = budgetGoal;
    }


    public Category(String name, String color, boolean isIncome)
    {
        this.name = name;
        this.color = color;
        this.isIncome = isIncome;
        this.budgetGoal = 0;
    }

    // ----------

    /**
     * Method for checking if a transaction belongs to this category.
     * @param transaction Transaction to be checked.
     * @return True if transaction belongs to category.
     */
    public boolean transactionBelongs(FinancialTransaction transaction)
    {
        return Equals(transaction.getCategory());
    }

    /**
     * Compares two categories
     * @param cat Category to compare to
     * @return True if the category id's are the same
     */
    public boolean Equals(Category cat)
    {
        return cat.getName().equals(name) && cat.getColor().equals(color);
    }

    @NonNull
    @Override
    public String toString()
    {
        return "" + name + " : " + color;
    }




    // GETTERS -------

    public String getName()
    {
        return name;
    }

    public String getColor()
    {
        return color;
    }

    public int getGoal(){return budgetGoal;}

    public boolean isIncome() { return isIncome; }

}
