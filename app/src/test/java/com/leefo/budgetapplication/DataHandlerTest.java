package com.leefo.budgetapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import androidx.constraintlayout.helper.widget.MotionEffect;

import java.util.*;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

//Break out some tests into separate classes when model classes are clear
public class DataHandlerTest {
/*
    @BeforeEach
    public void init(){
        Transaction testTransaction = new Transaction(1, 14.5, "Food from mcDonalds", 20210203, 3, true);
        DataHandler dh = new DataHandler();
    }

    @Test
    public void addedTransactionExistsInDatabase() {
        dh.addTransaction(testTransaction);
        List<Transaction> transactionList = dh.getTransactions();
        assertTrue(transactionList.contains(testTransaction));
    }

    @Test
    public void removedTransactionDoesntExistDatabase() {
        dh.addTransaction(testTransaction);
        List<Transaction> transactionList = dh.getTransactions();
        if (transactionList.contains(testTransaction)) {
            dh.removeTransaction(testTransaction);
        } else {
            fail("testTransaction wasn't added correctly.");
        }
        assertFalse(transactionList.contains(testTransaction));
    }

    @Test
    public void addedCategoryExistsInDatabase(){
        Category testCategory = new Category(1, "Test Name", "#FFFF00");
        dh.addCategory(testCategory);
        List<Category> categoryList = dh.getCategories();
        assertTrue(categoryList.contains(testCategory));
    }

    @Test
    public void removingCategoryMovesTransactionsToOther(){
        Category categoryToBeRemoved = new Category(1, "RemoveMe", "#FFFFFF");
        dh.removeCategory(categoryToBeRemoved);
        assertEquals(0, testTransaction.getCategoryId()); // Assuming that category ID for Other is 0
    }
*/

}
