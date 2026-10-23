package com.siphesihle.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewRecipes);
        tvEmpty = findViewById(R.id.tvEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        load();
    }

    private void load() {
        Cursor c = dbHelper.getSuggestedRecipes();
        if (c.getCount() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new Adapter(c));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.VH> {
        Cursor cursor;
        Adapter(Cursor c) { cursor = c; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            cursor.moveToPosition(pos);
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
            h.tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)));
            h.itemView.setOnClickListener(v -> {
                Intent i = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
                i.putExtra("RECIPE_ID", id);
                startActivity(i);
            });
        }

        @Override
        public int getItemCount() { return cursor.getCount(); }

        class VH extends RecyclerView.ViewHolder {
            TextView tv;
            VH(View v) { super(v); tv = v.findViewById(R.id.tvRecipeName); }
        }
    }
}