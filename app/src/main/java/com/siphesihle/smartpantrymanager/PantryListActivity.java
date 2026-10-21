package com.siphesihle.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PantryListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewPantry);
        btnAdd = findViewById(R.id.btnAddIngredient);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadPantryItems();

        btnAdd.setOnClickListener(v -> {
            // We will create the Add/Edit screen next
            // For now just show a message or leave empty
        });
    }

    private void loadPantryItems() {
        Cursor cursor = dbHelper.getAllPantryItems();
        adapter = new PantryAdapter(cursor, id -> {
            // Later we will open the edit screen here
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems(); // Refresh the list when coming back
    }
}