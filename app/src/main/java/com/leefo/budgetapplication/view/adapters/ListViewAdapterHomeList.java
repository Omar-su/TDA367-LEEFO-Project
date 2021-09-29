package com.leefo.budgetapplication.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.Transaction;
import com.leefo.budgetapplication.view.MainActivity;


import java.util.ArrayList;

/**
 * Class that represents the adapter for the list in the HomeListViewFragment
 */
public class ListViewAdapterHomeList extends ArrayAdapter<Transaction> {

    Context context;

    public ListViewAdapterHomeList(@NonNull Context context, ArrayList<Transaction> list) {
        super(context, R.layout.list_row_home, list);
        this.context = context;
    }


    /**
     * Method called every time a listView's row is being created, for lists using this adapter.
     * Gets the design and content of a row in the listView
     * @param position the position in the list
     * @param convertView
     * @param parent
     * @return the view of the list row
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_home, null);

            // The Transaction object in the list
            Transaction transaction = getItem(position);
            // The transaction's category
            Category categoryObject = transaction.getCategory();

            // get views
            TextView amount = convertView.findViewById(R.id.amount);
            TextView category = convertView.findViewById(R.id.category);
            View circle = convertView.findViewById(R.id.circle);
            TextView date = convertView.findViewById(R.id.date);
            RelativeLayout row = convertView.findViewById(R.id.row);

            // Some Transaction objects in the list have been given the description "DATE" to mark that this is not a transaction
            // instead this row in the list should be a date row displaying only a date.
            boolean dateRow = false; // start with false
            if (transaction.getDescription().equals("DATE")) dateRow = true;

            // if dateRow is true this row needs another design shoving a date instead of transaction
            // new design in the if block
            if (dateRow){
                dateRow = false;
                amount.setVisibility(View.GONE);
                category.setVisibility(View.GONE);
                circle.setVisibility(View.GONE);
                date.setText(transaction.getDate());
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_background));

            // else the row is a normal transaction and gets the transaction design
            } else {
                date.setVisibility(View.GONE);
                amount.setText(String.valueOf(transaction.getAmount()));
                category.setText(categoryObject.getName());
                circle.getBackground().setColorFilter(Color.parseColor(categoryObject.getColor()), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return convertView;
    }
}
