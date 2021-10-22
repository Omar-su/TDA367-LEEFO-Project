package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.CategoryController;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.data.ParcelableCategory;

import java.util.MissingResourceException;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * This class represents the fragment for adding a new category.
 * Used by (opened from) ManageCategoriesFragment. Uses (opens) ManageCategoriesFragment.
 * Uses AmbilWarnaDialog for color picking.
 * @author Emelie Edberg, Eugene Dvoryankov, Omar Suliman
 */
public class NewCategoryFragment extends Fragment {

    private EditText nameInput;
    private Button saveButton;
    private Button changeColorButton;
    private int defaultColor;
    private RadioGroup radioGroup;
    private ImageButton cross;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);


        // get argument passed with the fragment
        Bundle bundle = this.getArguments();
        boolean isIncomeActivated;
        if (bundle != null){
            isIncomeActivated = bundle.getBoolean("IS_INCOME_IS_ACTIVATED");
        } else {
            throw new MissingResourceException("No argument was sent with the fragment, hence fragment cannot be created", ParcelableCategory.class.toString(), "IS_INCOME_IS_ACTIVATED" );
        }
        // get views
        saveButton = view.findViewById(R.id.new_category_save_button);
        nameInput = view.findViewById(R.id.new_category_name_input);
        changeColorButton = view.findViewById(R.id.new_category_change_color_button);
        radioGroup = view.findViewById(R.id.new_category_radio_group);
        cross = view.findViewById(R.id.cross_new_category);

        defaultColor = ContextCompat.getColor(getContext(), R.color.design_default_color_primary);

        if (isIncomeActivated){
            radioGroup.check(R.id.new_category_radio_income);
        }

        // init onClick
        initSaveButtonOnClickListener();
        initChangeColorButtonOnClickListener();
        initCross();

        return view;
    }

    private void initCross(){
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new ManageCategoriesFragment()).commit();
            }
        });
    }

    private void initSaveButtonOnClickListener(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton();
            }
        });
    }

    private void initChangeColorButtonOnClickListener(){
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
        String name = nameInput.getText().toString();
        if (!nameIsUnique(name)) {
            makeToast("OBS this Category name is already in use");
            return;
        }
        addCategory();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new ManageCategoriesFragment()).commit();
    }

    private void addCategory(){
        boolean isIncome = radioGroup.getCheckedRadioButtonId() == R.id.new_category_radio_income;
        String name = nameInput.getText().toString();
        String color = "#" + Integer.toHexString(defaultColor);
        CategoryController.getInstance().addNewCategory(name, color, isIncome);
    }

    /**
     * Checks if name in already used
     * @param name The name of the new category
     * @return Returns true if the name is available and false if not
     */
    private boolean nameIsUnique(String name) {
        for (Category c: CategoryController.getInstance().getAllCategories()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        return true;
    }

    Toast t;
    private void makeToast(String s){
        if(t != null) {
            t.cancel();
        }
        t = Toast.makeText(requireActivity().getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }


}