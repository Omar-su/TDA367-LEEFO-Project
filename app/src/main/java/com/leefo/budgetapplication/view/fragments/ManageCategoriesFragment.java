package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.ParcelableCategory;
import com.leefo.budgetapplication.view.SharedTimePeriodViewModel;
import com.leefo.budgetapplication.view.adapters.ManageCategoriesListAdapter;

import java.util.ArrayList;


public class ManageCategoriesFragment extends Fragment {

    private Button newCategoryButton;
    private ListView listView;
    private RadioGroup radioGroup;
    private ManageCategoriesListAdapter adapter;
    private SharedTimePeriodViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

         viewModel = new ViewModelProvider(requireActivity()).get(SharedTimePeriodViewModel.class);

        radioGroup = view.findViewById(R.id.manage_categories_radioGroup);
        listView = view.findViewById(R.id.listView_manage_categories);
        adapter = new ManageCategoriesListAdapter(getActivity().getApplicationContext(), getExpenseCategoriesWithoutOther());
        listView.setAdapter(adapter);
        initRadioGroup();

        newCategoryButton = view.findViewById(R.id.button_add_new_cat_in_manage_cat);
        initNewCategoryButtonOnClickListener();
        initClickOnListItem();
        return view;
    }

    private ArrayList<Category> getExpenseCategoriesWithoutOther(){
        ArrayList<Category> list = Controller.getExpenseCategories();
        for (Category c : list){
            if (c.getName().equals("Other expense")){
                list.remove(c);
                break;
            }
        }
        return list;
    }

    private ArrayList<Category> getIncomeCategoriesWithoutOther(){
        ArrayList<Category> list = Controller.getIncomeCategories();
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
                ((MainActivity)getActivity()).openFragmentInMainFrameLayout(fragment);
            }
        });
    }

    private void initNewCategoryButtonOnClickListener(){
        newCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openFragmentInMainFrameLayout(new NewCategoryFragment());
            }
        });
    }

    private void initRadioGroup(){

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.manage_categories_radio_expense){
                    adapter = new ManageCategoriesListAdapter(getActivity().getApplicationContext(), getExpenseCategoriesWithoutOther());
                } else {
                    adapter = new ManageCategoriesListAdapter(getActivity().getApplicationContext(), getIncomeCategoriesWithoutOther());
                }
                listView.setAdapter(adapter);
            }
        });
    }


}