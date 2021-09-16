package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leefo.budgetapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // color example
        TextView textView;
        //textView.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
        //textView.setBackgroundColor(Color.parseColor("#A0A0A0"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {

            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.nav_budget:
                    makeToast("Budget was clicked");
                    fragment = new BudgetFragment();
                    break;

                case R.id.nav_home:
                    makeToast("Home was clicked");
                    fragment = new BudgetFragment();
                    break;

                case R.id.nav_more:
                    makeToast("More was clicked");
                    fragment = new BudgetFragment();
                    break;
            }

            fragmentManager.beginTransaction().add(R.id.FrameLayout_large, fragment).commit();

            return true;
        });

    }

    //Use to test
    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }


}