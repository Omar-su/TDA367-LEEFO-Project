package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.TransactionFake;
import com.leefo.budgetapplication.view.adapters.ListViewAdapterHomeList;

import java.util.ArrayList;

/**
 * The class that represents the fragment for the list view inside the HomeFragment
 */
public class HomeListViewFragment extends Fragment implements ModelObserver{

    ListView listView;
    ArrayList<TransactionFake> list;
    ListViewAdapterHomeList adapter;

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
        list = new ArrayList<>();

        // TODO hardcoded for now
        list.add(new TransactionFake(200, "date", "#558DF9"));
        list.add(new TransactionFake(300, "Musik", "#F95555"));
        list.add(new TransactionFake(100, "Danslektioner", "#55F979"));
        list.add(new TransactionFake(200, "Mat", "#558DF9"));
        list.add(new TransactionFake(300, "Musik", "#F95555"));
        list.add(new TransactionFake(100, "date", "#55F979"));
        list.add(new TransactionFake(200, "Mat", "#558DF9"));
        list.add(new TransactionFake(300, "Musik", "#F95555"));
        list.add(new TransactionFake(100, "date", "#55F979"));
        list.add(new TransactionFake(200, "Mat", "#558DF9"));
        list.add(new TransactionFake(300, "Musik", "#F95555"));
        list.add(new TransactionFake(100, "Danslektioner", "#55F979"));
        list.add(new TransactionFake(200, "Mat", "#558DF9"));
        list.add(new TransactionFake(300, "Musik", "#F95555"));
        list.add(new TransactionFake(100, "Danslektioner", "#55F979"));
        list.add(new TransactionFake(200, "Mat", "#558DF9"));
        list.add(new TransactionFake(300, "Musik", "#F95555"));

        adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(), list);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void update() {

    }
}