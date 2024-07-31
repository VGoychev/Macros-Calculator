package com.vgoychev.macroscalculator.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vgoychev.macroscalculator.R;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder>{
    private List<FoodItem> meals;
    private Context context;
    public MealAdapter(List<FoodItem> meals) {
        this.meals = new ArrayList<>(meals);
    }
    public FoodItem getMealAt(int position) {
        return meals.get(position);
    }

    public void setMeals(List<FoodItem> newMeals, Context context) {
        this.meals = newMeals;
        this.context = context;
        notifyDataSetChanged();
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
        FoodItem meal = meals.get(position);
        String unit = context.getString(R.string.g);
        if (context.getString(R.string.meal_type_drink).equals(meal.getMealType())){
            unit = context.getString(R.string.ml);
        }
        String mealNameWithUnits = meal.getMealName() + " (" + meal.getQuantity() + unit + ")";
        holder.mealName.setText(mealNameWithUnits);
        holder.kcalTextView.setText(String.format("%.0f %s", meal.getKcal(), context.getString(R.string.kcal)));
        holder.fatsTextView.setText(String.format("%.0f%s", meal.getFats(), context.getString(R.string.g_fats)));
        holder.carbsTextView.setText(String.format("%.0f%s", meal.getCarbs(), context.getString(R.string.g_carbs)));
        holder.proteinsTextView.setText(String.format("%.0f%s", meal.getProteins(), context.getString(R.string.g_proteins)));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
    public void addMeal(FoodItem meal) {
        meals.add(meal);
        notifyItemInserted(meals.size() - 1);
    }
    public List<FoodItem> getMeals() {
        return meals;
    }
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName;
        private final TextView kcalTextView, fatsTextView, carbsTextView, proteinsTextView;

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
