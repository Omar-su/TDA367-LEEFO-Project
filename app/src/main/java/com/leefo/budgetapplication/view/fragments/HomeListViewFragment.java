package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.ModelObserver;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class that represents the fragment for the list view inside the HomeFragment
 */
public class HomeListViewFragment extends Fragment implements ModelObserver {

    ListView listView;
    ListViewAdapterHomeList adapter;
    ArrayList<Transaction> transactions;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list_view, container, false);

        Controller.addObserver(this);

        // get views
        listView = view.findViewById(R.id.listView_home);
        TextView noTransactoins1 = view.findViewById(R.id.noTransactionsYetText1);
        TextView noTransactoins2 = view.findViewById(R.id.noTransactionsYetText2);

        transactions = Controller.getTransactions();




        if (transactions.isEmpty()){
            noTransactoins1.setVisibility(View.VISIBLE);
            noTransactoins2.setVisibility(View.VISIBLE);
        } else {
            putDatesIntoTransactionList();
            adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(),transactions);
            listView.setAdapter(adapter);
        }


        return view;
    }


    private void addDateRowInTransactionList(int index, String date){
        transactions.add(index, new Transaction(0,date,LocalDate.now(), new Category("DATE", "", true)));
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




    @Override
    public void update() {

    }
}