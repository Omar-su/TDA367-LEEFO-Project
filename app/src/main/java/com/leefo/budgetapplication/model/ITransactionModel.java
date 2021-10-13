package com.leefo.budgetapplication.model;

/**
 * Interface for interaction between CategoryModel and TransactionModel
 */
public interface ITransactionModel
{
    void replaceCategory(Category oldCategory, Category newCategory);
}
