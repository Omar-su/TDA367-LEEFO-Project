package com.leefo.budgetapplication.model;

/**
 * Interface for interaction between CategoryModel and TransactionModel
 */
public interface ITransactionModel {
    void replaceCatForTransactions(Category oldCategory, Category newCategory);
}
