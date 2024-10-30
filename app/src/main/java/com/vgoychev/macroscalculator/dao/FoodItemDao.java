package com.vgoychev.macroscalculator.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vgoychev.macroscalculator.model.FoodItem;

import java.util.List;

@Dao
public interface FoodItemDao {

    @Insert
    void insertFoodItem(FoodItem foodItem);

    @Update
    void update(List<FoodItem> foodItemList);

    @Query("SELECT * FROM food_items WHERE meal_date = :date")
    List<FoodItem> getMealsByDate(String date);

    @Query("DELETE FROM food_items WHERE meal_date = :date")
    void deleteMealsByDate(String date);

    @Query("DELETE FROM food_items WHERE meal_id = :id")
    void deleteMealsById(int id);
    @Query("SELECT * FROM food_items WHERE meal_date = :date AND meal_category = :category")
    List<FoodItem> getMealsByDateAndCategory(String date, String category);
}
