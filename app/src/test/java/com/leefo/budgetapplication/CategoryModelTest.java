package com.leefo.budgetapplication;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryModel;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.IDatabase;
import com.leefo.budgetapplication.model.ITransactionModel;
import com.leefo.budgetapplication.model.TransactionModel;
import com.leefo.budgetapplication.model.TransactionRequest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CategoryModelTest {

    IDatabase fakeDb;
    CategoryModel cm;
    TransactionModel transactionModel;
    FinancialTransaction testTransaction;
    Category testCategory1;

    @BeforeEach
    public void init() {
        fakeDb = new FakeIDatabase();
        transactionModel = new TransactionModel(fakeDb);
        cm = new CategoryModel(fakeDb, transactionModel);
        testCategory1 = new Category("Test", "#FFFAAA", false);
        testTransaction = new FinancialTransaction((float) 15.0, "Unique test transaction description 1", LocalDate.now(),
                testCategory1);
    }

    @Test
    public void canAddCategory() {
        cm.addCategory(testCategory1);
        assertTrue(cm.getCategoryList().contains(testCategory1));
    }

    @Test
    public void canRemoveCategory() {
        cm.addCategory(testCategory1);
        cm.deleteCategory(testCategory1);
        assertFalse(cm.getCategoryList().contains(testCategory1));
    }

    @Test
    public void removingCategoryChangesTransactionsCategoryToOther() {
        cm.addCategory(testCategory1);
        transactionModel.addTransaction(testTransaction);

        cm.deleteCategory(testCategory1);

        boolean outcome = false;
        for (FinancialTransaction t : transactionModel.getTransactionList()) {
            if (t.getDescription().equals("Unique test transaction description 1") &&
                    t.getCategory().getName().equals("Other expense")) {
                outcome = true;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void canEditCategory() {
        cm.addCategory(testCategory1);
        String editedColor = "#FFFFF5";
        String editedName = "EditedTestCategoryName";
        cm.editCategory(testCategory1, new Category(editedName, editedColor, true,0));

        boolean outcome = false;
        for (Category c : cm.getCategoryList()) {
            if (c.getName().equals(editedName) && c.getColor().equals(editedColor)) {
                outcome = true;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void canGetAllIncomeCategories() {
        Category c1 = new Category("c1", "#FFFFFF", true,0);
        Category c2 = new Category("c1", "#FFFFFF", false,0);
        Category c3 = new Category("c1", "#FFFFFF", true,0);
        Category c4 = new Category("c1", "#FFFFFF", true,0);
        Category c5 = new Category("c1", "#FFFFFF", false,0);

        cm.addCategory(c1);
        cm.addCategory(c2);
        cm.addCategory(c3);
        cm.addCategory(c4);
        cm.addCategory(c5);

        List<Category> returnedList = cm.getIncomeCategories();

        boolean outcome = true;
        //Check that list only contains income categories
        for (Category c : returnedList) {
            if (!c.isIncome()) {
                outcome = false;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void canGetAllExpenseCategories() {
        Category c1 = new Category("c1", "#FFFFFF", true,0);
        Category c2 = new Category("c1", "#FFFFFF", false,0);
        Category c3 = new Category("c1", "#FFFFFF", true,0);
        Category c4 = new  Category("c1", "#FFFFFF", true,0);
        Category c5 = new Category("c1", "#FFFFFF", false,0);

        cm.addCategory(c1);
        cm.addCategory(c2);
        cm.addCategory(c3);
        cm.addCategory(c4);
        cm.addCategory(c5);

        List<Category> returnedList = cm.getExpenseCategories();

        boolean outcome = true;
        //check that list only contains expense categories
        for (Category c : returnedList) {
            if (c.isIncome()) {
                outcome = false;
                break;
            }
        }
        assertTrue(outcome);
    }






}
