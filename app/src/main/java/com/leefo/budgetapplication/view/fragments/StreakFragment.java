package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.TransactionController;

/**
 * Fragment used for displaying spending streaks.
 *
 * @author Linus Lundgren
 */
public class StreakFragment extends Fragment {

    private TextView currentStreak, recordStreak, average, today;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_streak, container, false);

        currentStreak = view.findViewById(R.id.currentStreak);
        recordStreak = view.findViewById(R.id.recordStreak);
        average = view.findViewById(R.id.average_textView);
        today = view.findViewById(R.id.today_textView);

        loadStreakValues();

        return view;
    }

    private void loadStreakValues()
    {
        currentStreak.setText(String.valueOf(TransactionController.getCurrentStreak()));
        recordStreak.setText(String.valueOf(TransactionController.getRecordStreak()));
        average.setText(String.valueOf(TransactionController.getAverageSpending()));
        today.setText(String.valueOf(TransactionController.getTodaysExpenses()));

        //progressTextView.setText(Controller.getTodaysExpenses() + " / " + Controller.getAverageSpending());
    }

}