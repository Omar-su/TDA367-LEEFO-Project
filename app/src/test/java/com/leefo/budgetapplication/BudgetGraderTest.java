package com.leefo.budgetapplication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.lang.Double.compare;

import android.provider.ContactsContract;

import androidx.test.platform.app.InstrumentationRegistry;

import com.leefo.budgetapplication.model.BudgetGrader;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.IDatabase;
import com.leefo.budgetapplication.model.TransactionModel;
import com.leefo.budgetapplication.model.TransactionRequest;

import java.time.LocalDate;

public class BudgetGraderTest {

/*
    @Test
    public void canGetCorrectGrade1(){
        TransactionModel tm = new TransactionModel();
        BudgetGrader budgetGrader = new BudgetGrader(tm);

        Category testCat = new Category("Test", "#FFFFFF", false, 200);
        FinancialTransaction testTransaction = new FinancialTransaction(150, "Test",
                LocalDate.of(2021, 10, 12), testCat);

        //Outcome is 0.75 should be grade 4.5
        float grade = budgetGrader.gradeCategory(new TransactionRequest(testCat, 10,2021));
        assertEquals(compare(grade, 4.5), 0);
    }*/
}
