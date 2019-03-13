package main.dogappandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DogProfileActivity2 extends AppCompatActivity {

    private ImageView dogImage;
    private TextView name, age, gender, color, breed, address, subdistrict, district, province,
            status, pregnant, children, death, missing, sterilized, updated;
    private LinearLayout pregnantLayout, childrenLayout, deathLayout, missingLayout, sterilizedLayout;

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
        queryFromDB();
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

    private void queryFromDB() {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("internalDogID")) {
            dog = dbHelper.getDogById(extras.getInt("internalDogID"));
            dogImageData = dbHelper.getDogFrontImageById(extras.getInt("internalDogID"));
            dogInformation = dbHelper.getAllDogInformationByDogID(extras.getInt("internalDogID"));
            List<DogVaccine> vaccines = dbHelper.getRabiesVaccineListById(extras.getInt("internalDogID"));
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
}