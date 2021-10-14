package com.leefo.budgetapplication.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
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
    EditText editBudget;


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


        editBudget = convertView.findViewById(R.id.editBudgetGoal);

        if (cat.getGoal()!=0)editBudget.setText(String.valueOf(cat.getGoal()));

        TextView name = convertView.findViewById(R.id.budget_category_name);
        View circle = convertView.findViewById(R.id.budget_category_circle);
        name.setText(cat.getName());
        circle.getBackground().setColorFilter(Color.parseColor(cat.getColor()), PorterDuff.Mode.SRC_ATOP);

        initBudgetGoal(cat);
        return convertView;
    }

    private void initBudgetGoal(Category category) {
        editBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals("")) {
                    Controller.editCategoryInfo(category, Integer.parseInt(editable.toString()));
                }
            }
        });
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
