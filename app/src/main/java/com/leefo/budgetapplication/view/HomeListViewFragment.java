package com.leefo.budgetapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.model.TransactionFake;

import java.util.ArrayList;


public class HomeListViewFragment extends Fragment {

    ListView listView;
    ArrayList<TransactionFake> list;
    ListViewAdapterHomeList adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list_view, container, false);

        listView = view.findViewById(R.id.listView_home);
        list = new ArrayList<>();

        list.add(new TransactionFake(100, "Danslektioner", "#55F979"));
        list.add(new TransactionFake(200, "Mat", "#558DF9"));
        list.add(new TransactionFake(300, "Musik", "#F95555"));

        adapter = new ListViewAdapterHomeList(getActivity().getApplicationContext(), list);
        listView.setAdapter(adapter);

        return view;
    }
}