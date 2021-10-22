package com.leefo.budgetapplication.model;

import java.util.List;

/**
 * Interface for database used by TransactionModel.
 *
 * @author Linus Lundgren
 */
public interface IDatabase {

    /**
     * Gets all transactions stored in database.
     * @return A list of transactions.
     */
    List<FinancialTransaction> getFinancialTransactions();

    /**
     * Gets all categories stored in database.
     * @return A list of categories.
     */
    List<Category> getCategories();

    /**
     * Saves a transaction to database.
     * @param transaction Transaction to be saved.
     */
    void saveData(FinancialTransaction transaction);

    /**
     * Saves a category to database.
     * @param category Category to be saved.
     */
    void saveData(Category category);

    /**
     * Removes transaction from database.
     * @param transaction Transaction to be removed.
     */
    void removeData(FinancialTransaction transaction);

    /**
     * Removes category from database.
     * @param category Category to be removed.
     */
    void removeData(Category category);
}
