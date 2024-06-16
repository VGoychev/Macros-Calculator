package com.example.macroscalculator.Models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodItemDao {
    @Insert
    void insert(FoodItem foodItem);
    @Query("SELECT * FROM FoodItem")
    List<FoodItem> getAllMeals();
    @Query("SELECT * FROM FoodItem")
    FoodItem getMeal();
    @Delete
    void delete(FoodItem foodItem);
    @Query("SELECT * FROM FoodItem WHERE date = :date")
    List<FoodItem> getMealsByDate(String date);
    @Query("DELETE FROM FoodItem WHERE date = :date")
    void deleteMealsByDate(String date);
}
