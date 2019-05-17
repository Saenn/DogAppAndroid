package main.dogappandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddDomestic2 extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    private TextView addressLabel, subdistrictLabel, districtLabel, provinceLabel, homeConditionLabel, dayLifestyleLabel, nightLifestyleLabel, sameAddressLabel, headerLabel;
    private TextView requiredAddress, requiredSubdistrict, requiredDistrict, requiredProvince;
    private EditText address, subdistrict, district;
    private RadioGroup homeCondition, dayLifestyle, nightLifestyle, sameAddress;
    private Button nextBtn;
    private DBHelper mHelper;
    private RadioButton indoorDayBtn, outdoorDayBtn, indoorNightBtn, outdoorNightBtn, yesHomeBtn, noHomeBtn, yesSameBtn, noSameBtn;
    private Spinner provinceSpinner;
    private String[] provinceList;
    private String provinceValue;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic2);
        mHelper = new DBHelper(this);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        headerLabel = (TextView) findViewById(R.id.addDomesticHeader2);
        homeConditionLabel = (TextView) findViewById(R.id.homeConditionLabel);
        dayLifestyleLabel = (TextView) findViewById(R.id.dayLifestyleLabel);
        nightLifestyleLabel = (TextView) findViewById(R.id.nightLifestyleLabel);
        sameAddressLabel = (TextView) findViewById(R.id.sameAddressDomesticLabel);
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
        homeCondition = (RadioGroup) findViewById(R.id.homeCondition);
        dayLifestyle = (RadioGroup) findViewById(R.id.dayLifestyle);
        nightLifestyle = (RadioGroup) findViewById(R.id.nightLifestyle);
        sameAddress = (RadioGroup) findViewById(R.id.sameAddressDomestic);
        nextBtn = (Button) findViewById(R.id.nextDomestic2);
        indoorDayBtn = (RadioButton) findViewById(R.id.indoorDayLifestyle);
        outdoorDayBtn = (RadioButton) findViewById(R.id.outdoorDayLifestyle);
        indoorNightBtn = (RadioButton) findViewById(R.id.indoorNightLifestyle);
        outdoorNightBtn = (RadioButton) findViewById(R.id.outdoorNightLifestyle);
        yesHomeBtn = (RadioButton) findViewById(R.id.yesHomeCondition);
        noHomeBtn = (RadioButton) findViewById(R.id.noHomeCondition);
        yesSameBtn = (RadioButton) findViewById(R.id.yesSameAddressDomestic);
        noSameBtn = (RadioButton) findViewById(R.id.noSameAddressDomestic);

        //Set Language
        SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        getListInfo(preferences.getString("lang", "th"));

        // Setup Spinner //
        provinceValue = "Bangkok";
        provinceSpinner = (Spinner) findViewById(R.id.provinceSpinner);
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                provinceList);
        provinceSpinner.setAdapter(adapterProvince);

        if (preferences.getString("lang", "th").equals("th")) {
            provinceList = getResources().getStringArray(R.array.provinceListTHEN);
        }
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provinceValue = provinceList[position];
                Log.i("selectedvale : ", provinceValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                Bundle extras = getIntent().getExtras();
                if (homeCondition.getCheckedRadioButtonId() == RadioButton.NO_ID
                        || dayLifestyle.getCheckedRadioButtonId() == RadioButton.NO_ID
                        || nightLifestyle.getCheckedRadioButtonId() == RadioButton.NO_ID
                        || sameAddress.getCheckedRadioButtonId() == RadioButton.NO_ID)
                    Toast.makeText(AddDomestic2.this, "Please fill up all the required fields", Toast.LENGTH_LONG).show();
                else if (sameAddress.getCheckedRadioButtonId() == R.id.yesSameAddressDomestic) {
                    if (mPreferences.getString("address", "").equals("")
                            || mPreferences.getString("subdistrict", "").equals("")
                            || mPreferences.getString("district", "").equals("")
                            || mPreferences.getString("province", "").equals("")) {
                        new AlertDialog.Builder(AddDomestic2.this)
                                .setTitle(R.string.noData)
                                .setMessage(R.string.gotouserprofile)

                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent editUser = new Intent(AddDomestic2.this, EditUserProfile.class);
                                        startActivity(editUser);
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
//                        Toast.makeText(AddDomestic2.this, "You have yet to submit your address data", Toast.LENGTH_LONG).show();
                    } else {
                        extras.putString("address", mPreferences.getString("address", null));
                        extras.putString("subdistrict", mPreferences.getString("subdistrict", null));
                        extras.putString("subdistrict", mPreferences.getString("subdistrict", null));
                        extras.putString("district", mPreferences.getString("district", null));
                        extras.putString("province", mPreferences.getString("province", null));
                        extras.putString("dogType", calculateDomesticType() + "");
                        Intent addDomestic3 = new Intent(AddDomestic2.this, AddDomestic3.class);
                        addDomestic3.putExtras(extras);
                        startActivity(addDomestic3);
                    }
                } else if (sameAddress.getCheckedRadioButtonId() == R.id.noSameAddressDomestic) {
                    if (address.getText().toString().equals("")
                            || subdistrict.getText().toString().equals("")
                            || district.getText().toString().equals("")
                            || provinceValue.equals(""))
                        Toast.makeText(AddDomestic2.this, "Please fill up all the required fields", Toast.LENGTH_LONG).show();
                    else {
                        extras.putString("address", address.getText().toString());
                        extras.putString("subdistrict", subdistrict.getText().toString());
                        extras.putString("district", district.getText().toString());
                        extras.putString("province", provinceValue);
                        extras.putString("dogType", calculateDomesticType() + "");
                        Intent prevAdd = getIntent();
                        extras.putString("name", prevAdd.getStringExtra("name"));
                        extras.putInt("age", prevAdd.getIntExtra("age", 0));
                        extras.putString("ageRange", prevAdd.getStringExtra("ageRange"));
                        extras.putString("gender", prevAdd.getStringExtra("gender"));
                        extras.putString("breed", prevAdd.getStringExtra("breed"));
                        extras.putString("color", prevAdd.getStringExtra("color"));
                        extras.putBoolean("sterilized", prevAdd.getBooleanExtra("sterilized", false));
                        extras.putString("sterilizedDate", prevAdd.getStringExtra("sterilizedDate"));
                        Intent addDomestic3 = new Intent(AddDomestic2.this, AddDomestic3.class);
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
        provinceSpinner.setVisibility(visibility);
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

    private void getListInfo(String lang) {
        Context context = LocalHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        provinceList = resources.getStringArray(R.array.provinceList);
    }

}
