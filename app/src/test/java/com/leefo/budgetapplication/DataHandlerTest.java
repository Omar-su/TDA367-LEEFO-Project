package com.leefo.budgetapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.constraintlayout.helper.widget.MotionEffect;

import java.util.*;
import java.util.concurrent.ExecutionException;

//Break out some tests into separate classes when model classes are clear
public class DataHandlerTest {

    @Test
    public void addedTransactionExistsInDatabase() {
        Transaction testTransaction = new Transaction(1, 14.5, "Food from mcDonalds", 20210203, 3);
        DataHandler dh = new DataHandler();
        dh.addTransaction(testTransaction);
        List<Transaction> transactionList = dh.getTransactions();
        assertTrue(transactionList.contains(testTransaction));
    }

    @Test
    public void removedTransactionDoesntExistDatabase() {
        Transaction testTransaction = new Transaction(1, 14.5, "Food from mcDonalds", 20210203, 3);
        DataHandler dh = new DataHandler();
        dh.addTransaction(testTransaction);
        List<Transaction> transactionList = dh.getTransactions();
        if (transactionList.contains(testTransaction)) {
            dh.removeTransaction(testTransaction);
        } else {
            fail("testTransaction wasn't added correctly.");
        }
        assertFalse(transactionList.contains(testTransaction));
    }


}
