package com.leefo.budgetapplication.view;

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
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        transactions.add(index, new Transaction(0,"DATE", date, new Category("", "", true)));
    }

    private void putDatesIntoTransactionList(){
        String today = getTodaysDate();
        String yesterday = getYesterdaysDate();
        String date = transactions.get(0).getDate(); // first date

        if (date.equals(today)){
            addDateRowInTransactionList(0, "Today");
        } else if (date.equals(yesterday)){
            addDateRowInTransactionList(0, "Yesterday");
        } else{
            addDateRowInTransactionList(0,date);
        }

        for (int i = 2; i <= transactions.size()-1;){

            if (!date.equals(transactions.get(i).getDate())){
                date = transactions.get(i).getDate();
                if (date.equals(today)){
                    addDateRowInTransactionList(i, "Today");
                } else if (date.equals(yesterday)){
                    addDateRowInTransactionList(i, "Yesterday");
                } else{
                    addDateRowInTransactionList(i,date);
                }
                i++;
            }
            i++;
        }
    }

    private String getYesterdaysDate(){
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format((cal.getTime()));
    }



    @Override
    public void update() {

    }
}