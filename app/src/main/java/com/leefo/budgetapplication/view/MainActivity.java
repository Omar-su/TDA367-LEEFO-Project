package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.view.fragments.BudgetFragment;
import com.leefo.budgetapplication.view.fragments.EditTransactionFragment;
import com.leefo.budgetapplication.view.fragments.HomeFragment;
import com.leefo.budgetapplication.view.fragments.ManageCategoriesFragment;
import com.leefo.budgetapplication.view.fragments.MoreFragment;
import com.leefo.budgetapplication.view.fragments.NewTransactionFragment;

import java.time.LocalDate;

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


            // color example, because i always forget how to write this
            TextView textView;
            //textView.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
            //textView.setBackgroundColor(Color.parseColor("#A0A0A0"));

        SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        viewModel.setTimePeriod(new TimePeriod(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));

        viewModel.lastOpenedViewWasCategoryView = true;
        viewModel.mainActivityContext = getApplicationContext();
    }


    public void openHomeFragment(View v){
        openFragmentInMainFrameLayout(new HomeFragment());
        bottomNav.setVisibility(View.VISIBLE);
    }

    public void openManageCatgeries(View v){
        openFragmentInMainFrameLayout(new ManageCategoriesFragment());
    }

    public void openFragmentInMainFrameLayout(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
    }

    public void plusButton(View v){
        openFragmentInMainFrameLayout(new NewTransactionFragment());
        bottomNav.setVisibility(View.GONE);
    }

    public void openEditTransactionFragment(){
        openFragmentInMainFrameLayout(new EditTransactionFragment());
        bottomNav.setVisibility(View.GONE);
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

}