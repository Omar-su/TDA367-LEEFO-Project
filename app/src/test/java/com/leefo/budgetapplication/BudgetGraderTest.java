package com.leefo.budgetapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.lang.Double.compare;

import com.leefo.budgetapplication.model.BudgetGrader;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryModel;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.IDatabase;
import com.leefo.budgetapplication.model.TransactionModel;
import com.leefo.budgetapplication.model.TransactionRequest;

import java.time.LocalDate;
import java.util.ArrayList;

public class BudgetGraderTest {

    TransactionModel tm;
    CategoryModel cm;
    BudgetGrader budgetGrader;
    IDatabase fakeDb;

    @BeforeEach
    public void init() {
        fakeDb = new FakeIDatabase();
        tm = new TransactionModel(fakeDb);
        cm = new CategoryModel(fakeDb, tm);
        budgetGrader = new BudgetGrader(tm, cm);
    }

    @Test
    public void canGetCorrectGrade1() {
        Category testCat = new Category("Test", "#FFFFFF", false, 200);
        FinancialTransaction testTransaction = new FinancialTransaction(150, "Test",
                LocalDate.of(2021, 10, 12), testCat);
        tm.addTransaction(testTransaction);
        //Outcome is 0.75 should be grade 4.5
        float grade = budgetGrader.gradeCategory(new TransactionRequest(testCat, 10, 2021));
        assertEquals(compare(grade, 4.5), 0);
    }

    @Test
    public void canGetCorrectGrade2() {
        Category testCat = new Category("Test", "#FFFFFF", false, 100);
        FinancialTransaction testTransaction = new FinancialTransaction(200, "Test",
                LocalDate.of(2021, 10, 12), testCat);
        tm.addTransaction(testTransaction);
        //Outcome is 2 should be grade 0
        float grade = budgetGrader.gradeCategory(new TransactionRequest(testCat, 10, 2021));
        assertEquals(compare(grade, 0), 0);
    }

    @Test
    public void canGetBudgetCategories() {
        Category testCat1 = new Category("Test", "#FFFFFF", false, 100);
        Category testCat2 = new Category("Test", "#FFFFFE", false, 0);
        Category testCat3 = new Category("Test", "#FFFFFD", false, 20);
        cm.addCategory(testCat1);
        cm.addCategory(testCat2);
        cm.addCategory(testCat3);

        FinancialTransaction testTransaction1 = new FinancialTransaction(
                100, "Test1", LocalDate.of(2020, 5, 20), testCat1);
        FinancialTransaction testTransaction2 = new FinancialTransaction(
                200, "Test2", LocalDate.of(2020, 5, 20), testCat2);
        tm.addTransaction(testTransaction1);
        tm.addTransaction(testTransaction2);

        ArrayList<Category> testList = budgetGrader.getAllBudgetCategories();

        assertTrue(testList.contains(testCat1) && !testList.contains(testCat2) &&
                testList.contains(testCat3));
    }

    @Test
    public void canGetBudgetCategoriesWithTransactionsForSpecificMonth() {
        Category testCat1 = new Category("Test", "#FFFFFF", false, 100);
        Category testCat2 = new Category("Test", "#FFFFFE", false, 0);
        Category testCat3 = new Category("Test", "#FFFFFD", false, 20);
        cm.addCategory(testCat1);
        cm.addCategory(testCat2);
        cm.addCategory(testCat3);

        FinancialTransaction testTransaction1 = new FinancialTransaction(
                100, "Test1", LocalDate.of(2020, 5, 20), testCat1);
        FinancialTransaction testTransaction2 = new FinancialTransaction(
                200, "Test2", LocalDate.of(2020, 5, 20), testCat2);
        tm.addTransaction(testTransaction1);
        tm.addTransaction(testTransaction2);

        ArrayList<Category> testList = budgetGrader.getBudgetCategoriesByMonth(new TransactionRequest(
                null, 5, 2020));

        assertTrue(testList.contains(testCat1) && !testList.contains(testCat2) &&
                !testList.contains(testCat3));
    }

    @Test
    public void canGetRoundedBudgetOutcome() {
        int testBudget = 200;
        float testExpense = (float) 123.0;

        float expectedOutcome = (float) 0.62; // 123 / 200 = 0.615  => rounded is 0.62

        Category testCategory = new Category("Test", "#FFFFFF", false, testBudget);
        FinancialTransaction testTransaction = new FinancialTransaction(testExpense, "Test",
                LocalDate.now(), testCategory);
        tm.addTransaction(testTransaction);

        float actualTestOutcome = budgetGrader.getRoundedBudgetOutcome(
                new TransactionRequest(testCategory, 0, 0));

        assertEquals(expectedOutcome, actualTestOutcome);
    }

    @Test
    public void canGetAverageGrade() {
        Category testCat1 = new Category("Test", "#FFFFFF", false, 100);
        Category testCat2 = new Category("Test", "#FFFFFD", false, 20);
        cm.addCategory(testCat1);
        cm.addCategory(testCat2);

        FinancialTransaction testTransaction1 = new FinancialTransaction(80, "Test",
                LocalDate.of(2020, 5, 20), testCat1); // budget outcome = 80/100 = 0.8 = grade 4

        FinancialTransaction testTransaction2 = new FinancialTransaction(30, "Test",
                LocalDate.of(2020, 5, 20), testCat2); // budget outcome = 30 / 20 = 1.5 = grade 1

        tm.addTransaction(testTransaction1);
        tm.addTransaction(testTransaction2);

        // average should be 2.5
        assertEquals(budgetGrader.getAverageGradeForMonth(new TransactionRequest(null, 5, 2020)), 2.5);

    }
}
