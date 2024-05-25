package com.example.macroscalculator.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodMenuItemDao {
@Insert
    void insert(FoodMenuItem foodMenuItem);
@Query("SELECT * FROM meal")
    List<FoodMenuItem> getAllFoodMenuItem();
@Query("SELECT COUNT(*) FROM meal")
    int countMeals();
@Delete
    void delete(FoodMenuItem foodMenuItem);
}
