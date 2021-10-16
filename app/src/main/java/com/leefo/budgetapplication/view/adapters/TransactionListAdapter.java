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

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;


import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class that represents the adapter for the list in the HomeListViewFragment
 *
 * @author Emelie Edberg
 */
public class TransactionListAdapter extends ArrayAdapter<FinancialTransaction> {

    Context context;
    ArrayList<FinancialTransaction> list;


    public TransactionListAdapter(@NonNull Context context, ArrayList<FinancialTransaction> list) {
        super(context, R.layout.list_row_home, list); // list sent to super
        ArrayList<FinancialTransaction> copy = new ArrayList<>(list); // make a copy to have without dates
        putDatesIntoTransactionList(list); // put dates into the list sent to super
        this.context = context;
        this.list = copy;
    }

    /**
     * Getter a class using the adapter can call to get the adapters current list.
     * @return The list.
     */
    public ArrayList<FinancialTransaction> getList() {
        return list;
    }



    /**
     * Method called every time a listView's row is being created, for lists using this adapter.
     * Gets the design and content of a row in the listView
     * @param position the position in the list
     * @param convertView
     * @param parent
     * @return the view of the list row
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_home, null);

            // The Transaction object in the list
            FinancialTransaction transaction = getItem(position);
            // The transaction's category
            Category categoryObject = transaction.getCategory();

            // get views
            TextView amount = convertView.findViewById(R.id.amount);
            TextView category = convertView.findViewById(R.id.category);
            View circle = convertView.findViewById(R.id.circle);
            TextView date = convertView.findViewById(R.id.date);
            RelativeLayout row = convertView.findViewById(R.id.row);

            // Some Transaction objects in the list have been given the category name "DATE" to mark that this is not a transaction
            // instead this row in the list should be a date row displaying only a date.
            boolean dateRow = false; // start with false
            if (transaction.getCategory().getName().equals("DATE")) dateRow = true;

            // if dateRow is true this row needs another design showing a date instead of transaction
            // new design in the if block
            if (dateRow){
                amount.setVisibility(View.GONE);
                category.setVisibility(View.GONE);
                circle.setVisibility(View.GONE);
                date.setText(transaction.getDescription());
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_background));

            // else the row is a normal transaction and gets the transaction design
            } else {
                date.setVisibility(View.GONE);
                amount.setText(String.valueOf(transaction.getAmount()));
                category.setText(categoryObject.getName());
                if (!categoryObject.getColor().equals("")) {
                    circle.getBackground().setColorFilter(Color.parseColor(categoryObject.getColor()), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
        return convertView;
    }

    /**
     * In order to display date rows within the list. We must add extra objects in the list where we want the date row to be
     * The list adapter (getView) can differentiate between normal Transaction objects and the ones representing date rows and display those differently.
     *
     * The method works on lists sorted by date.
     * Inputs special date Transaction objects in front of every object with a new date.
     */
    private void putDatesIntoTransactionList(ArrayList<FinancialTransaction> list){
        if (list.isEmpty()) return;

        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate date = list.get(0).getDate(); // first date

        if (date.isEqual(today)){
            addDateRowInTransactionList(list, 0, "Today");
        } else if (date.isEqual(yesterday)){
            addDateRowInTransactionList(list, 0, "Yesterday");
        } else{
            addDateRowInTransactionList(list, 0,date.toString());
        }

        for (int i = 2; i <= list.size()-1;){

            if (!date.isEqual(list.get(i).getDate())){
                date = list.get(i).getDate();
                if (date.isEqual(today)){
                    addDateRowInTransactionList(list, i, "Today");
                } else if (date.isEqual(yesterday)){
                    addDateRowInTransactionList(list, i, "Yesterday");
                } else{
                    addDateRowInTransactionList(list, i,date.toString());
                }
                i++;
            }
            i++;
        }
    }

    private void addDateRowInTransactionList(ArrayList<FinancialTransaction> list, int index, String date){
        list.add(index, new FinancialTransaction(0,date, LocalDate.now(), new Category("DATE", "", true)));
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
