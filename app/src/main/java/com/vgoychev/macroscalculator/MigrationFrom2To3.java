package com.vgoychev.macroscalculator;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrationFrom2To3 extends Migration {
    public MigrationFrom2To3() {
        super(2, 3);
    }

    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // Add your SQL statements to update the schema here.
        // For example, if you need to add a new column:
        // database.execSQL("ALTER TABLE YourEntity ADD COLUMN new_column_name TYPE");
        database.execSQL("ALTER TABLE food_items ADD COLUMN meal_category TEXT DEFAULT 'Unknown'");
    }
}
