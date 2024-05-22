package com.example.macroscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MacrosCalculator extends AppCompatActivity {
Button btnEdit, btnAdd;
TextView txtViewGender, txtViewAge, txtViewHeight, txtViewWeight, txtViewMaintenance, txtViewGain, txtViewLose, txtViewTotal;
SharedPreferences sp;
    public void btnEditClick(View view){
        SharedPreferences sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        sp.edit().remove("height").commit();
        sp.edit().remove("weight").commit();
        sp.edit().remove("age").commit();
        sp.edit().remove("gender").commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros_calculator);
        sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        findViews();

        String height = sp.getString("height" , "");
        String weight = sp.getString("weight" , "");
        String age = sp.getString("age" , "");
        String gender= sp.getString("gender", "");

        txtViewGender.setText(gender + ",");
        txtViewAge.setText(age + " years");
        txtViewHeight.setText(height + " cm,");
        txtViewWeight.setText(weight + " kg");

    }
    public void findViews(){
        btnEdit = (Button) findViewById(R.id.button_edit);
        btnAdd = (Button) findViewById(R.id.button_macros_add);
        txtViewGender = (TextView) findViewById(R.id.textViewGenderValue);
        txtViewAge = (TextView) findViewById(R.id.textViewAgeValue);
        txtViewHeight = (TextView) findViewById(R.id.textViewHeightValue);
        txtViewWeight = (TextView) findViewById(R.id.textViewWeightValue);
        txtViewMaintenance = (TextView) findViewById(R.id.maintenanceCaloriesTextView);
        txtViewGain = (TextView) findViewById(R.id.weightGainCaloriesTextView);
        txtViewLose = (TextView) findViewById(R.id.weightLossCaloriesTextView);
        txtViewTotal = (TextView) findViewById(R.id.calculatedValues);
    }
}