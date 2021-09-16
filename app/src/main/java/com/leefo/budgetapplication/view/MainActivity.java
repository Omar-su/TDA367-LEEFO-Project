package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

        //start app by displaying Home Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout_main, new HomeFragment()).commit();

        initBottomNavigation();

            // color example
            TextView textView;
            //textView.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
            //textView.setBackgroundColor(Color.parseColor("#A0A0A0"));



    }

    // Method that sets the OnItemSelectedListener on the bottomNavigation
    private void initBottomNavigation(){
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
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

    //Method to make a Toast. Use to test
    Toast t;
    private void makeToast(String s){
        if(t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }


}