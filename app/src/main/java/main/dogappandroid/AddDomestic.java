package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddDomestic extends AppCompatActivity {

    private EditText name, age, breed, color;
    private TextView sterilizedDateLabel;
    private CalendarView sterilizedDate;
    private RadioButton maleBtn, femaleBtn, yesBtn, noBtn, unknownButton;
    private RadioGroup gender, sterilized;
    private Button nextBtn;
    private CheckBox knownSterilizedDate;

    private String sterilizedDateSelected;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic);

        name = findViewById(R.id.nameDomestic);
        age = findViewById(R.id.ageDomestic);
        breed = findViewById(R.id.breedDomestic);
        color = findViewById(R.id.colorDomestic);
        sterilizedDate = findViewById(R.id.sterilizedDateDomestic);
        maleBtn = findViewById(R.id.maleDomesticButton);
        femaleBtn = findViewById(R.id.femaleDomesticButton);
        yesBtn = findViewById(R.id.yesSterilizedButton);
        noBtn = findViewById(R.id.noSterilizedButton);
        gender = findViewById(R.id.genderDomestic);
        sterilized = findViewById(R.id.sterilizedDomestic);
        nextBtn = findViewById(R.id.nextDomesticButton);
        sterilizedDateLabel = findViewById(R.id.sterilizedDateLabel);
        knownSterilizedDate = findViewById(R.id.knownSterilizedDate);
        unknownButton = findViewById(R.id.unknownSterilizedButton2);

        sterilizedDateLabel.setVisibility(View.GONE);
        knownSterilizedDate.setVisibility(View.GONE);
        sterilizedDate.setVisibility(View.GONE);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sterilizedDateSelected = sdf.format(date);

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
                if (gender.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddDomestic.this, R.string.dog_gender_error, Toast.LENGTH_LONG).show();
                } else if (age.getText().toString().equals("")) {
                    Toast.makeText(AddDomestic.this, R.string.dog_age_error, Toast.LENGTH_LONG).show();
                } else if (sterilized.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddDomestic.this, R.string.sterilized_error_update, Toast.LENGTH_LONG).show();
                } else {
                    Bundle extras = new Bundle();
                    extras.putString("name", name.getText().toString());
                    if (maleBtn.isChecked()) extras.putString("gender", "M");
                    else if (femaleBtn.isChecked()) extras.putString("gender", "F");
                    if (Integer.parseInt(age.getText().toString()) <= 3) {
                        extras.putInt("age", Integer.parseInt(age.getText().toString()));
                        extras.putString("ageRange", "1"); // 1 represent less than or equal to 3
                    } else {
                        extras.putInt("age", Integer.parseInt(age.getText().toString()));
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
                    Intent addDomestic2 = new Intent(AddDomestic.this, AddDomestic2.class);
                    addDomestic2.putExtras(extras);
                    startActivity(addDomestic2);
                }
            }
        });
    }
}