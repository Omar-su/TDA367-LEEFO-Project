package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;

public class EditBudgetFragment extends Fragment {

    ListView editBudgetLV;
    Button saveBudgetButton;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_budget, container, false);

        saveBudgetButton = view.findViewById(R.id.saveButtonBudget);
        editBudgetLV = view.findViewById(R.id.editBudgetListV);

        initSaveBudgetOnClickListener();
        return view;
    }



    private void initSaveBudgetOnClickListener() {
        saveBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBudget();

            }
        });
    }

    private void editBudget(){
//        if (budget.getText().toString().equals("")){
//            makeToast("You need to enter an amount");
//            return;
//        }
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new BudgetFragment()).commit();
    }


    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(requireActivity().getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}
