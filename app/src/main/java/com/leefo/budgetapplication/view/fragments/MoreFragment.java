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

    private Button btnManageCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        btnManageCategory = view.findViewById(R.id.manage_categories_button);

        // Make sure the correct menu item is highlighted
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavigation);
        MenuItem item = bottomNav.getMenu().findItem(R.id.nav_more);
        item.setChecked(true);

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


}