package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;


public class AddDomestic extends AppCompatActivity {

    private EditText name, age, breed, color;
    private TextView requiredSterilizedDateLabel, sterilizedDateLabel;
    private CalendarView sterilizedDate;
    private RadioButton maleBtn, femaleBtn, yesBtn, noBtn;
    private RadioGroup gender, sterilized;
    private Button nextBtn;
    private CheckBox knownSterilizedDate;

    private String sterilizedDateSelected;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"th"));
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
        requiredSterilizedDateLabel = findViewById(R.id.addDogRequired3);
        sterilizedDateLabel = findViewById(R.id.sterilizedDateLabel);
        knownSterilizedDate = findViewById(R.id.knownSterilizedDate);

        requiredSterilizedDateLabel.setVisibility(View.GONE);
        sterilizedDateLabel.setVisibility(View.GONE);
        knownSterilizedDate.setVisibility(View.GONE);
        sterilizedDate.setVisibility(View.GONE);
        sterilizedDateView.setVisibility(View.GONE);

        sterilized.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (yesBtn.isChecked()) {
                    requiredSterilizedDateLabel.setVisibility(View.VISIBLE);
                    sterilizedDateLabel.setVisibility(View.VISIBLE);
                    knownSterilizedDate.setVisibility(View.VISIBLE);
                    sterilizedDate.setVisibility(View.GONE);
                } else if (noBtn.isChecked()) {
                    requiredSterilizedDateLabel.setVisibility(View.GONE);
                    sterilizedDateLabel.setVisibility(View.GONE);
                    knownSterilizedDate.setVisibility(View.GONE);
                    sterilizedDate.setVisibility(View.GONE);
                    sterilizedDateView.setVisibility(View.GONE);

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
                if (age.getText().toString().equals("")) {
                    Toast.makeText(AddDomestic.this, "Please enter your puppy's age", Toast.LENGTH_LONG).show();
                } else if (gender.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddDomestic.this, "Please enter your puppy's gender", Toast.LENGTH_LONG).show();
                } else if (sterilized.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(AddDomestic.this, "Please answer if your puppy has been sterilized", Toast.LENGTH_LONG).show();
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
                        extras.putBoolean("sterilized", true);
                        if (knownSterilizedDate.isChecked()) {
                            extras.putString("sterilizedDate", sterilizedDateSelected);
                        } else {
                            extras.putString("sterilizedDate", "");
                        }
                    } else if (noBtn.isChecked()) {
                        extras.putBoolean("sterilized", false);
                        extras.putString("sterilizedDate", "");
                    }
                    Intent addDomestic2 = new Intent(AddDomestic.this, AddDomestic2.class);
                    addDomestic2.putExtras(extras);
                    startActivity(addDomestic2);
                }
            }
        });


        SharedPreferences preferences = getSharedPreferences("defaultLanguage",Context.MODE_PRIVATE);
        updateView(preferences.getString("lang","en"));



    }

    private void updateView(String lang) {
        Context context = LocalHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        nameView.setText(resources.getString(R.string.dogname));
        nameView.setHint(resources.getString(R.string.dogname_hint));

        genderView.setText(resources.getString(R.string.gender));
        maleBtn.setText(resources.getString(R.string.dogmale));
        femaleBtn.setText(resources.getString(R.string.dogfemale));
        ageView.setText(resources.getString(R.string.age_label));
        ageView.setHint(resources.getString(R.string.age_hint));
        breedView.setText(resources.getString(R.string.breed_label));
        breedView.setHint(resources.getString(R.string.breed_hint));
        colorView.setText(resources.getString(R.string.color_label));
        colorView.setHint(resources.getString(R.string.color_hint));
        yesBtn.setText(resources.getString(R.string.yes));
        noBtn.setText(resources.getString(R.string.no));
        headerView.setText(resources.getString(R.string.header_mandatory));
        nextBtn.setText(resources.getString(R.string.nextButton));

    }

}