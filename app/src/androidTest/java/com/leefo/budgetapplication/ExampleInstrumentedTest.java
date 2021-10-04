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

import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.TransactionModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    DataBaseManager db;

    TransactionModel tm;

    FinancialTransaction testTransaction1;
    Category testCategory1;
    LocalDate testDate1;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.leefo.budgetapplication", appContext.getPackageName());
    }

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