package com.vgoychev.macroscalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vgoychev.macroscalculator.model.FoodItem;
import com.vgoychev.macroscalculator.dao.FoodItemDao;
import com.vgoychev.macroscalculator.model.FoodMenuItem;
import com.vgoychev.macroscalculator.dao.FoodMenuItemDao;
import com.vgoychev.macroscalculator.adapters.MealAdapter;
import com.vgoychev.macroscalculator.adapters.MealMenuAdapter;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MacrosCalculator extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private static final String PREFS_NAME = "ShowcasePrefs";
    private static final String KEY_FIRST_LAUNCH = "isFirstLaunch";

    public static AppDatabase db;
    Button btnAdd;
    ImageView imageEdit, imgViewGender;
    TextView  txtViewAge, txtViewHeight, txtViewWeight, txtViewMaintenance,
            txtViewGain, txtViewLose, dateTimeDisplay, txtViewTotalKcal, txtViewTotalFats,
            txtViewTotalCarbs, txtViewTotalProteins, addBreakfastText, addLunchText, addDinnerText;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String date;
    SharedPreferences sp;
    FoodMenuItemDao foodMenuItemDao;
    FoodItemDao foodItemDao;
    RecyclerView recyclerViewBreakfast, recyclerViewLunch, recyclerViewDinner;
    MealAdapter breakfastAdapter, lunchMealsAdapter, dinnerMealsAdapter;
    MealMenuAdapter menuAdapter;
    private static final int ADD_MEAL_REQUEST_CODE = 1;

    private AdView mAdView;

    int showCaseNumber = 0;

    final double MALE_CONST = 88.362;
    final double MALE_WEIGHT_MULT = 13.397;
    final double MALE_HEIGHT_MULT = 4.799;
    final double MALE_AGE_MULT = 5.677;
    final double FEMALE_CONST = 447.593;
    final double FEMALE_WEIGHT_MULT = 9.247;
    final double FEMALE_HEIGHT_MULT = 3.098;
    final double FEMALE_AGE_MULT = 4.330;
    double calculatedBMR = 0;

    private List<FoodItem> todayMealsList = new ArrayList<>();
    private List<FoodItem> lunchMealsList = new ArrayList<>();
    private List<FoodItem> dinnerMealsList = new ArrayList<>();


    public void imageEditClick(View view) {
        SharedPreferences sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Save old values
        editor.putString("oldHeight", sp.getString("height", ""));
        editor.putString("oldWeight", sp.getString("weight", ""));
        editor.putString("oldAge", sp.getString("age", ""));
        editor.putString("oldGender", sp.getString("gender", ""));
        editor.putString("oldActivity", sp.getString("activity", ""));

        // Remove current values
        editor.remove("height");
        editor.remove("weight");
        editor.remove("age");
        editor.remove("gender");
        editor.remove("activity");
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void btnAddClick(View view){
        showAddMealDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros_calculator);


        // Initialize the Google Mobile Ads SDK on a background thread.
        MobileAds.initialize(this, initializationStatus -> {});

        mAdView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);



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

        updateUserInfoAndBMR();


        recyclerViewBreakfast.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLunch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDinner.setLayoutManager(new LinearLayoutManager(this));

        todayMealsList = loadTodaysMealsFromDatabase("Breakfast");
        lunchMealsList = loadTodaysMealsFromDatabase("Lunch");
        dinnerMealsList = loadTodaysMealsFromDatabase("Dinner");

        Context context = this;
        breakfastAdapter = new MealAdapter(context, todayMealsList);
        lunchMealsAdapter = new MealAdapter(context, lunchMealsList);
        dinnerMealsAdapter = new MealAdapter(context, dinnerMealsList);

        recyclerViewLunch.setAdapter(lunchMealsAdapter);
        recyclerViewDinner.setAdapter(dinnerMealsAdapter);
        recyclerViewBreakfast.setAdapter(breakfastAdapter);

        ItemTouchHelper itemTouchHelperBreakfast = new ItemTouchHelper(new SwipeToDeleteCallback(breakfastAdapter, "Breakfast"));
        itemTouchHelperBreakfast.attachToRecyclerView(recyclerViewBreakfast);
        ItemTouchHelper itemTouchHelperLunch = new ItemTouchHelper(new SwipeToDeleteCallback(lunchMealsAdapter, "Lunch"));
        itemTouchHelperLunch.attachToRecyclerView(recyclerViewLunch);
        ItemTouchHelper itemTouchHelperDinner = new ItemTouchHelper(new SwipeToDeleteCallback(dinnerMealsAdapter, "Dinner"));
        itemTouchHelperDinner.attachToRecyclerView(recyclerViewDinner);

        toggleAddTextView();
        updateTotalValues();

    }

    private void toggleAddTextView() {
        if (breakfastAdapter.getItemCount() == 0) {
            addBreakfastText.setVisibility(View.VISIBLE);
        } else {
            addBreakfastText.setVisibility(View.GONE);
        }
        if (lunchMealsAdapter.getItemCount() == 0) {
            addLunchText.setVisibility(View.VISIBLE);
        } else {
            addLunchText.setVisibility(View.GONE);
        }
        if (dinnerMealsAdapter.getItemCount() == 0) {
            addDinnerText.setVisibility(View.VISIBLE);
        } else {
            addDinnerText.setVisibility(View.GONE);
        }
    }

    private List<FoodItem> loadTodaysMealsFromDatabase(String category) {
        String currentDate = dateFormat.format(calendar.getTime());
        List<FoodItem> meals = db.foodItemDao().getMealsByDateAndCategory(currentDate, category);

        Log.d("Database", "Loaded Meals: " + meals);

        return meals;
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private MealAdapter adapter;
        private String category;

        SwipeToDeleteCallback(MealAdapter adapter, String category) {
            super(0, ItemTouchHelper.LEFT);
            this.adapter = adapter;
            this.category = category;
        }
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            FoodItem deletedMeal = adapter.getMeals().get(position);
            deleteItem(deletedMeal, category);

        }
    }

    private void deleteItem(FoodItem foodItem, String category) {
        db = AppDatabase.getInstance(this.getApplicationContext());
        db.foodItemDao().deleteMealsById(foodItem.getId());
        List<FoodItem> updatedMeals = loadTodaysMealsFromDatabase("Breakfast");

        switch (category) {
            case "Breakfast":
                breakfastAdapter.setMeals(updatedMeals, getApplicationContext());
                breakfastAdapter.notifyDataSetChanged();
                toggleAddTextView();
                break;
            case "Lunch":
                lunchMealsAdapter.setMeals(updatedMeals, getApplicationContext());
                lunchMealsAdapter.notifyDataSetChanged();
                toggleAddTextView();
                break;
            case "Dinner":
                dinnerMealsAdapter.setMeals(updatedMeals, getApplicationContext());
                dinnerMealsAdapter.notifyDataSetChanged();
                toggleAddTextView();
                break;
        }

        updateTotalValues();
    }

    private void showAddMealDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogBackground);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);

        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        TextView txtViewType = dialogView.findViewById(R.id.txtViewType);
        RadioButton radioBreakfast = dialogView.findViewById(R.id.radioBreakfast);
        RadioButton radioLunch = dialogView.findViewById(R.id.radioLunch);
        RadioButton radioDinner = dialogView.findViewById(R.id.radioDinner);
        final ImageView imgAdd = dialogView.findViewById(R.id.imgAddNewItem);
        final ImageView imgDelete = dialogView.findViewById(R.id.imgDeleteItem);
        RecyclerView recyclerViewMeals = dialogView.findViewById(R.id.recyclerViewMeals);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(this));

        List<FoodMenuItem> loadedMeals = loadMealsFromDatabase();


        menuAdapter = new MealMenuAdapter(loadedMeals, meal -> {
            String mealType = meal.getMealType();

            Log.d("MealType", "MealType: " + mealType);
            Log.d("MealType", "Expected Drink: " + context.getString(R.string.meal_type_drink));
            Log.d("MealType", "Expected Meal: " + context.getString(R.string.meal_type_meal));
            if (context.getString(R.string.meal_type_drink).equals(mealType)) {
                txtViewType.setText(context.getString(R.string.ml));
            } else if (context.getString(R.string.meal_type_meal).equals(mealType)) {
                txtViewType.setText(context.getString(R.string.g));
            }
        });
        recyclerViewMeals.setAdapter(menuAdapter);

        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MacrosCalculator.this, AddNewMealToMenu.class);
            startActivityForResult(intent, ADD_MEAL_REQUEST_CODE);
        });

        imgDelete.setOnClickListener(v -> {
            int position = menuAdapter.getSelectedPosition(); // Replace with your logic to get selected position
            if (position != RecyclerView.NO_POSITION) {
                menuAdapter.removeMeal(position, MacrosCalculator.this); // Remove from adapter
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.select_meal_delete), Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.setPositiveButton(context.getString(R.string.add), null)
                .setNegativeButton(context.getString(R.string.cancel), (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.white));
            negativeButton.setTextColor(getResources().getColor(R.color.white));

            positiveButton.setOnClickListener(v -> {
                String quantityText = editTextQuantity.getText().toString();

                if (quantityText.isEmpty()) {
                    editTextQuantity.setError(getString(R.string.error_empty));
                    return; // Do not dismiss the dialog
                }

                int selectedQuantity;
                try {
                    selectedQuantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    editTextQuantity.setError(getString(R.string.error_invalid_number));
                    return; // Do not dismiss the dialog
                }

                if (selectedQuantity <= 0) {
                    editTextQuantity.setError(getString(R.string.error_zero));
                    return; // Do not dismiss the dialog
                }

                FoodMenuItem selectedMeal = menuAdapter.getSelectedMeal();
                if (selectedMeal == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.select_meal), Toast.LENGTH_SHORT).show();
                    return; // Do not dismiss the dialog
                }
                String category;
                if (radioBreakfast.isChecked()){
                    category = "Breakfast";
                } else if (radioLunch.isChecked()) {
                    category = "Lunch";
                } else if (radioDinner.isChecked()) {
                    category = "Dinner";
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.select_category), Toast.LENGTH_SHORT).show();
                    return;
                }
                String currentDate = dateFormat.format(calendar.getTime());
                AppDatabase database = AppDatabase.getInstance(getApplicationContext());

                FoodItem mealToAdd = new FoodItem(selectedMeal.getMealName(),
                        selectedMeal.getKcal(), selectedMeal.getFats(),
                        selectedMeal.getCarbs(), selectedMeal.getProteins(),
                        currentDate, selectedMeal.getMealType(), category);

                mealToAdd.calculateNutritionForQuantity(selectedQuantity);

                mealToAdd.setDate(currentDate);

                database.foodItemDao().insertFoodItem(mealToAdd);

                List<FoodItem> updatedMeals = loadTodaysMealsFromDatabase(category);

                updateRecyclerView(category, updatedMeals);

                toggleAddTextView();

                updateTotalValues();

                dialog.dismiss(); // Dismiss the dialog only after successful addition
            });
        });

        dialog.show();
    }
    private void updateRecyclerView(String category, List<FoodItem> updatedMeals) {
        switch (category) {
            case "Breakfast":
                breakfastAdapter.setMeals(updatedMeals, getApplicationContext());
                breakfastAdapter.notifyDataSetChanged();
                break;
            case "Lunch":
                lunchMealsAdapter.setMeals(updatedMeals, getApplicationContext());
                lunchMealsAdapter.notifyDataSetChanged();
                break;
            case "Dinner":
                dinnerMealsAdapter.setMeals(updatedMeals, getApplicationContext());
                dinnerMealsAdapter.notifyDataSetChanged();
                break;
        }
    }
    private List<FoodMenuItem> loadMealsFromDatabase() {
        return DefaultMeals.loadMealsFromDatabase(this);

    }


    private void updateTotalValues() {
        List<FoodItem> breakfastMeals = breakfastAdapter.getMeals(); // Get the list of meals from the adapter
        List<FoodItem> lunchMeals = lunchMealsAdapter.getMeals();
        List<FoodItem> dinnerMeals = dinnerMealsAdapter.getMeals();
        if (breakfastMeals == null) {
            breakfastMeals = new ArrayList<>(); // Initialize to avoid NullPointerException
        }

        int totalKcal = 0;
        double totalFats = 0;
        double totalCarbs = 0;
        double totalProteins = 0;

        if (breakfastMeals != null) {
            for (FoodItem meal : breakfastMeals) {
                totalKcal += meal.getKcal();
                totalFats += meal.getFats();
                totalCarbs += meal.getCarbs();
                totalProteins += meal.getProteins();
            }
        }

        // Sum values from lunch meals
        if (lunchMeals != null) {
            for (FoodItem meal : lunchMeals) {
                totalKcal += meal.getKcal();
                totalFats += meal.getFats();
                totalCarbs += meal.getCarbs();
                totalProteins += meal.getProteins();
            }
        }

        // Sum values from dinner meals
        if (dinnerMeals != null) {
            for (FoodItem meal : dinnerMeals) {
                totalKcal += meal.getKcal();
                totalFats += meal.getFats();
                totalCarbs += meal.getCarbs();
                totalProteins += meal.getProteins();
            }
        }

        txtViewTotalKcal.setText(totalKcal + " " + getString(R.string.kcal1));
        txtViewTotalFats.setText(String.format("%.0f", totalFats) + getString(R.string.g_fats1));
        txtViewTotalCarbs.setText(String.format("%.0f", totalCarbs) + getString(R.string.g_carbs1));
        txtViewTotalProteins.setText(String.format("%.0f", totalProteins) + getString(R.string.g_proteins1));
    }

    public void findViews(){
        imageEdit = (ImageView) findViewById(R.id.imgViewEdit);
        btnAdd = (Button) findViewById(R.id.button_macros_add);
        imgViewGender = (ImageView) findViewById(R.id.imgViewGenderValue);
        txtViewAge = (TextView) findViewById(R.id.textViewAgeValue);
        txtViewHeight = (TextView) findViewById(R.id.textViewHeightValue);
        txtViewWeight = (TextView) findViewById(R.id.textViewWeightValue);
        txtViewMaintenance = (TextView) findViewById(R.id.maintenanceCaloriesTextView);
        txtViewGain = (TextView) findViewById(R.id.weightGainCaloriesTextView);
        txtViewLose = (TextView) findViewById(R.id.weightLossCaloriesTextView);
        txtViewTotalKcal = (TextView) findViewById(R.id.txtViewTotalKcal);
        txtViewTotalFats = (TextView) findViewById(R.id.txtViewTotalFats);
        txtViewTotalCarbs = (TextView) findViewById(R.id.txtViewTotalCarbs);
        txtViewTotalProteins = (TextView) findViewById(R.id.txtViewTotalProtein);
        dateTimeDisplay = (TextView)findViewById(R.id.text_date_display);
        recyclerViewBreakfast = (RecyclerView) findViewById(R.id.recyclerViewTodayMeals);
        recyclerViewLunch = (RecyclerView) findViewById(R.id.recyclerViewLunch);
        recyclerViewDinner = (RecyclerView) findViewById(R.id.recyclerViewDinner);
        addBreakfastText = findViewById(R.id.addBreakfastText);
        addLunchText = findViewById(R.id.addLunchText);
        addDinnerText = findViewById(R.id.addDinnerText);
    }
    private void updateUserInfoAndBMR() {
        int height = Integer.parseInt(sp.getString("height" , ""));
        int weight = Integer.parseInt(sp.getString("weight" , ""));
        int age = Integer.parseInt(sp.getString("age" , ""));
        double activity = Double.parseDouble(sp.getString("activity",""));
        String gender = sp.getString("gender", "");

        if (gender.equals(getString(R.string.male))){
            imgViewGender.setImageResource(R.drawable.male);
        } else if (gender.equals(getString(R.string.female))) {
            imgViewGender.setImageResource(R.drawable.female);
        }
        txtViewAge.setText(age + " " + getString(R.string.years));
        txtViewHeight.setText(height + " " + getString(R.string.cm));
        txtViewWeight.setText(weight + " " + getString(R.string.kg));

        if (gender.equalsIgnoreCase(getString(R.string.male))) {
            calculatedBMR = MALE_CONST + (MALE_WEIGHT_MULT * weight) + (MALE_HEIGHT_MULT * height) - (MALE_AGE_MULT * age);
        } else if (gender.equalsIgnoreCase(getString(R.string.female))) {
            calculatedBMR = FEMALE_CONST + (FEMALE_WEIGHT_MULT * weight) + (FEMALE_HEIGHT_MULT * height) - (FEMALE_AGE_MULT * age);
        }

        double caloriesForMaintenance = Math.ceil(calculatedBMR * activity);
        double caloriesForWeightGain = caloriesForMaintenance + 300;
        double caloriesForWeightLoss = caloriesForMaintenance - 300;

        txtViewMaintenance.setText(String.format("%.0f", caloriesForMaintenance) + " " + getString(R.string.kcal_per_day));
        txtViewGain.setText(String.format("%.0f", caloriesForWeightGain) + " " + getString(R.string.kcal_per_day));
        txtViewLose.setText(String.format("%.0f", caloriesForWeightLoss) + " " + getString(R.string.kcal_per_day));
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
            String mealType = data.getStringExtra("mealType");

            FoodMenuItem newMeal = new FoodMenuItem(name, kcal, fats, carbs, proteins, mealType);

            // Save to database
            foodMenuItemDao.insert(newMeal);
            DefaultMeals.updateDatabase(DefaultMeals.getCurrentMeals(this), getApplicationContext());

            // Update RecyclerView
            menuAdapter.addMeal(newMeal);
        }
    }
}