package main.dogappandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import main.dogappandroid.Utilities.NetworkUtils;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Dog> mDataset;
    private DBHelper mHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        mHelper = new DBHelper(this);
        mDataset = mHelper.getDog();

//        handle recycler view
        recyclerView = (RecyclerView) findViewById(R.id.dogListView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DogListAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);

//        handle navigation bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout navigationHeader = (LinearLayout) navigationView.getHeaderView(0);
        navigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfile = new Intent(HomeActivity.this, UserProfile.class);
                startActivity(userProfile);
            }
        });


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_domestic:
                        Intent addDomestic = new Intent(HomeActivity.this, AddDomestic.class);
                        startActivity(addDomestic);
                        return true;
                    case R.id.add_stray:
                        Intent addStray = new Intent(HomeActivity.this, AddStray.class);
                        startActivity(addStray);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logout() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
        Intent login = new Intent(HomeActivity.this, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataset = mHelper.getDog();
        mAdapter = new DogListAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
        if (isNetworkAvailable()) {
            for (Dog dog : mDataset) {
                if (dog.getIsSubmit() == 0 && dog.getDogID() == -1) {
                    new addNewDog().execute(dog);
                } else if (dog.getIsSubmit() == 0) {
                    new updateExistingDog().execute(dog);
                }
                if (dog.getDogID() != -1) {
                    List<DogInformation> dogInformations = mHelper.getAllDogInformationByDogID(dog.getId());
                    for (DogInformation di : dogInformations) {
                        if (di.getIsSubmit() == 0) {
                            Log.i("DogInformation", "Submit new dog information");
                            new addNewDogInformation().execute(di);
                        } else {
                            Log.i("DogInformation", "Already Submitted this data");
                        }
                    }

                    List<DogVaccine> dogVaccineList = mHelper.getRabiesVaccineListById(dog.getId());
                    dogVaccineList.addAll(mHelper.getOtherVaccineListById(dog.getId()));
                    for (DogVaccine dv : dogVaccineList) {
                        if (dv.getIsSubmit() == 0) {
                            Log.i("DogVaccine", "Submit new dog vaccine");
                            new addNewVaccine().execute(dv);
                        } else {
                            Log.i("DogVaccine", "Already submitted this data");
                        }
                    }

                    List<DogImage> dogImageList = mHelper.getDogImageById(dog.getId());
                    for (DogImage di : dogImageList) {
                        if (di.getIsSubmit() == 0) {
                            Log.i("DogImage", "Submit new dog image");
                            new addNewDogImage().execute(di);
                        } else {
                            Log.i("DogImage", "Already submitted this image");
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_news) {

        } else if (id == R.id.nav_report) {
            Intent intent = new Intent(HomeActivity.this, Report.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_sign_out) {
            logout();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class DogListAdapter extends RecyclerView.Adapter<DogListViewHolder> {
        private List<Dog> mDataset;

        public DogListAdapter(List<Dog> mDataset) {
            this.mDataset = mDataset;
        }

        @Override
        public DogListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dog_list_item, parent, false);
            DogListViewHolder vh = new DogListViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(DogListViewHolder holder, int position) {
            final Dog dog = mDataset.get(position);
            final DogInformation info = mHelper.getLastestDogInformationByDogID(dog.getId());
            final DogImage image = mHelper.getDogFrontImageById(dog.getId());

            // need to set image //
            if (dog.getName().equals("")) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setText(dog.getName().toUpperCase());
            }
            if (info.getAge() == -1) {
                if (info.getAgeRange().equals("1")) {
                    holder.age.setText("Age : " + "0-3");
                } else {
                    holder.age.setText("Age : " + "Older than 3");
                }
            } else {
                holder.age.setText("Age : " + info.getAge());
            }
            holder.color.setText("Color : " + dog.getColor());
            holder.gender.setText("Gender : " + dog.getGender());
            holder.breed.setText("Breed :" + dog.getBreed());
            holder.pic.setImageBitmap(mHelper.getImage(image.getKeyImage()));


            holder.setOnClickListener(new ClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

                    if (isLongClick) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage("Are you sure to delete this vaccine?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // DB Helper Delete //

                                mHelper.deleteDog(String.valueOf(dog.getId()));
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
                        Intent I = new Intent(HomeActivity.this, DogProfileActivity.class);
                        I.putExtra("internalDogID", dog.getId());
                        startActivity(I);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    public class DogListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
        // each data item is just a string in this case
        public TextView age, gender, breed, color, name;
        public ImageView pic;
        private ClickListener myListener;

        public DogListViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);
            name = (TextView) v.findViewById(R.id.dog_list_name);
            age = (TextView) v.findViewById(R.id.dog_list_age);
            gender = (TextView) v.findViewById(R.id.dog_list_gender);
            breed = (TextView) v.findViewById(R.id.dog_list_breed);
            color = (TextView) v.findViewById(R.id.dog_list_color);
            pic = (ImageView) v.findViewById(R.id.dog_list_image);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class addNewDog extends AsyncTask<Dog, Void, String> {

        Dog dog;

        @Override
        protected String doInBackground(Dog... dogs) {
            dog = dogs[0];
            return NetworkUtils.addDog(dogs[0],
                    Integer.parseInt(mPreferences.getString("userID", "")),
                    mPreferences.getString("token", ""),
                    mPreferences.getString("username", ""));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    JSONObject sqlResponse = new JSONObject(data);
                    int rdsDogID = sqlResponse.getInt("insertId");
//                check if the data has already been in rds database
                    if (status.equals("Success") && sqlResponse.getInt("affectedRows") == 1) {
                        dog.setIsSubmit(1);
                        dog.setDogID(rdsDogID);
                        mHelper.updateDog(dog);
                        Log.i("AddDog", "AddDog Success : " + dog.getDogID());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class updateExistingDog extends AsyncTask<Dog, Void, String> {

        Dog dog;

        @Override
        protected String doInBackground(Dog... dogs) {
            dog = dogs[0];
            return NetworkUtils.updateDog(dogs[0],
                    Integer.parseInt(mPreferences.getString("userID", "")),
                    mPreferences.getString("token", ""),
                    mPreferences.getString("username", ""));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String data = jsonObject.getString("data");
                JSONObject sqlResponse = new JSONObject(data);
                if (status.equals("Success") && sqlResponse.getInt("affectedRows") == 1) {
                    dog.setIsSubmit(1);
                    mHelper.updateDog(dog);
                    Log.i("UpdateDog", "Update Success : " + dog.getDogID());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class addNewDogInformation extends AsyncTask<DogInformation, Void, String> {
        Dog dog;
        DogInformation dogInformation;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    JSONObject sqlResponse = new JSONObject(data);
                    if (status.equals("Success") && sqlResponse.getInt("affectedRows") == 1) {
                        dogInformation.setIsSubmit(1);
                        mHelper.updateDogInfo(dogInformation);
                        Log.i("DogInformation", "Add Dog Information Success");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(DogInformation... dogInformations) {
            dogInformation = dogInformations[0];
            dog = mHelper.getDogById(dogInformation.getDogID());
            return NetworkUtils.addDogInformation(dogInformation,
                    dog.getDogID(),
                    mPreferences.getString("token", ""),
                    mPreferences.getString("username", ""));
        }
    }

    public class addNewVaccine extends AsyncTask<DogVaccine, Void, String> {
        Dog dog;
        DogVaccine dogVaccine;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if(status.equals("Success")){
                        String data = jsonObject.getString("data");
                        JSONObject sqlResponse = new JSONObject(data);
                        if (sqlResponse.getInt("affectedRows") == 1) {
                            dogVaccine.setIsSubmit(1);
                            mHelper.updateVaccine(dogVaccine);
                            Log.i("DogVaccine", "Add Dog Vaccine Success");
                        }
                    }else{
                        Log.i("DogVaccine", "Add Dog Vaccine Fail");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(DogVaccine... dogVaccines) {
            dogVaccine = dogVaccines[0];
            Log.i("innerDogVaccine", "" + dogVaccine.getDogID());
            dog = mHelper.getDogById(dogVaccine.getDogID());
            return NetworkUtils.addDogVaccine(dogVaccine,
                    dog.getDogID(),
                    mPreferences.getString("token", ""),
                    mPreferences.getString("username", ""));
        }
    }

    public class addNewDogImage extends AsyncTask<DogImage, Void, String> {
        DogImage dogImage;
        Dog dog;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    if(data != null){
                        JSONObject sqlResponse = new JSONObject(data);
                        if (status.equals("Success") && sqlResponse.getInt("affectedRows") == 1) {
                            dogImage.setIsSubmit(1);
                            mHelper.updateDogImage(dogImage, dogImage.getType());
                            Log.i("DogVaccine", "Add Dog Image Success");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(DogImage... dogImages) {
            dogImage = dogImages[0];
            dog = mHelper.getDogById(dogImage.getDog_internal_id());
            return NetworkUtils.addDogImage(HomeActivity.this,
                    dogImage,
                    dog.getDogID(),
                    mPreferences.getString("username", ""),
                    mPreferences.getString("token", "")
            );
        }
    }
}
