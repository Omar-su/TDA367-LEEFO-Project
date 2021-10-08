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
import com.leefo.budgetapplication.model.Category;

import java.util.ArrayList;

/**
 * Class that represents the adapter for the category-chooser spinner in the NewTransactionFragment
 *
 * @author Emelie Edberg
 */
public class SpinnerAdapter extends ArrayAdapter<Category> {

    LayoutInflater layoutInflater;

    public SpinnerAdapter(@NonNull Context context, @NonNull ArrayList<Category> categories) {
        super(context, R.layout.spinner_row, categories);
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * Method called every time a spinner row is being created, for spinners using this adapter.
     * Gets the design and content of a row in the spinner
     * @param position the position in the list
     * @param convertView
     * @param parent
     * @return the view of the spinner row
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.spinner_row, null, true);
        Category category = getItem(position);

        TextView name = rowView.findViewById(R.id.category_name);
        View circle = rowView.findViewById(R.id.circle);

        name.setText(category.getName());
        if (!category.getColor().equals("")) {
            circle.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_ATOP);
        }

        return rowView;
    }

    /**
     * Method called every time a spinner row is being created, for spinners using this adapter.
     * Gets the design and content of a row in the spinner
     * @param position the position in the list
     * @param convertView
     * @param parent
     * @return the view of the spinner row
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.spinner_row, parent, false);

        }
        Category category = getItem(position);

        TextView name = convertView.findViewById(R.id.category_name);
        View circle = convertView.findViewById(R.id.circle);

        name.setText(category.getName());
        if (!category.getColor().equals("")) {
            circle.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_ATOP);
        }

        return convertView;
    }


}
