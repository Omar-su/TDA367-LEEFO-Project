package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.view.adapters.ManageCategoriesListAdapter;

/**
 * @author Linus Lundgren
 */
public class StreakFragment extends Fragment {

    private TextView currentStreak, longestStreak;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

        currentStreak = view.findViewById(R.id.currentStreak);
        longestStreak = view.findViewById(R.id.longestStreak);

        return view;
    }

    private void loadStreakValues()
    {

    }





}