package main.dogappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditDomestic extends AppCompatActivity {
    private EditText dogname, dogage, dogbreed, dogcolor,dogaddress,dogsubdistrict;
    private TextView ageView, genderView, nameView, colorage, colorgender;
    private RadioButton maleBtn, femaleBtn;
    private RadioGroup gender;
    private Button doneBtn;
    private DBHelper dbHelper;
    private Dog dog;
    private DogInformation info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_domestic);
        dbHelper = new DBHelper(this);

        dogname = (EditText) findViewById(R.id.nameDomestic);
        dogage = (EditText) findViewById(R.id.ageDomestic);
        dogbreed = (EditText) findViewById(R.id.breedDomestic);
        dogcolor = (EditText) findViewById(R.id.colorDomestic);
        dogaddress = (EditText) findViewById(R.id.addressDomestic);
        dogsubdistrict = (EditText) findViewById(R.id.subdistrictDomestic);
        maleBtn = (RadioButton) findViewById(R.id.maleDomesticButton);
        femaleBtn = (RadioButton) findViewById(R.id.femaleDomesticButton);
        gender = (RadioGroup) findViewById(R.id.genderDomestic);
        doneBtn = (Button) findViewById(R.id.doneDomesticButton);
        genderView = (TextView) findViewById(R.id.genderDomesticLabel);
        ageView = (TextView) findViewById(R.id.ageDomesticLabel);
        nameView = (TextView) findViewById(R.id.nameDomesticLabel);
        colorage = (TextView) findViewById(R.id.addDogRequired);
        colorgender = (TextView) findViewById(R.id.addDogRequired2);

        getEditDogInfo();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dogage.getText().toString().equals("")) {
                    Toast.makeText(EditDomestic.this, "Please enter your puppy's age", Toast.LENGTH_LONG).show();
                } else if (gender.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(EditDomestic.this, "Please enter your puppy's gender", Toast.LENGTH_LONG).show();
                } else {
                    Bundle extras = new Bundle();
                    dog.setName(dogname.getText().toString());
                    extras.putString("name", dogname.getText().toString());
                    if (maleBtn.isChecked()) {
                        dog.setGender("M");
                        extras.putString("gender", "M");
                    }else if (femaleBtn.isChecked()) {
                        dog.setGender("F");
                        extras.putString("gender", "F");
                    }
                    if (Integer.parseInt(dogage.getText().toString()) <= 3) {
                        dog.setAgeRange("1");
                        extras.putInt("age", Integer.parseInt(dogage.getText().toString()));
                        extras.putString("ageRange", "1"); // 1 represent less than or equal to 3
                    } else {
                        dog.setAgeRange("2");
                        extras.putInt("age", Integer.parseInt(dogage.getText().toString()));
                        extras.putString("ageRange", "2"); // 2 represent more than 3
                    }
                    dog.setAge(Integer.parseInt(dogage.getText().toString()));
                    dog.setBreed(dogbreed.getText().toString());
                    dog.setColor(dogcolor.getText().toString());
                    dog.setAddress(dogaddress.getText().toString());
                    dog.setSubdistrict(dogsubdistrict.getText().toString());
                    extras.putString("breed", dogbreed.getText().toString());
                    extras.putString("color", dogcolor.getText().toString());
                    extras.putString("address", dogaddress.getText().toString());
                    extras.putString("subdistrict", dogsubdistrict.getText().toString());

                    Intent DogProfile = new Intent(EditDomestic.this, DogProfileActivity2.class);
                    DogProfile.putExtras(extras);
                    startActivity(DogProfile);
                }
            }
        });
    }

    private void getEditDogInfo(){
        Bundle prevBundle = getIntent().getExtras();
        if (prevBundle != null && prevBundle.containsKey("internal_dog_id")) {
            dog = dbHelper.getDogById(prevBundle.getInt("internal_dog_id"));
            info = dbHelper.getAllDogInformationByDogID(prevBundle.getInt("internal_dog_id"));
            setAllField();
        }
    }

    private void setAllField(){

        // need to set name and image //
        dogname.setText(dog.getName());
        if(dog.getGender().equals("M")){
            maleBtn.setChecked(true);
            femaleBtn.setChecked(false);
        }else if(dog.getGender().equals("F")){
            maleBtn.setChecked(false);
            femaleBtn.setChecked(true);
        }
        dogcolor.setText(dog.getColor());
        dogbreed.setText(dog.getBreed());
        dogaddress.setText(dog.getAddress());
        dogsubdistrict.setText(dog.getSubdistrict());

        // info //
        dogage.setText(String.valueOf(info.getAgeRange()));

    }
}
