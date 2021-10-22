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

import java.util.List;

/**
 * Class that represents the adapter for the list in the HomeCategoryViewFragment
 *
 * @author Emelie Edberg
 */
public class ManageCategoriesListAdapter extends ArrayAdapter<Category> {
    Context context;

    public ManageCategoriesListAdapter(@NonNull Context context, @NonNull List<Category> list) {
        super(context, R.layout.list_row_manage_categories, list);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_manage_categories,
                    parent, false);
        }

        Category cat = getItem(position);

        TextView name = convertView.findViewById(R.id.category_name);
        View circle = convertView.findViewById(R.id.category_circle);

        name.setText(cat.getName());
        circle.getBackground().setColorFilter(Color.parseColor(cat.getColor()), PorterDuff.Mode.SRC_ATOP);

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

