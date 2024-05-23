package com.example.macroscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MacrosCalculator extends AppCompatActivity {
Button btnAdd;
ImageView imageEdit;
TextView txtViewGender, txtViewAge, txtViewHeight, txtViewWeight, txtViewMaintenance, txtViewGain, txtViewLose, txtViewTotal, dateTimeDisplay;
Calendar calendar;
SimpleDateFormat dateFormat;
String date;
SharedPreferences sp;
    final double MALE_CONST = 88.362;
    final double MALE_WEIGHT_MULT = 13.397;
    final double MALE_HEIGHT_MULT = 4.799;
    final double MALE_AGE_MULT = 5.677;
    final double FEMALE_CONST = 447.593;
    final double FEMALE_WEIGHT_MULT = 9.247;
    final double FEMALE_HEIGHT_MULT = 3.098;
    final double FEMALE_AGE_MULT = 4.330;
    double calculatedBMR = 0;
    public void imageEditClick(View view){
        SharedPreferences sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        sp.edit().remove("height").commit();
        sp.edit().remove("weight").commit();
        sp.edit().remove("age").commit();
        sp.edit().remove("gender").commit();
        sp.edit().remove("activity").commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros_calculator);
        sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        findViews();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        int height = Integer.parseInt(sp.getString("height" , ""));
        int weight = Integer.parseInt(sp.getString("weight" , ""));
        int age = Integer.parseInt(sp.getString("age" , ""));
        double activity = Double.parseDouble(sp.getString("activity",""));
        String gender= sp.getString("gender", "");

        txtViewGender.setText(gender + ",");
        txtViewAge.setText(age + " years");
        txtViewHeight.setText(height + " cm,");
        txtViewWeight.setText(weight + " kg");

        if (gender.equalsIgnoreCase("male")) {
            calculatedBMR = MALE_CONST + (MALE_WEIGHT_MULT * weight) + (MALE_HEIGHT_MULT * height) - (MALE_AGE_MULT * age);
        } else if (gender.equalsIgnoreCase("female")) {
            calculatedBMR = FEMALE_CONST + (FEMALE_WEIGHT_MULT * weight) + (FEMALE_HEIGHT_MULT * height) - (FEMALE_AGE_MULT * age);
        }
        double caloriesForMaintenance = Math.ceil(calculatedBMR * activity);
        double caloriesForWeightGain = caloriesForMaintenance + 300;
        double caloriesForWeightLoss = caloriesForMaintenance - 300;
        txtViewMaintenance.setText("To maintain the weight: " + String.format("%.0f", caloriesForMaintenance) + " calories/day");
        txtViewGain.setText("To gain weight: " + String.format("%.0f", caloriesForWeightGain) + " calories/day");
        txtViewLose.setText("To lose weight: " + String.format("%.0f", caloriesForWeightLoss) + " calories/day");

    }
    public void findViews(){
        imageEdit = (ImageView) findViewById(R.id.imgViewEdit);
        btnAdd = (Button) findViewById(R.id.button_macros_add);
        txtViewGender = (TextView) findViewById(R.id.textViewGenderValue);
        txtViewAge = (TextView) findViewById(R.id.textViewAgeValue);
        txtViewHeight = (TextView) findViewById(R.id.textViewHeightValue);
        txtViewWeight = (TextView) findViewById(R.id.textViewWeightValue);
        txtViewMaintenance = (TextView) findViewById(R.id.maintenanceCaloriesTextView);
        txtViewGain = (TextView) findViewById(R.id.weightGainCaloriesTextView);
        txtViewLose = (TextView) findViewById(R.id.weightLossCaloriesTextView);
        txtViewTotal = (TextView) findViewById(R.id.calculatedValues);
        dateTimeDisplay = (TextView)findViewById(R.id.text_date_display);
    }
}