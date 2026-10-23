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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnAddIngredient).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditIngredientActivity.class)));

        findViewById(R.id.btnSuggested).setOnClickListener(v ->
                startActivity(new Intent(this, SuggestedRecipesActivity.class)));

        findViewById(R.id.btnSettings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void loadItems() {
        Cursor cursor = dbHelper.getAllPantryItems();
        if (adapter == null) {
            adapter = new PantryAdapter(cursor, id -> {
                Intent i = new Intent(this, AddEditIngredientActivity.class);
                i.putExtra("ITEM_ID", id);
                startActivity(i);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }
}