package com.example.macroscalculator.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodMenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "meal_name")
    public String mealName;
    @ColumnInfo(name = "meal_kcal")
    public double kcal;
    @ColumnInfo(name = "meal_fats")
    public double fats;
    @ColumnInfo(name = "meal_proteins")
    public double proteins;
    @ColumnInfo(name = "meal_carbs")
    public double carbs;
    @ColumnInfo(name = "meal_quantity")
    public int quantity;
    @ColumnInfo(name = "meal_date")
    public String date;
    public FoodMenuItem(String mealName, double kcal, double fats, double carbs, double proteins) {
        this.mealName = mealName;
        this.kcal = kcal;
        this.fats = fats;
        this.carbs = carbs;
        this.proteins = proteins;
        this.quantity = 100;
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
    public int getQuantity(){
        return quantity;
    }
    public String getDate(){
        return date;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
