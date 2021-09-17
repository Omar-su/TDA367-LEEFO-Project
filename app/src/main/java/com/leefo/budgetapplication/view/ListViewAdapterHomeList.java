package com.leefo.budgetapplication.view;

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
import com.leefo.budgetapplication.model.TransactionFake;

import java.util.ArrayList;

public class ListViewAdapterHomeList extends ArrayAdapter<TransactionFake> {

    ArrayList<TransactionFake> list;
    Context context;

    public ListViewAdapterHomeList(@NonNull Context context, ArrayList<TransactionFake> list) {
        super(context, R.layout.list_row_home, list);
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_home, null);


            // get views
            TextView amount = convertView.findViewById(R.id.amount);
            TextView category = convertView.findViewById(R.id.category);
            View circle = convertView.findViewById(R.id.circle);
            TextView date = convertView.findViewById(R.id.date);
            RelativeLayout row = convertView.findViewById(R.id.row);

            boolean newDay = false;
            if (list.get(position).getCategory().equals("date")) newDay = true; // TODO

            if (newDay){ // change a list item design to display a date instead of transaction
                newDay = false;
                amount.setVisibility(View.GONE);
                category.setVisibility(View.GONE);
                circle.setVisibility(View.GONE);
                date.setText("Today"); // TODO
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_background));
            } else { // set transaction values
                date.setVisibility(View.GONE);
                amount.setText(Integer.toString(list.get(position).getAmount()));
                category.setText(list.get(position).getCategory());
                circle.getBackground().setColorFilter(Color.parseColor(list.get(position).getColor()), PorterDuff.Mode.SRC_ATOP);

            }


        }



        return convertView;
    }
}
