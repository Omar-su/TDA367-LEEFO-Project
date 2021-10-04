package com.leefo.budgetapplication.view;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<TimePeriod> timePeriod = new MutableLiveData<>();

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod.setValue(timePeriod);
    }
    public LiveData<TimePeriod> getTimePeriod() {
        return timePeriod;
    }
}
