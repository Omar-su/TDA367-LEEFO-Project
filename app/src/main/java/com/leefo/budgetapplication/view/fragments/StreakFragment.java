package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;

/**
 * Fragment used for displaying spending streaks.
 *
 * @author Linus Lundgren
 */
public class StreakFragment extends Fragment {

    private TextView currentStreak, recordStreak, currentAverage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_streak, container, false);

        currentStreak = view.findViewById(R.id.currentStreak);
        recordStreak = view.findViewById(R.id.recordStreak);
        currentAverage = view.findViewById(R.id.currentAverage);

        loadStreakValues();

        return view;
    }

    private void loadStreakValues()
    {
        currentStreak.setText(String.valueOf(Controller.getCurrentStreak()));
        recordStreak.setText(String.valueOf(Controller.getRecordStreak()));

        currentAverage.setText("Current average: " + Controller.getAverageSpending());
    }

}