package com.example.fesenkoexamen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button furnitureButton = findViewById(R.id.button);
        Button ordersButton = findViewById(R.id.button2);

        furnitureButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BranchActivity.class);
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorkersActivity.class);
            startActivity(intent);
        });
    }
}