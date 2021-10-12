package com.leefo.budgetapplication.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.ParcelableTransaction;
import com.leefo.budgetapplication.view.TimePeriodViewModel;
import com.leefo.budgetapplication.view.TimePeriod;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;
import com.leefo.budgetapplication.view.adapters.TransactionListAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The class that represents the fragment for the list view inside the HomeFragment
 * @author Emelie Edberg, Eugene Dvoryankov
 */
public class HomeListViewFragment extends Fragment {

    private ListView listView;
    private TextView noTransactions1, noTransactions2;
    private TimePeriod timePeriod;
    private ImageButton sort_button;
    private Dialog dialog;
    private RadioGroup sort_radio_group;

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
        noTransactions1 = view.findViewById(R.id.noTransactionsYetText1);
        noTransactions2 = view.findViewById(R.id.noTransactionsYetText2);
        sort_button = view.findViewById(R.id.sort_button);

        TimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(TimePeriodViewModel.class);
        timePeriod = viewModel.getTimePeriodLiveData().getValue();

        viewModel.getTimePeriodLiveData().observe(getViewLifecycleOwner(), new Observer<TimePeriod>() {
            @Override
            public void onChanged(TimePeriod newTimePeriod) {
                updateList(Controller.getTransactions(timePeriod.getMonth(), timePeriod.getYear()));
            }
        });


        updateList(Controller.getTransactions(timePeriod.getMonth(), timePeriod.getYear()));

        initList();

        initSortDialog();
        initSortButton();

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

    private void updateList(ArrayList<FinancialTransaction> transactions) {
        if (getActivity() != null) {

            if (transactions.isEmpty()) {
                noTransactions1.setVisibility(View.VISIBLE);
                noTransactions2.setVisibility(View.VISIBLE);
                sort_button.setVisibility(View.INVISIBLE);
            } else {
                putDatesIntoTransactionList(transactions);
                noTransactions1.setVisibility(View.INVISIBLE);
                noTransactions2.setVisibility(View.INVISIBLE);
                sort_button.setVisibility(View.VISIBLE);
            }
            TransactionListAdapter adapter = new TransactionListAdapter(getActivity().getApplicationContext(), transactions);
            listView.setAdapter(adapter);
        }
    }


    private void addDateRowInTransactionList(ArrayList<FinancialTransaction> list, int index, String date){
        list.add(index, new FinancialTransaction(0,date, LocalDate.now(), new Category("DATE", "", true,0)));
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
    private void initSortButton(){
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
    private void initSortDialog(){
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sort_dialog);
        sort_radio_group = dialog.findViewById(R.id.sort_radio_group);
        sort_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                ArrayList<FinancialTransaction> transactions = Controller.getTransactions(timePeriod.getMonth(), timePeriod.getYear());

                switch (checkedId){
                    case R.id.newest_date_radio:

                        break;

                    case R.id.oldest_date_radio:
                        Collections.reverse(transactions);
                        break;

                    case R.id.highest_amount_radio:
                        Controller.getSortByAmount(transactions);
                        break;

                    case R.id.lowest_amount_radio:
                        Controller.getSortByAmount(transactions);
                        Collections.reverse(transactions);
                        break;
                }
                updateList(transactions);
                dialog.cancel();
            }
        });
    }
}