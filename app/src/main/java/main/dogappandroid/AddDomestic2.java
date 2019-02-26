package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddDomestic2 extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    private TextView addressLabel, subdistrictLabel, districtLabel, provinceLabel;
    private TextView requiredAddress, requiredSubdistrict, requiredDistrict, requiredProvince;
    private EditText address, subdistrict, district, province;
    private RadioGroup homeCondition, dayLifestyle, nightLifestyle, sameAddress;
    private Button nextBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic2);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        requiredAddress = (TextView) findViewById(R.id.requiredAddressDomestic);
        requiredSubdistrict = (TextView) findViewById(R.id.requiredSubdistrictDomestic);
        requiredDistrict = (TextView) findViewById(R.id.requiredDistrictDomestic);
        requiredProvince = (TextView) findViewById(R.id.requiredProvinceDomestic);
        addressLabel = (TextView) findViewById(R.id.addressDomesticLabel);
        subdistrictLabel = (TextView) findViewById(R.id.subdistrictDomesticLabel);
        districtLabel = (TextView) findViewById(R.id.districtDomesticLabel);
        provinceLabel = (TextView) findViewById(R.id.provinceDomesticLabel);
        address = (EditText) findViewById(R.id.addressDomestic);
        subdistrict = (EditText) findViewById(R.id.subdistrictDomestic);
        district = (EditText) findViewById(R.id.districtDomestic);
        province = (EditText) findViewById(R.id.provinceDomestic);
        homeCondition = (RadioGroup) findViewById(R.id.homeCondition);
        dayLifestyle = (RadioGroup) findViewById(R.id.dayLifestyle);
        nightLifestyle = (RadioGroup) findViewById(R.id.nightLifestyle);
        sameAddress = (RadioGroup) findViewById(R.id.sameAddressDomestic);
        nextBtn = (Button) findViewById(R.id.nextDomestic2);

        handleAddressField(View.GONE);

        sameAddress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.noSameAddressDomestic) handleAddressField(View.VISIBLE);
                else if (checkedId == R.id.yesSameAddressDomestic) handleAddressField(View.GONE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                if (homeCondition.getCheckedRadioButtonId() == RadioButton.NO_ID
                        || dayLifestyle.getCheckedRadioButtonId() == RadioButton.NO_ID
                        || nightLifestyle.getCheckedRadioButtonId() == RadioButton.NO_ID
                        || sameAddress.getCheckedRadioButtonId() == RadioButton.NO_ID)
                    Toast.makeText(AddDomestic2.this, "Please fill up all the required fields", Toast.LENGTH_LONG).show();
                else if (sameAddress.getCheckedRadioButtonId() == R.id.yesSameAddressDomestic) {
                    if (mPreferences.getString("address", "").equals("")
                            || mPreferences.getString("subdistrict", "").equals("")
                            || mPreferences.getString("district", "").equals("")
                            || mPreferences.getString("province", "").equals(""))
                        Toast.makeText(AddDomestic2.this, "You have yet to submit your address data", Toast.LENGTH_LONG).show();
                    else {
                        extras.putString("address", mPreferences.getString("address", null));
                        extras.putString("subdistrict", mPreferences.getString("subdistrict", null));
                        extras.putString("district", mPreferences.getString("district", null));
                        extras.putString("province", mPreferences.getString("province", null));
                        extras.putString("dogType",calculateDomesticType()+"");
                        Intent prevAdd = getIntent();
                        extras.putString("name",prevAdd.getStringExtra("name"));
                        extras.putInt("age",prevAdd.getIntExtra("age",0));
                        extras.putString("ageRange",prevAdd.getStringExtra("ageRange"));
                        extras.putString("breed",prevAdd.getStringExtra("breed"));
                        extras.putString("color",prevAdd.getStringExtra("color"));
                        extras.putBoolean("sterilized",prevAdd.getBooleanExtra("sterilized",false));
                        extras.putString("sterilizedDate",prevAdd.getStringExtra("sterilizedDate"));
                        Intent addDomestic3 = new Intent(AddDomestic2.this,AddDomestic3Old.class);
                        addDomestic3.putExtras(extras);
                        startActivity(addDomestic3);
                    }
                } else if (sameAddress.getCheckedRadioButtonId() == R.id.noSameAddressDomestic) {
                    if (address.getText().toString().equals("")
                            || subdistrict.getText().toString().equals("")
                            || district.getText().toString().equals("")
                            || province.getText().toString().equals(""))
                        Toast.makeText(AddDomestic2.this, "Please fill up all the required fields", Toast.LENGTH_LONG).show();
                    else {
                        extras.putString("address", address.getText().toString());
                        extras.putString("subdistrict", subdistrict.getText().toString());
                        extras.putString("district", district.getText().toString());
                        extras.putString("province", province.getText().toString());
                        extras.putString("dogType",calculateDomesticType()+"");
                        Intent prevAdd = getIntent();
                        extras.putString("name",prevAdd.getStringExtra("name"));
                        extras.putInt("age",prevAdd.getIntExtra("age",0));
                        extras.putString("ageRange",prevAdd.getStringExtra("ageRange"));
                        extras.putString("breed",prevAdd.getStringExtra("breed"));
                        extras.putString("color",prevAdd.getStringExtra("color"));
                        extras.putBoolean("sterilized",prevAdd.getBooleanExtra("sterilized",false));
                        extras.putString("sterilizedDate",prevAdd.getStringExtra("sterilizedDate"));
                        Intent addDomestic3 = new Intent(AddDomestic2.this,AddDomestic3Old.class);
                        addDomestic3.putExtras(extras);
                        startActivity(addDomestic3);
                    }
                }
            }
        });


    }

    private void handleAddressField(int visibility) {
        requiredAddress.setVisibility(visibility);
        requiredSubdistrict.setVisibility(visibility);
        requiredDistrict.setVisibility(visibility);
        requiredProvince.setVisibility(visibility);
        addressLabel.setVisibility(visibility);
        subdistrictLabel.setVisibility(visibility);
        districtLabel.setVisibility(visibility);
        provinceLabel.setVisibility(visibility);
        address.setVisibility(visibility);
        subdistrict.setVisibility(visibility);
        district.setVisibility(visibility);
        province.setVisibility(visibility);
    }

    private int calculateDomesticType() {
        // 1 represents indoor, 2 represents outdoor, 0 represents null
        if (homeCondition.getCheckedRadioButtonId() == R.id.yesHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.indoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.indoorNightLifestyle) return 1;
        else if (homeCondition.getCheckedRadioButtonId() == R.id.yesHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.indoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.outdoorNightLifestyle) return 1;
        else if (homeCondition.getCheckedRadioButtonId() == R.id.yesHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.outdoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.indoorNightLifestyle) return 1;
        else if (homeCondition.getCheckedRadioButtonId() == R.id.yesHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.outdoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.outdoorNightLifestyle) return 2;
        else if (homeCondition.getCheckedRadioButtonId() == R.id.noHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.indoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.indoorNightLifestyle) return 1;
        else if (homeCondition.getCheckedRadioButtonId() == R.id.noHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.indoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.outdoorNightLifestyle) return 2;
        else if (homeCondition.getCheckedRadioButtonId() == R.id.noHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.outdoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.indoorNightLifestyle) return 2;
        else if (homeCondition.getCheckedRadioButtonId() == R.id.noHomeCondition
                && dayLifestyle.getCheckedRadioButtonId() == R.id.outdoorDayLifestyle
                && nightLifestyle.getCheckedRadioButtonId() == R.id.outdoorNightLifestyle) return 2;
        else return 0;
    }
}
