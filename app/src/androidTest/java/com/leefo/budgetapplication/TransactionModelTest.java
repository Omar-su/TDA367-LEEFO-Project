package com.leefo.budgetapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leefo.budgetapplication.model.TransactionRequest;
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
        testCategory1 = new Category(categoryTestName, "#FFFFFF", true, 0);
        testDate1 = LocalDate.now();
        testTransaction1 = new FinancialTransaction((float) 16.9, transactionTestDescription, testDate1,
                testCategory1);
    }






    //Currently crashes because of the database not working as intended when running tests.









}
