package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryHandler;
import com.leefo.budgetapplication.model.DataBaseManager;
import com.leefo.budgetapplication.model.Transaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize database
        Controller.InitializeBackend(this);



        // start app with displaying Home Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_main, new HomeFragment()).commit();

        // get views
        bottomNav = findViewById(R.id.bottomNavigation);

        // init components
        initBottomNavigationOnClick();

        // so we have some categories for testing
        if (Controller.getAllCategories().size() == 1){
            setDefaultCategories();
        }

        // color example, because i always forget how to write this
        TextView textView;
        //textView.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
        //textView.setBackgroundColor(Color.parseColor("#A0A0A0"));
    }

    private void removeAllCategories(){
        for (Category c : Controller.getAllCategories()){
            Controller.removeCategory(c.getId());
        }
    }

    private void removeAllTransactions(){
        for (Transaction t : Controller.getAllTransactions()){
            Controller.removeTransaction(t.getId());
        }
    }

    private void setDefaultCategories(){
        // Expenses, need an attribute for setting income/expense
        Controller.addNewCategory("Home", "#FF6464", false);
        Controller.addNewCategory("Food", "#64FF7D", false);
        Controller.addNewCategory("Transportation", "#64BEFF", false);
        Controller.addNewCategory("Clothes", "#FF64DD", false);
        Controller.addNewCategory("Entertainment", "#FFAE64", false);
        Controller.addNewCategory("Electronics", "#64FFEC",false);

        //Income
        Controller.addNewCategory("Salary", "#FCFF64", true);
        Controller.addNewCategory("Gift", "#6473FF", true);
    }



    public void openHomeFragment(View v){
        openFragmentInMainFrameLayout(new HomeFragment());
        bottomNav.setVisibility(View.VISIBLE);
    }

    private void openFragmentInMainFrameLayout(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
    }

    public void plusButton(View v){
        openFragmentInMainFrameLayout(new NewTransactionFragment());
        bottomNav.setVisibility(View.GONE);
    }

    public void openSingleCategortFragment(){
        openFragmentInMainFrameLayout(new SingleCategoryFragment());
    }

    private void initBottomNavigationOnClick(){
        bottomNav.setOnItemSelectedListener(item -> {

            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.nav_budget:
                    fragment = new BudgetFragment();
                    break;

                case R.id.nav_home:
                    fragment = new HomeFragment();
                    break;

                case R.id.nav_more:
                    fragment = new MoreFragment();
                    break;
            }

            openFragmentInMainFrameLayout(fragment);
            return true;
        });

    }

    //Method to make a Toast. Use to test
    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }


}