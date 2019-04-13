package main.dogappandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddStray4 extends AppCompatActivity {

    private RecyclerView recyclerViewRabies, recyclerViewOthers;
    private RecyclerView.LayoutManager layoutManagerRabies, layoutManagerOther;
    private RecyclerView.Adapter mAdapterRabies, mAdapterOther;
    private List<DogVaccine> rabiesVaccine, othersVaccine;
    private Button addButton, doneButton;
    private DBHelper mHelper;
    private ClickListener rabiesListener, othersListener;
    private ProgressBar bar;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stray4);
        Intent service = new Intent(this, ServiceRunning.class);
        startService(service);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i("new location", location.getLatitude() + " " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // set var //
        mHelper = new DBHelper(this);
        rabiesVaccine = new ArrayList<DogVaccine>();
        othersVaccine = new ArrayList<DogVaccine>();
        addButton = (Button) findViewById(R.id.addstray4_addbutton);
        doneButton = (Button) findViewById(R.id.addstray4_doneButton);
        bar = (ProgressBar) findViewById(R.id.addstray4_progressbar);

        rabiesVaccine = mHelper.getRabiesVaccineList();
        othersVaccine = mHelper.getOtherVaccineList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                Intent intent = new Intent(AddStray4.this, AddVaccineDropdown.class);
                intent.putExtra("fromstray", "1");
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
                    Toast.makeText(AddStray4.this, "Calibrating location ...", Toast.LENGTH_LONG).show();
                } else {
                    Bundle extras = getIntent().getExtras();
//                    insert data into dog table -- prepare data
                    Dog dog = new Dog();
                    dog.setDogType(extras.getString("dogType"));
                    dog.setGender(extras.getString("gender"));
                    if (extras.getString("color") != "") dog.setColor(extras.getString("color"));
                    if (extras.getString("name") != "") dog.setName(extras.getString("name"));
                    if (extras.getString("breed") != "") dog.setBreed(extras.getString("breed"));
                    dog.setAge(-1);
                    dog.setAgeRange(extras.getString("ageRange"));
                    dog.setAddress(extras.getString("address"));
                    dog.setSubdistrict(extras.getString("subdistrict"));
                    dog.setDistrict(extras.getString("district"));
                    dog.setProvince(extras.getString("province"));
                    dog.setLatitude(latitude);
                    dog.setLongitude(longitude);
                    dog.setDogID(-1);
                    dog.setIsSubmit(0);

                    int newDogID = (int) mHelper.addDog(dog);
                    if (newDogID == -1) {
                        Toast.makeText(AddStray4.this, "there is some conflict occur, please try again.", Toast.LENGTH_LONG).show();
                    } else {
                        DogInformation dogInformation = new DogInformation();
                        dogInformation.setDogID(newDogID);
                        dogInformation.setDogStatus("1"); // 1 = stay with owner, 2 = missing, 3 = dead
                        if (extras.getString("sterilized").equals("0")) {
                            dogInformation.setSterilized(0);
                        } else if (extras.getString("sterilized").equals("1")) {
                            dogInformation.setSterilized(1);
                            dogInformation.setSterilizedDate(extras.getString("sterilizedDate"));
                        } else if (extras.getString("sterilized").equals("2")) {
                            dogInformation.setSterilized(2);
                        }
                        if (extras.getInt("age", -1) != -1)
                            dogInformation.setAge(extras.getInt("age"));
                        dogInformation.setAgeRange(extras.getString("ageRange"));
                        int newDogInfo = (int) mHelper.addDogInformation(dogInformation);
                        if (newDogInfo == -1) {
                            Toast.makeText(AddStray4.this, "there is some conflict occur, please try again.", Toast.LENGTH_LONG).show();
                            // TODO: 13-Apr-19 delete dog from internal db
                        }
                        for (DogVaccine v : rabiesVaccine) {
                            v.setDogID(newDogID);
                            mHelper.updateVaccine(v);
                        }
                        for (DogVaccine v : othersVaccine) {
                            v.setDogID(newDogID);
                            mHelper.updateVaccine(v);
                        }
                        List<DogVaccine> dvr = mHelper.getRabiesVaccineListById(newDogID);
                        List<DogVaccine> dvo = mHelper.getOtherVaccineListById(newDogID);
                        for (DogVaccine v : dvr) {
                            Log.i("DogVaccineRabies", v.getId() + " " + v.getDate());
                        }
                        for (DogVaccine v : dvo) {
                            Log.i("DogVaccineOthers", v.getId() + " " + v.getDate());
                        }
                        addPicToSqlite(extras.getString("frontview"), 1, newDogID);
                        addPicToSqlite(extras.getString("sideview"), 2, newDogID);
                    }
                    Intent intent = new Intent(AddStray4.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    mHelper.deleteNull();
                    finish();
                }
            }
        });

        // set recycle view //
        recyclerViewRabies = (RecyclerView) findViewById(R.id.addstray4_listview_rabies);
        recyclerViewOthers = (RecyclerView) findViewById(R.id.addstray4_listview_other);
        layoutManagerRabies = new LinearLayoutManager(this);
        recyclerViewRabies.setLayoutManager(layoutManagerRabies);
        mAdapterRabies = new RecyclerViewAdapter(rabiesVaccine);
        recyclerViewRabies.setAdapter(mAdapterRabies);
        layoutManagerOther = new LinearLayoutManager(this);
        recyclerViewOthers.setLayoutManager(layoutManagerOther);
        mAdapterOther = new RecyclerViewAdapter(othersVaccine);
        recyclerViewOthers.setAdapter(mAdapterOther);
    }

    // Recycler class //
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
            holder.addstray4.setText(v.getName());
            holder.vaccinatedDate.setText(v.getDate());

            holder.setOnClickListener(new ClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (isLongClick) {
                        //LongClick//
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(AddStray4.this);
                        builder.setMessage("Are you sure to delete this addstray4?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // DB Helper Delete //

                                mHelper.deleteVaccine(String.valueOf(v.getId()));
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

                    } else {
                        //not LongClick
                        Intent I = new Intent(AddStray4.this, AddVaccineDropdown.class);
                        Bundle extras = getIntent().getExtras();
                        I.putExtras(extras);
                        I.putExtra("isAdding", 1);
                        I.putExtra("vid", v.getId());
                        I.putExtra("vname", v.getName());
                        I.putExtra("vdate", v.getDate());
                        startActivity(I);
                        finish();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return myDataset.size();
        }


    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
        // each data item is just a string in this case
        TextView addstray4, vaccinatedDate;
        private ClickListener myListener;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            addstray4 = (TextView) v.findViewById(R.id.vaccine_name_label);
            vaccinatedDate = (TextView) v.findViewById(R.id.vaccine_date);
        }

        @Override
        public void onClick(View v) {
            myListener.onClick(v, getAdapterPosition(), false, null);
        }

        @Override
        public boolean onLongClick(View view) {
            myListener.onClick(view, getAdapterPosition(), true, null);
            return true;
        }

        public void setOnClickListener(ClickListener listener) {
            this.myListener = listener;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            myListener.onClick(view, getAdapterPosition(), false, motionEvent);
            return true;
        }

    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void addPicToSqlite(String imagePath, int type, int dogInternalID) {
        DogImage dogImage = new DogImage();
        dogImage.setDogInternalId(dogInternalID);
        if (type == 1) {
            dogImage.setType(1);
        } else {
            dogImage.setType(2);
        }
        dogImage.setImagePath(imagePath);
        dogImage.setIsSubmit(0);
        mHelper.addDogImage(dogImage);

    }
}
