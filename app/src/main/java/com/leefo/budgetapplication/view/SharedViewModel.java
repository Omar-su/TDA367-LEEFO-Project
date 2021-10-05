package com.leefo.budgetapplication.view;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<TimePeriod> timePeriod = new MutableLiveData<>();

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod.setValue(timePeriod);
    }
    public LiveData<TimePeriod> getTimePeriod() {
        return timePeriod;
    }


}
