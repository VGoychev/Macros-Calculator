package com.example.macroscalculator.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodMenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String mealName;
    public double kcal;
    public double fats;
    public double proteins;
    public double carbs;
    public String date;
    public int quantity;

    public FoodMenuItem(String mealName, double kcal, double fats, double carbs, double proteins, String date) {
        this.mealName = mealName;
        this.kcal = kcal;
        this.fats = fats;
        this.carbs = carbs;
        this.proteins = proteins;
        this.date = date;
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
    public String getDate() {
        return date;
    }
    public int getQuantity(){
        return quantity;
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

    public void setDate(String date) {
        this.date = date;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
