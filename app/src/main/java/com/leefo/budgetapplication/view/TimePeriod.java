package com.leefo.budgetapplication.view;

/**
 *
 * @author Emelie Edberg
 */
public class TimePeriod {

    private int year, month; // can be 0

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

    public void setNoSpecifiedTimePeriod(){
        year = month = 0;
    }

    public void setSpecifiedTimePeriod(int year, int month){
        this.year = year;
        this.month = month;
    }

    public boolean isTimeSpecified(){
        return month != 0;
    }
}
