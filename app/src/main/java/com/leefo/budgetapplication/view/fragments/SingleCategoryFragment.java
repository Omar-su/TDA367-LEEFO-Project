package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.data.ParcelableTransaction;
import com.leefo.budgetapplication.view.data.ParcelableCategory;
import com.leefo.budgetapplication.view.data.TimePeriodViewModel;
import com.leefo.budgetapplication.view.data.TimePeriod;
import com.leefo.budgetapplication.view.adapters.TransactionListAdapter;

import java.time.Month;
import java.util.ArrayList;
import java.util.MissingResourceException;

/**
 * This class represents the fragment opened when selecting a specific category in the list in HomeCategoryViewFragment.
 * It displays the transactions belonging to the chosen category and the current time period.
 * Used by (opened from) HomeCategoryViewFragment.
 * Uses SharedTimePeriodViewModel for getting the time period data. Uses ListViewAdapterHomeList.
 * Uses (opens) Home and EditTransaction fragments
 * @author Emelie Edberg
 */
public class SingleCategoryFragment extends Fragment {

    private ListView listView;
    private ArrayList<FinancialTransaction> transactionList;
    private TextView timePeriodTextView;
    private TimePeriod timePeriod;
    private ImageButton back_button;
    private Category chosenCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_category, container, false);

        // get data from view model
        TimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(TimePeriodViewModel.class);
        timePeriod = viewModel.getTimePeriodLiveData().getValue();

        // get argument sent with the fragment
        Bundle bundle = this.getArguments();
        if (bundle != null){
            ParcelableCategory chosen_category = bundle.getParcelable("CHOSEN_CATEGORY");
            chosenCategory = chosen_category.category;
        } else {
            throw new MissingResourceException("No chosen category was sent with the fragment, hence fragment cannot be created", ParcelableCategory.class.toString(), "CHOSEN_CATEGORY" );
        }

        // get views
        listView = view.findViewById(R.id.listview_single_category);
        timePeriodTextView = view.findViewById(R.id.time_period);
        back_button = view.findViewById(R.id.back_button);


        // init components
        initList();
        initListOnItemClick();
        initBackButton();
        setTimePeriodLabel();

        return view;
    }

    private void initList(){
        transactionList = Controller.getTransactions(chosenCategory, timePeriod.getMonth(), timePeriod.getYear());
        TransactionListAdapter adapter = new TransactionListAdapter(requireActivity().getApplicationContext(), transactionList);
        listView.setAdapter(adapter);
    }

    private void initBackButton(){
        back_button.setOnClickListener(view ->
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new HomeFragment()).commit());
    }

    private void setTimePeriodLabel(){
        String text;
        if (timePeriod.isTimeSpecified()) {
            text = Month.of(timePeriod.getMonth()) + " " + timePeriod.getYear();
        } else {
            text = "All transactions";
        }
        timePeriodTextView.setText(text);
    }

    private void initListOnItemClick(){
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            // get data from model
            FinancialTransaction transaction = (FinancialTransaction) adapterView.getItemAtPosition(i);

            // check if it is a date row, should not be clickable
            if (transaction.getCategory().getName().equals("DATE")){
                return;
            }

            // attach clicked category as argument to EditTransactionFragment and open the fragment
            Fragment fragment = new EditTransactionFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CHOSEN_TRANSACTION", new ParcelableTransaction(transaction));
            fragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
        });
    }

}
