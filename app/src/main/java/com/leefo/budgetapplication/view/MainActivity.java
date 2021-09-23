package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    static private ArrayList<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start app by displaying Home Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_main, new HomeFragment()).commit();

        // initialize database
        Controller.InitializeBackend(this);


        categories = Controller.getAllCategories();



        bottomNav = findViewById(R.id.bottomNavigation);


        initBottomNavigation();



        // color example
            TextView textView;
            //textView.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
            //textView.setBackgroundColor(Color.parseColor("#A0A0A0"));

    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    static public Category getCategoryFromId(int id){
        for (Category c : categories){
            if (c.getId() == id){
                return c;
            }
        }
        return new Category(0, "Other", "#C4C4C4");
    }


    public void closeNewtransactionFragment(View v){
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

    // Method that sets the OnItemSelectedListener on the bottomNavigation
    private void initBottomNavigation(){
        bottomNav.setOnItemSelectedListener(item -> {

            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.nav_budget:
                    fragment = new BudgetFragment();
                    makeToast(categories.toString());
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