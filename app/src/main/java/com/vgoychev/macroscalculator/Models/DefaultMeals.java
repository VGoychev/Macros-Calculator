package com.vgoychev.macroscalculator.Models;

import android.content.Context;
import android.os.AsyncTask;

import com.vgoychev.macroscalculator.AppDatabase;
import com.vgoychev.macroscalculator.MacrosCalculator;
import com.vgoychev.macroscalculator.R;

import java.util.ArrayList;
import java.util.List;

public class DefaultMeals {
    private static final List<FoodMenuItem> defaultMeals = new ArrayList<>();
    private static List<FoodMenuItem> currentMeals = new ArrayList<>();
    private static void initializeDefaultMeals(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        List<FoodMenuItem> existingMeals = db.foodMenuItemDao().getAllMeals();

        if (existingMeals.isEmpty()) {
            db.foodMenuItemDao().insert(new FoodMenuItem(context.getString(R.string.chicken_salad), 70, 4, 1, 6, context.getString(R.string.meal_type_meal))); // Values per 100 grams
            db.foodMenuItemDao().insert(new FoodMenuItem(context.getString(R.string.beef_steak), 250, 17, 1, 20, context.getString(R.string.meal_type_meal)));
            db.foodMenuItemDao().insert(new FoodMenuItem(context.getString(R.string.vegetable_stir_fry), 50, 2, 4, 2, context.getString(R.string.meal_type_meal)));
            db.foodMenuItemDao().insert(new FoodMenuItem(context.getString(R.string.protein_shake), 100, 2.5, 7.5, 12.5, context.getString(R.string.meal_type_drink)));
            db.foodMenuItemDao().insert(new FoodMenuItem(context.getString(R.string.grilled_salmon), 200, 12.5, 0, 20, context.getString(R.string.meal_type_meal)));
            currentMeals.addAll(db.foodMenuItemDao().getAllMeals());
        } else {
            currentMeals.addAll(existingMeals);
        }
    }
    private static class InitializeMealsTask extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... contexts) {
            initializeDefaultMeals(contexts[0]);
            return null;
        }
    }
    public static void init(Context context) {
        new InitializeMealsTask().execute(context);
    }
    public static List<FoodMenuItem> getDefaultMeals() {
        return defaultMeals;
    }
    public static List<FoodMenuItem> getCurrentMeals(Context context){
        if (currentMeals.isEmpty()) {
            currentMeals = MacrosCalculator.db.foodMenuItemDao().getDefaultMeals();
        }
        return currentMeals;
    }
    public static void setDefaultMeals(List<FoodMenuItem> meals) {
        defaultMeals.clear();
        defaultMeals.addAll(meals);
    }
    public static void setCurrentMeals(List<FoodMenuItem> meals) {
        currentMeals = meals;
    }
    public static List<FoodMenuItem> loadMealsFromDatabase(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        return db.foodMenuItemDao().getAllMeals();
    }
    public static void updateDatabase(List<FoodMenuItem> meals, Context context) {
        MacrosCalculator.db.foodMenuItemDao().updateDefaultMeals(meals);
    }
}
