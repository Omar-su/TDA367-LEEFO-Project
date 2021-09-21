package com.leefo.budgetapplication.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.model.CategoryFake;
import com.leefo.budgetapplication.view.adapters.SpinnerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * The class that represents the fragment for adding a new transaction
 */
public class NewTransactionFragment extends Fragment {

    private EditText amount, description, edittext_date;
    private Spinner spinner;
    final Calendar myCalendar = Calendar.getInstance();


    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_transaction, container, false);


        ArrayList<CategoryFake> categories = new ArrayList<>();
        categories.add(new CategoryFake("Övrigt", "#8A9094"));
        categories.add(new CategoryFake("Mat", "#558DF9")); // TODO replace hardcoded data
        categories.add(new CategoryFake("Musik", "#F95555"));
        categories.add(new CategoryFake("Danslektioner", "#55F979"));

        spinner = (Spinner)view.findViewById(R.id.spinner_categoty);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), categories);
        spinner.setAdapter(spinnerAdapter);


        edittext_date = view.findViewById(R.id.editTextDate);
        initDatePickerDialog();

        return view;
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

        edittext_date.setOnClickListener(new View.OnClickListener() {
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
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        edittext_date.setText(sdf.format(myCalendar.getTime()));
    }


}