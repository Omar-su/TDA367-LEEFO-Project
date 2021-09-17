package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;


public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Start with displaying category fragment
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_middleSection_Home, new HomeCategoryViewFragment()).commit();

        ToggleButton toggle = view.findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_middleSection_Home, new HomeListViewFragment()).commit();

                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_middleSection_Home, new HomeCategoryViewFragment()).commit();
                }
            }
        });

        return view;
    }

}