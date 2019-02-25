package main.dogappandroid;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class AddDomestic2 extends AppCompatActivity {

    private EditText sterilizeDate;
    private TextView sterilizeDateLabel;
    private Button nextButton;
    private RadioButton sterilizedBtn, notSterilizedBtn;
    private Spinner ageRangeSpinner;
    private ArrayList<String> ageRangeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic2);

        // Setup Var //
        sterilizedBtn = (RadioButton) findViewById(R.id.sterilized);
        notSterilizedBtn = (RadioButton) findViewById(R.id.notSterilized);
        sterilizeDateLabel = (TextView) findViewById(R.id.sterilizeddatelabel);
        sterilizeDate = (EditText) findViewById(R.id.sterilizedEditText);
        addAgeDataToList();
        nextButton = (Button) findViewById(R.id.nextButtonDomestic2);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddDomestic2.this, AddDomestic3.class);
                startActivity(intent);
            }
        });
        // Setup Spinner //
        ageRangeSpinner = (Spinner) findViewById(R.id.spinnerAge);
        ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ageRangeList);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageRangeSpinner.setAdapter(adapterAge);

        ageRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){}
                else{
                    Toast.makeText(AddDomestic2.this,
                            "Select : " + ageRangeList.get(position),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.domesticstergroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (sterilizedBtn.isChecked()){
                    sterilizeDateLabel.setVisibility(View.VISIBLE);
                    sterilizeDate.setVisibility(View.VISIBLE);
                }
                else{
                    sterilizeDateLabel.setVisibility(View.GONE);
                    sterilizeDate.setVisibility(View.GONE);
                }
            }
        });

    }

    private void addAgeDataToList() {
        ageRangeList.add(0,"Choose Age Range");
        ageRangeList.add("Not exceed 3 years");
        ageRangeList.add("More than 3 years");

    }
}
