package main.dogappandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddStray2 extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    private TextView addressLabel, subdistrictLabel, districtLabel, provinceLabel;
    private TextView requiredAddress, requiredSubdistrict, requiredDistrict, requiredProvince;
    private EditText address, subdistrict, district;
    private RadioGroup sameAddress;
    private Button nextBtn;
    private Spinner provinceSpinner;
    private String[] provinceList;
    private String provinceValue;
    private Drawable originalStyle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

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
        sameAddress = (RadioGroup) findViewById(R.id.sameAddressStray);
        nextBtn = (Button) findViewById(R.id.nextStray2);
        originalStyle = address.getBackground();

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

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    if (!address.getText().toString().matches("[0-9a-zA-Z\\u0E00-\\u0E7F/.,_ ]+")) {
                        address.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        address.setBackground(originalStyle);
                    }
                }
            }
        });

        subdistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    if (!subdistrict.getText().toString().matches("[a-zA-Z\\u0E00-\\u0E7F ]+")) {
                        subdistrict.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        subdistrict.setBackground(originalStyle);
                    }
                }
            }
        });

        district.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    if (!district.getText().toString().matches("[a-zA-Z\\u0E00-\\u0E7F ]+")) {
                        district.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        district.setBackground(originalStyle);
                    }
                }
            }
        });

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
                        new AlertDialog.Builder(AddStray2.this)
                                .setTitle(R.string.noData)
                                .setMessage(R.string.gotouserprofile)

                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent editUser = new Intent(AddStray2.this, EditUserProfile.class);
                                        startActivity(editUser);
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    else {
                        int checkInput = validateAllInput();
                        if (checkInput == 0) {
                            extras.putString("address", mPreferences.getString("address", null));
                            extras.putString("subdistrict", mPreferences.getString("subdistrict", null));
                            extras.putString("district", mPreferences.getString("district", null));
                            extras.putString("province", mPreferences.getString("province", null));
                            extras.putString("dogType", "3");
                            Intent addStray3 = new Intent(AddStray2.this, AddStray3.class);
                            addStray3.putExtras(extras);
                            startActivity(addStray3);
                        } else if (checkInput == 1) {
                            Toast.makeText(AddStray2.this, R.string.address_error, Toast.LENGTH_LONG).show();
                        } else if (checkInput == 2) {
                            Toast.makeText(AddStray2.this, R.string.subdistrict_error, Toast.LENGTH_LONG).show();
                        } else if (checkInput == 3) {
                            Toast.makeText(AddStray2.this, R.string.district_error, Toast.LENGTH_LONG).show();
                        }

                    }
                } else if (sameAddress.getCheckedRadioButtonId() == R.id.noSameAddressStray) {
                    if (address.getText().toString().equals("")
                            || subdistrict.getText().toString().equals("")
                            || district.getText().toString().equals("")
                            || provinceValue.equals(""))
                        Toast.makeText(AddStray2.this, R.string.empty_field_error, Toast.LENGTH_LONG).show();
                    else {
                        extras.putString("address", address.getText().toString());
                        extras.putString("subdistrict", subdistrict.getText().toString());
                        extras.putString("district", district.getText().toString());
                        extras.putString("province", provinceValue);
                        extras.putString("dogType", "3");
                        Intent addStray3 = new Intent(AddStray2.this, AddStray3.class);
                        addStray3.putExtras(extras);
                        startActivity(addStray3);
                    }
                }
            }
        });
    }

    protected int validateAllInput() {
        String addressRegex = "[0-9a-zA-Z\\u0E00-\\u0E7F/.,_ ]+";
        String regex = "[a-zA-Z\\u0E00-\\u0E7F ]+";
        if (!address.getText().toString().matches(addressRegex) && sameAddress.getCheckedRadioButtonId() == R.id.noSameAddressStray) {
            return 1;
        } else if (!subdistrict.getText().toString().matches(regex) && sameAddress.getCheckedRadioButtonId() == R.id.noSameAddressStray) {
            return 2;
        } else if (!district.getText().toString().matches(regex) && sameAddress.getCheckedRadioButtonId() == R.id.noSameAddressStray) {
            return 3;
        } else {
            return 0;
        }
    }

    private void getListInfo(String lang) {
        Context context = LocalHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        provinceList = resources.getStringArray(R.array.provinceList);
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
}
