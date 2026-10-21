# Data Model - Smart Pantry Manager

## Tables

### 1. pantry_items
Stores the ingredients the user currently has.

| Column         | Type     | Description                          |
|----------------|----------|--------------------------------------|
| id             | INTEGER  | Primary key, auto-increment          |
| name           | TEXT     | Ingredient name (e.g. tomato)        |
| quantity       | REAL     | How much the user has                |
| unit           | TEXT     | Unit (e.g. pieces, grams, ml)        |
| expiry_date    | TEXT     | Optional expiry date (YYYY-MM-DD)    |

### 2. recipes
Stores the list of recipes (pre-seeded).

| Column     | Type    | Description                     |
|------------|---------|---------------------------------|
| id         | INTEGER | Primary key, auto-increment     |
| name       | TEXT    | Recipe name                     |
| steps      | TEXT    | Preparation steps               |

### 3. recipe_ingredients
Links recipes to the ingredients they require.

| Column            | Type    | Description                          |
|-------------------|---------|--------------------------------------|
| id                | INTEGER | Primary key, auto-increment          |
| recipe_id         | INTEGER | Foreign key → recipes.id             |
| ingredient_name   | TEXT    | Name of required ingredient          |
| quantity          | REAL    | How much is needed                   |
| unit              | TEXT    | Unit required                        |

## Notes
- Strict matching will check that every ingredient in recipe_ingredients exists in pantry_items with enough quantity.
- We will normalize names (lowercase + simple singular/plural handling) for better matching.