package com.leefo.budgetapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryHandler;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.model.TransactionHandler;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Before
    public void init() {
        Context c = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DataBaseManager.initialize(c);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.leefo.budgetapplication", appContext.getPackageName());
    }

    @Test
    public void canAddTransaction() {
        TransactionHandler th = new TransactionHandler();
            th.addTransaction(14, "sf", "2021/05/03", 1);
            List<Transaction> transactionList = th.getAllTransactions();
            assertEquals(1, transactionList.size());
    }
}