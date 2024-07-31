package com.vgoychev.macroscalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;

public class AddNewMealToMenu extends AppCompatActivity {
Button buttonAdd, buttonCancel;
EditText editName, editFats, editCarbs, editProteins;
RadioGroup radioGroup;
RadioButton radioButtonDrink, radioButtonMeal;

public void btnAddClick(View view) {
    String name = editName.getText().toString().trim();
    String fatsText = editFats.getText().toString().trim();
    String carbsText = editCarbs.getText().toString().trim();
    String proteinsText = editProteins.getText().toString().trim();
    String mealType = "";
    if (radioButtonDrink.isChecked()){
        mealType = radioButtonDrink.getText().toString();
    } else if (radioButtonMeal.isChecked()) {
        mealType = radioButtonMeal.getText().toString();
    }
    double fats = 0;
    double carbs = 0;
    double proteins = 0;

    boolean hasError = false;

    if (name.isEmpty()) {
        editName.setError(getString(R.string.error_name_empty));
        hasError = true;
    } else if (isNumeric(name)) {
        editName.setError(getString(R.string.error_name_numeric));
        hasError = true;
    }

    if (fatsText.isEmpty()) {
        editFats.setError(getString(R.string.error_fats_empty));
        hasError = true;
    }
    else {
        fats = Double.parseDouble(fatsText);
        if (fats > 100) {
            editFats.setError(getString(R.string.error_fats_exceed));
            hasError = true;
        }
    }

    if (carbsText.isEmpty()) {
        editCarbs.setError(getString(R.string.error_carbs_empty));
        hasError = true;
    }
    else {
        carbs = Double.parseDouble(carbsText);
        if (carbs > 100) {
            editCarbs.setError(getString(R.string.error_carbs_exceed));
            hasError = true;
        }
    }

    if (proteinsText.isEmpty()) {
        editProteins.setError(getString(R.string.error_proteins_empty));
        hasError = true;
    }
    else {
        proteins = Double.parseDouble(proteinsText);
        if (proteins > 90) {
            editProteins.setError(getString(R.string.error_proteins_exceed));
            hasError = true;
        }
    }
    if (hasError) {
        return; // Stop execution if there are errors
    }

    double kcal = (fats * 9) + (carbs * 4) + (proteins * 4); // Calculate kcal based on macronutrients

    Intent resultIntent = new Intent();
    resultIntent.putExtra("name", name);
    resultIntent.putExtra("fats", fats);
    resultIntent.putExtra("carbs", carbs);
    resultIntent.putExtra("proteins", proteins);
    resultIntent.putExtra("kcal", kcal);
    resultIntent.putExtra("mealType", mealType);

    setResult(Activity.RESULT_OK, resultIntent);
    finish();
}
    public void btnCancelClick(View view){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_meal_to_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-4087996753169079/7132802137")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                        TemplateView templateView = findViewById(R.id.nativeAdTemplate);
                        templateView.setStyles(style);
                        templateView.setNativeAd(nativeAd);

                    }
                }).build();
        findViews();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void findViews(){
        buttonAdd = findViewById(R.id.button_add);
        buttonCancel = findViewById(R.id.button_cancel);
        editName = findViewById(R.id.editText_add_meal_name);
        editFats = findViewById(R.id.editText_add_meal_fats);
        editCarbs = findViewById(R.id.editText_add_meal_carbs);
        editProteins = findViewById(R.id.editText_add_meal_proteins);
        radioGroup = findViewById(R.id.radioGroupType);
        radioButtonDrink = findViewById(R.id.radioDrink);
        radioButtonMeal = findViewById(R.id.radioMeal);
    }
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}