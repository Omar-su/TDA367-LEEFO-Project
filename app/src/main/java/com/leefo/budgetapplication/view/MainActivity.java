package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.view.fragments.BudgetFragment;
import com.leefo.budgetapplication.view.fragments.EditTransactionFragment;
import com.leefo.budgetapplication.view.fragments.HomeFragment;
import com.leefo.budgetapplication.view.fragments.ManageCategoriesFragment;
import com.leefo.budgetapplication.view.fragments.MoreFragment;
import com.leefo.budgetapplication.view.fragments.NewTransactionFragment;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ImageButton plusButton;


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
        plusButton = findViewById(R.id.plusButton);

        // init components
        initBottomNavigationOnClick();
        initPlusButton();

    }


    public void initPlusButton(){
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, new NewTransactionFragment()).commit();
                bottomNav.setVisibility(View.GONE);
            }
        });
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
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
            return true;
        });

    }

}