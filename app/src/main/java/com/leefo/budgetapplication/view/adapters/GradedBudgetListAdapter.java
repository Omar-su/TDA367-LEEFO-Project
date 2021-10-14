package com.leefo.budgetapplication.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.TimePeriod;

import java.util.ArrayList;

public class GradedBudgetListAdapter extends ArrayAdapter<Category> {

    Context context;
    TimePeriod timePeriod;

    public GradedBudgetListAdapter(@NonNull Context context, ArrayList<Category> list, TimePeriod timePeriod) {
        super(context, R.layout.list_row_graded_budget, list);
        this.context = context;
        this.timePeriod = timePeriod;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_graded_budget,
                    parent, false);
        }
        // The Category object in the list
        Category category = getItem(position);

        // get views
        TextView catName = convertView.findViewById(R.id.catName);
        TextView budgetRatioText = convertView.findViewById(R.id.budgetRatioText);
        TextView budgetOutcomeText = convertView.findViewById(R.id.budgetOutcomeText);
        View catIndicator = convertView.findViewById(R.id.catIndicator);
        RatingBar ratingBar = convertView.findViewById(R.id.budgetGradeRatingBar);

        catName.setText(category.getName());
        catIndicator.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_ATOP);

        String categoryExpenseSum = String.valueOf(Controller.getTransactionSum(category, timePeriod.getMonth(), timePeriod.getYear()));
        String categoryBudgetGoal = String.valueOf(category.getGoal());
        budgetRatioText.setText(categoryExpenseSum + " / " + categoryBudgetGoal);

        ratingBar.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setRating(Controller.gradeCategory(category));

        budgetOutcomeText.setText(String.valueOf(Controller.getRoundedBudgetOutcome(category)));

        return convertView;
    }
}

