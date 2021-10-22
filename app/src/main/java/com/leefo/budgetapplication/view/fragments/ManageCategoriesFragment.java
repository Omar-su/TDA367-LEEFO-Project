package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.CategoryController;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.data.ParcelableCategory;
import com.leefo.budgetapplication.view.adapters.ManageCategoriesListAdapter;

import java.util.List;

/**
 * This class represents the fragment for editing and/or adding new categories.
 * The fragment displays a list of all categories, together with a New Category button.
 * Opens Edit Category fragment, when a category is clicked.
 * Opens New Category fragment, when New Category button is pressed.
 * Opened from MoreFragment
 * @author Emelie Edberg, Eugene Dvoryankov
 */
public class ManageCategoriesFragment extends Fragment {

    private Button newCategoryButton;
    private ListView listView;
    private RadioGroup radioGroup;
    private ManageCategoriesListAdapter adapter;
    private ImageButton cross;
    private boolean isIncomeActivated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

        radioGroup = view.findViewById(R.id.manage_categories_radioGroup);
        listView = view.findViewById(R.id.listView_manage_categories);
        cross = view.findViewById(R.id.cross_manage_categories);
        adapter = new ManageCategoriesListAdapter(requireActivity().getApplicationContext(), getExpenseCategoriesWithoutOther());
        listView.setAdapter(adapter);
        initRadioGroup();

        newCategoryButton = view.findViewById(R.id.button_add_new_cat_in_manage_cat);
        initNewCategoryButtonOnClickListener();
        initClickOnListItem();
        initCross();

        return view;
    }

    private void initCross(){
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new MoreFragment()).commit();
            }
        });
    }

    private List<Category> getExpenseCategoriesWithoutOther(){
        List<Category> list = CategoryController.getInstance().getExpenseCategories();
        for (Category c : list){
            if (c.getName().equals("Other expense")){
                list.remove(c);
                break;
            }
        }
        return list;
    }

    private List<Category> getIncomeCategoriesWithoutOther(){
        List<Category> list = CategoryController.getInstance().getIncomeCategories();
        for (Category c : list){
            if (c.getName().equals("Other income")){
                list.remove(c);
                break;
            }
        }
        return list;
    }

    private void initClickOnListItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) adapterView.getItemAtPosition(i);

                Fragment fragment = new EditCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CHOSEN_CATEGORY_TO_EDIT", new ParcelableCategory(category));
                fragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
            }
        });
    }

    private void initNewCategoryButtonOnClickListener(){
        newCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NewCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_INCOME_IS_ACTIVATED", isIncomeActivated);
                fragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
            }
        });
    }

    private void initRadioGroup(){

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.manage_categories_radio_expense){
                    adapter = new ManageCategoriesListAdapter(requireActivity().getApplicationContext(), getExpenseCategoriesWithoutOther());
                    isIncomeActivated = false;
                } else {
                    adapter = new ManageCategoriesListAdapter(requireActivity().getApplicationContext(), getIncomeCategoriesWithoutOther());
                    isIncomeActivated = true;
                }
                listView.setAdapter(adapter);
            }
        });
    }


}