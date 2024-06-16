package com.example.macroscalculator.Models;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macroscalculator.MacrosCalculator;
import com.example.macroscalculator.R;

import java.util.ArrayList;
import java.util.List;

public class MealMenuAdapter extends RecyclerView.Adapter<MealMenuAdapter.MealViewHolder>{
    private List<FoodMenuItem> menuMeals;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public MealMenuAdapter(List<FoodMenuItem> meals) {
        this.menuMeals = meals;
    }
    public int getSelectedPosition(){
        return selectedPosition;
    }
    public void addMeal(FoodMenuItem meal) {
        menuMeals.add(meal);
        notifyDataSetChanged();
    }
    public void removeMeal(int position, Context context) {
        if (position >= 0 && position < menuMeals.size()) {
            FoodMenuItem mealToDelete = menuMeals.get(position);
            menuMeals.remove(position);
            if (selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION; // Reset selected position
            } else if (selectedPosition > position) {
                selectedPosition--; // Adjust selected position after removal
                 // Update view for new selected position
            }

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, menuMeals.size());

            DefaultMeals.setCurrentMeals(new ArrayList<>(menuMeals));
            DefaultMeals.updateDatabase(DefaultMeals.getCurrentMeals(context), context.getApplicationContext());
            MacrosCalculator.db.foodMenuItemDao().delete(mealToDelete);
        }
    }
    public List<FoodMenuItem> getMeals() {
        return menuMeals;
    }
    public void setMeals(List<FoodMenuItem> meals) {
        this.menuMeals = meals;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MealMenuAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealMenuAdapter.MealViewHolder holder, int position) {
        FoodMenuItem meal = menuMeals.get(position);
        holder.bind(meal, position == selectedPosition);
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = position;
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return menuMeals.size();
    }
    public FoodMenuItem getSelectedMeal() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return menuMeals.get(selectedPosition);
        }
        return null;
    }
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName;
        private final LinearLayout itemLayout;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }

        public void bind(FoodMenuItem meal, boolean isSelected) {
            mealName.setText(meal.mealName);
            mealName.setGravity(isSelected ? Gravity.CENTER : Gravity.START);
            mealName.setTextColor(isSelected ? Color.BLACK : Color.WHITE);
            itemLayout.setBackgroundColor(isSelected ? Color.LTGRAY : Color.TRANSPARENT);
        }
    }
}
