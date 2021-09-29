package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.leefo.budgetapplication.R;

/**
 * Class that represents the fragment for the More page
 */
public class MoreFragment extends Fragment {

    private Button btnCompareMonths;
    private Button btnEditCategory;
    private Button btnNewCategory;
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
        btnEditCategory = view.findViewById(R.id.edit_category_button);
        btnNewCategory = view.findViewById(R.id.new_category_button);
        btnManageCategory = view.findViewById(R.id.manage_categories_button);
        init_btnCompareMonths_OnClickListener();
        init_btnNewCategory_OnClickListener();
        init_btnEditCategory_OnClickListener();
        init_btnManageCategory_OnClickListener();
        return view;
    }

    private void init_btnManageCategory_OnClickListener(){
        btnManageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllButtons();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout_more, ManageCategoriesFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    private void init_btnEditCategory_OnClickListener(){
        btnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllButtons();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout_more, EditCategoryFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }


        });
    }

    private void hideAllButtons() {
        btnCompareMonths.setVisibility(View.GONE);
        btnEditCategory.setVisibility(View.GONE);
        btnNewCategory.setVisibility(View.GONE);
        btnManageCategory.setVisibility(View.GONE);
    }

    private void init_btnNewCategory_OnClickListener() {
        btnNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllButtons();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout_more, NewCategoryFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    private void init_btnCompareMonths_OnClickListener() {
        btnCompareMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllButtons();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout_more, CompareMonthsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


}