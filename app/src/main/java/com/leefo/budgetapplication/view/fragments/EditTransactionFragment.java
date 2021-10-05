package com.leefo.budgetapplication.view.fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.leefo.budgetapplication.view.ParcelableTransaction;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * The class that represents the fragment for editing an existing transaction
 */
public class EditTransactionFragment extends Fragment {

    private EditText amountInput, noteInput, dateInput;
    private Button saveButton;
    private Button deleteButton;
    private Spinner categorySpinner;
    private RadioGroup radioGroup;

    final Calendar myCalendar = Calendar.getInstance();
    private View view;
    private FinancialTransaction oldTransaction;

    ArrayList<Category> income, expense;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_transaction, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            ParcelableTransaction chosen_transaction = bundle.getParcelable("CHOSEN_TRANSACTION");
            oldTransaction = chosen_transaction.financialTransaction;
        } else {
            throw new MissingResourceException("No chosen transaction was sent with the fragment, hence fragment cannot be created", ParcelableTransaction.class.toString(), "CHOSEN_TRANSACTION" );
        }

        // get views
        categorySpinner = view.findViewById(R.id.edit_transaction_spinner_category);
        amountInput = view.findViewById(R.id.edit_transaction_amount_input);
        noteInput = view.findViewById(R.id.edit_transaction_note_input);
        dateInput = view.findViewById(R.id.edit_transaction_dateInput);
        saveButton = view.findViewById(R.id.edit_transaction_save_button);
        deleteButton = view.findViewById(R.id.edit_transaction_delete_button);
        radioGroup = view.findViewById(R.id.edit_transaction_radioGroup);

        income = Controller.getIncomeCategories();
        expense = Controller.getExpenseCategories();

        // init
        initSpinner();
        initDatePickerDialog();
        setOldTransactionValues(oldTransaction);
        initSaveButtonOnClickListener();
        initDeleteButtonOnClickListener();

        return view;
    }

    private void setOldTransactionValues(FinancialTransaction transaction){
        noteInput.setText(transaction.getDescription());
        amountInput.setText(Double.toString(Math.abs(transaction.getAmount())));
        Category oldCategory = transaction.getCategory();
        dateInput.setText(oldTransaction.getDate().toString());
        if(oldCategory.isIncome()){
            radioGroup.check(R.id.edit_transaction_radioIncome);
        } else {
            radioGroup.check(R.id.edit_transaction_radioExpense);
        }

        SpinnerAdapter spinnerAdapter;
        if (oldCategory.isIncome()){
            ArrayList<Category> newIncomeList = new ArrayList<>(income);
            newIncomeList.remove(oldCategory);
            newIncomeList.add(0, oldCategory);
            spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), newIncomeList);

        } else {
            ArrayList<Category> newExpenseList = new ArrayList<>(expense);
            newExpenseList.remove(oldCategory);
            newExpenseList.add(0, oldCategory);
            spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), newExpenseList);
        }
        categorySpinner.setAdapter(spinnerAdapter);

    }

    private void initSpinner(){
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), expense);
        categorySpinner.setAdapter(spinnerAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.edit_transaction_radioExpense){
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

    private void initDeleteButtonOnClickListener(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to delete this transaction?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Controller.removeTransaction(oldTransaction);
                                ((MainActivity)getActivity()).openHomeFragment(view);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }

    private void editTransaction(){
        String description = noteInput.getText().toString();
        float amount = Float.parseFloat(amountInput.getText().toString());
        Category category = (Category) categorySpinner.getSelectedItem();
        LocalDate date = myCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // convert Date to LocalDate

        if (!category.isIncome()){
            amount = Math.abs(amount) * -1;
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