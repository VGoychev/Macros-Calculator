package com.vgoychev.macroscalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public void btnAddClick(View view) {
    String name = editName.getText().toString();
    String fatsText = editFats.getText().toString();
    String carbsText = editCarbs.getText().toString();
    String proteinsText = editProteins.getText().toString();

    boolean hasError = false;

    if (name.isEmpty()) {
        editName.setError("Name can't be empty");
        hasError = true;
    } else if (isNumeric(name)) {
        editName.setError("Name can't be a number");
        hasError = true;
    }

    if (fatsText.isEmpty()) {
        editFats.setError("Fats can't be empty");
        hasError = true;
    }

    if (carbsText.isEmpty()) {
        editCarbs.setError("Carbs can't be empty");
        hasError = true;
    }

    if (proteinsText.isEmpty()) {
        editProteins.setError("Proteins can't be empty");
        hasError = true;
    }

    if (hasError) {
        return; // Stop execution if there are errors
    }

    double fats = Double.parseDouble(fatsText);
    double carbs = Double.parseDouble(carbsText);
    double proteins = Double.parseDouble(proteinsText);
    double kcal = (fats * 9) + (carbs * 4) + (proteins * 4); // Calculate kcal based on macronutrients

    Intent resultIntent = new Intent();
    resultIntent.putExtra("name", name);
    resultIntent.putExtra("fats", fats);
    resultIntent.putExtra("carbs", carbs);
    resultIntent.putExtra("proteins", proteins);
    resultIntent.putExtra("kcal", kcal);

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