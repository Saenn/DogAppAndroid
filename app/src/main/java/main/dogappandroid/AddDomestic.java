package main.dogappandroid;

import android.content.Intent;
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

public class AddDomestic extends AppCompatActivity {

    private TextView addressLabel,districtLabel,subdistrictLabel,provinceLabel;
    private EditText addressText,districtText,subdistrictText,provinceText;
    private Button nextButton;
    private RadioButton yesBtn,noBtn,maleBtn,femaleBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic);

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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddDomestic.this, AddDomestic2.class);
                startActivity(intent);
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.sameaddress);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (noBtn.isChecked()){
                    addressLabel.setVisibility(View.VISIBLE);
                    districtLabel.setVisibility(View.VISIBLE);
                    subdistrictLabel.setVisibility(View.VISIBLE);
                    provinceLabel.setVisibility(View.VISIBLE);
                    addressText.setVisibility(View.VISIBLE);
                    districtText.setVisibility(View.VISIBLE);
                    subdistrictText.setVisibility(View.VISIBLE);
                    provinceText.setVisibility(View.VISIBLE);
                }
                else{
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
    }

}
