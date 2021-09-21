package com.leefo.budgetapplication.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.model.CategoryFake;

import java.util.ArrayList;

/**
 * Class that represents the adapter for the category-chooser spinner in the NewTransactionFragment
 */
public class SpinnerAdapter extends ArrayAdapter<CategoryFake> {

    LayoutInflater layoutInflater;

    public SpinnerAdapter(@NonNull Context context, @NonNull ArrayList<CategoryFake> categories) {
        super(context, R.layout.spinner_row, categories);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.spinner_row, null, true);
        CategoryFake categoryFake = getItem(position);

        TextView name = rowView.findViewById(R.id.category_name);
        View circle = rowView.findViewById(R.id.circle);

        name.setText(categoryFake.getName());
        circle.getBackground().setColorFilter(Color.parseColor(categoryFake.getColor()), PorterDuff.Mode.SRC_ATOP);


        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.spinner_row, parent, false);

        }
        CategoryFake categoryFake = getItem(position);

        TextView name = convertView.findViewById(R.id.category_name);
        View circle = convertView.findViewById(R.id.circle);

        name.setText(categoryFake.getName());
        circle.getBackground().setColorFilter(Color.parseColor(categoryFake.getColor()), PorterDuff.Mode.SRC_ATOP);


        return convertView;
    }


}
