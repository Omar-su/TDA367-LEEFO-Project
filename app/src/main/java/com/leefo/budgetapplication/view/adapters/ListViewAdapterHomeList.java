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



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_home, null);

            // the Transaction object in the list
            Transaction transaction = getItem(position);
            Category categoryObject = Controller.getCategoryFromId(transaction.getCategoryId());

            // get views
            TextView amount = convertView.findViewById(R.id.amount);
            TextView category = convertView.findViewById(R.id.category);
            View circle = convertView.findViewById(R.id.circle);
            TextView date = convertView.findViewById(R.id.date);
            RelativeLayout row = convertView.findViewById(R.id.row);

            boolean newDay = false;
            if (transaction.getDescription().equals("DATE")) newDay = true; // TODO

            if (newDay){ // change a list item design to display a date instead of transaction
                newDay = false;
                amount.setVisibility(View.GONE);
                category.setVisibility(View.GONE);
                circle.setVisibility(View.GONE);
                date.setText(transaction.getDate());
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_background));
            } else { // set transaction values
                date.setVisibility(View.GONE);
                amount.setText(String.valueOf(transaction.getAmount()));
                category.setText(categoryObject.getName());
                circle.getBackground().setColorFilter(Color.parseColor(categoryObject.getColor()), PorterDuff.Mode.SRC_ATOP); // TODO

            }


        }



        return convertView;
    }
}
