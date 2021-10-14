package com.leefo.budgetapplication.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class StreakCalculator
{


    /**
     * Gets the streak of today.
     * @param transactions List of transactions to base calculations off of.
     * @return Spending streak of today.
     */
    public int getCurrentStreak(ArrayList<FinancialTransaction> transactions)
    {
        return getStreak(transactions, LocalDate.now());
    }

    public int getRecordStreak(ArrayList<FinancialTransaction> transactions)
    {
        int record = getCurrentStreak(transactions);



        return record;
    }


    /**
     * Returns the streak of a specific date. A streak is a sequence of dates where the spending of each day is less than the previous average spending.
     *
     * @param transactions List of transactions to base calculations off of.
     * @param date Date to check streak at.
     * @return Returns total amount of days included in streak.
     */
    private int getStreak(ArrayList<FinancialTransaction> transactions, LocalDate date)
    {
        float average = getAverageDailySpending(transactions, date);

        int streak = 0;
        boolean streaking = true;

        while(streaking)
        {
            float spending = getDaySpending(transactions, date);

            if(spending < average)
            {
                streak++;
                date = date.minusDays(1); // makes sure to check previous day in next iteration
            }
            else
            {
                // if a streak is ongoing, then the average spending should be going down after every day
                // this means that when we reach a date where spending > average we have to see if  it also applies to the average spending
                // before that date, since the average spending should be higher the further back in the streak we go

                float newAverage = getAverageDailySpending(transactions, date);

                if(newAverage == average) // if it comes back here again, we are no longer streaking
                    streaking = false;
                else
                    average = newAverage;
            }
        }

        return streak;
    }

    /**
     * Gets total spending on a specific date.
     *
     * @param transactions List of transactions to check spending in.
     * @param date Date to check spending on.
     * @return Returns the total spending on specified date.
     */
    private float getDaySpending(ArrayList<FinancialTransaction> transactions, LocalDate date)
    {
        float sum = 0;

        for(FinancialTransaction transaction : transactions)
        {
            if(transaction.getDate().isEqual(date))
                sum += transaction.getAmount();
        }

        return sum;
    }

    /**
     * Gets average daily expenses before specified date.
     *
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
