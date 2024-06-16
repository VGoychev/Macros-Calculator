package com.example.macroscalculator.Models;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macroscalculator.R;

import java.util.List;

public class MealMenuAdapter extends RecyclerView.Adapter<MealMenuAdapter.MealViewHolder>{
    private List<FoodMenuItem> menuMeals;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public MealMenuAdapter(List<FoodMenuItem> menuMeals) {
        this.menuMeals = menuMeals;
    }
    public void addMeal(FoodMenuItem meal) {
        menuMeals.add(meal);
        notifyDataSetChanged();
    }
    public void removeMeal(FoodMenuItem meal) {
        menuMeals.remove(meal);
        notifyDataSetChanged();
    }
    public List<FoodMenuItem> getMeals() {
        return menuMeals;
    }
    public void setMeals(List<FoodMenuItem> menuMeals) {
        this.menuMeals = menuMeals;
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
