package com.siphesihle.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    // Common columns
    public static final String COL_ID = "id";

    // Pantry columns
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    // Recipe columns
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_STEPS = "steps";

    // Recipe ingredients columns
    public static final String COL_RECIPE_ID = "recipe_id";
    public static final String COL_INGREDIENT_NAME = "ingredient_name";
    public static final String COL_REQ_QUANTITY = "quantity";
    public static final String COL_REQ_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create pantry_items table
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL, " +
                COL_EXPIRY + " TEXT)";
        db.execSQL(createPantry);

        // Create recipes table
        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_STEPS + " TEXT NOT NULL)";
        db.execSQL(createRecipes);

        // Create recipe_ingredients table
        String createRecipeIngredients = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_INGREDIENT_NAME + " TEXT NOT NULL, " +
                COL_REQ_QUANTITY + " REAL NOT NULL, " +
                COL_REQ_UNIT + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_ID + "))";
        db.execSQL(createRecipeIngredients);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    // ========== PANTRY CRUD METHODS ==========

    // CREATE - Add a new pantry item
    public long addPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiryDate);
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    // READ - Get all pantry items
    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " ORDER BY " + COL_NAME, null);
    }

    // UPDATE - Update an existing pantry item
    public int updatePantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiryDate);
        int rows = db.update(TABLE_PANTRY, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // DELETE - Delete a pantry item
    public void deletePantryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Get a single pantry item by ID
    public Cursor getPantryItemById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " WHERE " + COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}