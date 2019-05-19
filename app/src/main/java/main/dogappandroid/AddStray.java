
package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddStray extends AppCompatActivity {

    private Spinner ageSpinner;
    private ArrayList<String> ageList = new ArrayList<String>();
    private String selectedValue;
    private EditText name, breed, color;
    private CalendarView sterilizedDate;
    private RadioButton maleBtn, femaleBtn, yesBtn, noBtn, unknownButton;
    private RadioGroup gender, sterilized;
    private Button nextBtn;
    private TextView sterilizedDateLabel;
    private CheckBox knownSterilizedDate;
    private String sterilizedDateSelected;
    private int selectedPosition = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stray);

        // Setup Spinner //
        addAgeDataToList();
        selectedValue = "";
        ageSpinner = findViewById(R.id.ageStray);
        ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ageList);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapterAge);

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedValue = "";
                } else {
                    selectedValue = ageList.get(position);
                    selectedPosition = position;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        name = findViewById(R.id.nameStray);
        breed = findViewById(R.id.breedStray);
        color = findViewById(R.id.colorStray);
        sterilizedDate = findViewById(R.id.sterilizedDateStray);
        maleBtn = findViewById(R.id.maleStrayButton);
        femaleBtn = findViewById(R.id.femaleStrayButton);
        yesBtn = findViewById(R.id.yesSterilizedButton);
        noBtn = findViewById(R.id.noSterilizedButton);
        unknownButton = findViewById(R.id.unknownSterilizedButton);
        gender = findViewById(R.id.genderStray);
        sterilized = findViewById(R.id.sterilizedStray);
        nextBtn = findViewById(R.id.nextStrayButton);
        sterilizedDateLabel = findViewById(R.id.sterilizedStrayDateLabel);
        knownSterilizedDate = findViewById(R.id.knownSterilizedDate);

        sterilizedDateLabel.setVisibility(View.GONE);
        knownSterilizedDate.setVisibility(View.GONE);
        sterilizedDate.setVisibility(View.GONE);

        sterilized.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (yesBtn.isChecked()) {
                    sterilizedDateLabel.setVisibility(View.VISIBLE);
                    knownSterilizedDate.setVisibility(View.VISIBLE);
                } else {
                    sterilizedDateLabel.setVisibility(View.GONE);
                    knownSterilizedDate.setVisibility(View.GONE);
                    sterilizedDate.setVisibility(View.GONE);
                    knownSterilizedDate.setChecked(false);
                }
            }
        });

        sterilizedDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (dayOfMonth < 10) sterilizedDateSelected = "0" + dayOfMonth + "/";
                else sterilizedDateSelected = dayOfMonth + "/";
                if (month < 9) sterilizedDateSelected += "0" + (month + 1) + "/" + year;
                else sterilizedDateSelected += (month + 1) + "/" + year;
            }
        });

        knownSterilizedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (knownSterilizedDate.isChecked()) {
                    sterilizedDate.setVisibility(View.VISIBLE);
                } else {
                    sterilizedDate.setVisibility(View.GONE);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddStray.this, "Please enter your puppy's gender", Toast.LENGTH_LONG).show();
                }
                else if (selectedValue.equals("")) {
                    Toast.makeText(AddStray.this, "Please select your puppy's age", Toast.LENGTH_LONG).show();
                }
                else if (sterilized.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddStray.this, "Please answer if your puppy has been sterilized", Toast.LENGTH_LONG).show();
                } else {
                    Bundle extras = new Bundle();
                    extras.putString("name", name.getText().toString());
                    if (maleBtn.isChecked()) extras.putString("gender", "M");
                    else if (femaleBtn.isChecked()) extras.putString("gender", "F");
                    if (selectedPosition == 1) {
                        Log.i("selected" , selectedValue);
                        extras.putString("ageRange", "1"); // 1 represent less than or equal to 3
                    } else {
                        Log.i("selected" , selectedValue);
                        extras.putString("ageRange", "2"); // 2 represent more than 3
                    }
                    extras.putString("breed", breed.getText().toString());
                    extras.putString("color", color.getText().toString());
                    if (yesBtn.isChecked()) {
                        extras.putString("sterilized", "1");
                        if (knownSterilizedDate.isChecked()) {
                            extras.putString("sterilizedDate", sterilizedDateSelected);
                        } else {
                            extras.putString("sterilizedDate", "");
                        }
                    } else if (noBtn.isChecked()) {
                        extras.putString("sterilized", "0");
                        extras.putString("sterilizedDate", "");
                    } else if (unknownButton.isChecked()) {
                        extras.putString("sterilized", "2");
                        extras.putString("sterilizedDate", "");
                    }
                    Intent addStray2 = new Intent(AddStray.this, AddStray2.class);
                    addStray2.putExtras(extras);
                    startActivity(addStray2);
                }
            }
        });
    }

    private void addAgeDataToList() {
        SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        if (preferences.getString("lang", "th") == "en") {
            ageList.add(0, "Select an age");
            ageList.add(1, "Not exceed 3 years");
            ageList.add(2, "More than 3 years");
        } else {
            ageList.add(0, "เลือกอายุของสุนัข");
            ageList.add(1, "ไม่เกิน 3 ปี");
            ageList.add(2, "มากกว่า 3 ปี");
        }
    }
}
