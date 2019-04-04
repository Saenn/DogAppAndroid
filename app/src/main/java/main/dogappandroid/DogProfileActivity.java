package main.dogappandroid;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DogProfileActivity extends AppCompatActivity {

    private ImageView dogImage;
    private TextView name, age, gender, color, breed, address, subdistrict, district, province,
            status, pregnant, children, death, missing, sterilized;
    private LinearLayout pregnantLayout, childrenLayout, deathLayout, missingLayout, sterilizedLayout;
    private Button editButton, updateButton;
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
        setContentView(R.layout.activity_dog_profile);
        dbHelper = new DBHelper(this);
        bindData();
        retriveDogDataFromInternalDB();
        showDogStatus();
        showDogData();
        if (vaccines != null) {
            showVaccineList();
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DogProfileActivity.this, UpdateDog.class);
                intent.putExtra("internalDogID", dog.getId());
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dog.getDogType().equals("1") || dog.getDogType().equals("2")) {
                    Intent intent = new Intent(DogProfileActivity.this, EditDomestic.class);
                    intent.putExtra("internalDogID", dog.getId());
                    startActivity(intent);
                } else if (dog.getDogType().equals("3")) {
                    // TODO: Add Edit Stray
                }
            }
        });
    }

    private void bindData() {
        dogImage = (ImageView) findViewById(R.id.dogProfilePicture);
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
        pregnantLayout = (LinearLayout) findViewById(R.id.pregnantLayout);
        childrenLayout = (LinearLayout) findViewById(R.id.childLayout);
        deathLayout = (LinearLayout) findViewById(R.id.deathLayout);
        missingLayout = (LinearLayout) findViewById(R.id.missingLayout);
        sterilizedLayout = (LinearLayout) findViewById(R.id.sterilizedLayout);
        vaccineRecycler = (RecyclerView) findViewById(R.id.dog_profile_vaccine_recycler);
        editButton = (Button) findViewById(R.id.editDogButton);
        updateButton = (Button) findViewById(R.id.updateDogButton);
    }

    private void retriveDogDataFromInternalDB() {
        Bundle extras = getIntent().getExtras();
        dog = dbHelper.getDogById(extras.getInt("internalDogID"));
        dogImageData = dbHelper.getDogFrontImageById(extras.getInt("internalDogID"));
        dogInformation = dbHelper.getLastestDogInformationByDogID(extras.getInt("internalDogID"));
        vaccines = dbHelper.getRabiesVaccineListById(extras.getInt("internalDogID"));
        vaccines.addAll(dbHelper.getOtherVaccineListById(extras.getInt("internalDogID")));
    }

    private void showVaccineList() {
        vaccineLayoutManager = new LinearLayoutManager(DogProfileActivity.this);
        vaccineRecycler.setLayoutManager(vaccineLayoutManager);
        vaccineAdapter = new RecyclerViewAdapter(vaccines);
        vaccineRecycler.setAdapter(vaccineAdapter);
    }

    private void showDogData() {
        dogImage.setImageBitmap(BitmapFactory.decodeByteArray(dogImageData.getKeyImage(), 0, dogImageData.getKeyImage().length));
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

    private void showDogStatus() {
        if (dogInformation.getDogStatus().equals("1")) {
            status.setText("Alive");
            deathLayout.setVisibility(View.GONE);
            missingLayout.setVisibility(View.GONE);
            if (dog.getGender().equals("M")) {
                pregnantLayout.setVisibility(View.GONE);
                childrenLayout.setVisibility(View.GONE);
            } else if (dog.getGender().equals("F")) {
                pregnantLayout.setVisibility(View.VISIBLE);
                if (dogInformation.getPregnant() == 0) {
                    pregnant.setText("Not pregnant");
                } else if (dogInformation.getPregnant() == 1) {
                    pregnant.setText("Pregnant");
                    children.setText(String.valueOf(dogInformation.getChildNumber()));
                }
            }
            if (dogInformation.getSterilized() == 0) {
                sterilized.setText("Not yet");
            } else {
                sterilized.setText("on " + dogInformation.getSterilizedDate());
            }
        } else if (dogInformation.getDogStatus().equals("2")) {
            status.setText("Missing");
            deathLayout.setVisibility(View.GONE);
            pregnantLayout.setVisibility(View.GONE);
            childrenLayout.setVisibility(View.GONE);
            sterilizedLayout.setVisibility(View.GONE);
            missing.setText(dogInformation.getMissingDate());
        } else if (dogInformation.getDogStatus().equals("3")) {
            status.setText("Dead");
            missingLayout.setVisibility(View.GONE);
            pregnantLayout.setVisibility(View.GONE);
            childrenLayout.setVisibility(View.GONE);
            sterilizedLayout.setVisibility(View.GONE);
            death.setText(dogInformation.getDeathRemark());
        }
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<DogProfileActivity.ViewHolder> {

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
        public void onBindViewHolder(DogProfileActivity.ViewHolder holder, int position) {
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

    @Override
    public void onResume() {
        super.onResume();
        showDogStatus();
        showDogData();
    }


}