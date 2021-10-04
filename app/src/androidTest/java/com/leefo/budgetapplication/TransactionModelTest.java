package com.leefo.budgetapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.TransactionModel;

import java.time.LocalDate;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TransactionModelTest {

    DataBaseManager db;

    TransactionModel tm;

    FinancialTransaction testTransaction1;
    Category testCategory1;
    LocalDate testDate1;

    @Before
    public void init(){
        Context c = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = new DataBaseManager(c);
        tm = new TransactionModel(db);
        testCategory1 = new Category("Test", "#FFFFFF", true);
        testDate1 = LocalDate.now();
        testTransaction1 = new FinancialTransaction((float)16.9, "test", testDate1,
                testCategory1);
    }

    @Test
    public void canAddTransaction() {
        tm.addTransaction(testTransaction1);

        assertTrue(tm.getTransactionList().contains(testTransaction1));
    }

    @Test
    public void canRemoveTransaction() {

    }
}
