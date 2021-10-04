package com.leefo.budgetapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.TransactionModel;

import java.time.LocalDate;
import java.util.List;

import androidx.test.core.app.ApplicationProvider.*;
import androidx.test.core.app.ApplicationProvider;

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
        testCategory1 = new Category("Test", "#FFFFFF", true);
        testDate1 = LocalDate.now();
        testTransaction1 = new FinancialTransaction((float) 16.9, "test", testDate1,
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
        for (FinancialTransaction t : tm.getTransactionList()) {
            if (t.getDescription().equals(editedDescription)) {
                assertTrue(true);
            }
        }
        fail();
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

        assertEquals(testTransaction1.getCategory().getName(), "Other income");
    }

    @Test
    public void canEditCategory() {
        tm.addCategory(testCategory1);
        String editedColor = "#FFFFF5";
        String editedName = "EditedTestCategoryName";
        tm.editCategory(testCategory1, new Category(editedName, editedColor, true));

        for (Category c : tm.getCategoryList()) {
            if (c.getName().equals(editedName) && c.getColor().equals(editedColor)) {
                assertTrue(true);
            }
        }
        fail();
    }

}
