package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;

/**
 * This class represents the fragment that contains buttons for opening other fragments.
 * Opens ManageCategoriesFragment.
 * Opens CompareMonthsFragment.
 * Opened from MainActivity.
 * @author Emelie Edberg, Eugene Dvoryankov
 */
public class MoreFragment extends Fragment {

    private Button btnCompareMonths;
    private Button btnManageCategory;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        btnCompareMonths = view.findViewById(R.id.compare_month_button);
        btnManageCategory = view.findViewById(R.id.manage_categories_button);

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavigation);
        MenuItem item = bottomNav.getMenu().findItem(R.id.nav_more);
        item.setChecked(true);

        init_btnCompareMonths_OnClickListener();
        init_btnManageCategory_OnClickListener();
        return view;
    }

    private void init_btnManageCategory_OnClickListener(){
        btnManageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new ManageCategoriesFragment()).commit();
            }
        });
    }

    private void init_btnCompareMonths_OnClickListener() {
        btnCompareMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new CompareMonthsFragment()).commit();
            }
        });
    }


}