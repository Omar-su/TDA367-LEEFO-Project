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
import com.leefo.budgetapplication.model.Category;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that represents the adapter for the list in the EditBudgetFragment
 *
 * @author Omar Sulaiman
 */
public class EditBudgetListAdapter extends ArrayAdapter<Category>{


    Context context;
    private EditText editBudget;
    private TextView name;
    private View circle;
    private final HashMap<Category , Integer > editBudgetHashMap;


    public EditBudgetListAdapter(Context context, @NonNull ArrayList<Category> list) {
        super(context, R.layout.list_row_edit_budget, list);
        this.context = context;
        editBudgetHashMap = getCategoryBudgetHashMap(list);
    }

    /**
     * Creates a hashmap with all categories as keys and their budgetGoal as the values
     * @param list The category list that is to be inserted into the hashmap
     * @return Returns a hashmap with with all categories as keys and their budgetGoal as the values
     */
    @NonNull
    private HashMap<Category, Integer> getCategoryBudgetHashMap(@NonNull ArrayList<Category> list) {

        final HashMap<Category, Integer> editBudgetHashMap;
        editBudgetHashMap = new HashMap<>();
        for (Category cat : list){
            editBudgetHashMap.put(cat , cat.getBudgetGoal());
        }
        return editBudgetHashMap;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_edit_budget,
                    parent, false);
        }
        Category cat = getItem(position);

        getItemsId(convertView);
        setItemsValue(cat);

        initBudgetGoal(cat);
        return convertView;
    }

    /**
     * Sets values for the every category row
     * @param cat Which category to created
     */
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

    /**
     * Gets all inout changes from the user
     * @param category Which category row the user is inserting the input into
     */
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

    /**
     * Updates the hashmap with the new budget values
     * @param editable The input changes from the user
     * @param category Which category the changes are related to
     */
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

    /**
     * Method only used to send the new changes the EditBudgetFragment
     * @return A hashMap with the categories and all the new changes in the budgetGoals if there are
     */
    public HashMap<Category, Integer> getEditBudgetHashMap() {
        return editBudgetHashMap;
    }
}
