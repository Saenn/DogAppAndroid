package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddDomestic extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    private TextView addressLabel, districtLabel, subdistrictLabel, provinceLabel;
    private EditText addressText, districtText, subdistrictText, provinceText, dogDomesticNameText;
    private Button nextButton;
    private RadioButton yesBtn, noBtn, maleBtn, femaleBtn;
    private RadioGroup sameAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic);

        dogDomesticNameText = (EditText) findViewById(R.id.dogDomesticNameText);
        nextButton = (Button) findViewById(R.id.nextButtonAddDomestic);
        addressLabel = (TextView) findViewById(R.id.dogdomesticaddresslabel);
        districtLabel = (TextView) findViewById(R.id.dogdomesticdistrictlabel);
        subdistrictLabel = (TextView) findViewById(R.id.dogdomesticsubdistrictlabel);
        provinceLabel = (TextView) findViewById(R.id.domesticdogprovincelabel);
        addressText = (EditText) findViewById(R.id.dogdomesticaddressedittext);
        districtText = (EditText) findViewById(R.id.dogdomesticdistrictedittext);
        subdistrictText = (EditText) findViewById(R.id.dogdomesticsubdistrictedittext);
        provinceText = (EditText) findViewById(R.id.domesticdogprovinceedittext);
        yesBtn = (RadioButton) findViewById(R.id.yesdomestic);
        noBtn = (RadioButton) findViewById(R.id.nodosmestic);
        maleBtn = (RadioButton) findViewById(R.id.dogmale);
        femaleBtn = (RadioButton) findViewById(R.id.dogfemale);
        sameAddress = (RadioGroup) findViewById(R.id.sameaddress);

        sameAddress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (noBtn.isChecked()) {
                    addressLabel.setVisibility(View.VISIBLE);
                    districtLabel.setVisibility(View.VISIBLE);
                    subdistrictLabel.setVisibility(View.VISIBLE);
                    provinceLabel.setVisibility(View.VISIBLE);
                    addressText.setVisibility(View.VISIBLE);
                    districtText.setVisibility(View.VISIBLE);
                    subdistrictText.setVisibility(View.VISIBLE);
                    provinceText.setVisibility(View.VISIBLE);
                } else {
                    addressLabel.setVisibility(View.GONE);
                    districtLabel.setVisibility(View.GONE);
                    subdistrictLabel.setVisibility(View.GONE);
                    provinceLabel.setVisibility(View.GONE);
                    addressText.setVisibility(View.GONE);
                    districtText.setVisibility(View.GONE);
                    subdistrictText.setVisibility(View.GONE);
                    provinceText.setVisibility(View.GONE);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                if (dogDomesticNameText.getText().toString().equals("")) {
                    Toast.makeText(AddDomestic.this, "Please fill all required inputs", Toast.LENGTH_LONG).show();
                } else {
                    extras.putString("name", dogDomesticNameText.getText().toString());
                    if (maleBtn.isChecked()) extras.putString("gender", "M");
                    else if (femaleBtn.isChecked()) extras.putString("gender", "F");
                    if (yesBtn.isChecked()) {
                        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
                        String address = mPreferences.getString("address", "");
                        String subdistrict = mPreferences.getString("subdistrict", "");
                        String district = mPreferences.getString("district", "");
                        String province = mPreferences.getString("province", "");
                        if (!address.equals("") && !subdistrict.equals("") && !district.equals("") && !province.equals("")) {
                            extras.putString("address", address);
                            extras.putString("subdistrict", subdistrict);
                            extras.putString("district", district);
                            extras.putString("province", province);
                            Intent intent = new Intent(AddDomestic.this, AddDomestic2.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddDomestic.this, "You have yet record your address", Toast.LENGTH_LONG).show();
                        }
                    } else if (noBtn.isChecked()) {
                        String address = addressText.getText().toString();
                        String subdistrict = subdistrictText.getText().toString();
                        String district = districtText.getText().toString();
                        String province = provinceText.getText().toString();
                        if (address.equals("") && subdistrict.equals("") && district.equals("") && province.equals("")) {
                            Toast.makeText(AddDomestic.this, "Please fill all required inputs", Toast.LENGTH_LONG).show();
                        } else {
                            extras.putString("address", address);
                            extras.putString("subdistrict", subdistrict);
                            extras.putString("district", district);
                            extras.putString("province", province);
                            Intent intent = new Intent(AddDomestic.this, AddDomestic2.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

    }

}
