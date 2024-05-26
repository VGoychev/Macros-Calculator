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
    private List<FoodMenuItem> meals;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public MealMenuAdapter(List<FoodMenuItem> meals) {
        this.meals = meals;
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
        FoodMenuItem meal = meals.get(position);
        holder.bind(meal, position == selectedPosition);
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = position;
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
    public FoodMenuItem getSelectedMeal() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return meals.get(selectedPosition);
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
