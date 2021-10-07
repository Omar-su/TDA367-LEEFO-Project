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
        Context c = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = new DataBaseManager(c, null); //null name creates in memory database
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
        LocalDate d1 = LocalDate.of(1900, 5, 3);
        LocalDate d2 = LocalDate.of(1900, 5, 14);
        LocalDate d3 = LocalDate.of(1901, 5, 14); //Same month and day as above but different year
        LocalDate d4 = LocalDate.of(1900, 12, 24);
        LocalDate d5 = LocalDate.of(1900, 5, 20);

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

        TransactionRequest request = new TransactionRequest(null, 5, 1900);

        List<FinancialTransaction> searchedList = tm.searchTransactions(request);

        boolean outcome = true;

        for (FinancialTransaction t : searchedList) {
            //If list contains transaction where month and year arent correct
            if (!((t.getDate().getYear() == 1900) && (t.getDate().getMonthValue() == 5))) {
                outcome = false;
                break;
            }
        }
        int expectedSize = 3;
        assertTrue(outcome && searchedList.size() == expectedSize);
    }

    @Test
    public void canFindTransactionWithSpecificCategory() {
        LocalDate d1 = LocalDate.of(1900, 6, 3);
        LocalDate d2 = LocalDate.of(1900, 6, 14);
        LocalDate d3 = LocalDate.of(1901, 5, 14);
        LocalDate d4 = LocalDate.of(1900, 12, 24);
        LocalDate d5 = LocalDate.of(1900, 6, 20);

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
        int expectedSize = 1;
        assertTrue(outcome && searchedList.size() == expectedSize);
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

        List<Category> returnedList = tm.getExpenseCategories();

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

    @Test
    public void canGetSumForSpecificRequest() {
        LocalDate d1 = LocalDate.of(1905, 1, 2);
        LocalDate d2 = LocalDate.of(1902, 3, 16);
        LocalDate d3 = LocalDate.of(1905, 1, 27); //Same month and day as above but different year

        FinancialTransaction t1 = new FinancialTransaction((float) 200.0, "t1", d1, testCategory1);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.0, "t2", d2, testCategory1);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.0, "t3", d3, testCategory1);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);

        float expectedSum = t1.getAmount() + t3.getAmount();

        TransactionRequest request = new TransactionRequest(null, 1, 1905);
        float calculatedSum = tm.getTransactionSum(request);

        assertEquals(expectedSum, calculatedSum);
    }

    @Test
    public void canGetTotalIncomeForSpecificRequest() {
        LocalDate d1 = LocalDate.of(1905, 1, 2);
        LocalDate d2 = LocalDate.of(1902, 3, 16);
        LocalDate d3 = LocalDate.of(1905, 1, 27);
        LocalDate d4 = LocalDate.of(1905, 1, 15);

        Category incomeCat1 = new Category("IncomeTest1", "#111111", true);
        Category incomeCat2 = new Category("IncomeTest2", "#555555", true);
        tm.addCategory(incomeCat1);
        tm.addCategory(incomeCat2);

        FinancialTransaction t1 = new FinancialTransaction((float) 200.0, "t1", d1, incomeCat1);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.0, "t2", d2, incomeCat1);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.0, "t3", d3, incomeCat1);
        FinancialTransaction t4 = new FinancialTransaction((float) 116.0, "t4", d4, incomeCat2);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);

        float expectedSum = t1.getAmount() + t3.getAmount() + t4.getAmount(); //amount for transactions created 01/1905
        TransactionRequest request = new TransactionRequest(null, 1, 1905);

        float calculatedSum = tm.getTotalIncome(request);

        assertEquals(expectedSum, calculatedSum);
    }

    @Test
    public void canGetTotalExpenseForSpecificRequest() {
        LocalDate d1 = LocalDate.of(1905, 1, 2);
        LocalDate d2 = LocalDate.of(1902, 3, 16);
        LocalDate d3 = LocalDate.of(1905, 1, 27);
        LocalDate d4 = LocalDate.of(1905, 1, 15);

        Category expenseCat1 = new Category("expenseTest1", "#111111", false);
        Category expenseCat2 = new Category("expenseTest2", "#555555", false);
        tm.addCategory(expenseCat1);
        tm.addCategory(expenseCat2);

        FinancialTransaction t1 = new FinancialTransaction((float) 200.0, "t1", d1, expenseCat1);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.0, "t2", d2, expenseCat1);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.0, "t3", d3, expenseCat1);
        FinancialTransaction t4 = new FinancialTransaction((float) 116.0, "t4", d4, expenseCat2);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);

        float expectedSum = t1.getAmount() + t3.getAmount() + t4.getAmount(); //Amount for transactions created 01/1905
        TransactionRequest request = new TransactionRequest(null, 1, 1905);

        float calculatedSum = tm.getTotalExpense(request);

        assertEquals(expectedSum, calculatedSum);
    }

    @Test
    public void canCalculateBalanceForSpecificRequest() {
        LocalDate d1 = LocalDate.of(1899, 8, 10);
        LocalDate d2 = LocalDate.of(1899, 8, 16);
        LocalDate d3 = LocalDate.of(1965, 8, 15);
        LocalDate d4 = LocalDate.of(1965, 8, 15);

        Category incomeCat1 = new Category("incomeTest1", "#111111", true);
        Category expenseCat1 = new Category("expenseTest1", "#555555", false);
        tm.addCategory(incomeCat1);
        tm.addCategory(expenseCat1);

        FinancialTransaction t1 = new FinancialTransaction((float) 255.0, "t1", d1, incomeCat1);
        FinancialTransaction t2 = new FinancialTransaction((float) 180.4, "t2", d2, expenseCat1);
        FinancialTransaction t3 = new FinancialTransaction((float) 500.2, "t3", d3, incomeCat1);
        FinancialTransaction t4 = new FinancialTransaction((float) 60.2, "t4", d4, expenseCat1);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);

        float expectedBalance = t1.getAmount() - t2.getAmount(); //Balance for transactions made 08/1899
        TransactionRequest request = new TransactionRequest(null, 8, 1899);

        float calculatedBalance = tm.getTransactionBalance(request);

        assertEquals(expectedBalance, calculatedBalance);
    }

    @Test
    public void canGetNonEmptyCategoriesForSpecificRequest() {
        LocalDate d1 = LocalDate.of(1899, 8, 10);
        LocalDate d2 = LocalDate.of(1899, 8, 16);
        LocalDate d3 = LocalDate.of(1965, 8, 15);
        LocalDate d4 = LocalDate.of(1965, 8, 15);

        Category incomeCat1 = new Category("incomeTest1", "#111111", true);
        Category expenseCat1 = new Category("expenseTest1", "#555555", false);
        Category emptyCategory = new Category("ImEmpty", "#FFFFFF", true);
        tm.addCategory(incomeCat1);
        tm.addCategory(expenseCat1);
        tm.addCategory(emptyCategory);

        FinancialTransaction t1 = new FinancialTransaction((float) 255.0, "t1", d1, incomeCat1);
        FinancialTransaction t2 = new FinancialTransaction((float) 180.4, "t2", d2, expenseCat1);
        FinancialTransaction t3 = new FinancialTransaction((float) 500.2, "t3", d3, incomeCat1);
        FinancialTransaction t4 = new FinancialTransaction((float) 60.2, "t4", d4, expenseCat1);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);

        TransactionRequest request = new TransactionRequest(null, 1899, 8);
        List<Category> nonEmptyList = tm.removeEmptyCategories(tm.getCategoryList(), request);

        boolean outcome = true;
        for (Category c : nonEmptyList) {
            request.setCategory(c);
            if (tm.searchTransactions(request).isEmpty()) { //If category is empty for the specific months test -> fail
                outcome = false;
                break;
            }
        }
        assertTrue(outcome);
    }

    @Test
    public void categoriesAreSortedWithLargestFirst() {
        LocalDate d1 = LocalDate.of(1897, 3, 10);
        LocalDate d2 = LocalDate.of(1897, 3, 16);
        LocalDate d3 = LocalDate.of(1897, 3, 15);
        LocalDate d4 = LocalDate.of(1897, 3, 15);

        Category testCat1 = new Category("testcat1", "#111111", false);
        Category testCat2 = new Category("testcat2", "#555555", false);
        Category testCat3 = new Category("testcat3", "#555555", false);
        Category testCat4 = new Category("testcat4", "#555555", false);

        tm.addCategory(testCat1);
        tm.addCategory(testCat2);
        tm.addCategory(testCat3);
        tm.addCategory(testCat4);

        FinancialTransaction t1 = new FinancialTransaction((float) 255.0, "t1", d1, testCat1);
        FinancialTransaction t2 = new FinancialTransaction((float) 180.4, "t2", d2, testCat2);
        FinancialTransaction t3 = new FinancialTransaction((float) 500.2, "t3", d3, testCat3);
        FinancialTransaction t4 = new FinancialTransaction((float) 60.2, "t4", d4, testCat4);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);

        TransactionRequest request = new TransactionRequest(null, 3, 1897);
        List<Category> sortedList = tm.sortCategoryListBySum(tm.getCategoryList(), request);

        boolean outcome = true;
        //Check if list is sorted with largest first
        for (int i = 0; i < sortedList.size() - 1; i++) {
            float sum1 = tm.getTransactionSum(new TransactionRequest(sortedList.get(i), request.getMonth(), request.getYear()));
            float sum2 = tm.getTransactionSum(new TransactionRequest(sortedList.get(i + 1), request.getMonth(), request.getYear()));
            if (sum1 < sum2) {
                outcome = false;
            }
        }
        assertTrue(outcome);
    }

}
