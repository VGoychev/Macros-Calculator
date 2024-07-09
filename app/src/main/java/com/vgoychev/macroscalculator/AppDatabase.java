package com.vgoychev.macroscalculator;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vgoychev.macroscalculator.Models.FoodItem;
import com.vgoychev.macroscalculator.Models.FoodItemDao;
import com.vgoychev.macroscalculator.Models.FoodMenuItem;
import com.vgoychev.macroscalculator.Models.FoodMenuItemDao;


@Database(entities = {FoodItem.class, FoodMenuItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FoodItemDao foodItemDao();
    public abstract FoodMenuItemDao foodMenuItemDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
