package com.vgoychev.macroscalculator.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "food_items")
public class FoodItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_id")
    public int id;
    @ColumnInfo(name = "meal_type")
    public String mealType;
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
    @ColumnInfo(name = "meal_date")
    public String date;
    @ColumnInfo(name = "meal_quantity")
    public int quantity;

    public FoodItem(String mealName, double kcal, double fats, double carbs, double proteins, String date, String mealType) {
        this.mealName = mealName;
        this.kcal = kcal;
        this.fats = fats;
        this.carbs = carbs;
        this.proteins = proteins;
        this.date = date;
        this.quantity = 100;
        this.mealType = mealType;
    }

    public void calculateNutritionForQuantity(int grams) {
        this.kcal = (this.kcal * grams) / 100;
        this.fats = (this.fats * grams) / 100;
        this.carbs = (this.carbs * grams) / 100;
        this.proteins = (this.proteins * grams) / 100;
        this.quantity = grams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodItem foodItem = (FoodItem) o;

        return id == foodItem.id &&
                Objects.equals(mealName, foodItem.mealName) &&
                Objects.equals(date, foodItem.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mealName, date);
    }


    public int getId() {
        return id;
    }

    public String getMealName() {
        return mealName;
    }

    public double getKcal() {
        return kcal;
    }

    public double getFats() {
        return fats;
    }

    public double getProteins() {
        return proteins;
    }

    public double getCarbs() {
        return carbs;
    }

    public String getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }
    public String getMealType(){
        return mealType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
