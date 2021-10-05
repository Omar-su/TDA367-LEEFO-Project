package com.leefo.budgetapplication.view.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * The class that represents the fragment for adding a new transaction
 */
public class NewTransactionFragment extends Fragment {

    private EditText amountInput, descriptionInput, dateInput;
    private Button saveButton;
    private Spinner categorySpinner;
    private RadioGroup radioGroup;
    private ImageButton cross;
    private BottomNavigationView bottomNav;

    final Calendar myCalendar = Calendar.getInstance();
    private View view;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_transaction, container, false);

        // get views
        categorySpinner = view.findViewById(R.id.spinner_category);
        amountInput = view.findViewById(R.id.edit_category_name_input);
        descriptionInput = view.findViewById(R.id.descriptionInput);
        dateInput = view.findViewById(R.id.dateInput);
        saveButton = view.findViewById(R.id.edit_category_save_button);
        radioGroup = view.findViewById(R.id.radioGroup);
        cross = view.findViewById(R.id.cross_new_transaction);
        bottomNav = requireActivity().findViewById(R.id.bottomNavigation);

        // init
        initSpinner();
        initDatePickerDialog();
        initSaveButtonOnClickListener();
        initCross();

        return view;
    }

    private void initCross(){
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new HomeFragment()).commit();
                bottomNav.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initSpinner(){
        ArrayList<Category> income, expense;
        income = Controller.getIncomeCategories();
        expense = Controller.getExpenseCategories();
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), expense);
        categorySpinner.setAdapter(spinnerAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioExpense){
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), expense);
                    categorySpinner.setAdapter(spinnerAdapter);
                } else {
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), income);
                    categorySpinner.setAdapter(spinnerAdapter);
                }
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



    private void saveButton(){
        if (amountInput.getText().toString().equals("")){
            makeToast("You need to enter an amount");
            return;
        }
        addTransaction();
        bottomNav.setVisibility(View.VISIBLE);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new HomeFragment()).commit();
    }

    private void addTransaction(){
        boolean isExpense = radioGroup.getCheckedRadioButtonId() == R.id.radioExpense;
        String description = descriptionInput.getText().toString();
        float amount = Float.parseFloat(amountInput.getText().toString());
        Category category = (Category) categorySpinner.getSelectedItem();
        LocalDate date = myCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // convert Date to LocalDate

        if (isExpense){
            amount = amount * -1;
        }

        Controller.addNewTransaction(amount, description, date, category);
    }

    /**
     * Initializes Calendar object myCalander and sets onClickListener for the edittext edittext_date
     * in which a DatePickerDialog is created using myCalendar.
     */
    private void initDatePickerDialog(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        updateDateLabel();

    }

    /**
     * Updates the edittext edittext_date with the date from the myCalendar chosen by the datePickerDialog.
     */
    private void updateDateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        dateInput.setText(sdf.format(myCalendar.getTime()));
    }

    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

}