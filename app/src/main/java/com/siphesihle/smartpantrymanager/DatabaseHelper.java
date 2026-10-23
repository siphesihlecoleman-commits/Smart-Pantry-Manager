package com.siphesihle.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";
    public static final String COL_STEPS = "steps";
    public static final String COL_RECIPE_ID = "recipe_id";
    public static final String COL_INGREDIENT_NAME = "ingredient_name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL, " +
                COL_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_STEPS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_INGREDIENT_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL)");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    // ===== PANTRY CRUD =====
    public long addPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name.toLowerCase().trim());
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit.toLowerCase().trim());
        values.put(COL_EXPIRY, expiryDate);
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " ORDER BY " + COL_NAME, null);
    }

    public int updatePantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name.toLowerCase().trim());
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit.toLowerCase().trim());
        values.put(COL_EXPIRY, expiryDate);
        int rows = db.update(TABLE_PANTRY, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public void deletePantryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getPantryItemById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " WHERE " + COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // ===== STRICT MATCHING =====
    public Cursor getSuggestedRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r." + COL_ID + ", r." + COL_NAME + ", r." + COL_STEPS +
                " FROM " + TABLE_RECIPES + " r " +
                "WHERE NOT EXISTS (" +
                "  SELECT 1 FROM " + TABLE_RECIPE_INGREDIENTS + " ri " +
                "  WHERE ri." + COL_RECIPE_ID + " = r." + COL_ID +
                "  AND NOT EXISTS (" +
                "    SELECT 1 FROM " + TABLE_PANTRY + " p " +
                "    WHERE LOWER(p." + COL_NAME + ") = LOWER(ri." + COL_INGREDIENT_NAME + ") " +
                "    AND p." + COL_QUANTITY + " >= ri." + COL_QUANTITY +
                "  )" +
                ")";
        return db.rawQuery(query, null);
    }

    public Cursor getRecipeById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " WHERE " + COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getIngredientsForRecipe(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPE_INGREDIENTS + " WHERE " + COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)});
    }

    // ===== SEED 20 RECIPES =====
    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Tomato Egg Scramble", "1. Beat eggs\n2. Fry tomatoes\n3. Add eggs and scramble",
                new String[]{"egg", "tomato"}, new double[]{2, 1}, new String[]{"pieces", "pieces"});
        insertRecipe(db, "Simple Pasta", "1. Boil pasta\n2. Heat oil and garlic\n3. Mix together",
                new String[]{"pasta", "garlic", "oil"}, new double[]{200, 2, 1}, new String[]{"grams", "cloves", "tbsp"});
        insertRecipe(db, "Fried Rice", "1. Cook rice\n2. Fry with vegetables and egg\n3. Season",
                new String[]{"rice", "egg", "onion"}, new double[]{1, 1, 0.5}, new String[]{"cups", "pieces", "pieces"});
        insertRecipe(db, "Cheese Omelette", "1. Beat eggs\n2. Add cheese\n3. Cook in pan",
                new String[]{"egg", "cheese"}, new double[]{3, 50}, new String[]{"pieces", "grams"});
        insertRecipe(db, "Garlic Bread", "1. Slice bread\n2. Mix butter and garlic\n3. Toast",
                new String[]{"bread", "butter", "garlic"}, new double[]{4, 30, 2}, new String[]{"slices", "grams", "cloves"});
        insertRecipe(db, "Banana Smoothie", "1. Peel banana\n2. Blend with milk\n3. Serve cold",
                new String[]{"banana", "milk"}, new double[]{2, 200}, new String[]{"pieces", "ml"});
        insertRecipe(db, "Potato Fries", "1. Cut potatoes\n2. Fry until golden\n3. Add salt",
                new String[]{"potato", "oil", "salt"}, new double[]{3, 2, 1}, new String[]{"pieces", "tbsp", "tsp"});
        insertRecipe(db, "Chicken Stir Fry", "1. Cut chicken\n2. Stir fry with vegetables\n3. Serve with rice",
                new String[]{"chicken", "onion", "rice"}, new double[]{200, 1, 1}, new String[]{"grams", "pieces", "cups"});
        insertRecipe(db, "Avocado Toast", "1. Toast bread\n2. Mash avocado\n3. Spread and season",
                new String[]{"bread", "avocado", "salt"}, new double[]{2, 1, 1}, new String[]{"slices", "pieces", "tsp"});
        insertRecipe(db, "Simple Salad", "1. Chop tomato and cucumber\n2. Add onion\n3. Dress with oil",
                new String[]{"tomato", "cucumber", "onion", "oil"}, new double[]{2, 1, 0.5, 1}, new String[]{"pieces", "pieces", "pieces", "tbsp"});
        insertRecipe(db, "Boiled Eggs", "1. Place eggs in water\n2. Boil 8 minutes\n3. Peel and serve",
                new String[]{"egg"}, new double[]{4}, new String[]{"pieces"});
        insertRecipe(db, "Milk Porridge", "1. Heat milk\n2. Add oats\n3. Cook until thick",
                new String[]{"milk", "oats"}, new double[]{300, 50}, new String[]{"ml", "grams"});
        insertRecipe(db, "Butter Toast", "1. Toast bread\n2. Spread butter\n3. Optional jam",
                new String[]{"bread", "butter"}, new double[]{2, 20}, new String[]{"slices", "grams"});
        insertRecipe(db, "Onion Soup", "1. Slice onions\n2. Cook slowly\n3. Add water and simmer",
                new String[]{"onion", "butter", "salt"}, new double[]{3, 20, 1}, new String[]{"pieces", "grams", "tsp"});
        insertRecipe(db, "Egg Fried Rice", "1. Cook rice\n2. Scramble egg\n3. Mix with rice",
                new String[]{"rice", "egg", "oil"}, new double[]{1, 2, 1}, new String[]{"cups", "pieces", "tbsp"});
        insertRecipe(db, "Tomato Sandwich", "1. Slice bread and tomato\n2. Assemble",
                new String[]{"bread", "tomato"}, new double[]{2, 1}, new String[]{"slices", "pieces"});
        insertRecipe(db, "Garlic Potatoes", "1. Cut potatoes\n2. Fry with garlic\n3. Season",
                new String[]{"potato", "garlic", "oil"}, new double[]{3, 3, 2}, new String[]{"pieces", "cloves", "tbsp"});
        insertRecipe(db, "Banana Pancakes", "1. Mash banana\n2. Mix with egg and flour\n3. Fry",
                new String[]{"banana", "egg", "flour"}, new double[]{1, 1, 50}, new String[]{"pieces", "pieces", "grams"});
        insertRecipe(db, "Cheese Toastie", "1. Butter bread\n2. Add cheese\n3. Toast",
                new String[]{"bread", "cheese", "butter"}, new double[]{2, 40, 15}, new String[]{"slices", "grams", "grams"});
        insertRecipe(db, "Vegetable Stir Fry", "1. Chop vegetables\n2. Stir fry in oil\n3. Season",
                new String[]{"onion", "tomato", "carrot", "oil"}, new double[]{1, 1, 1, 1}, new String[]{"pieces", "pieces", "pieces", "tbsp"});
    }

    private void insertRecipe(SQLiteDatabase db, String name, String steps,
                              String[] ingredients, double[] quantities, String[] units) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_NAME, name);
        recipeValues.put(COL_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (int i = 0; i < ingredients.length; i++) {
            ContentValues ing = new ContentValues();
            ing.put(COL_RECIPE_ID, recipeId);
            ing.put(COL_INGREDIENT_NAME, ingredients[i].toLowerCase());
            ing.put(COL_QUANTITY, quantities[i]);
            ing.put(COL_UNIT, units[i].toLowerCase());
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ing);
        }
    }
}