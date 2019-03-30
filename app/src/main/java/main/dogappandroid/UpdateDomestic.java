package main.dogappandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateDomestic extends AppCompatActivity {

    private Spinner dogStatus;
    private LinearLayout missingLayout, deathLayout, pregnantLayout, childrenLayout, sterilizedLayout;
    private CalendarView sterilizedCalendar;
    private RadioButton yesSterilized, noSterilized;

    private DBHelper dbHelper;
    private Dog dog;
    private String sterilizedDateSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_domestic);

        dbHelper = new DBHelper(this);

        bindData();
        setInitialLayout();

        yesSterilized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sterilizedCalendar.setVisibility(View.VISIBLE);
            }
        });

        noSterilized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sterilizedCalendar.setVisibility(View.GONE);
            }
        });

        sterilizedCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (dayOfMonth < 10) sterilizedDateSelected = "0" + dayOfMonth + "/";
                else sterilizedDateSelected = dayOfMonth + "/";
                if (month < 9) sterilizedDateSelected += "0" + (month + 1) + "/" + year;
                else sterilizedDateSelected += (month + 1) + "/" + year;
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dogStatusArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dogStatus.setAdapter(adapter);
        dogStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "Alive":
                        handleAliveOption();
                        break;
                    case "Missing":
                        handleMissingOption();
                        break;
                    case "Dead":
                        handleDeadOption();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void bindData() {
        dog = dbHelper.getDogById(getIntent().getExtras().getInt("internalDogID"));
        dogStatus = (Spinner) findViewById(R.id.dogStatus);
        missingLayout = (LinearLayout) findViewById(R.id.missingLayout);
        deathLayout = (LinearLayout) findViewById(R.id.deathLayout);
        pregnantLayout = (LinearLayout) findViewById(R.id.pregnantLayout);
        childrenLayout = (LinearLayout) findViewById(R.id.childrenLayout);
        sterilizedLayout = (LinearLayout) findViewById(R.id.sterilizedLayout);
        sterilizedCalendar = (CalendarView) findViewById(R.id.sterilizedCalendar);
//        sterilizedOption = (RadioGroup) findViewById(R.id.sterilizedOption);
        yesSterilized = (RadioButton) findViewById(R.id.yesSterilized);
        noSterilized = (RadioButton) findViewById(R.id.noSterilized);
    }

    private void setInitialLayout() {
        missingLayout.setVisibility(View.GONE);
        deathLayout.setVisibility(View.GONE);
        pregnantLayout.setVisibility(View.GONE);
        childrenLayout.setVisibility(View.GONE);
        sterilizedLayout.setVisibility(View.GONE);
        sterilizedCalendar.setVisibility(View.GONE);
    }

    private void handleAliveOption() {
        setInitialLayout();
        if (dog.getGender() == "F") {
            pregnantLayout.setVisibility(View.VISIBLE);
            childrenLayout.setVisibility(View.VISIBLE);
        }
        sterilizedLayout.setVisibility(View.VISIBLE);
    }

    private void handleMissingOption() {
        setInitialLayout();
        missingLayout.setVisibility(View.VISIBLE);
    }

    private void handleDeadOption() {
        setInitialLayout();
        deathLayout.setVisibility(View.VISIBLE);
    }
}
