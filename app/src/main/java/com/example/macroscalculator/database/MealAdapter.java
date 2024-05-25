package com.example.macroscalculator.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macroscalculator.R;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder>{
    private List<FoodMenuItem> meals;

    public MealAdapter(List<FoodMenuItem> meals) {
        this.meals = meals;
    }
    @NonNull
    @Override
    public MealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todays_item_meal, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MealViewHolder holder, int position) {
        FoodMenuItem meal = meals.get(position);
        String mealNameWithGrams = getMealNameWithGrams(meal, selectedGrams);
        holder.mealName.setText(mealNameWithGrams);
        holder.kcalTextView.setText(String.format("%.0f kcal", meal.getKcal()));
        holder.fatsTextView.setText(String.format("%.0fg fats", meal.getFats()));
        holder.carbsTextView.setText(String.format("%.0fg carbs", meal.getCarbs()));
        holder.proteinsTextView.setText(String.format("%.0fg proteins", meal.getProteins()));
    }
    private String getMealNameWithGrams(FoodMenuItem meal, int grams) {
        return meal.getMealName() + " (" + grams + "g)";
    }
    private int selectedGrams = 100; // Default to 100g

    public void setSelectedGrams(int grams) {
        selectedGrams = grams;
        notifyDataSetChanged(); // Update the RecyclerView when grams selection changes
    }
    @Override
    public int getItemCount() {
        return meals.size();
    }
    public void addMeal(FoodMenuItem meal) {
        meals.add(meal);
        notifyItemInserted(meals.size() - 1);
    }
    public List<FoodMenuItem> getMeals() {
        return meals;
    }
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName;
        private TextView kcalTextView, fatsTextView, carbsTextView, proteinsTextView;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            kcalTextView = itemView.findViewById(R.id.kcalTextView);
            fatsTextView = itemView.findViewById(R.id.fatsTextView);
            carbsTextView = itemView.findViewById(R.id.carbsTextView);
            proteinsTextView = itemView.findViewById(R.id.proteinsTextView);

        }
    }
}
