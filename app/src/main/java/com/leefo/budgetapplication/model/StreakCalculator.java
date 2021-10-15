package com.leefo.budgetapplication.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class used for calculating spending streak from a list of transactions.
 *
 * A spending streak is a sequence of days where each day you spend less than your average daily spending before that day.
 *
 * All methods in this class assumes that the list of transactions given is sorted by date, with the most recent transactions in lower indexes.
 */
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

    /**
     * Gets longest achieved streak from list of transactions.
     *
     * @param transactions transaction list
     * @return Longest ever spending streak.
     */
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

                if(newAverage == average) // if it comes back here again despite new average, we are no longer streaking
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

        boolean found_date = false;

        // will loop through list until date is found, after which it will start adding transactionAmounts to sum and then break
        for(FinancialTransaction transaction : transactions)
        {
            // if loop has looped past given date
            if(found_date && !transaction.getDate().isEqual(date)) break;

            if(transaction.getDate().isEqual(date)){
                sum += transaction.getAmount();
                found_date = true;
            }
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
        int days = 0;

        LocalDate previousDate = null;

        for(FinancialTransaction transaction : transactions)
        {
            // must be before specified date and an expense
            if(transaction.getDate().isBefore(date) && !transaction.getCategory().isIncome()) {

                sum += transaction.getAmount();

                // previousDate == null means first found transaction, which means days needs to be incremented
                // if this transaction has a different day than the previous one, then it's a new day (which means days should be incremented)
                if(previousDate == null || !transaction.getDate().isEqual(previousDate))
                {
                    previousDate = transaction.getDate();
                    days++;
                }
            }
        }

        if(days == 0) return 0;

        return sum / (float)days;
    }
}
