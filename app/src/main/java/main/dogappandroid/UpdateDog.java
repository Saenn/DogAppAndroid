package main.dogappandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

public class UpdateDog extends AppCompatActivity {

    private Spinner dogStatus;
    private LinearLayout missingLayout, deathLayout, pregnantLayout, childrenLayout, sterilizedLayout, vaccineLayout, vaccineListLayout;
    private CalendarView sterilizedCalendar, missingCalendar;
    private RadioButton yesSterilized, noSterilized, unknownSterilized, yesPregnant, noPregnant, yesVaccine, noVaccine;
    private CheckBox knownSterilizedDate;
    private RecyclerView vaccineListRecycler;
    private RecyclerView.LayoutManager vaccineLayoutManager;
    private RecyclerView.Adapter vaccineAdapter;
    private List<DogVaccine> vaccineList;
    private String[] vaccineListFromResource;
    private Button addVaccineButton, doneButton;
    private EditText children, deadDescription;
    private TextView sterilizedDateLabel;

    private DBHelper dbHelper;
    private Dog dog;
    private DogInformation dogInformation;
    private SharedPreferences languagePreferences;
    private String sterilizedDateSelected, latestSeenDateSelected, injectionDate;
    private int selectedVaccinePosition;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dog);
        Intent service = new Intent(this, ServiceRunning.class);
        startService(service);

        languagePreferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        Context context = LocalHelper.setLocale(this, languagePreferences.getString("lang", "th"));
        Resources resources = context.getResources();
        vaccineListFromResource = resources.getStringArray(R.array.vaccineList);
        selectedVaccinePosition = -1;

        dbHelper = new DBHelper(this);

        bindData();
        setInitialLayout();
        handleVaccineList();
        handleLayoutButton();
        handleCalendarEvent();

        addVaccineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDog.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog_add_vaccine, null);
                builder.setView(dialogView);

                // Setup Vaccine Spinner
                Spinner vaccineDropdown = dialogView.findViewById(R.id.vaccineDialogSpinner);
                ArrayAdapter<CharSequence> vaccineDropDownAdapter = ArrayAdapter.createFromResource(UpdateDog.this,
                        R.array.vaccineList, android.R.layout.simple_spinner_item);
                vaccineDropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vaccineDropdown.setAdapter(vaccineDropDownAdapter);
                vaccineDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedVaccinePosition = i;
                        Log.i("SelectedPosition", selectedVaccinePosition + "");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                // Setup calendar
                CalendarView injectionCalendar = dialogView.findViewById(R.id.vaccineDialogCalendar);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                injectionDate = sdf.format(injectionCalendar.getDate());
                injectionCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        if (dayOfMonth < 10) injectionDate = "0" + dayOfMonth + "/";
                        else injectionDate = dayOfMonth + "/";
                        if (month < 9) injectionDate += "0" + (month + 1) + "/" + year;
                        else injectionDate += (month + 1) + "/" + year;
                    }
                });


                //get App Locale Language
                SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
                String addText;
                String cancelText;

                // Setup actions
                if (preferences.getString("lang", "th").equals("th")) {
                    addText = "เพิ่ม";
                    cancelText = "ยกเลิก";
                } else {
                    addText = "Add";
                    cancelText = "Cancel";
                }
                builder.setPositiveButton(addText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DogVaccine dogVaccine = new DogVaccine();
                        dogVaccine.setName(getNameFromPosition(selectedVaccinePosition));
                        dogVaccine.setPosition(selectedVaccinePosition);
                        dogVaccine.setDate(injectionDate);
                        dbHelper.addVaccine(dogVaccine);
                        Log.i("UpdateDog-Dog", dogVaccine.getPosition() + "");
                        handleVaccineList();
                    }
                });
                builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
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
                    case "มีชีวิตอยู่":
                        handleAliveOption();
                        break;
                    case "Missing":
                        handleMissingOption();
                        break;
                    case "หาย":
                        handleMissingOption();
                        break;
                    case "Dead":
                        handleDeadOption();
                        break;
                    case "เสียชีวิต":
                        handleDeadOption();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogStatus.getSelectedItem().toString().equals("Alive") ||
                        dogStatus.getSelectedItem().toString().equals("มีชีวิตอยู่")) {
                    if(!yesPregnant.isChecked()
                            && !noPregnant.isChecked()) {
                        Toast.makeText(UpdateDog.this, R.string.pregnent_error_update, Toast.LENGTH_LONG).show();
                    }else if(!yesSterilized.isChecked()
                            && !noSterilized.isChecked()){
                        Toast.makeText(UpdateDog.this, R.string.sterilized_error_update, Toast.LENGTH_LONG).show();
                    }else if(!yesVaccine.isChecked()
                            && !noVaccine.isChecked()){
                        Toast.makeText(UpdateDog.this, R.string.vaccine_error_update, Toast.LENGTH_LONG).show();
                    }else {
                        DogInformation dogInformationTmp = new DogInformation();
                        dogInformationTmp.setDogStatus("1");
                        dogInformationTmp.setAgeRange(dog.getAgeRange());
                        dogInformationTmp.setAge(dog.getAge());
                        if (yesPregnant.isChecked()) {
                            dogInformationTmp.setPregnant(1);
                            try {
                                dogInformationTmp.setChildNumber(Integer.parseInt(children.getText().toString()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (noPregnant.isChecked()) {
                            dogInformationTmp.setPregnant(0);
                        }
                        if (yesSterilized.isChecked()) {
                            dogInformationTmp.setSterilized(1);
                            if (knownSterilizedDate.isChecked()) {
                                dogInformationTmp.setSterilizedDate(sterilizedDateSelected);
                            }
                        } else if (noSterilized.isChecked()) {
                            dogInformationTmp.setSterilized(0);
                        } else if (unknownSterilized.isChecked()) {
                            dogInformationTmp.setSterilized(2);
                        } else {
                            if (dogInformation.getSterilized() == 1) {
                                dogInformationTmp.setSterilized(1);
                                dogInformationTmp.setSterilizedDate(dogInformation.getSterilizedDate());
                            }
                        }
                        dogInformationTmp.setIsSubmit(0);
                        dogInformationTmp.setDogID(getIntent().getExtras().getInt("internalDogID"));
                        dbHelper.addDogInformation(dogInformationTmp);

                        if (yesVaccine.isChecked()) {
                            vaccineList = dbHelper.getRabiesVaccineList();
                            vaccineList.addAll(dbHelper.getOtherVaccineList());
                            for (DogVaccine dv : vaccineList) {
                                dv.setDogID(getIntent().getExtras().getInt("internalDogID"));
                                dbHelper.addVaccine(dv);
                            }
                        }
                        dbHelper.deleteNull();
                        Intent intent = new Intent(UpdateDog.this, DogProfileActivity.class);
                        intent.putExtra("internalDogID", getIntent().getExtras().getInt("internalDogID"));
                        startActivity(intent);
                        finish();
                    }
                } else if (dogStatus.getSelectedItem().toString().equals("Missing") ||
                        dogStatus.getSelectedItem().toString().equals("หาย")) {
                    DogInformation dogInformationTmp = new DogInformation();
                    dogInformationTmp.setDogStatus("2");
                    dogInformationTmp.setMissingDate(latestSeenDateSelected);
                    dogInformationTmp.setDogID(getIntent().getExtras().getInt("internalDogID"));
                    dbHelper.addDogInformation(dogInformationTmp);

                    dbHelper.deleteNull();
                    Intent intent = new Intent(UpdateDog.this, DogProfileActivity.class);
                    intent.putExtra("internalDogID", getIntent().getExtras().getInt("internalDogID"));
                    startActivity(intent);
                    finish();
                } else if (dogStatus.getSelectedItem().toString().equals("Dead") ||
                        dogStatus.getSelectedItem().toString().equals("เสียชีวิต")) {
                    DogInformation dogInformationTmp = new DogInformation();
                    dogInformationTmp.setDogStatus("3");
                    dogInformationTmp.setDeathRemark(deadDescription.getText().toString());
                    dogInformationTmp.setDogID(getIntent().getExtras().getInt("internalDogID"));
                    dbHelper.addDogInformation(dogInformationTmp);

                    dbHelper.deleteNull();
                    Intent intent = new Intent(UpdateDog.this, DogProfileActivity.class);
                    intent.putExtra("internalDogID", getIntent().getExtras().getInt("internalDogID"));
                    startActivity(intent);
                    finish();
                }

            }
        });

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

    private void handleLayoutButton() {
        yesSterilized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sterilizedDateLabel.setVisibility(View.VISIBLE);
                knownSterilizedDate.setVisibility(View.VISIBLE);
            }
        });

        noSterilized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sterilizedCalendar.setVisibility(View.GONE);
                sterilizedDateLabel.setVisibility(View.GONE);
                knownSterilizedDate.setVisibility(View.GONE);
                knownSterilizedDate.setChecked(false);
                sterilizedCalendar.setVisibility(View.GONE);
            }
        });

        unknownSterilized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sterilizedCalendar.setVisibility(View.GONE);
                sterilizedDateLabel.setVisibility(View.GONE);
                knownSterilizedDate.setVisibility(View.GONE);
                knownSterilizedDate.setChecked(false);
                sterilizedCalendar.setVisibility(View.GONE);
            }
        });

        knownSterilizedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (knownSterilizedDate.isChecked()) {
                    sterilizedCalendar.setVisibility(View.VISIBLE);
                } else {
                    sterilizedCalendar.setVisibility(View.GONE);
                }
            }
        });

        yesPregnant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childrenLayout.setVisibility(View.VISIBLE);
            }
        });

        noPregnant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childrenLayout.setVisibility(View.GONE);
            }
        });

        yesVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaccineListLayout.setVisibility(View.VISIBLE);
            }
        });
        noVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaccineListLayout.setVisibility(View.GONE);
            }
        });
    }

    private void bindData() {
        dog = dbHelper.getDogById(getIntent().getExtras().getInt("internalDogID"));
        dogInformation = dbHelper.getLastestDogInformationByDogID(getIntent().getExtras().getInt("internalDogID"));
        dogStatus = findViewById(R.id.dogStatus);
        missingLayout = findViewById(R.id.missingLayout);
        deathLayout = findViewById(R.id.deathLayout);
        pregnantLayout = findViewById(R.id.pregnantLayout);
        childrenLayout = findViewById(R.id.childrenLayout);
        sterilizedLayout = findViewById(R.id.sterilizedLayout);
        vaccineLayout = findViewById(R.id.vaccineLayout);
        vaccineListLayout = findViewById(R.id.vaccineListLayout);
        sterilizedCalendar = findViewById(R.id.sterilizedCalendar);
        missingCalendar = findViewById(R.id.lastestSeenDate);
        yesSterilized = findViewById(R.id.yesSterilized);
        noSterilized = findViewById(R.id.noSterilized);
        yesPregnant = findViewById(R.id.yesPregnant);
        noPregnant = findViewById(R.id.noPregnant);
        yesVaccine = findViewById(R.id.yesVaccine);
        noVaccine = findViewById(R.id.noVaccine);
        addVaccineButton = findViewById(R.id.addVaccineButton);
        doneButton = findViewById(R.id.doneButton);
        vaccineListRecycler = findViewById(R.id.updateDogVaccineList);
        children = findViewById(R.id.children);
        deadDescription = findViewById(R.id.deathRemark);
        unknownSterilized = findViewById(R.id.unknownSterilized);
        knownSterilizedDate = findViewById(R.id.knownSterilized);
        sterilizedDateLabel = findViewById(R.id.sterilizedDateLabel);
    }

    private void handleCalendarEvent() {
        sterilizedCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (dayOfMonth < 10) sterilizedDateSelected = "0" + dayOfMonth + "/";
                else sterilizedDateSelected = dayOfMonth + "/";
                if (month < 9) sterilizedDateSelected += "0" + (month + 1) + "/" + year;
                else sterilizedDateSelected += (month + 1) + "/" + year;
            }
        });

        missingCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (dayOfMonth < 10) latestSeenDateSelected = "0" + dayOfMonth + "/";
                else latestSeenDateSelected = dayOfMonth + "/";
                if (month < 9) latestSeenDateSelected += "0" + (month + 1) + "/" + year;
                else latestSeenDateSelected += (month + 1) + "/" + year;
            }
        });
    }

    private void setInitialLayout() {
        missingLayout.setVisibility(View.GONE);
        deathLayout.setVisibility(View.GONE);
        pregnantLayout.setVisibility(View.GONE);
        childrenLayout.setVisibility(View.GONE);
        sterilizedLayout.setVisibility(View.GONE);
        sterilizedDateLabel.setVisibility(View.GONE);
        knownSterilizedDate.setVisibility(View.GONE);
        sterilizedCalendar.setVisibility(View.GONE);
        vaccineLayout.setVisibility(View.GONE);
        vaccineListLayout.setVisibility(View.GONE);
        if (dog.getDogType().equals("1")) {
            unknownSterilized.setVisibility(View.GONE);
        }
    }

    private void handleVaccineList() {
        vaccineList = dbHelper.getRabiesVaccineList();
        vaccineList.addAll(dbHelper.getOtherVaccineList());
        vaccineLayoutManager = new LinearLayoutManager(UpdateDog.this);
        vaccineListRecycler.setLayoutManager(vaccineLayoutManager);
        vaccineAdapter = new UpdateDog.RecyclerViewAdapter(vaccineList);
        vaccineListRecycler.setAdapter(vaccineAdapter);
    }

    private void handleAliveOption() {
        setInitialLayout();
        if (dog.getGender().equals("F")) {
            pregnantLayout.setVisibility(View.VISIBLE);
        }
        if (dogInformation.getSterilized() != 1) { // dog has never been sterilized before
            sterilizedLayout.setVisibility(View.VISIBLE);
        }
        vaccineLayout.setVisibility(View.VISIBLE);
    }

    private void handleMissingOption() {
        setInitialLayout();
        missingLayout.setVisibility(View.VISIBLE);
    }

    private void handleDeadOption() {
        setInitialLayout();
        deathLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHelper.deleteNull();
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<DogVaccine> myDataset;

        public RecyclerViewAdapter(List<DogVaccine> mDataset) {
            myDataset = mDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vaccine_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final DogVaccine v = myDataset.get(position);
            holder.vaccine.setText(vaccineListFromResource[v.getPosition()]);
            holder.vaccinatedDate.setText(v.getDate());
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(UpdateDog.this);
                    builder.setMessage(getResources().getString(R.string.delete_vaccine));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbHelper.deleteVaccine(String.valueOf(v.getId()));
                            reload();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return myDataset.size();
        }


    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView vaccine, vaccinatedDate;

        public ViewHolder(View v) {
            super(v);
            vaccine = v.findViewById(R.id.vaccine_name_label);
            vaccinatedDate = v.findViewById(R.id.vaccine_date);
        }

    }

    public void reload() {
        vaccineList = dbHelper.getRabiesVaccineList();
        vaccineList.addAll(dbHelper.getOtherVaccineList());
        vaccineAdapter = new UpdateDog.RecyclerViewAdapter(vaccineList);
        vaccineListRecycler.setAdapter(vaccineAdapter);
    }
}
