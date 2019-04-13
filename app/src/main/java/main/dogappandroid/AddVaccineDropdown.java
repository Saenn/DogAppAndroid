package main.dogappandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddVaccineDropdown extends AppCompatActivity {

    private Spinner vaccineSpinner;
    private ArrayList<String> vaccineList = new ArrayList<String>();
    private Button confirmButton;
    private String curDate, selectedValue;
    private CalendarView calendarView;
    public static final DecimalFormat mFormat = new DecimalFormat("00");
    private DBHelper mHelper;
    private int ID = -1;
    private Bundle prevBundle;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccine_dropdown);
        prevBundle = getIntent().getExtras();

        // Setup var //

        confirmButton = (Button) findViewById(R.id.vaccine_dropdown_confirmbutton);
        calendarView = (CalendarView) findViewById(R.id.addvaccine_dropdown_calendar);
        addVaccineDataToList();
        mHelper = new DBHelper(this);
        selectedValue = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        curDate = sdf.format(new Date(calendarView.getDate()));


        // Setup Spinner //
        vaccineSpinner = (Spinner) findViewById(R.id.vaccine_dropdown_spinner);
        ArrayAdapter<String> adapterVaccine = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vaccineList);
        adapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccineSpinner.setAdapter(adapterVaccine);

        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedValue = "";
                } else {
                    Toast.makeText(AddVaccineDropdown.this,
                            "Select : " + vaccineList.get(position),
                            Toast.LENGTH_SHORT).show();
                    selectedValue = vaccineList.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedValue.equals("") || curDate.equals("")) {
                    Toast.makeText(AddVaccineDropdown.this, "No vaccine is selected.", Toast.LENGTH_LONG).show();
                } else {
                    DogVaccine vaccine = new DogVaccine();
                    vaccine.setName(selectedValue);
                    vaccine.setDate(curDate);
                    if (prevBundle.containsKey("internal_dog_id")) {
                        vaccine.setDogID(getIntent().getExtras().getInt("internal_dog_id"));
                    }
                    if (ID == -1) {
                        mHelper.addVaccine(vaccine);
                    } else {
                        vaccine.setId(ID);
                        if (getIntent().getExtras().getInt("isAdding") == 1) {
                            mHelper.updateVaccineWhileAddingDog(vaccine);
                        } else {
                            mHelper.updateVaccine(vaccine);
                        }
                    }
                    if (prevBundle.containsKey("fromstray")) {
                        Intent I = new Intent(AddVaccineDropdown.this, AddStray4.class);
                        removeVaccineBundle();
                        prevBundle.remove("fromstray");
                        I.putExtras(prevBundle);
                        startActivity(I);
                        finish();
                    } else if (prevBundle.containsKey("isEditVaccine")) {
                        Intent I = new Intent(AddVaccineDropdown.this, EditVaccine.class);
                        removeVaccineBundle();
                        prevBundle.remove("isEditVaccine");
                        I.putExtras(prevBundle);
                        startActivity(I);
                        finish();
                    } else {
                        Intent I = new Intent(AddVaccineDropdown.this, AddDomestic4.class);
                        removeVaccineBundle();
                        I.putExtras(prevBundle);
                        startActivity(I);
                        finish();
                    }

                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                Log.i("SelectedDay :", String.valueOf(d));
                curDate = mFormat.format(Double.valueOf(d)) + '/' + mFormat.format(Double.valueOf(month + 1)) + '/' + String.valueOf(year);
            }
        });

        editVaccine();
    }

    private void addVaccineDataToList() {
        vaccineList.add(0, "Select a vaccine");
        vaccineList.add(1, "Rabies");
        vaccineList.add(2, "DHPP");
        vaccineList.add(3, "Distemper");
        vaccineList.add(4, "Measles");
        vaccineList.add(5, "Parainfluenza");
    }

    public void removeVaccineBundle() {
        prevBundle.remove("vid");
        prevBundle.remove("vname");
        prevBundle.remove("vdate");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        Intent I = new Intent(AddVaccineDropdown.this, AddDomestic4.class);
        removeVaccineBundle();
        I.putExtras(prevBundle);
        startActivity(I);
        finish();
    }

    protected void editVaccine() {

        if (prevBundle.containsKey("vname")) {

            ID = prevBundle.getInt("vid");
            String vname = prevBundle.getString("vname");
            String vdate = prevBundle.getString("vdate");
            String parts[] = vdate.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            long milliTime = calendar.getTimeInMillis();
            if (vname.equals("Rabies")) {
                vaccineSpinner.setSelection(1);

            } else if (vname.equals("DHPP")) {
                vaccineSpinner.setSelection(2);

            } else if (vname.equals("Distemper")) {
                vaccineSpinner.setSelection(3);

            } else if (vname.equals("Measles")) {
                vaccineSpinner.setSelection(4);

            } else if (vname.equals("Parainfluenza")) {
                vaccineSpinner.setSelection(5);
            }

            calendarView.setDate(milliTime, true, true);
        }
    }
}
