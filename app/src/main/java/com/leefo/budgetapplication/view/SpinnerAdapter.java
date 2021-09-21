package com.leefo.budgetapplication.view;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.leefo.budgetapplication.model.CategoryFake;

public class SpinnerAdapter extends ArrayAdapter<CategoryFake> {
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull CategoryFake[] objects) {
        super(context, resource, objects);
    }
}
