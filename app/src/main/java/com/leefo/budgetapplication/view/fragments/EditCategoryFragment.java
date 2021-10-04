package com.leefo.budgetapplication.view.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.SharedViewModel;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * The class that represents the fragment for editing an existing category
 */
public class EditCategoryFragment extends Fragment {

    private EditText nameInput;
    private Button saveButton;
    private Button deleteButton;
    private Button changeColorButton;
    private int defaultColor;
    private View view;
    private RadioGroup radioGroup;
    private Category oldCategory;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_category, container, false);

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        oldCategory = viewModel.singleCategory;
        // get views
        saveButton = view.findViewById(R.id.edit_category_save_button);
        deleteButton = view.findViewById(R.id.edit_category_delete_button);
        nameInput = view.findViewById(R.id.edit_category_name_input);
        changeColorButton = view.findViewById(R.id.edit_category_change_color_button);
        radioGroup = view.findViewById(R.id.edit_category_radio_group);

        // init
        setOldCategoryValues(oldCategory);
        initSaveButtonOnClickListener();
        initDeleteButtonOnClickListener();
        initChangeColorButtonOnClickListener();

        return view;
    }

    private void setOldCategoryValues(Category category){
        nameInput.setText(category.getName());
        defaultColor = Color.parseColor(category.getColor());
        if(oldCategory.isIncome()){
            radioGroup.check(R.id.edit_category_radio_income);
        } else {
            radioGroup.check(R.id.edit_category_radio_expense);
        }
    }

    private void initSaveButtonOnClickListener(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton();
            }
        });
    }

    private void initDeleteButtonOnClickListener(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to delete this category?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Controller.removeCategory(oldCategory);
                                ((MainActivity)getActivity()).openFragmentInMainFrameLayout(new ManageCategoriesFragment());
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }

    private void initChangeColorButtonOnClickListener(){
        changeColorButton.setBackgroundColor(defaultColor);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });
    }

    private void openColorPicker(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                changeColorButton.setBackgroundColor(defaultColor);
            }
        });
        ambilWarnaDialog.show();
    }

    private void saveButton(){
        if (nameInput.getText().toString().equals("")){
            makeToast("You need to enter a name");
            return;
        }
        editCategory();
        ((MainActivity)getActivity()).openFragmentInMainFrameLayout(new ManageCategoriesFragment());
    }

    private void editCategory(){
        boolean isIncome = radioGroup.getCheckedRadioButtonId() == R.id.edit_category_radio_income;
        String name = nameInput.getText().toString();
        String color = "#" + Integer.toHexString(defaultColor);
        if (!nameIsUnique(name)) {
            makeToast("OBS this Category name is already in use");
            return;
        }
        Controller.editCategoryInfo(oldCategory,name,color,isIncome);
    }

    /**
     * Checks if name in already used
     * @param name The name of the new category
     * @return Returns true if the name is available and false if not
     */
    private boolean nameIsUnique(String name) {
        for (Category c: Controller.getAllCategories()) {
            if (c.getName().equals(name)) return false;
        }
        return true;
    }



    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}