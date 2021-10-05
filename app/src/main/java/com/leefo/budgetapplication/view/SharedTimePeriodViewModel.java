package com.leefo.budgetapplication.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

/**
 * ViewModel responsible for the applications currently chosen time period
 * uses TimePeriod
 * used by fragments who need the time period information
 *
 * fragments observes the attribute timePeriodLiveData and gets notified when its value changes
 *
 * @author Emelie Edberg
 */
public class SharedTimePeriodViewModel extends ViewModel {

    /**
     * TimePeriod object wrapped in a MutableLiveData.
     * Holds the current TimePeriod of th application.
     * start value is the month and year of current day.
     */
    private final MutableLiveData<TimePeriod> timePeriodLiveData = new MutableLiveData<>(new TimePeriod(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));

    /**
     * Sets the value of timePeriodLiveData. This sends notification to observers
     * @param timePeriod
     */
    private void setTimePeriodLiveData(TimePeriod timePeriod) {
        timePeriodLiveData.setValue(timePeriod);
    }

    public LiveData<TimePeriod> getTimePeriodLiveData() {
        return timePeriodLiveData;
    }

    // ----- perform TimePeriod methods and update the value of MutableLiveData object (so observers gets notified)
    public void incrementMonth(){
        TimePeriod timePeriod = timePeriodLiveData.getValue();
        timePeriod.incrementMonth();
        setTimePeriodLiveData(timePeriod);
    }

    public void decrementMonth(){
        TimePeriod timePeriod = timePeriodLiveData.getValue();
        timePeriod.decrementMonth();
        setTimePeriodLiveData(timePeriod);
    }

    public void setNoSpecifiedTimePeriod(){
        TimePeriod timePeriod = timePeriodLiveData.getValue();
        timePeriod.setNoSpecifiedTimePeriod();
        setTimePeriodLiveData(timePeriod);
    }

    public void setSpecifiedTimePeriod(int year, int month){
        TimePeriod timePeriod = timePeriodLiveData.getValue();
        timePeriod.setSpecifiedTimePeriod(year, month);
        setTimePeriodLiveData(timePeriod);
    }


}
