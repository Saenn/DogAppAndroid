package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddStray2 extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    private TextView addressLabel, subdistrictLabel, districtLabel, provinceLabel;
    private TextView requiredAddress, requiredSubdistrict, requiredDistrict, requiredProvince;
    private EditText address, subdistrict, district, province;
    private RadioGroup sameAddress;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stray2);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        requiredAddress = (TextView) findViewById(R.id.requiredAddressStray);
        requiredSubdistrict = (TextView) findViewById(R.id.requiredSubdistrictStray);
        requiredDistrict = (TextView) findViewById(R.id.requiredDistrictStray);
        requiredProvince = (TextView) findViewById(R.id.requiredProvinceStray);
        addressLabel = (TextView) findViewById(R.id.addressStrayLabel);
        subdistrictLabel = (TextView) findViewById(R.id.subdistrictStrayLabel);
        districtLabel = (TextView) findViewById(R.id.districtStrayLabel);
        provinceLabel = (TextView) findViewById(R.id.provinceStrayLabel);
        address = (EditText) findViewById(R.id.addressStray);
        subdistrict = (EditText) findViewById(R.id.subdistrictStray);
        district = (EditText) findViewById(R.id.districtStray);
        province = (EditText) findViewById(R.id.provinceStray);
        sameAddress = (RadioGroup) findViewById(R.id.sameAddressStray);
        nextBtn = (Button) findViewById(R.id.nextStray2);

        handleAddressField(View.GONE);

        sameAddress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.noSameAddressStray) handleAddressField(View.VISIBLE);
                else if (checkedId == R.id.yesSameAddressStray) handleAddressField(View.GONE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (sameAddress.getCheckedRadioButtonId() == R.id.yesSameAddressStray) {
                    if (mPreferences.getString("address", "").equals("")
                            || mPreferences.getString("subdistrict", "").equals("")
                            || mPreferences.getString("district", "").equals("")
                            || mPreferences.getString("province", "").equals(""))
                        Toast.makeText(AddStray2.this, "You have yet to submit your address data", Toast.LENGTH_LONG).show();
                    else {
                        extras.putString("address", mPreferences.getString("address", null));
                        extras.putString("subdistrict", mPreferences.getString("subdistrict", null));
                        extras.putString("subdistrict", mPreferences.getString("subdistrict", null));
                        extras.putString("district", mPreferences.getString("district", null));
                        extras.putString("province", mPreferences.getString("province", null));
                        extras.putString("dogType", "3");
                        Intent addStray3 = new Intent(AddStray2.this, AddStray3.class);
                        addStray3.putExtras(extras);
                        startActivity(addStray3);
                    }
                } else if (sameAddress.getCheckedRadioButtonId() == R.id.noSameAddressStray) {
                    if (address.getText().toString().equals("")
                            || subdistrict.getText().toString().equals("")
                            || district.getText().toString().equals("")
                            || province.getText().toString().equals(""))
                        Toast.makeText(AddStray2.this, "Please fill up all the required fields", Toast.LENGTH_LONG).show();
                    else {
                        extras.putString("address", address.getText().toString());
                        extras.putString("subdistrict", subdistrict.getText().toString());
                        extras.putString("district", district.getText().toString());
                        extras.putString("province", province.getText().toString());
                        extras.putString("dogType", "3");
                        Intent addStray3 = new Intent(AddStray2.this, AddStray3.class);
                        addStray3.putExtras(extras);
                        startActivity(addStray3);
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
}
