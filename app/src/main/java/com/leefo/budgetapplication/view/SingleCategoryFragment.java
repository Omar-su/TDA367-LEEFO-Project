package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.view.adapters.CategoryListAdapter;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SingleCategoryFragment extends Fragment {

    ListView listView;
    ArrayList<Transaction> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_category, container, false);

        Category category = SharedViewData.singleCatgery;
        list = Controller.getTransactions(category); // TODO

        TextView textView = view.findViewById(R.id.title_category);
        textView.setText(category.getName());

        putDatesIntoTransactionList();
        listView = view.findViewById(R.id.listview_single_category);
        ListViewAdapterHomeList adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(),list);
        listView.setAdapter(adapter);
        return view;
    }

    private void putDatesIntoTransactionList(){
        String today = getTodaysDate();
        String yesterday = getYesterdaysDate();
        String date = list.get(0).getDate(); // first date

        if (date.equals(today)){
            addDateRowInTransactionList(0, "Today");
        } else if (date.equals(yesterday)){
            addDateRowInTransactionList(0, "Yesterday");
        } else{
            addDateRowInTransactionList(0,date);
        }

        for (int i = 2; i <= list.size()-1;){

            if (!date.equals(list.get(i).getDate())){
                date = list.get(i).getDate();
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
    private void addDateRowInTransactionList(int index, String date){
        list.add(index, new Transaction(0,"DATE", date, new Category("", "", true)));
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


}
