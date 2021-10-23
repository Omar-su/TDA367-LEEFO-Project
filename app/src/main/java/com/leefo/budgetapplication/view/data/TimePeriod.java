package com.leefo.budgetapplication.view.data;

/**
 * class representing a time period of a specific month.
 * used directly by SharedTimePeriodViewModel.
 * used indirect through the view model by several fragment classes.
 * @author Emelie Edberg
 */
public class TimePeriod {

    /**
     * year number.
     * Value 0 represents no specified month, interpreted as every month.
     */
    private int year;
    /**
     * month number 1 - 12.
     * Value 0 represents no specified year, interpreted as every year.
     */
    private int month;

    public TimePeriod(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    /**
     * updates attributes to the next month
     */
    public void incrementMonth(){
        if (month == 12){
            incrementYear();
            month = 1;
        } else {
            month++;
        }
    }

    private void incrementYear(){
        year++;
    }

    /**
     * updates attributes to the previous month
     */
    public void decrementMonth(){
        if (month == 1){
            decrementYear();
            month = 12;
        } else {
            month--;
        }
    }

    private void decrementYear(){
        year--;
    }

    /**
     * sets attributes to 0, representing every month and every year
     */
    public void setNoSpecifiedTimePeriod(){
        year = month = 0;
    }

    public void setSpecifiedTimePeriod(int year, int month){
        this.year = year;
        this.month = month;
    }

    /**
     * checks if month is specified or 0
     * @return true if month is specified
     */
    public boolean isTimeSpecified(){
        return month != 0;
    }
}
