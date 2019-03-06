package main.dogappandroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vaccine extends AppCompatActivity {

    private RecyclerView recyclerViewRabies, recyclerViewOthers;
    private RecyclerView.LayoutManager layoutManagerRabies, layoutManagerOther;
    private RecyclerView.Adapter mAdapterRabies, mAdapterOther;
    private List<DogVaccine> rabiesVaccine, othersVaccine;
    private Button addButton, doneButton;
    private DBHelper mHelper;
    private ClickListener rabiesListener, othersListener;
    private ProgressBar bar;
    private int isAdding = 0;
    private Bundle prevBundle;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        // set var //
        mHelper = new DBHelper(this);
        rabiesVaccine = new ArrayList<DogVaccine>();
        othersVaccine = new ArrayList<DogVaccine>();
        addButton = (Button) findViewById(R.id.vaccine_addbutton);
        doneButton = (Button) findViewById(R.id.vaccine_doneButton);
        bar = (ProgressBar) findViewById(R.id.vaccine_progressbar);
        prevBundle = getIntent().getExtras();

        if (prevBundle != null && !prevBundle.containsKey("addingdog")) {
            bar.setVisibility(View.GONE);
            if (prevBundle.containsKey("internal_dog_id")) {
                Log.i("มาจกาหน้า edit ", " ครับ");
                rabiesVaccine = mHelper.getRabiesVaccineListById(prevBundle.getInt("internal_dog_id"));
                othersVaccine = mHelper.getOtherVaccineListById(prevBundle.getInt("internal_dog_id"));

                doneButton.setVisibility(View.GONE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Vaccine.this, AddVaccineDropdown.class);
                        intent.putExtras(prevBundle);
                        intent.putExtra("isAdding", isAdding);
                        intent.putExtra("internal_dog_id", prevBundle.getInt("internal_dog_id"));
                        startActivity(intent);
                        finish();
                    }
                });

            }
        } else {
            // มาจากหน้า add dog //
            Log.i("มาจากหน้า add dog", " ครับ");
            isAdding = 1;
            rabiesVaccine = mHelper.getRabiesVaccineList();
            othersVaccine = mHelper.getOtherVaccineList();
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Vaccine.this, AddVaccineDropdown.class);
                    intent.putExtras(prevBundle);
                    intent.putExtra("isAdding", isAdding);
                    startActivity(intent);
                    finish();
                }
            });

            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
                        Toast.makeText(Vaccine.this, "Calibrating location ...", Toast.LENGTH_LONG).show();
                    } else {
                        int newDogIndex = (int) mHelper.addDog(new Dog(prevBundle));
                        DogInformation newDogInfo = new DogInformation();
                        newDogInfo.setDogID(newDogIndex);
                        newDogInfo.setDogType(prevBundle.getString("dogType"));
                        if (prevBundle.getInt("age", -1) != -1)
                            newDogInfo.setAge(prevBundle.getInt("age"));
                        newDogInfo.setAgeRange(prevBundle.getString("ageRange"));
                        newDogInfo.setAddress(prevBundle.getString("address"));
                        newDogInfo.setSubdistrict(prevBundle.getString("subdistrict"));
                        newDogInfo.setDistrict(prevBundle.getString("district"));
                        newDogInfo.setProvince(prevBundle.getString("province"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String submitDate = dateFormat.format(new Date());
                        newDogInfo.setSubmitDate(submitDate);
                        newDogInfo.setLatitude(latitude);
                        newDogInfo.setLongitude(longitude);
                        newDogInfo.setIsSubmit(0);
                        long dogInfoIndex = mHelper.addDogInformation(newDogInfo);
                        Log.i("ข้อมูลหมา-ID", String.valueOf(dogInfoIndex));
                        Log.i("ข้อมูลหมา-dogID", String.valueOf(newDogIndex));
                        Log.i("ข้อมูลหมา-dogtype", prevBundle.getString("dogType"));
                        Log.i("ข้อมูลหมา-age", prevBundle.getInt("age") + "");
                        Log.i("ข้อมูลหมา-agerange", newDogInfo.getAgeRange());
                        Log.i("ข้อมูลหมา-address", prevBundle.getString("address"));
                        Log.i("ข้อมูลหมา-subdistrict", prevBundle.getString("subdistrict"));
                        Log.i("ข้อมูลหมา-district", prevBundle.getString("district"));
                        Log.i("ข้อมูลหมา-province", prevBundle.getString("province"));
                        Log.i("ข้อมูลหมา-submitDate", submitDate);
                        Log.i("ข้อมูลหมา-lat", latitude + "");
                        Log.i("ข้อมูลหมา-long", longitude + "");
                        for (DogVaccine v : rabiesVaccine) {
                            v.setDogID(newDogIndex);
                            mHelper.updateVaccine(v);
                        }
                        for (DogVaccine v : othersVaccine) {
                            v.setDogID(newDogIndex);
                            mHelper.updateVaccine(v);
                        }
                        List<DogVaccine> dvr = mHelper.getRabiesVaccineListById(newDogIndex);
                        List<DogVaccine> dvo = mHelper.getOtherVaccineListById(newDogIndex);
                        for (DogVaccine v : dvr) {
                            Log.i("DogVaccineRabies", v.getId() + " " + v.getDate());
                        }
                        for (DogVaccine v : dvo) {
                            Log.i("DogVaccineOthers", v.getId() + " " + v.getDate());
                        }
                        Intent intent = new Intent(Vaccine.this, HomeActivity.class);
                        startActivity(intent);
                        mHelper.deleteNull();
                    }
                }
            });
        }

        // set recycle view //
        recyclerViewRabies = (RecyclerView) findViewById(R.id.vaccine_listview_rabies);
        recyclerViewOthers = (RecyclerView) findViewById(R.id.vaccine_listview_other);

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
            holder.vaccine.setText(v.getName());
            holder.vaccinatedDate.setText(v.getDate());

            holder.setOnClickListener(new ClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (isLongClick) {
                        //LongClick//
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(Vaccine.this);
                        builder.setMessage("Are you sure to delete this vaccine?");
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
                        Intent I = new Intent(Vaccine.this, AddVaccineDropdown.class);
                        I.putExtras(prevBundle);
                        I.putExtra("isAdding", isAdding);
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
        TextView vaccine, vaccinatedDate;
        private ClickListener myListener;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            vaccine = (TextView) v.findViewById(R.id.vaccine_name);
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

    @Override
    public void onBackPressed() {
        mHelper.deleteNull();
        finish();
    }
}


