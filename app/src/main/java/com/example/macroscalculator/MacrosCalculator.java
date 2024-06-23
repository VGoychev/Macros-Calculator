package com.example.macroscalculator;

import static com.example.macroscalculator.Models.DefaultMeals.loadMealsFromDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macroscalculator.Models.DefaultMeals;
import com.example.macroscalculator.Models.FoodItem;
import com.example.macroscalculator.Models.FoodItemDao;
import com.example.macroscalculator.Models.FoodMenuItem;
import com.example.macroscalculator.Models.FoodMenuItemDao;
import com.example.macroscalculator.Models.MealAdapter;
import com.example.macroscalculator.Models.MealMenuAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MacrosCalculator extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Do nothing, so the back button is disabled.
    }
    public static AppDatabase db;
    Button btnAdd;
    ImageView imageEdit;
    TextView txtViewGender, txtViewAge, txtViewHeight, txtViewWeight, txtViewMaintenance, txtViewGain, txtViewLose, txtViewTotal, dateTimeDisplay;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String date;
    SharedPreferences sp;
    FoodMenuItemDao foodMenuItemDao;
    FoodItemDao foodItemDao;
    RecyclerView recyclerViewTodayMeals;
    MealAdapter mealAdapter;
    MealMenuAdapter menuAdapter;
    private static final int ADD_MEAL_REQUEST_CODE = 1;

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
        foodItemDao = db.foodItemDao();

        DefaultMeals.init(this);

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
        List<FoodItem> savedMeals = foodItemDao.getMealsByDate(currentDate);
        for (FoodItem meal : savedMeals) {
            mealAdapter.addMeal(meal);
        }
        updateTotalValues();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MEAL_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            double fats = data.getDoubleExtra("fats", 0);
            double carbs = data.getDoubleExtra("carbs", 0);
            double proteins = data.getDoubleExtra("proteins", 0);
            double kcal = data.getDoubleExtra("kcal", 0);

            FoodMenuItem newMeal = new FoodMenuItem(name, kcal, fats, carbs, proteins);

            // Save to database
            foodMenuItemDao.insert(newMeal);
            DefaultMeals.updateDatabase(DefaultMeals.getCurrentMeals(this), getApplicationContext());

            // Update RecyclerView
            menuAdapter.addMeal(newMeal);
        }
    }


    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT);
        }
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
//            FoodItem meal = mealAdapter.getMealAt(position);
            FoodItem deletedMeal = mealAdapter.getMeals().get(position);
            deleteItem(deletedMeal);
            updateTotalValues();
//            mealAdapter.notifyDataSetChanged();

        }
    }

    private void deleteItem(FoodItem foodItem) {
        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
        database.foodItemDao().delete(foodItem);
        mealAdapter.removeMeal(foodItem);
    }

    private void showAddMealDialog() {
        Log.d("Dialog", "Showing add meal dialog...");
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogBackground);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);


        final ImageView imgAdd = dialogView.findViewById(R.id.imgAddNewItem);
        final ImageView imgDelete = dialogView.findViewById(R.id.imgDeleteItem);
        RadioGroup radioGroupQuantities = dialogView.findViewById(R.id.radioGroupQuantities);
        RecyclerView recyclerViewMeals = dialogView.findViewById(R.id.recyclerViewMeals);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(this));

        List<FoodMenuItem> loadedMeals = loadMealsFromDatabase();

        Log.d("Dialog", "Loaded " + loadedMeals.size() + " meals from database.");

        menuAdapter = new MealMenuAdapter(loadedMeals);
        recyclerViewMeals.setAdapter(menuAdapter);

        imgAdd.setOnClickListener(v ->{
            Intent intent = new Intent(MacrosCalculator.this, AddNewMealToMenu.class);
            startActivityForResult(intent, ADD_MEAL_REQUEST_CODE);
        });


        imgDelete.setOnClickListener(v -> {
            int position = menuAdapter.getSelectedPosition(); // Replace with your logic to get selected position
            if (position != RecyclerView.NO_POSITION) {
                menuAdapter.removeMeal(position, MacrosCalculator.this); // Remove from adapter
            }
        });


        builder.setPositiveButton("Add", (dialog, which) -> {
                    int selectedQuantity = getSelectedQuantity(radioGroupQuantities);
                    FoodMenuItem selectedMeal = menuAdapter.getSelectedMeal();
                    if (selectedMeal != null && selectedQuantity > 0) {
                        String currentDate = dateFormat.format(calendar.getTime());
                        FoodItem mealToAdd = calculateMealForQuantity(selectedMeal, selectedQuantity);
                        mealToAdd.setDate(currentDate);
                        mealAdapter.addMeal(mealToAdd);
                        foodItemDao.insert(mealToAdd);
                        updateTotalValues();
                    } else {
                        Toast.makeText(this, "Please select a meal and enter a valid quantity.", Toast.LENGTH_SHORT).show();
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
        } else {
            mealAdapter.setSelectedGrams(100);
            return 100;
        }
    }

    private FoodItem calculateMealForQuantity(FoodMenuItem meal, int quantity) {
        FoodItem newMeal = new FoodItem(meal.getMealName(),
                (meal.getKcal() * quantity) / 100,
                (meal.getFats() * quantity) / 100,
                (meal.getCarbs() * quantity) / 100,
                (meal.getProteins() * quantity) / 100,
                meal.getDate());
        newMeal.setQuantity(quantity);
        return newMeal;
    }


    private List<FoodMenuItem> loadMealsFromDatabase() {
        return DefaultMeals.loadMealsFromDatabase(this);
    }


    private void updateTotalValues() {
        List<FoodItem> meals = mealAdapter.getMeals(); // Get the list of meals from the adapter
        int totalKcal = 0;
        double totalFats = 0;
        double totalCarbs = 0;
        double totalProteins = 0;

        for (FoodItem meal : meals) {
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(recyclerViewTodayMeals);
    }
}