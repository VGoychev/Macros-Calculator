package com.example.macroscalculator.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodMenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "meal_name")
    public String mealName;
    @ColumnInfo(name = "kcal")
    public int kcal;
    @ColumnInfo(name = "fats")
    public int fats;
    @ColumnInfo(name = "proteins")
    public int proteins;
    @ColumnInfo(name = "carbs")
    public int carbs;
    public String getMealName(){
        return mealName;
    }
    public int getKcal(){
        return kcal;
    }
    public int getFats(){
        return fats;
    }
    public int getProteins(){
        return proteins;
    }
    public int getCarbs(){
        return carbs;
    }
    public int getId(){
        return id;
    }
}
