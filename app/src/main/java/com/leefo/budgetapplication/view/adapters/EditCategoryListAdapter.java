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
import java.util.HashMap;

public class EditCategoryListAdapter extends ArrayAdapter<Category>{


    Context context;
    EditText editBudget;
    TextView name;
    View circle;
    private final HashMap<Category , Integer > editBudgetHashMap;


    public EditCategoryListAdapter(Context context, @NonNull ArrayList<Category> list) {
        super(context, R.layout.list_row_category_budget, list);
        this.context = context;
        editBudgetHashMap = getCategoryIntegerHashMap(list);
    }

    @NonNull
    private HashMap<Category, Integer> getCategoryIntegerHashMap(@NonNull ArrayList<Category> list) {

        final HashMap<Category, Integer> editBudgetHashMap;
        editBudgetHashMap = new HashMap<>();
        for (Category cat : list){
            editBudgetHashMap.put(cat , cat.getBudgetGoal());
        }
        return editBudgetHashMap;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_category_budget,
                    parent, false);
        }
        Category cat = getItem(position);

        getItemsId(convertView);
        setItemsValue(cat);

        initBudgetGoal(cat);
        return convertView;
    }

    private void setItemsValue(Category cat) {
        if (cat.getBudgetGoal() != 0)editBudget.setText(String.valueOf(cat.getBudgetGoal()));
        name.setText(cat.getName());
        circle.getBackground().setColorFilter(Color.parseColor(cat.getColor()), PorterDuff.Mode.SRC_ATOP);
    }

    private void getItemsId(View convertView) {
        editBudget = convertView.findViewById(R.id.editBudgetGoal);
        name = convertView.findViewById(R.id.budget_category_name);
        circle = convertView.findViewById(R.id.budget_category_circle);
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
                updateValueHashMap(editable, category);
            }
        });
    }

    private void updateValueHashMap(Editable editable, Category category) {
        if (!editable.toString().equals("")) {
            if (editBudgetHashMap.containsKey(category)){
                editBudgetHashMap.replace(category, Integer.parseInt(editable.toString()));
            }
        }else {
            editBudgetHashMap.replace(category, 0);
        }
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

    public HashMap<Category, Integer> getEditBudgetHashMap() {
        return editBudgetHashMap;
    }
}
