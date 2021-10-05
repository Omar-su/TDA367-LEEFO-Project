package com.leefo.budgetapplication.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

public class SharedTimePeriodViewModel extends ViewModel {

    private final MutableLiveData<TimePeriod> timePeriodLiveData = new MutableLiveData<>(new TimePeriod(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));

    private void setTimePeriodLiveData(TimePeriod timePeriod) {
        timePeriodLiveData.setValue(timePeriod);
    }
    public LiveData<TimePeriod> getTimePeriodLiveData() {
        return timePeriodLiveData;
    }

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
