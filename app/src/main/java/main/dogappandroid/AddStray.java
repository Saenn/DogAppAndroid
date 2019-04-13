
package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
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
    private RadioButton maleBtn, femaleBtn, yesBtn, noBtn;
    private RadioGroup gender, sterilized;
    private Button nextBtn;
    private DBHelper dbHelper;
    private Dog dog;
    private DogInformation info;
    private String sterilizedDateSelected;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stray);
        dbHelper = new DBHelper(this);

        // Setup Spinner //
        addAgeDataToList();
        selectedValue = "";
        ageSpinner = (Spinner) findViewById(R.id.ageStray);
        ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ageList);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapterAge);

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    selectedValue = "";
                }
                else{
                    Toast.makeText(AddStray.this,
                            "Select : " + ageList.get(position),
                            Toast.LENGTH_SHORT).show();
                    selectedValue = ageList.get(position);
                    Log.i("selectedvale : " , selectedValue);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        name = (EditText) findViewById(R.id.nameStray);
        breed = (EditText) findViewById(R.id.breedStray);
        color = (EditText) findViewById(R.id.colorStray);
        sterilizedDate = (CalendarView) findViewById(R.id.sterilizedDateStray);
        maleBtn = (RadioButton) findViewById(R.id.maleStrayButton);
        femaleBtn = (RadioButton) findViewById(R.id.femaleStrayButton);
        yesBtn = (RadioButton) findViewById(R.id.yesSterilizedButton);
        noBtn = (RadioButton) findViewById(R.id.noSterilizedButton);
        gender = (RadioGroup) findViewById(R.id.genderStray);
        sterilized = (RadioGroup) findViewById(R.id.sterilizedStray);
        nextBtn = (Button) findViewById(R.id.nextStrayButton);

        sterilizedDate.setVisibility(View.GONE);

        sterilized.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (yesBtn.isChecked()) {
                    sterilizedDate.setVisibility(View.VISIBLE);
                } else if (noBtn.isChecked()) {
                    sterilizedDate.setVisibility(View.GONE);
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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedValue.equals("")) {
                    Toast.makeText(AddStray.this, "Please select your puppy's age", Toast.LENGTH_LONG).show();
                } else if (gender.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddStray.this, "Please enter your puppy's gender", Toast.LENGTH_LONG).show();
                } else if (sterilized.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddStray.this, "Please answer if your puppy has been sterilized", Toast.LENGTH_LONG).show();
                } else {
                    Bundle extras = new Bundle();
                    extras.putString("name", name.getText().toString());
                    if (maleBtn.isChecked()) extras.putString("gender", "M");
                    else if (femaleBtn.isChecked()) extras.putString("gender", "F");
                    if (selectedValue.equals("Not exceed 3 years")) {
//                        extras.putInt("age", Integer.parseInt(age.getText().toString()));
                        extras.putString("ageRange", "1"); // 1 represent less than or equal to 3
                    } else {
//                        extras.putInt("age", Integer.parseInt(age.getText().toString()));
                        extras.putString("ageRange", "2"); // 2 represent more than 3
                    }
                    extras.putString("breed", breed.getText().toString());
                    extras.putString("color", color.getText().toString());
                    if (yesBtn.isChecked()) {
                        extras.putBoolean("sterilized", true);
                        extras.putString("sterilizedDate", sterilizedDateSelected);
                    } else if (noBtn.isChecked()) {
                        extras.putBoolean("sterilized", false);
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
        ageList.add(0,"Select an age");
        ageList.add(1,"Not exceed 3 years");
        ageList.add(2,"More than 3 years");
    }
}
