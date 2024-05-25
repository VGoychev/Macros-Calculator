package com.example.macroscalculator.database;

import java.util.ArrayList;
import java.util.List;

public class DefaultMeals {
    public static List<FoodMenuItem> getDefaultMeals() {
        List<FoodMenuItem> meals = new ArrayList<>();
        meals.add(new FoodMenuItem("Chicken Salad", 70, 4, 1, 6)); // Values per 100 grams
        meals.add(new FoodMenuItem("Beef Steak", 250, 17, 1, 20));
        meals.add(new FoodMenuItem("Vegetable Stir Fry", 50, 2, 4, 2));
        meals.add(new FoodMenuItem("Protein Shake", 100, 2.5, 7.5, 12.5));
        meals.add(new FoodMenuItem("Grilled Salmon", 200, 12.5, 0, 20));
        return meals;
    }
}
