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
import com.leefo.budgetapplication.view.ParcelableTransaction;
import com.leefo.budgetapplication.view.ParcelableCategory;
import com.leefo.budgetapplication.view.SharedTimePeriodViewModel;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.time.LocalDate;
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
        SharedTimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedTimePeriodViewModel.class);
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
        putDatesIntoTransactionList(transactionList);
        ListViewAdapterHomeList adapter = new ListViewAdapterHomeList(requireActivity().getApplicationContext(), transactionList);
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

    private void addDateRowInTransactionList(ArrayList<FinancialTransaction> list, int index, String date){
        list.add(index, new FinancialTransaction(0,date, LocalDate.now(), new Category("DATE", "", true)));
    }

    /**
     * In order to display date rows within the list. We must add extra objects in the list where we want the date row to be
     * then when the list is sent to the list adapter it can differentiate between normal Transaction objects and the ones representing date rows and display those differently.
     *
     * The method works on lists sorted by date.
     * Inputs special date Transaction objects in front of every object with a new date.
     */
    private void putDatesIntoTransactionList(ArrayList<FinancialTransaction> list){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate date = list.get(0).getDate(); // first date

        if (date.isEqual(today)){
            addDateRowInTransactionList(list, 0, "Today");
        } else if (date.isEqual(yesterday)){
            addDateRowInTransactionList(list, 0, "Yesterday");
        } else{
            addDateRowInTransactionList(list, 0,date.toString());
        }

        for (int i = 2; i <= list.size()-1;){

            if (!date.isEqual(list.get(i).getDate())){
                date = list.get(i).getDate();
                if (date.isEqual(today)){
                    addDateRowInTransactionList(list, i, "Today");
                } else if (date.isEqual(yesterday)){
                    addDateRowInTransactionList(list, i, "Yesterday");
                } else{
                    addDateRowInTransactionList(list, i,date.toString());
                }
                i++;
            }
            i++;
        }
    }

}
