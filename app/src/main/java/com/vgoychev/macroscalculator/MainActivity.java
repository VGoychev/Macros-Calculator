package com.vgoychev.macroscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

@Override
public void onBackPressed() {
    super.onBackPressed();
    SharedPreferences sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    if (sp.contains("oldHeight") || sp.contains("oldWeight") || sp.contains("oldAge") || sp.contains("oldGender") || sp.contains("oldActivity")) {
        editor.putString("height", sp.getString("oldHeight", ""));
        editor.putString("weight", sp.getString("oldWeight", ""));
        editor.putString("age", sp.getString("oldAge", ""));
        editor.putString("gender", sp.getString("oldGender", ""));
        editor.putString("activity", sp.getString("oldActivity", ""));
        editor.remove("oldHeight");
        editor.remove("oldWeight");
        editor.remove("oldAge");
        editor.remove("oldGender");
        editor.remove("oldActivity");
        editor.commit();
    }
}


    EditText enterHeight, enterWeight, enterAge;
    private double height;
    private double weight;
    private double age;
    private double selectedRadioActivity;
    String selectedRadiobut;
    Button button;
    RadioButton radioMale, radioFemale, radioButtonOpt1, radioButtonOpt2, radioButtonOpt3, radioButtonOpt4;
    RadioGroup radioGroup, radioGroupActivity;
    SharedPreferences sp;

    private boolean heightIsValid() {
        String heightStr = enterHeight.getText().toString().trim();
        if (heightStr.isEmpty()) {
            enterHeight.setError("Can't be empty");
            return false;
        }
        double height;
        try {
            height = Double.parseDouble(heightStr);
        } catch (NumberFormatException e) {
            enterHeight.setError("Invalid input");
            return false;
        }
        if (height <= 0) {
            enterHeight.setError("Can't be zero or negative");
            return false;
        }
        if (height <= 40 || height > 250) {
            enterHeight.setError("Must be between 40 cm and 250 cm");
            return false;
        }
        return true;
    }

    private boolean weightIsValid() {
        String weightStr = enterWeight.getText().toString().trim();
        if (weightStr.isEmpty()) {
            enterWeight.setError("Can't be empty");
            return false;
        }
        double weight;
        try {
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            enterWeight.setError("Invalid input");
            return false;
        }
        if (weight <= 0) {
            enterWeight.setError("Can't be zero or negative");
            return false;
        }
        if (weight > 600) {
            enterWeight.setError("Can't be more than 600 kg");
            return false;
        }
        return true;
    }

    private boolean ageIsValid() {
        String ageStr = enterAge.getText().toString().trim();
        if (ageStr.isEmpty()) {
            enterAge.setError("Can't be empty");
            return false;
        }
        double age;
        try {
            age = Double.parseDouble(ageStr);
        } catch (NumberFormatException e) {
            enterAge.setError("Invalid input");
            return false;
        }
        if (age <= 0) {
            enterAge.setError("Can't be zero or negative");
            return false;
        }
        if (age > 116) {
            enterAge.setError("Can't be older than 116 years");
            return false;
        }
        return true;
    }

    private boolean genderIsSelected() {
        if (radioGroup.getCheckedRadioButtonId() == -1) { //checking if the user has selected a option using id
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (radioMale.isChecked()) {
            selectedRadiobut = radioMale.getText().toString();
        } else if (radioFemale.isChecked()) {
            selectedRadiobut = radioFemale.getText().toString();
        }
        return true;
    }
    private boolean activityIsSelected(){
        if(radioGroupActivity.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please select activity level", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (radioButtonOpt1.isChecked()) {
            selectedRadioActivity = 1.2;
        } else if (radioButtonOpt2.isChecked()) {
            selectedRadioActivity = 1.375;
        } else if (radioButtonOpt3.isChecked()) {
            selectedRadioActivity = 1.55;
        } else if (radioButtonOpt4.isChecked()) {
            selectedRadioActivity = 1.725;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (sp.contains("height") && sp.contains("weight") && sp.contains("age") && sp.contains("gender") && sp.contains("activity")) {
            // Values are stored, navigate to MainMenu directly
            Intent intent = new Intent(MainActivity.this, MacrosCalculator.class);
            startActivity(intent);
        } else {
            radioMale = findViewById(R.id.radioMale);
            radioFemale = findViewById(R.id.radioFemale);
            button = findViewById(R.id.button);
            enterHeight = findViewById(R.id.enterHeight);
            enterWeight = findViewById(R.id.enterWeight);
            enterAge = findViewById(R.id.enterAge);
            radioGroup = findViewById(R.id.radioSex);
            radioGroupActivity = findViewById(R.id.radioActivity);
            radioButtonOpt1 = findViewById(R.id.option1);
            radioButtonOpt2 = findViewById(R.id.option2);
            radioButtonOpt3 = findViewById(R.id.option3);
            radioButtonOpt4 = findViewById(R.id.option4);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (heightIsValid() & weightIsValid() & ageIsValid() & genderIsSelected() & activityIsSelected()) {
                        height = Double.parseDouble(enterHeight.getText().toString().trim());
                        weight = Double.parseDouble(enterWeight.getText().toString().trim());
                        age = Double.parseDouble(enterAge.getText().toString().trim());
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("height", String.valueOf((int) height));
                        editor.putString("weight", String.valueOf((int) weight));
                        editor.putString("age", String.valueOf((int) age));
                        editor.putString("gender", selectedRadiobut);
                        editor.putString("activity", String.valueOf((double)selectedRadioActivity));
                        editor.commit();

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        Intent intent = new Intent(MainActivity.this, MacrosCalculator.class);
                        startActivity(intent);
                    }
                }
            });
            if (sp.contains("oldHeight") || sp.contains("oldWeight") || sp.contains("oldAge") || sp.contains("oldGender") || sp.contains("oldActivity")) {
                enterHeight.setText(sp.getString("oldHeight", ""));
                enterWeight.setText(sp.getString("oldWeight", ""));
                enterAge.setText(sp.getString("oldAge", ""));
                String oldGender = sp.getString("oldGender", "");
                if (oldGender.equals(radioMale.getText().toString())) {
                    radioMale.setChecked(true);
                } else if (oldGender.equals(radioFemale.getText().toString())) {
                    radioFemale.setChecked(true);
                }
                String oldActivity = sp.getString("oldActivity", "");
                if (oldActivity.equals("1.2")) {
                    radioButtonOpt1.setChecked(true);
                } else if (oldActivity.equals("1.375")) {
                    radioButtonOpt2.setChecked(true);
                } else if (oldActivity.equals("1.55")) {
                    radioButtonOpt3.setChecked(true);
                } else if (oldActivity.equals("1.725")) {
                    radioButtonOpt4.setChecked(true);
                }
            }
        }
    }
}
