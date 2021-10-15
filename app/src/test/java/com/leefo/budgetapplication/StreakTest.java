package com.leefo.budgetapplication;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.StreakCalculator;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;


public class StreakTest {

    static ArrayList<FinancialTransaction> transactions;

    @BeforeAll
    static void init()
    {
        transactions = new ArrayList<>();

        LocalDate today = LocalDate.now();

        Category expense = new Category("expense", "#000000", false);

        float[] amounts = new float[] {
                1, 2, 3, 4, 10, 3, 4, 5, 6, 7, 8, 9, 10
        };

        int[] dayMade = new int[] {
                0, 1, 2, 2, 3, 4, 5, 5, 6, 7, 8, 9, 9
        };

        for(int i = 0; i < amounts.length; i++)
        {
            transactions.add(new FinancialTransaction(amounts[i], "", today.minusDays(dayMade[i]), expense));
        }
    }

    @Test
    public void currentStreak()
    {
        Assertions.assertEquals(3, StreakCalculator.getCurrentStreak(transactions));
    }

    @Test
    public void recordStreak()
    {
        Assertions.assertEquals(4, StreakCalculator.getRecordStreak(transactions));
    }
}
