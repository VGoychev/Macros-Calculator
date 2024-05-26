package com.example.macroscalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.macroscalculator.database.DefaultMeals;
import com.example.macroscalculator.database.FoodMenuItem;
import com.example.macroscalculator.database.FoodMenuItemDao;
import com.example.macroscalculator.database.MealAdapter;
import com.example.macroscalculator.database.MealMenuAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MacrosCalculator extends AppCompatActivity {
Button btnAdd;
ImageView imageEdit;
TextView txtViewGender, txtViewAge, txtViewHeight, txtViewWeight, txtViewMaintenance, txtViewGain, txtViewLose, txtViewTotal, dateTimeDisplay;
Calendar calendar;
SimpleDateFormat dateFormat;
String date;
SharedPreferences sp;
AppDatabase db;
FoodMenuItemDao foodMenuItemDao;
RecyclerView recyclerViewTodayMeals;
MealAdapter mealAdapter;
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
    public void btnAddClick(View view){
    showAddMealDialog();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros_calculator);
        sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        findViews();

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "DB_NAME").allowMainThreadQueries().build();
        foodMenuItemDao = db.foodMenuItemDao();

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

        recyclerViewTodayMeals.setLayoutManager(new LinearLayoutManager(this));
        mealAdapter = new MealAdapter(new ArrayList<>());
        recyclerViewTodayMeals.setAdapter(mealAdapter);

        String currentDate = dateFormat.format(calendar.getTime());
        List<FoodMenuItem> savedMeals = foodMenuItemDao.getMealsByDate(currentDate);
        for (FoodMenuItem meal : savedMeals) {
            mealAdapter.addMeal(meal);
        }
        updateTotalValues();
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
        recyclerViewTodayMeals = (RecyclerView) findViewById(R.id.recyclerViewTodayMeals);
    }
    private void showAddMealDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogBackground);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);


        final ImageView imgAdd = dialogView.findViewById(R.id.imgAddNewItem);
        RadioGroup radioGroupQuantities = dialogView.findViewById(R.id.radioGroupQuantities);
        RecyclerView recyclerViewMeals = dialogView.findViewById(R.id.recyclerViewMeals);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(this));
        List<FoodMenuItem> defaultMeals = DefaultMeals.getDefaultMeals(dateFormat.format(calendar.getTime()));
        MealMenuAdapter menuAdapter = new MealMenuAdapter(defaultMeals);
        recyclerViewMeals.setAdapter(menuAdapter);



        builder.setPositiveButton("Add", (dialog, which) -> {
                    int selectedQuantity = getSelectedQuantity(radioGroupQuantities);
                    FoodMenuItem selectedMeal = menuAdapter.getSelectedMeal();
                    if (selectedMeal != null) {
                        String currentDate = dateFormat.format(calendar.getTime());
                        FoodMenuItem mealToAdd = calculateMealForQuantity(selectedMeal, selectedQuantity);
                        mealToAdd.setDate(currentDate);
                        ((MealAdapter) mealAdapter).addMeal(mealToAdd);
                        foodMenuItemDao.insert(mealToAdd);
                        updateTotalValues();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.white));
                negativeButton.setTextColor(getResources().getColor(R.color.white));
            }
        });
        dialog.show();
    }

    private int getSelectedQuantity(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radio200g) {
            mealAdapter.setSelectedGrams(200);
            return 200;
        } else if (selectedId == R.id.radio300g) {
            mealAdapter.setSelectedGrams(300);
            return 300;
        }
        mealAdapter.setSelectedGrams(100);
        return 100;
    }
    private FoodMenuItem calculateMealForQuantity(FoodMenuItem meal, int quantity) {
        FoodMenuItem newMeal = new FoodMenuItem(meal.mealName,
                (meal.kcal * quantity) / 100,
                (meal.fats * quantity) / 100,
                (meal.carbs * quantity) / 100,
                (meal.proteins * quantity) / 100,
                meal.date);
        return newMeal;
    }
    private void updateTotalValues() {
        List<FoodMenuItem> meals = mealAdapter.getMeals(); // Get the list of meals from the adapter
        int totalKcal = 0;
        double totalFats = 0;
        double totalCarbs = 0;
        double totalProteins = 0;

        for (FoodMenuItem meal : meals) {
            totalKcal += meal.getKcal();
            totalFats += meal.getFats();
            totalCarbs += meal.getCarbs();
            totalProteins += meal.getProteins();
        }

        // Update the TextView with the total values
        txtViewTotal.setText("Total - " + totalKcal + "kcal " +
                totalFats + "g fats " +
                totalCarbs + "g carbs " +
                totalProteins +"g proteins");
    }
}