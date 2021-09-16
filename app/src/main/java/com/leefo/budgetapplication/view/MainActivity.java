package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.Intent;
import android.view.View;

import com.leefo.budgetapplication.R;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnToNewTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToNewTransaction = (ImageButton) findViewById(R.id.button_new_transaction);

        btnToNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
                startActivity(intent);
            }
        });

    }
}