package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

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
    }


}