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
import com.leefo.budgetapplication.controller.TransactionController;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.data.TimePeriod;

import java.util.List;

/**
 * Class that represents the adapter for the list in the HomeCategoryViewFragment
 *
 * @author Emelie Edberg
 */
public class CategoryListAdapter extends ArrayAdapter<Category> {
    Context context;
    TimePeriod timePeriod;

    public CategoryListAdapter(Context context, @NonNull List<Category> list, TimePeriod timePeriod) {
        super(context, R.layout.list_row_category, list);
        this.context = context;
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
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_category,
                    parent, false);
        }

        Category cat = getItem(position);

        int numberOfTransactions;
        numberOfTransactions = TransactionController.getInstance().getTransactions(cat, timePeriod.getMonth(), timePeriod.getYear()).size();

        TextView sum = convertView.findViewById(R.id.category_sum);
        TextView name = convertView.findViewById(R.id.category_name);
        View circle = convertView.findViewById(R.id.category_circle);
        TextView numberOfTransactionsTextView = convertView.findViewById(R.id.number_of_transactions);

        sum.setText(String.valueOf(TransactionController.getInstance().getTransactionSum(cat, timePeriod.getMonth(), timePeriod.getYear())));
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
        if (getCount() == 0) {
            return 1;
        }
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}

