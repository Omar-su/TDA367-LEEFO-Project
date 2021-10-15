package com.leefo.budgetapplication.model;

import android.util.Log;

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
    public static int getCurrentStreak(ArrayList<FinancialTransaction> transactions)
    {
        return getStreak(transactions, LocalDate.now());
    }

    /**
     * Gets longest achieved streak from list of transactions.
     *
     * @param transactions transaction list
     * @return Longest ever spending streak.
     */
    public static int getRecordStreak(ArrayList<FinancialTransaction> transactions)
    {
        int record = getCurrentStreak(transactions);

        // used for looping through streaks until we reach the first transaction made
        int current_streak = record;

        // date of first transaction ever made
        LocalDate first_transaction_date = transactions.get(transactions.size() - 1).getDate();



        // temporary date to keep track of where in the transaction list we are
        LocalDate temp_date = LocalDate.now().minusDays(current_streak + 1);

        // loops until loop has passed all transactions (reached a date before first ever transaction)
        while (first_transaction_date.isBefore(temp_date))
        {
            current_streak = getStreak(transactions, temp_date);

            if(current_streak > record) record = current_streak;

            temp_date = temp_date.minusDays(current_streak + 1);
        }

        return record;
    }


    /**
     * Returns the streak of a specific date. A streak is a sequence of dates where the spending of each day is less than the previous average spending.
     *
     * Assumes list of transactions is sorted.
     *
     * @param transactions List of transactions to base calculations off of.
     * @param date Date to check streak at.
     * @return Returns total amount of days included in streak.
     */
    private static int getStreak(ArrayList<FinancialTransaction> transactions, LocalDate date)
    {
        float average = getAverageDailySpending(transactions, date);

        LocalDate previousDate = date;

        boolean found_first_date = false;
        float day_sum = 0;
        int streak = 0;

        for(FinancialTransaction transaction : transactions)
        {
            LocalDate current_date = transaction.getDate(); // date of current transaction

            if(!found_first_date && !current_date.isEqual(date)) continue; // makes loop iterate until given date is found
            else found_first_date = true;


            // if we are still summing transactions of a date
            if(current_date.isEqual(previousDate))
            {
                if(!transaction.getCategory().isIncome()) // must be an expense
                    day_sum += Math.abs(transaction.getAmount());
            }

            // if we've reached a new day, check if the day_sum was below average
            else
            {
                // increase streak if day spending is below previous average
                if(day_sum < average)
                    streak++;
                else
                {
                    // if a streak lasts for several, days, then the further the streak continues, the lower the average will become
                    // And since the average will be higher earlier in the streak, then comparing the day_sum with the average of the
                    // most recent date in the streak will break the streak.
                    // this is why the average gets updated here to account for this problem

                    average = getAverageDailySpending(transactions, current_date);

                    if(day_sum < average) // and another check with the new average
                        streak++;
                    else
                        break; // if it doesn't pass the check with the new average either, then the streak is broken
                }

                previousDate = current_date;
                day_sum = 0;
            }

        }

        return streak;
    }

    /**
     * Gets average daily expenses before specified date.
     *
     * Assumes list of transactions is sorted by date.
     *
     * @param transactions List of transactions to calculate with.
     * @param date Date before which the transactions are made.
     * @return Average spending before date argument.
     */
    private static float getAverageDailySpending(ArrayList<FinancialTransaction> transactions, LocalDate date)
    {
        float sum = 0;
        int days = 0;

        LocalDate previousDate = null;

        for(FinancialTransaction transaction : transactions)
        {
            // must be before specified date and an expense
            if(transaction.getDate().isBefore(date) && !transaction.getCategory().isIncome()) {

                sum += Math.abs(transaction.getAmount());

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
