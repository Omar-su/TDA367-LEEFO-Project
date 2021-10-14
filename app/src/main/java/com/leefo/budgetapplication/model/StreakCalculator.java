package com.leefo.budgetapplication.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class StreakCalculator
{




    /**
     * Gets average expenses daily before specified date.
     * @param transactions List of transactions to calculate with.
     * @param date Date before which the transactions are made.
     * @return Average spending before date argument.
     */
    private float getAverageDailySpending(ArrayList<FinancialTransaction> transactions, LocalDate date)
    {
        float sum = 0;
        int transactionAmount = 0;

        for(FinancialTransaction transaction : transactions)
        {
            // must be before specified date and an expense
            if(transaction.getDate().isBefore(date) && !transaction.getCategory().isIncome()) {
                sum += transaction.getAmount();
                transactionAmount++;
            }
        }

        if(transactionAmount == 0) return 0;

        return sum / (float)transactionAmount;
    }
}
