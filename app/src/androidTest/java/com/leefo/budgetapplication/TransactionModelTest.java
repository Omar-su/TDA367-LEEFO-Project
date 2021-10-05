package com.leefo.budgetapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leefo.budgetapplication.controller.TransactionRequest;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.TransactionModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionModelTest {

    DataBaseManager db;

    TransactionModel tm;

    FinancialTransaction testTransaction1;
    Category testCategory1;
    LocalDate testDate1;

    @Before
    public void init() {
        Context c = InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        db = new DataBaseManager(c);
        tm = new TransactionModel(db);
        String categoryTestName = "Unique test category name 1";
        String transactionTestDescription = "Unique test transaction description 1";
        testCategory1 = new Category(categoryTestName, "#FFFFFF", true);
        testDate1 = LocalDate.now();
        testTransaction1 = new FinancialTransaction((float) 16.9, transactionTestDescription, testDate1,
                testCategory1);
    }

    @Test
    public void canAddTransaction() {
        tm.addTransaction(testTransaction1);

        assertTrue(tm.getTransactionList().contains(testTransaction1));
    }

    @Test
    public void canRemoveTransaction() {
        tm.addTransaction(testTransaction1);
        tm.deleteTransaction(testTransaction1);
        assertFalse(tm.getTransactionList().contains(testTransaction1));
    }

    @Test
    public void canEditTransaction() {
        tm.addTransaction(testTransaction1);
        String editedDescription = "EditedTestTransaction";
        tm.editTransaction(testTransaction1, new FinancialTransaction((float) 16.9, editedDescription,
                testDate1, testCategory1));

        boolean outcome = false;
        for (FinancialTransaction t : tm.getTransactionList()) {
            if (t.getDescription().equals(editedDescription)) {
                outcome = true;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void canAddCategory() {
        tm.addCategory(testCategory1);
        assertTrue(tm.getCategoryList().contains(testCategory1));
    }

    @Test
    public void canRemoveCategory() {
        tm.addCategory(testCategory1);
        tm.deleteCategory(testCategory1);
        assertFalse(tm.getCategoryList().contains(testCategory1));
    }

    @Test
    public void removingCategoryChangesTransactionsCategoryToOther() {
        tm.addCategory(testCategory1);
        tm.addTransaction(testTransaction1);

        tm.deleteCategory(testCategory1);

        boolean outcome = false;
        for (FinancialTransaction t : tm.getTransactionList()) {
            if (t.getDescription().equals("Unique test transaction description 1") &&
                    t.getCategory().getName().equals("Other income")) {
                outcome = true;
                break;
            }
        }

        assertTrue(outcome);
    }

    @Test
    public void canEditCategory() {
        tm.addCategory(testCategory1);
        String editedColor = "#FFFFF5";
        String editedName = "EditedTestCategoryName";
        tm.editCategory(testCategory1, new Category(editedName, editedColor, true));

        boolean outcome = false;
        for (Category c : tm.getCategoryList()) {
            if (c.getName().equals(editedName) && c.getColor().equals(editedColor)) {
                outcome = true;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void canFindTransactionMadeInSpecificMonth() {
        LocalDate d1 = LocalDate.of(2020, 5, 3);
        LocalDate d2 = LocalDate.of(2020, 5, 14);
        LocalDate d3 = LocalDate.of(2019, 5, 14); //Same month and day as above but different year
        LocalDate d4 = LocalDate.of(2020, 12, 24);
        LocalDate d5 = LocalDate.of(2020, 5, 20);

        FinancialTransaction t1 = new FinancialTransaction((float) 200.5, "t1", d1, testCategory1);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.4, "t2", d2, testCategory1);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.5, "t3", d3, testCategory1);
        FinancialTransaction t4 = new FinancialTransaction((float) 21235.6, "t4", d4, testCategory1);
        FinancialTransaction t5 = new FinancialTransaction((float) 0.5, "t5", d5, testCategory1);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);
        tm.addTransaction(t5);

        TransactionRequest request = new TransactionRequest(null, 5, 2020);

        List<FinancialTransaction> searchedList = tm.searchTransactions(request);

        boolean outcome = true;

        for (FinancialTransaction t : searchedList) {
            //If list contains transaction where month and year arent correct
            if (!((t.getDate().getYear() == 2020) && (t.getDate().getMonthValue() == 5))) {
                outcome = false;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void canFindTransactionWithSpecificCategory() {
        LocalDate d1 = LocalDate.of(2020, 5, 3);
        LocalDate d2 = LocalDate.of(2020, 5, 14);
        LocalDate d3 = LocalDate.of(2019, 5, 14); //Same month and day as above but different year
        LocalDate d4 = LocalDate.of(2020, 12, 24);
        LocalDate d5 = LocalDate.of(2020, 5, 20);

        Category testCategory2 = new Category("Unique Test Category Name 2", "#F25A57", false);

        FinancialTransaction t1 = new FinancialTransaction((float) 200.5, "t1", d1, testCategory1);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.4, "t2", d2, testCategory2);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.5, "t3", d3, testCategory1);
        FinancialTransaction t4 = new FinancialTransaction((float) 21235.6, "t4", d4, testCategory1);
        FinancialTransaction t5 = new FinancialTransaction((float) 0.5, "t5", d5, testCategory1);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);
        tm.addTransaction(t5);

        TransactionRequest request = new TransactionRequest(testCategory2, 0, 0);

        List<FinancialTransaction> searchedList = tm.searchTransactions(request);

        boolean outcome = true;

        for (FinancialTransaction t : searchedList) {
            //If list contains transaction where month and year arent correct
            if (!testCategory2.transactionBelongs(t)) {
                outcome = false;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void canGetAllIncomeCategories() {
        Category c1 = new Category("c1", "#FFFFFF", true);
        Category c2 = new Category("c1", "#FFFFFF", false);
        Category c3 = new Category("c1", "#FFFFFF", true);
        Category c4 = new Category("c1", "#FFFFFF", true);
        Category c5 = new Category("c1", "#FFFFFF", false);

        tm.addCategory(c1);
        tm.addCategory(c2);
        tm.addCategory(c3);
        tm.addCategory(c4);
        tm.addCategory(c5);

        List<Category> returnedList = tm.getIncomeCategories();

        boolean outcome = true;
        for (Category c : returnedList) {
            if (!c.isIncome()) {
                outcome = false;
                break;
            }
        }
        assertTrue(outcome);
    }


}
