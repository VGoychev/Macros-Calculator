package com.example.macroscalculator.Models;

import java.util.ArrayList;
import java.util.List;

public class DefaultMeals {
    public static List<FoodMenuItem> getDefaultMeals(String currentDate) {
        List<FoodMenuItem> meals = new ArrayList<>();
        meals.add(new FoodMenuItem("Chicken Salad", 70, 4, 1, 6, currentDate)); // Values per 100 grams
        meals.add(new FoodMenuItem("Beef Steak", 250, 17, 1, 20, currentDate));
        meals.add(new FoodMenuItem("Vegetable Stir Fry", 50, 2, 4, 2, currentDate));
        meals.add(new FoodMenuItem("Protein Shake", 100, 2.5, 7.5, 12.5, currentDate));
        meals.add(new FoodMenuItem("Grilled Salmon", 200, 12.5, 0, 20, currentDate));
        return meals;
    }
}
