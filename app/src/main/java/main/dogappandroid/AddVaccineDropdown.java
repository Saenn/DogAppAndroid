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

import java.text.DecimalFormat;
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
    public static final DecimalFormat mFormat= new DecimalFormat("00");
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
                    if(ID == -1) {
                        builder.setTitle(getString(R.string.addvaccinetitle));
                        builder.setMessage(getString(R.string.addmessage));
                    }
                    else{
                        builder.setTitle(getString(R.string.editvaccine));
                        builder.setMessage(getString(R.string.editvaccinemessage));
                    }

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
                                        Log.i("updating :", String.valueOf(ID));
                                        vaccine.setId(ID);
                                        mHelper.updateVaccine(vaccine);
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
                curDate = mFormat.format(Double.valueOf(d)) + '/' + mFormat.format(Double.valueOf(month+1)) + '/' + String.valueOf(year);
            }
        });

        editVaccine();
    }

    private void addVaccineDataToList() {
        vaccineList.add(0,"Select a Vaccine");
        vaccineList.add(1,"Rabies");
        vaccineList.add(2,"DHPP");
        vaccineList.add(3,"Distemper");
        vaccineList.add(4,"Measles");
        vaccineList.add(5,"Parainfluenza");
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

    protected void editVaccine(){

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            ID = bundle.getInt("vid");
            String vname = bundle.getString("vname");
            String vdate = bundle.getString("vdate");
            Log.i("Vname :" , vname);
            String parts[] = vdate.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            long milliTime = calendar.getTimeInMillis();
            if(vname.equals("Rabies")){
                vaccineSpinner.setSelection(1);

            }
            else if(vname.equals("DHPP")){
                vaccineSpinner.setSelection(2);

            }
            else if(vname.equals("Distemper")){
                vaccineSpinner.setSelection(3);

            }
            else if(vname.equals("Measles")){
                vaccineSpinner.setSelection(4);

            }
            else if(vname.equals("Parainfluenza")){
                vaccineSpinner.setSelection(5);
            }

            calendarView.setDate(milliTime,true,true);
        }
    }
}
