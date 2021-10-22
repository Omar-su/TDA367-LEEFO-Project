package com.leefo.budgetapplication.model;


import androidx.annotation.NonNull;

import java.time.LocalDate;

/**
* Immutable container class for transaction data.
 *
 * @author Linus Lundgren
* */
public class FinancialTransaction {

    /**
     * Id of the category that the transaction belongs to.
     */
    private final Category category;
    /**
     * Date that the transaction occurred.
     */
    private final LocalDate date;
    /**
     * Total currency amount handled by the transaction.
     */
    private final float amount;
    /**
     * Description for transaction, optional.
     */
    private final String description;

    public FinancialTransaction(float amount, String description, LocalDate date, Category category) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
    }



    // GETTERS ------


    public Category getCategory()
    {
        return category;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public float getAmount()
    {
        return amount;
    }

    public String getDescription()
    {
        return description;
    }


    //  --------


}
