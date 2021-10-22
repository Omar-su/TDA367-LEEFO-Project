package com.leefo.budgetapplication.view.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.BudgetGradeController;
import com.leefo.budgetapplication.controller.TransactionController;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.data.TimePeriod;

import java.util.List;

/**
 * The GradedBudgetListAdapter class represents the adapter for handling list items in
 * the {@link com.leefo.budgetapplication.view.fragments.BudgetFragment} class.
 * @author Felix Edholm
 */

public class GradedBudgetListAdapter extends ArrayAdapter<Category> {

    private final TimePeriod timePeriod;

    public GradedBudgetListAdapter(@NonNull Context context, List<Category> list, TimePeriod timePeriod) {
        super(context, R.layout.list_row_graded_budget, list);
        this.timePeriod = timePeriod;
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

        String categoryExpenseSum = String.valueOf(TransactionController.getInstance().getTransactionSum(category, timePeriod.getMonth(), timePeriod.getYear()));
        String categoryBudgetGoal = String.valueOf(category.getBudgetGoal());
        budgetRatioText.setText(categoryExpenseSum + " / " + categoryBudgetGoal);

        ratingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(category.getColor())));
        ratingBar.setRating(BudgetGradeController.getInstance().gradeCategory(category, timePeriod.getMonth(),  timePeriod.getYear()));

        budgetOutcomeText.setText(BudgetGradeController.getInstance().getRoundedBudgetOutcome(category, timePeriod.getMonth(), timePeriod.getYear()) + "x");

        return convertView;
    }
}

