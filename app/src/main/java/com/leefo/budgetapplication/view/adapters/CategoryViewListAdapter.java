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

import com.github.mikephil.charting.data.PieEntry;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.SharedViewData;

import java.util.ArrayList;

/**
 * Class that represents the adapter for the list in the HomeCategoryViewFragment
 */
public class CategoryViewListAdapter extends ArrayAdapter<Category> {
    Context context;

    public CategoryViewListAdapter(Context context, @NonNull ArrayList<Category> list) {
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

        int numberOfTransactions = 0;
        numberOfTransactions = Controller.getTransactions(cat, SharedViewData.timePeriod.getMonth(), SharedViewData.timePeriod.getYear()).size();

        TextView sum = convertView.findViewById(R.id.category_sum);
        TextView name = convertView.findViewById(R.id.category_name);
        View circle = convertView.findViewById(R.id.category_circle);
        TextView numberOfTransactionsTextView = convertView.findViewById(R.id.number_of_transactions);

        sum.setText(String.valueOf(Controller.getTransactionSum(cat, SharedViewData.timePeriod.getMonth(), SharedViewData.timePeriod.getYear())));
        name.setText(cat.getName());
        circle.getBackground().setColorFilter(Color.parseColor(cat.getColor()), PorterDuff.Mode.SRC_ATOP);
        if (numberOfTransactions == 1){
            numberOfTransactionsTextView.setText("1 Transaction");
        } else {
            numberOfTransactionsTextView.setText(numberOfTransactions + " Transactions");
        }

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

