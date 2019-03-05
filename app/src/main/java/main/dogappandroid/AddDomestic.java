package main.dogappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;



public class AddDomestic extends AppCompatActivity {

    private EditText name, age, breed, color;
    private CalendarView sterilizedDate;
    private RadioButton maleBtn, femaleBtn, yesBtn, noBtn;
    private RadioGroup gender, sterilized;
    private Button nextBtn;
    private Dog dog;
    private DogInformation info;
    private int edit = 0;
    private DBHelper dbHelper;

    private String sterilizedDateSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic);
        name = (EditText) findViewById(R.id.nameDomestic);
        age = (EditText) findViewById(R.id.ageDomestic);
        breed = (EditText) findViewById(R.id.breedDomestic);
        color = (EditText) findViewById(R.id.colorDomestic);
        sterilizedDate = (CalendarView) findViewById(R.id.sterilizedDateDomestic);
        maleBtn = (RadioButton) findViewById(R.id.maleDomesticButton);
        femaleBtn = (RadioButton) findViewById(R.id.femaleDomesticButton);
        yesBtn = (RadioButton) findViewById(R.id.yesSterilizedButton);
        noBtn = (RadioButton) findViewById(R.id.noSterilizedButton);
        gender = (RadioGroup) findViewById(R.id.genderDomestic);
        sterilized = (RadioGroup) findViewById(R.id.sterilizedDomestic);
        nextBtn = (Button) findViewById(R.id.nextDomesticButton);

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
                        extras.putString("sterilizedDate", sterilizedDateSelected);
                    } else if (noBtn.isChecked()) {
                        extras.putBoolean("sterilized", false);
                        extras.putString("sterilizedDate", "");
                    }
                    extras.putString("edit",String.valueOf(edit));
                    Intent addDomestic2 = new Intent(AddDomestic.this, AddDomestic2.class);
                    addDomestic2.putExtras(extras);
                    startActivity(addDomestic2);
                }
            }
        });

        // from edit //
//        getDogInfo();
    }

    private void getDogInfo() {
        Bundle prevBundle = getIntent().getExtras();
        if (prevBundle != null && prevBundle.containsKey("internal_dog_id")) {
            dog = dbHelper.getDogById(prevBundle.getInt("internal_dog_id"));

            // waiting for doginfo //
            edit = 1;
            info = dbHelper.getAllDogInformationByDogID(prevBundle.getInt("internal_dog_id"));
//
            // hide //
            TextView header = (TextView) findViewById(R.id.addDomesticHeader);
            ProgressBar bar = (ProgressBar) findViewById(R.id.progressBarAddDomestic);
            header.setText(R.string.editdog);
            bar.setVisibility(View.GONE);

            // set text //
            name.setText(dog.getName());
            if(dog.getGender().equals("M")){
                maleBtn.setChecked(true);
            }
            else{
                femaleBtn.setChecked(true);
            }
            color.setText(dog.getColor());
            breed.setText(dog.getColor());
            age.setText(info.getAge());
            if(dog.getSterilized() == 1){
                yesBtn.setChecked(true);
                sterilizedDate.setVisibility(View.VISIBLE);
                String parts[] = dog.getSterilizedDate().split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                long milliTime = calendar.getTimeInMillis();
                sterilizedDate.setDate(milliTime,true,true);
            }
            else{
                noBtn.setChecked(true);
            }


        }
    }


}