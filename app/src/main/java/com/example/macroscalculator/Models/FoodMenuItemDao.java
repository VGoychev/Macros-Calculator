package com.example.macroscalculator.Models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import java.util.List;

@Dao
public interface FoodMenuItemDao {
    @Update
    void updateFoodMenuItem(FoodMenuItem foodMenuItem);
    @Query("SELECT * FROM FoodMenuItem")
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
    @Insert
    void insertAll(List<FoodMenuItem> meals);
    @Query("SELECT * FROM FoodMenuItem")
    List<FoodMenuItem> getAllMeals();
    @Query("SELECT * FROM FoodMenuItem")
    FoodMenuItem getMeal();
    @Delete
    void delete(FoodMenuItem foodMenuItem);
    @Delete
    void deleteAll(List<FoodMenuItem> meals);
}
