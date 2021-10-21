package com.leefo.budgetapplication.controller;

import com.leefo.budgetapplication.model.BudgetGrader;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.TransactionRequest;

import java.util.ArrayList;

/**
 * The BudgetGradeController class represents a Controller in the Model-View-Controller pattern.
 * The class responsibility is to listen to the View and respond by retrieving graded budgets and
 * updating the view.
 *
 * @author Felix Edholm, Linus Lundgren, Emelie Edberg
 */
public class BudgetGradeController {

    /**
     * Object handling logic for budget grading
     */
    private static BudgetGrader budgetGrader;

    public BudgetGradeController(BudgetGrader budgetGrader) {
        BudgetGradeController.budgetGrader = budgetGrader;
    }

    /**
     * Returns a list of categories with budgets for the month specific.
     *
     * @param month The month to get categories for.
     * @param year  The year of the month to get categories for.
     * @return The list of categories.
     */
    public static ArrayList<Category> getBudgetCategoriesByMonth(int month, int year) {
        return budgetGrader.getBudgetCategoriesByMonth(new TransactionRequest(null, month, year));
    }

    /**
     * Returns a grade for the budget outcome of a category.
     * Outcome = actual expense/budget goal.
     * Category is graded on a scale from 0-5 in .5 intervals.
     *
     * @param category The category to be graded.
     * @return The calculated grade.
     */
    public static float gradeCategory(Category category, int month, int year) {
        return budgetGrader.gradeCategory(new TransactionRequest(category, month, year));
    }

    /**
     * Returns the rounded budget outcome for a specific category. Rounded to two decimals.
     * Outcome = actual expense/budget goal.
     *
     * @param category The category to get rounded outcome for.
     * @return The rounded budget outcome.
     */
    public static float getRoundedBudgetOutcome(Category category, int month, int year) {
        return budgetGrader.getRoundedBudgetOutcome(new TransactionRequest(category, month, year));
    }

    /**
     * Returns the average budget grade for all categories in a specific month.
     *
     * @param month The month to calculate average for.
     * @param year  The year of the month to calculate average for.
     * @return The average budget grade for the specific month.
     */
    public static float getAverageGradeForMonth(int month, int year) {
        return budgetGrader.getAverageGradeForMonth(new TransactionRequest(null, month, year));
    }

    public static ArrayList<Category> getAllBudgetCategories() {
        return budgetGrader.getAllBudgetCategories();
    }
}
