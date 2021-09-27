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

import com.github.mikephil.charting.data.PieEntry;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the adapter for the list in the HomeCategoryViewFragment
 */
public class CategoryListAdapter extends ArrayAdapter<Category> {
    Context context;

    public CategoryListAdapter(@NonNull Context context, @NonNull ArrayList<Category> list) {
        super(context, R.layout.list_row_category, list);
        this.context = context;
    }

    /**
     * Method called every time a listView's row is being created, for lists using this adapter.
     * Gets the design and content of a row in the listView
     * @param position the position in the list
     * @param convertView
     * @param parent
     * @return the view of the list row
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_category,
                    parent, false);
        }

        Category cat = getItem(position);

        TextView sum = convertView.findViewById(R.id.category_sum);
        TextView name = convertView.findViewById(R.id.category_name);
        View circle = convertView.findViewById(R.id.category_circle);

        sum.setText(String.valueOf(getCategorySum(cat.getId(),"21","09")));
        name.setText(cat.getName());
        circle.getBackground().setColorFilter(Color.parseColor(cat.getColor()), PorterDuff.Mode.SRC_ATOP);


        return convertView;
    }

    private double getCategorySum(int id, String year, String month){
        return Controller.getCategorySumByMonth(id, year, month);
    }
}

