# Smart Pantry Manager

Java Android application that tracks pantry ingredients and suggests recipes using **strict matching** only (every required ingredient must be present in sufficient quantity).

## Database Choice
SQLite (local on-device using SQLiteOpenHelper) – chosen because it matches the module content, requires no external accounts, and fully supports the required CRUD + persistence.

## Features
- Pantry management (Add, Edit, Delete ingredients)
- Pre-seeded collection of 15–20 recipes
- Strict recipe matching (only shows recipes the user can make right now)
- Suggested Recipes screen
- Recipe Detail screen
- Settings screen

## Setup Instructions
1. Clone the repository
2. Open the project in Android Studio
3. Let Gradle sync
4. Run on an emulator or physical device (API 24+)

## Author
Siphesihle Coleman  
Student Number: 402307146  
Module: Mobile App Development 700