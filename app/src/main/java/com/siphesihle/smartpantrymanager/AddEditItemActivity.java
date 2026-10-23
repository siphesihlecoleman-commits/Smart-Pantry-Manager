package com.siphesihle.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etUnit, etExpiry;
    private Button btnSave, btnDelete;
    private DatabaseHelper dbHelper;
    private long itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        if (getIntent().hasExtra("ITEM_ID")) {
            itemId = getIntent().getLongExtra("ITEM_ID", -1);
            loadItem(itemId);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnSave.setOnClickListener(v -> saveItem());
        btnDelete.setOnClickListener(v -> {
            dbHelper.deletePantryItem(itemId);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadItem(long id) {
        Cursor c = dbHelper.getPantryItemById(id);
        if (c != null && c.moveToFirst()) {
            etName.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)));
            etQuantity.setText(String.valueOf(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY))));
            etUnit.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)));
            String exp = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_EXPIRY));
            if (exp != null) etExpiry.setText(exp);
            c.close();
        }
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        if (name.isEmpty()) { etName.setError("Required"); return; }
        if (qtyStr.isEmpty()) { etQuantity.setError("Required"); return; }
        if (unit.isEmpty()) { etUnit.setError("Required"); return; }

        double qty;
        try { qty = Double.parseDouble(qtyStr); }
        catch (Exception e) { etQuantity.setError("Invalid number"); return; }

        if (itemId == -1) {
            dbHelper.addPantryItem(name, qty, unit, expiry.isEmpty() ? null : expiry);
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updatePantryItem(itemId, name, qty, unit, expiry.isEmpty() ? null : expiry);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}