package com.siphesihle.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Immediately go to the Pantry List screen
        Intent intent = new Intent(this, PantryListActivity.class);
        startActivity(intent);
        finish(); // Close MainActivity so user can't go back to it
    }
}