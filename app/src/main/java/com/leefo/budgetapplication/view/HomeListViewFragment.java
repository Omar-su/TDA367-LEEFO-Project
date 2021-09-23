package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The class that represents the fragment for the list view inside the HomeFragment
 */
public class HomeListViewFragment extends Fragment implements ModelObserver{

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

        listView = view.findViewById(R.id.listView_home);
        transactions = Controller.getAllTransactions();

        putDatesIntoTransactionList();

        adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(),transactions);
        listView.setAdapter(adapter);

        return view;
    }

    private void putDatesIntoTransactionList(){
        String today = getTodaysDate();
        String date = transactions.get(0).getDate(); // first date
        transactions.add(0,new Transaction(0,0,"DATE", date, 0));

        for (int i = 2; i < transactions.size()-1; i++){
            if (!date.equals(transactions.get(i).getDate())){
                date = transactions.get(i).getDate();
                transactions.add(i,new Transaction(0,0,"DATE", date, 0));
                i++;
            }
        }
    }

    private String getTodaysDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return formatter.format(date);
    }



    @Override
    public void update() {

    }
}