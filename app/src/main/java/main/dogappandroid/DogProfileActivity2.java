package main.dogappandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DogProfileActivity2 extends AppCompatActivity {

    private ImageView dogImage;
    private TextView name, age, gender, color, breed, address, subdistrict, district, province,
            status, pregnant, children, death, missing, sterilized, updated;
    private LinearLayout pregnantLayout, childrenLayout, deathLayout, missingLayout, sterilizedLayout;
    private ImageButton editProfileButton;
    private RecyclerView vaccineRecycler;
    private RecyclerView.Adapter vaccineAdapter;
    private RecyclerView.LayoutManager vaccineLayoutManager;

    private DBHelper dbHelper;
    private Dog dog;
    private DogImage dogImageData;
    private DogInformation dogInformation;
    private List<DogVaccine> vaccines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile_2);
        dbHelper = new DBHelper(this);
        bindImageView();
        bindTextView();
        bindLinearLayout();
        bindImageButton();
        queryFromDB();
        setAllButton();
        for(DogVaccine dv : vaccines){
            Log.d("This is vaccine",dv.getDogID() + " / " + dv.getName() + " / " + dv.getDate());
        }
        bindRecyclerView();
    }
    private void bindImageButton(){
        editProfileButton = (ImageButton) findViewById(R.id.edit_dog_button);

    }

    private void bindImageView() {
        dogImage = (ImageView) findViewById(R.id.profile_dog_image);
    }

    private void bindTextView() {
        name = (TextView) findViewById(R.id.profile_dog_name);
        age = (TextView) findViewById(R.id.profile_dog_age);
        gender = (TextView) findViewById(R.id.profile_dog_gender);
        color = (TextView) findViewById(R.id.profile_dog_color);
        breed = (TextView) findViewById(R.id.profile_dog_breed);
        address = (TextView) findViewById(R.id.profile_dog_address);
        subdistrict = (TextView) findViewById(R.id.profile_dog_subdistrict);
        district = (TextView) findViewById(R.id.profile_dog_district);
        province = (TextView) findViewById(R.id.profile_dog_province);
        status = (TextView) findViewById(R.id.profile_dog_status);
        pregnant = (TextView) findViewById(R.id.profile_dog_pregnant);
        children = (TextView) findViewById(R.id.profile_dog_child);
        death = (TextView) findViewById(R.id.profile_dog_death);
        missing = (TextView) findViewById(R.id.profile_dog_missing);
        sterilized = (TextView) findViewById(R.id.profile_dog_sterilized);
        updated = (TextView) findViewById(R.id.profile_dog_submit);
    }

    private void bindLinearLayout() {
        pregnantLayout = (LinearLayout) findViewById(R.id.pregnantLayout);
        childrenLayout = (LinearLayout) findViewById(R.id.childLayout);
        deathLayout = (LinearLayout) findViewById(R.id.deathLayout);
        missingLayout = (LinearLayout) findViewById(R.id.missingLayout);
        sterilizedLayout = (LinearLayout) findViewById(R.id.sterilizedLayout);
    }

    private void bindRecyclerView() {
        vaccineRecycler = (RecyclerView) findViewById(R.id.dog_profile_vaccine_recycler);
//        setup layout manager
        vaccineLayoutManager = new LinearLayoutManager(DogProfileActivity2.this);
        vaccineRecycler.setLayoutManager(vaccineLayoutManager);
//        set adapter
        Log.d("before adapter", vaccines.size() + "");
        vaccineAdapter = new RecyclerViewAdapter(vaccines);
        vaccineRecycler.setAdapter(vaccineAdapter);
    }

    private void queryFromDB() {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("internalDogID")) {
            dog = dbHelper.getDogById(extras.getInt("internalDogID"));
            dogImageData = dbHelper.getDogFrontImageById(extras.getInt("internalDogID"));
            dogInformation = dbHelper.getAllDogInformationByDogID(extras.getInt("internalDogID"));
            vaccines = dbHelper.getRabiesVaccineListById(extras.getInt("internalDogID"));
            vaccines.addAll(dbHelper.getOtherVaccineListById(extras.getInt("internalDogID")));
            Log.i("DogVaccineList", vaccines.size() + "");
            setAllFields();
        }
    }

    private void setAllFields() {
        name.setText(dog.getName());
        if (dog.getAgeRange().equals("1")) {
            age.setText("Puppy (" + dog.getAge() + ")");
        } else {
            age.setText("Adult (" + dog.getAge() + ")");
        }
        gender.setText(dog.getGender());
        color.setText(dog.getColor());
        breed.setText(dog.getBreed());
        address.setText(dog.getAddress());
        subdistrict.setText(dog.getSubdistrict());
        district.setText(dog.getDistrict());
        province.setText(dog.getProvince());
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<DogProfileActivity2.ViewHolder> {

        private List<DogVaccine> myDataset;

        public RecyclerViewAdapter(List<DogVaccine> mDataset) {
            myDataset = mDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vaccine_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(DogProfileActivity2.ViewHolder holder, int position) {
            final DogVaccine v = myDataset.get(position);
            holder.vaccine.setText(v.getName());
            holder.vaccinatedDate.setText(v.getDate());
        }

        @Override
        public int getItemCount() {
            return myDataset.size();
        }


    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView vaccine, vaccinatedDate;

        public ViewHolder(View v) {
            super(v);
            vaccine = (TextView) v.findViewById(R.id.vaccine_item_name);
            vaccinatedDate = (TextView) v.findViewById(R.id.vaccine_item_date);
        }

    }


    private void setAllButton() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogProfileActivity2.this, EditDomestic.class);
                intent.putExtra("internal_dog_id", dog.getId());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        queryFromDB();
    }



}