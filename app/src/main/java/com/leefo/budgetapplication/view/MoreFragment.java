package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.leefo.budgetapplication.R;

/**
 * Class that represents the fragment for the More page
 */
public class MoreFragment extends Fragment {

    private Button btnCompareMonths;
    private Button btnEditCategory;
    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        btnCompareMonths = (Button) view.findViewById(R.id.compare_month_button);
        btnEditCategory = (Button) view.findViewById(R.id.edit_category_button);
        btnCompareMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCompareMonths.setVisibility(View.GONE);
                btnEditCategory.setVisibility(View.GONE);
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout_more, CompareMonthsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                /*
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.FrameLayout_more, new CompareMonthsFragment());
                fr.commit();
                 */
            }
        });

        return view;

    }


}