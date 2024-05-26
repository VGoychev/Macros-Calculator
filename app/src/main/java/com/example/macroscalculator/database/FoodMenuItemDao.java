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
@Query("SELECT * FROM FoodMenuItem WHERE date = :date")
    List<FoodMenuItem> getMealsByDate(String date);
@Query("DELETE FROM FoodMenuItem WHERE date = :date")
    void deleteMealsByDate(String date);
}
