package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.ParcelableTransaction;
import com.leefo.budgetapplication.view.ParcelableCategory;
import com.leefo.budgetapplication.view.SharedViewModel;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.MissingResourceException;

public class SingleCategoryFragment extends Fragment {

    ListView listView;
    ArrayList<FinancialTransaction> list;
    TextView timePeriodTextView;
    TimePeriod timePeriod;
    SharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_category, container, false);

        Category chosenCategory;

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timePeriod = viewModel.getTimePeriod().getValue();

        Bundle bundle = this.getArguments();
        if (bundle != null){
            ParcelableCategory chosen_category = bundle.getParcelable("CHOSEN_CATEGORY");
            chosenCategory = chosen_category.category;
        } else {
            throw new MissingResourceException("No chosen category was sent with the fragment, hence fragment cannot be created", ParcelableCategory.class.toString(), "CHOSEN_CATEGORY" );
        }

        list = Controller.getTransactions(chosenCategory, timePeriod.getMonth(), timePeriod.getYear());
        putDatesIntoTransactionList();
        listView = view.findViewById(R.id.listview_single_category);
        ListViewAdapterHomeList adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(),list);
        listView.setAdapter(adapter);

        initListOnItemClick();

        timePeriodTextView = view.findViewById(R.id.time_period);
        setTimePeriodLabel();
        return view;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FinancialTransaction transaction = (FinancialTransaction) adapterView.getItemAtPosition(i);
                if (transaction.getCategory().getName().equals("DATE")){ // then it is a date row, should not be clickable
                    return;
                }
                Fragment fragment = new EditTransactionFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CHOSEN_TRANSACTION", new ParcelableTransaction(transaction));
                fragment.setArguments(bundle);
                ((MainActivity)getActivity()).openFragmentInMainFrameLayout(fragment);
            }
        });
    }

    private void addDateRowInTransactionList(int index, String date){
        list.add(index, new FinancialTransaction(0,date, LocalDate.now(), new Category("DATE", "", true)));
    }

    private void putDatesIntoTransactionList(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate date = list.get(0).getDate(); // first date

        if (date.isEqual(today)){
            addDateRowInTransactionList(0, "Today");
        } else if (date.isEqual(yesterday)){
            addDateRowInTransactionList(0, "Yesterday");
        } else{
            addDateRowInTransactionList(0,date.toString());
        }

        for (int i = 2; i <= list.size()-1;){

            if (!date.isEqual(list.get(i).getDate())){
                date = list.get(i).getDate();
                if (date.isEqual(today)){
                    addDateRowInTransactionList(i, "Today");
                } else if (date.isEqual(yesterday)){
                    addDateRowInTransactionList(i, "Yesterday");
                } else{
                    addDateRowInTransactionList(i,date.toString());
                }
                i++;
            }
            i++;
        }
    }

}
