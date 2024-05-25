package com.example.macroscalculator.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal")
public class FoodMenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "meal_name")
    public String mealName;
    public double kcal;
    public double fats;
    public double proteins;
    public double carbs;

    public FoodMenuItem(String mealName, double kcal, double fats, double carbs, double proteins) {
        this.mealName = mealName;
        this.kcal = kcal;
        this.fats = fats;
        this.carbs = carbs;
        this.proteins = proteins;
    }
    public String getMealName(){
        return mealName;
    }
    public double getKcal(){
        return kcal;
    }
    public double getFats(){
        return fats;
    }
    public double getProteins(){
        return proteins;
    }
    public double getCarbs(){
        return carbs;
    }
    public int getId(){
        return id;
    }

}
