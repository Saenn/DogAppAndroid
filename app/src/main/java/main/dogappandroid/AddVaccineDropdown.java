package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddVaccineDropdown extends AppCompatActivity {

    private Spinner vaccineSpinner;
    private ArrayList<String> vaccineList = new ArrayList<String>();
    private Button confirmButton;
    private String curDate;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccine_dropdown);

        // Setup var //
        confirmButton = (Button) findViewById(R.id.vaccine_dropdown_confirmbutton);
        calendarView = (CalendarView) findViewById(R.id.addvaccine_dropdown_calendar);
        addVaccineDataToList();

        // Setup Spinner //
        vaccineSpinner = (Spinner) findViewById(R.id.vaccine_dropdown_spinner);
        ArrayAdapter<String> adapterVaccine = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vaccineList);
        adapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccineSpinner.setAdapter(adapterVaccine);

        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){}
                else{
                    Toast.makeText(AddVaccineDropdown.this,
                            "Select : " + vaccineList.get(position),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                curDate = String.valueOf(d) + '-' + String.valueOf(month) + '-' + String.valueOf(year);
            }
        });
    }

    private void addVaccineDataToList() {
        vaccineList.add(0,"Select a Vaccine");
        vaccineList.add(1,"Rabies");
        vaccineList.add(2,"DHPP");
        vaccineList.add("Distemper");
        vaccineList.add("Measles");
        vaccineList.add("Parainfluenza");
    }
}
