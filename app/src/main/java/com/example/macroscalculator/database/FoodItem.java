package com.example.macroscalculator.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
}
