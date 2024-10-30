package com.vgoychev.macroscalculator;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vgoychev.macroscalculator.model.FoodItem;
import com.vgoychev.macroscalculator.dao.FoodItemDao;
import com.vgoychev.macroscalculator.model.FoodMenuItem;
import com.vgoychev.macroscalculator.dao.FoodMenuItemDao;


@Database(entities = {FoodItem.class, FoodMenuItem.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FoodItemDao foodItemDao();
    public abstract FoodMenuItemDao foodMenuItemDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .addMigrations(new MigrationFrom2To3())
                    .build();
        }
        return INSTANCE;
    }
}

