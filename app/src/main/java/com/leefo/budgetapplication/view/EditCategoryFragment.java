package com.leefo.budgetapplication.view;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * The class that represents the fragment for editing an existing category
 */
public class EditCategoryFragment extends Fragment {

    private EditText nameInput;
    private Button saveButton;
    private Button changeColorButton;
    private int defaultColor;
    private Spinner categorySpinner;
    private View view;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_category, container, false);

        //get views
        saveButton = view.findViewById(R.id.edit_category_save_button);
        nameInput = view.findViewById(R.id.edit_category_name_input);
        changeColorButton = view.findViewById(R.id.edit_category_change_color_button);
        defaultColor = ContextCompat.getColor(getContext(), R.color.design_default_color_primary);
        categorySpinner = view.findViewById(R.id.edit_category_name_spinner);

        //init category spinner
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), Controller.getAllCategories());
        categorySpinner.setAdapter(spinnerAdapter);

        // init save button onClick
        initSaveButtonOnClickListener();
        initChangeColorButtonOnClickListener();

        return view;
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
        editCategory();
        ((MainActivity)getActivity()).openHomeFragment(view);
    }

    private void editCategory(){
        Category cat = (Category) categorySpinner.getSelectedItem();
        int id = cat.getId();
        String name = nameInput.getText().toString();
        String color = "#" + Integer.toHexString(defaultColor);
        Controller.editCategoryInfo(id, name, color);
    }

    //Method to make a Toast. Use to test
    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}