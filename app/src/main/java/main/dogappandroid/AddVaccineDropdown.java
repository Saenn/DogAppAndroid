package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
    private String[] vaccineList;
    private Button confirmButton;
    private String curDate;
    private int selectedValue;
    private CalendarView calendarView;
    public static final DecimalFormat mFormat = new DecimalFormat("00");
    private DBHelper mHelper;
    private int ID = -1;
    private Bundle prevBundle;
    private SharedPreferences preferences;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccine_dropdown);
        prevBundle = getIntent().getExtras();

        // Setup var //
        preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        getVaccineList(preferences.getString("lang", "th"));
        confirmButton = findViewById(R.id.vaccine_dropdown_confirmbutton);
        calendarView = findViewById(R.id.addvaccine_dropdown_calendar);
        mHelper = new DBHelper(this);
        selectedValue = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        curDate = sdf.format(new Date(calendarView.getDate()));


        // Setup Spinner //
        vaccineSpinner = findViewById(R.id.vaccine_dropdown_spinner);
        ArrayAdapter<String> adapterVaccine = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vaccineList);
        adapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccineSpinner.setAdapter(adapterVaccine);

        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddVaccineDropdown.this, position + "", Toast.LENGTH_LONG).show();
                selectedValue = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedValue == -1 || curDate.equals("")) {
                    Toast.makeText(AddVaccineDropdown.this, "No vaccine is selected.", Toast.LENGTH_LONG).show();
                } else {
                    DogVaccine vaccine = new DogVaccine();
                    vaccine.setName(getNameFromPosition(selectedValue));
                    vaccine.setPosition(selectedValue);
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

    private void getVaccineList(String lang) {
        Context context = LocalHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        vaccineList = resources.getStringArray(R.array.vaccineList);
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

    private String getNameFromPosition(int position) {
        String output = "";
        switch (position) {
            case 0: {
                output = "Rabies";
                break;
            }
            case 1: {
                output = "Canine Distemper Virus";
                break;
            }
            case 2: {
                output = "Canine hepatitis Adenovirus type 2";
                break;
            }
            case 3: {
                output = "Parvovirus/Coronavirus";
                break;
            }
            case 4: {
                output = "Parainfluenza";
                break;
            }
            case 5: {
                output = "Leptospirosis";
                break;
            }
            case 6: {
                output = "Canine 5-way";
                break;
            }
            case 7: {
                output = "Others";
                break;
            }
        }
        return output;
    }

    protected void editVaccine() {
        // TODO: 19-Apr-19 edit vaccine doesn't support new vaccine list
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
