# Contact App (Android / Kotlin)

A native Android contact manager built with Kotlin, XML layouts, RecyclerView, Material Components, and SQLite local storage.

## Features
- Create, view, edit, and delete contacts
- Search/filter by name, phone number, or email as the user types
- SQLite persistence across app restarts
- RecyclerView adapter and ViewHolder pattern
- Input validation and delete confirmation
- Empty and no-search-results states

## Open and run
1. Open this folder in Android Studio (recent version).
2. Allow Gradle to sync and download dependencies.
3. Run the `app` configuration on an emulator or Android device (min SDK 23).

## Project structure
- `Contact.kt` — contact data model
- `ContactDbHelper.kt` — SQLite CRUD operations
- `ContactAdapter.kt` — RecyclerView adapter and click listeners
- `MainActivity.kt` — contact list, filtering, view and delete actions
- `AddEditContactActivity.kt` — create/edit form and validation
- `res/layout/` — XML screen and row layouts

## Notes
This starter uses SQLiteOpenHelper to keep the project self-contained and easy to study. `onUpgrade` currently recreates the table; if you later change the schema and need to preserve existing user data, implement versioned migrations instead.
