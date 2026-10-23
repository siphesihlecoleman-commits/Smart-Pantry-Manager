package com.siphesihle.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        long id = getIntent().getLongExtra("RECIPE_ID", -1);
        DatabaseHelper db = new DatabaseHelper(this);

        TextView tvTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvIng = findViewById(R.id.tvIngredients);
        TextView tvSteps = findViewById(R.id.tvSteps);

        Cursor r = db.getRecipeById(id);
        if (r.moveToFirst()) {
            tvTitle.setText(r.getString(r.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)));
            tvSteps.setText(r.getString(r.getColumnIndexOrThrow(DatabaseHelper.COL_STEPS)));
        }
        r.close();

        Cursor ings = db.getIngredientsForRecipe(id);
        StringBuilder sb = new StringBuilder();
        while (ings.moveToNext()) {
            sb.append("• ")
                    .append(ings.getString(ings.getColumnIndexOrThrow(DatabaseHelper.COL_INGREDIENT_NAME)))
                    .append(" - ")
                    .append(ings.getDouble(ings.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)))
                    .append(" ")
                    .append(ings.getString(ings.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)))
                    .append("\n");
        }
        ings.close();
        tvIng.setText(sb.toString());
    }
}