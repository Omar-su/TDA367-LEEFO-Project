package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.ParcelableTransaction;
import com.leefo.budgetapplication.view.SharedTimePeriodViewModel;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class that represents the fragment for the list view inside the HomeFragment
 */
public class HomeListViewFragment extends Fragment {

    ListView listView;
    ListViewAdapterHomeList adapter;
    ArrayList<FinancialTransaction> transactions;
    TextView noTransactoins1, noTransactoins2;
    TimePeriod timePeriod;
    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list_view, container, false);

        // get views
        listView = view.findViewById(R.id.listView_home);
        noTransactoins1 = view.findViewById(R.id.noTransactionsYetText1);
        noTransactoins2 = view.findViewById(R.id.noTransactionsYetText2);

        SharedTimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedTimePeriodViewModel.class);
        timePeriod = viewModel.getTimePeriodLiveData().getValue();
        viewModel.getTimePeriodLiveData().observe(getViewLifecycleOwner(), new Observer<TimePeriod>() {
            @Override
            public void onChanged(TimePeriod newTimePeriod) {
               // timePeriod = newTimePeriod;
                updateList();
            }
        });


        updateList();

        initList();

        return view;
    }

    private void initList() {

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
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
            }
        });
    }

    private void updateList() {
        if (getActivity() != null) {
            transactions = Controller.getTransactions(timePeriod.getMonth(), timePeriod.getYear());

            if (transactions.isEmpty()) {
                noTransactoins1.setVisibility(View.VISIBLE);
                noTransactoins2.setVisibility(View.VISIBLE);
            } else {
                putDatesIntoTransactionList();
                noTransactoins1.setVisibility(View.INVISIBLE);
                noTransactoins2.setVisibility(View.INVISIBLE);
            }
            adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(), transactions);
            listView.setAdapter(adapter);
        }
    }


    private void addDateRowInTransactionList(int index, String date){
        transactions.add(index, new FinancialTransaction(0,date,LocalDate.now(), new Category("DATE", "", true)));
    }

    private void putDatesIntoTransactionList(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate date = transactions.get(0).getDate(); // first date

        if (date.isEqual(today)){
            addDateRowInTransactionList(0, "Today");
        } else if (date.isEqual(yesterday)){
            addDateRowInTransactionList(0, "Yesterday");
        } else{
            addDateRowInTransactionList(0,date.toString());
        }

        for (int i = 2; i <= transactions.size()-1;){

            if (!date.isEqual(transactions.get(i).getDate())){
                date = transactions.get(i).getDate();
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