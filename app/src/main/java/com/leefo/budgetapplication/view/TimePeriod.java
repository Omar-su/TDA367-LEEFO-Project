package com.leefo.budgetapplication.view;

public class TimePeriod {

    private int year, month;

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
}
