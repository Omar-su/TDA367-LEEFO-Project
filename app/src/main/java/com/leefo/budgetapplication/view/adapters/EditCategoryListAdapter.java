package com.leefo.budgetapplication.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;

import java.util.ArrayList;

public class EditCategoryListAdapter extends ArrayAdapter<Category>{


    Context context;


    public EditCategoryListAdapter(Context context, @NonNull ArrayList<Category> list) {
        super(context, R.layout.list_row_category_budget, list);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_category_budget,
                    parent, false);
        }

        Category cat = getItem(position);


        EditText budgetGoal = convertView.findViewById(R.id.editBudgetListV);
        TextView name = convertView.findViewById(R.id.budget_category_name);
        View circle = convertView.findViewById(R.id.budget_category_circle);
        name.setText(cat.getName());
        circle.getBackground().setColorFilter(Color.parseColor(cat.getColor()), PorterDuff.Mode.SRC_ATOP);

        return convertView;
    }
    @Override
    public int getViewTypeCount() {
        if (getCount() == 0) return 1;
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }




}
