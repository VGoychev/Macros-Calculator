package com.vgoychev.macroscalculator.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import com.vgoychev.macroscalculator.model.FoodMenuItem;

import java.util.List;

@Dao
public interface FoodMenuItemDao {
    @Update
    void updateFoodMenuItem(FoodMenuItem foodMenuItem);
    @Query("SELECT * FROM food_menu_items")
    List<FoodMenuItem> getDefaultMeals();

    @Transaction
    default void updateDefaultMeals(List<FoodMenuItem> meals) {
        // Update all meals in a transaction
        for (FoodMenuItem meal : meals) {
            updateFoodMenuItem(meal);
        }
    }
    @Insert
    void insert(FoodMenuItem foodMenuItem);

    @Query("SELECT * FROM food_menu_items")
    List<FoodMenuItem> getAllMeals();
    @Delete
    void delete(FoodMenuItem foodMenuItem);
}
