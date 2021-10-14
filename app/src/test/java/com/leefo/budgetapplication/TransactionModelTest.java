package com.leefo.budgetapplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryModel;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.IDatabase;
import com.leefo.budgetapplication.model.TransactionModel;
import com.leefo.budgetapplication.model.TransactionRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionModelTest {

    IDatabase fakeDb;
    TransactionModel tm;
    Category testCategory;

    @BeforeEach
    public void init() {
        fakeDb = new FakeIDatabase();
        tm = new TransactionModel(fakeDb);
        testCategory = new Category("test", "#FFFFFF", false);
    }

    @Test
    public void canAddTransaction() {
        FinancialTransaction testTransaction = new FinancialTransaction((float) 12.0, "test",
                LocalDate.now(), testCategory);
        tm.addTransaction(testTransaction);

        assertTrue(tm.getTransactionList().contains(testTransaction));
    }

    @Test
    public void canRemoveTransaction() {
        FinancialTransaction testTransaction = new FinancialTransaction((float) 12.0, "test",
                LocalDate.now(), testCategory);
        tm.addTransaction(testTransaction);
        tm.deleteTransaction(testTransaction);

        assertFalse(tm.getTransactionList().contains(testTransaction));
    }

    @Test
    public void canEditTransaction() {
        FinancialTransaction testTransaction = new FinancialTransaction((float) 12.0, "test",
                LocalDate.now(), testCategory);
        tm.addTransaction(testTransaction);
        String editedDescription = "EditedTestTransaction";
        tm.editTransaction(testTransaction, new FinancialTransaction((float) 12.0, editedDescription,
                LocalDate.now(), testCategory));

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
    public void canFindTransactionsMadeInSpecificMonth() {
        LocalDate d1 = LocalDate.of(1900, 5, 3);
        LocalDate d2 = LocalDate.of(1900, 5, 14);
        LocalDate d3 = LocalDate.of(1901, 5, 14); //Same month and day as above but different year
        LocalDate d4 = LocalDate.of(1900, 12, 24);
        LocalDate d5 = LocalDate.of(1900, 5, 20);

        FinancialTransaction t1 = new FinancialTransaction((float) 200.5, "t1", d1, testCategory);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.4, "t2", d2, testCategory);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.5, "t3", d3, testCategory);
        FinancialTransaction t4 = new FinancialTransaction((float) 21235.6, "t4", d4, testCategory);
        FinancialTransaction t5 = new FinancialTransaction((float) 0.5, "t5", d5, testCategory);

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

        Category testCategory2 = new Category("Unique Test Category Name 2", "#F25A57", false, 0);

        FinancialTransaction t1 = new FinancialTransaction((float) 200.5, "t1", d1, testCategory);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.4, "t2", d2, testCategory2);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.5, "t3", d3, testCategory);
        FinancialTransaction t4 = new FinancialTransaction((float) 21235.6, "t4", d4, testCategory);
        FinancialTransaction t5 = new FinancialTransaction((float) 0.5, "t5", d5, testCategory);

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
    public void canGetSumForSpecificRequest() {
        LocalDate d1 = LocalDate.of(1905, 1, 2);
        LocalDate d2 = LocalDate.of(1902, 3, 16);
        LocalDate d3 = LocalDate.of(1905, 1, 27); //Same month and day as above but different year

        FinancialTransaction t1 = new FinancialTransaction((float) 200.0, "t1", d1, testCategory);
        FinancialTransaction t2 = new FinancialTransaction((float) 15.0, "t2", d2, testCategory);
        FinancialTransaction t3 = new FinancialTransaction((float) 17.0, "t3", d3, testCategory);

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

        Category incomeCat1 = new Category("IncomeTest1", "#111111", true, 0);
        Category incomeCat2 = new Category("IncomeTest2", "#555555", true, 0);

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

        Category expenseCat1 = new Category("expenseTest1", "#111111", false, 0);
        Category expenseCat2 = new Category("expenseTest2", "#555555", false, 0);

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

        Category incomeCat1 = new Category("incomeTest1", "#111111", true, 0);
        Category expenseCat1 = new Category("expenseTest1", "#555555", false, 0);

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

        Category incomeCat1 = new Category("incomeTest1", "#111111", true, 0);
        Category expenseCat1 = new Category("expenseTest1", "#555555", false, 0);
        Category emptyCategory = new Category("ImEmpty", "#FFFFFF", true, 0);

        CategoryModel cm = new CategoryModel(fakeDb, tm);
        cm.addCategory(incomeCat1);
        cm.addCategory(expenseCat1);
        cm.addCategory(emptyCategory);

        FinancialTransaction t1 = new FinancialTransaction((float) 255.0, "t1", d1, incomeCat1);
        FinancialTransaction t2 = new FinancialTransaction((float) 180.4, "t2", d2, expenseCat1);
        FinancialTransaction t3 = new FinancialTransaction((float) 500.2, "t3", d3, incomeCat1);
        FinancialTransaction t4 = new FinancialTransaction((float) 60.2, "t4", d4, expenseCat1);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);

        TransactionRequest request = new TransactionRequest(null, 1899, 8);
        ArrayList<Category> nonEmptyList = cm.getCategoryList();
        tm.removeEmptyCategories(nonEmptyList, request);

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

        Category testCat1 = new Category("testcat1", "#111111", false, 0);
        Category testCat2 = new Category("testcat2", "#555555", false, 0);
        Category testCat3 = new Category("testcat3", "#555555", false, 0);
        Category testCat4 = new Category("testcat4", "#555555", false, 0);

        CategoryModel cm = new CategoryModel(fakeDb, tm);
        cm.addCategory(testCat1);
        cm.addCategory(testCat2);
        cm.addCategory(testCat3);
        cm.addCategory(testCat4);

        FinancialTransaction t1 = new FinancialTransaction((float) 255.0, "t1", d1, testCat1);
        FinancialTransaction t2 = new FinancialTransaction((float) 180.4, "t2", d2, testCat2);
        FinancialTransaction t3 = new FinancialTransaction((float) 500.2, "t3", d3, testCat3);
        FinancialTransaction t4 = new FinancialTransaction((float) 60.2, "t4", d4, testCat4);

        tm.addTransaction(t1);
        tm.addTransaction(t2);
        tm.addTransaction(t3);
        tm.addTransaction(t4);

        TransactionRequest request = new TransactionRequest(null, 3, 1897);
        ArrayList<Category> sortedList = cm.getCategoryList();
        tm.sortCategoryListBySum(sortedList, request);

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
