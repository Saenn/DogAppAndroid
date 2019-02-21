package main.dogappandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddVaccineDropdown extends AppCompatActivity {

    private Spinner vaccineSpinner;
    private ArrayList<String> vaccineList = new ArrayList<String>();
    private Button confirmButton;
    private String curDate,selectedValue;
    private CalendarView calendarView;
    private DBHelper mHelper;
    private int ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccine_dropdown);

        // Setup var //
        confirmButton = (Button) findViewById(R.id.vaccine_dropdown_confirmbutton);
        calendarView = (CalendarView) findViewById(R.id.addvaccine_dropdown_calendar);
        addVaccineDataToList();
        mHelper = new DBHelper(this);
        selectedValue = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        curDate = sdf.format(new Date(calendarView.getDate()));

        Log.i("Date : " , curDate);

        // Setup Spinner //
        vaccineSpinner = (Spinner) findViewById(R.id.vaccine_dropdown_spinner);
        ArrayAdapter<String> adapterVaccine = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vaccineList);
        adapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccineSpinner.setAdapter(adapterVaccine);

        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    selectedValue = "";
                }
                else{
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
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddVaccineDropdown.this);
                    builder.setMessage("No Vaccine is selected. Do you want to go back to the previuos page?");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent I = new Intent(AddVaccineDropdown.this, Vaccine.class);
                            startActivity(I);
                            finish();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddVaccineDropdown.this);
                    builder.setTitle(getString(R.string.addvaccinetitle));
                    builder.setMessage(getString(R.string.addmessage));

                    builder.setPositiveButton(getString(android.R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DogVaccine vaccine = new DogVaccine();
                                    vaccine.setName(selectedValue);
                                    vaccine.setDate(curDate);

                                    if (ID == -1) {
                                        mHelper.addVaccine(vaccine);
                                    } else {
                                        vaccine.setId(ID);
                                        //mHelper.updateFriend(friend);
                                    }
                                    Intent I = new Intent(AddVaccineDropdown.this, Vaccine.class);
                                    startActivity(I);
                                    finish();
                                }
                            });

                    builder.setNegativeButton(getString(android.R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });


                    builder.show();

                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                curDate = String.valueOf(d) + '/' + String.valueOf(month+1) + '/' + String.valueOf(year);
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        System.gc();
        Intent I = new Intent(AddVaccineDropdown.this, Vaccine.class);
        startActivity(I);
        finish();
    }
}
