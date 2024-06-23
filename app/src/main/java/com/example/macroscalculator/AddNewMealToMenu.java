package com.example.macroscalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddNewMealToMenu extends AppCompatActivity {
Button buttonAdd, buttonCancel;
EditText editName, editFats, editCarbs, editProteins;

    public void btnAddClick(View view) {
        String name = editName.getText().toString();
        double fats = Double.parseDouble(editFats.getText().toString());
        double carbs = Double.parseDouble(editCarbs.getText().toString());
        double proteins = Double.parseDouble(editProteins.getText().toString());
        double kcal = (fats * 9) + (carbs * 4) + (proteins * 4);  // Calculate kcal based on macronutrients

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
        findViews();
    }
    public void findViews(){
        buttonAdd = findViewById(R.id.button_add);
        buttonCancel = findViewById(R.id.button_cancel);
        editName = findViewById(R.id.editText_add_meal_name);
        editFats = findViewById(R.id.editText_add_meal_fats);
        editCarbs = findViewById(R.id.editText_add_meal_carbs);
        editProteins = findViewById(R.id.editText_add_meal_proteins);
    }
}