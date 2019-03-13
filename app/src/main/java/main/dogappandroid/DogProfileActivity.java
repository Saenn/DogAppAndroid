package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DogProfileActivity extends AppCompatActivity {

    private ImageView dogProfilePic;
    private TextView dogName, gender, color, age, breed, sterilized, subdistrict, district, province, address, submittedDate, type;
    private Button editProfileButton, editVaccineButton;
    private Dog dog;
    private TextView vaccine_name1, vaccine_name2, vaccine_date1, vaccine_date2, vaccinehead;
    private LinearLayout vaccine1, vaccine2;
    private List<DogVaccine> vaccines;
    private DogInformation info;
    private DBHelper dbHelper;
    private DogImage image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);

        // setup var //
        dogProfilePic = (ImageView) findViewById(R.id.dog_profile_imageview);
        dogName = (TextView) findViewById(R.id.dog_profile_name);
        editProfileButton = (Button) findViewById(R.id.dog_profile_editprofile_button);
        editVaccineButton = (Button) findViewById(R.id.dog_profile_editvaccine_button);
        gender = (TextView) findViewById(R.id.dog_profile_gender);
        color = (TextView) findViewById(R.id.dog_profile_color);
        age = (TextView) findViewById(R.id.dog_profile_age);
        breed = (TextView) findViewById(R.id.dog_profile_breed);
        sterilized = (TextView) findViewById(R.id.dog_profile_sterilized);
        subdistrict = (TextView) findViewById(R.id.dog_profile_subdistrict);
        district = (TextView) findViewById(R.id.dog_profile_district);
        province = (TextView) findViewById(R.id.dog_profile_province);
        address = (TextView) findViewById(R.id.dog_profile_address);
        submittedDate = (TextView) findViewById(R.id.dog_profile_submitDate);
        type = (TextView) findViewById(R.id.dog_profile_type);
        vaccine_name1 = (TextView) findViewById(R.id.dog_profile_vaccine1_name);
        vaccine_date1 = (TextView) findViewById(R.id.dog_profile_vaccine1_date);
        vaccine_name2 = (TextView) findViewById(R.id.dog_profile_vaccine2_name);
        vaccine_date2 = (TextView) findViewById(R.id.dog_profile_vaccine2_date);
        vaccine1 = (LinearLayout) findViewById(R.id.vaccine1);
        vaccine2 = (LinearLayout) findViewById(R.id.vaccine2);
        vaccinehead = (TextView) findViewById(R.id.vaccinehead);
        dbHelper = new DBHelper(this);
        getDogInfo();
        setAllButton();
        // finished setup var //

    }

    private void getDogInfo() {
        Bundle prevBundle = getIntent().getExtras();
        if (prevBundle != null && prevBundle.containsKey("internal_dog_id")) {
            dog = dbHelper.getDogById(prevBundle.getInt("internal_dog_id"));
            vaccines = dbHelper.getTwoLatestVaccines(prevBundle.getInt("internal_dog_id"));
            info = dbHelper.getAllDogInformationByDogID(prevBundle.getInt("internal_dog_id"));
            image = dbHelper.getDogFrontImageById(prevBundle.getInt("internal_dog_id"));
            int s = vaccines.size();
            if (s == 0) {
                vaccine1.setVisibility(View.GONE);
                vaccine2.setVisibility(View.GONE);
                vaccinehead.setVisibility(View.GONE);
            } else if (s == 1) {
                vaccine2.setVisibility(View.GONE);
                vaccine_name1.setText(vaccines.get(0).getName());
                vaccine_date1.setText(vaccines.get(0).getDate());
            } else {
                vaccine_name1.setText(vaccines.get(0).getName());
                vaccine_date1.setText(vaccines.get(0).getDate());
                vaccine_name2.setText(vaccines.get(1).getName());
                vaccine_date2.setText(vaccines.get(1).getDate());
            }
            setAllField();
        }
    }

    private void setAllField() {

        // need to set name and image //
        dogName.setText(dog.getName());
        gender.setText(dog.getGender());
        color.setText(dog.getColor());
        breed.setText(dog.getBreed());
        if (info.getSterilized() == 1) {
            sterilized.setText("Yes (" + info.getSterilizedDate() + ")");
        } else {
            sterilized.setText("No");
        }

        // info //
        age.setText(String.valueOf(info.getAgeRange()));
        subdistrict.setText(dog.getSubdistrict());
        address.setText(dog.getAddress());
        district.setText(dog.getDistrict());
        province.setText(dog.getProvince());
        type.setText(dog.getDogType());
        dogProfilePic.setImageBitmap(dbHelper.getImage(image.getKeyImage()));
    }

    private void setAllButton() {
        editVaccineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogProfileActivity.this, Vaccine.class);
                intent.putExtra("internal_dog_id", dog.getId());
                startActivity(intent);
                finish();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogProfileActivity.this, EditDomestic.class);
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
        getDogInfo();
    }
}
