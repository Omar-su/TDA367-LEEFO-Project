package com.leefo.budgetapplication.view.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.MainActivity;
import com.leefo.budgetapplication.view.SharedViewData;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * The class that represents the fragment for editing an existing transaction
 */
public class EditTransactionFragment extends Fragment {

    private EditText amountInput, noteInput, dateInput;
    private Button saveButton;
    private Spinner categorySpinner;
    private RadioGroup radioGroup;

    final Calendar myCalendar = Calendar.getInstance();
    private View view;
    private FinancialTransaction oldTransaction;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_transaction, container, false);

        oldTransaction = SharedViewData.singleTransaction;

        // get views
        categorySpinner = view.findViewById(R.id.edit_transaction_spinner_category);
        amountInput = view.findViewById(R.id.edit_transaction_amount_input);
        noteInput = view.findViewById(R.id.edit_transaction_note_input);
        dateInput = view.findViewById(R.id.edit_transaction_dateInput);
        saveButton = view.findViewById(R.id.edit_transaction_save_button);
        radioGroup = view.findViewById(R.id.edit_transaction_radioGroup);

        // init category spinner
        initSpinner();

        // init date picker
        initDatePickerDialog();

        // init save button onClick
        initSaveButtonOnClickListener();

        return view;
    }

    private void initSpinner(){
        ArrayList<Category> income, expense;
        income = new ArrayList<>();
        expense = new ArrayList<>();
        for (Category c : Controller.getAllCategories()){
            if (c.isIncome()){
                income.add(c);
            } else {
                expense.add(c);
            }
        }
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), expense);
        categorySpinner.setAdapter(spinnerAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioExpense){
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), expense);
                    categorySpinner.setAdapter(spinnerAdapter);
                } else {
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), income);
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
        editTransaction();
        ((MainActivity)getActivity()).openHomeFragment(view);

    }

    private void editTransaction(){
        boolean isExpense = radioGroup.getCheckedRadioButtonId() == R.id.radioExpense;
        String description = noteInput.getText().toString();
        float amount = Float.parseFloat(amountInput.getText().toString());
        Category category = (Category) categorySpinner.getSelectedItem();
        LocalDate date = myCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // convert Date to LocalDate

        if (isExpense){
            amount = amount * -1;
        }
        Controller.editTransaction(oldTransaction, amount, description, date, category );
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

    //Method to make a Toast. Use to test
    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

}