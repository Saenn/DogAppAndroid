package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity2 extends AppCompatActivity {

    String housenotext,roadtext,alleytext,provincetext,districttext,subdistricttext,phonenotext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        // declare variables //
        Button nextButton2 = (Button) findViewById(R.id.nextButton2);
        Button backButton = (Button) findViewById(R.id.backButton);
        final EditText houseno = (EditText) findViewById(R.id.houseno);
        final EditText road = (EditText) findViewById(R.id.road);
        final EditText alley = (EditText) findViewById(R.id.alley);
        final EditText province = (EditText) findViewById(R.id.province);
        final EditText district = (EditText) findViewById(R.id.district);
        final EditText subdistrict = (EditText) findViewById(R.id.subdistrict);
        final EditText phoneno = (EditText) findViewById(R.id.phonetext);


        // spinner //
//        Spinner spinnerprovince = (Spinner) findViewById(R.id.spinprovince);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.province_eng, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerprovince.setAdapter(adapter);
//        Spinner spinnerdistrict = (Spinner) findViewById(R.id.spindistrict);
//        Spinner spinnersubdistrict = (Spinner) findViewById(R.id.spinsubdistrict);

        // declare onClick //
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                housenotext = houseno.getText().toString();
                roadtext = road.getText().toString();
                alleytext = alley.getText().toString();
                provincetext = province.getText().toString();
                districttext = district.getText().toString();
                subdistricttext = subdistrict.getText().toString();
                phonenotext = phoneno.getText().toString();
                Intent i = new Intent(RegisterActivity2.this,RegisterActivity3.class);
                i.putExtra ( "housenotext",housenotext);
                i.putExtra ( "roadtext",roadtext);
                i.putExtra ( "allytext",alleytext);
                i.putExtra ( "provincetext",provincetext);
                i.putExtra ( "districttext",districttext);
                i.putExtra ( "subdistricttext",subdistricttext);
                i.putExtra ( "phonenotext",phonenotext);
                startActivity(i);

            }
        });

    }
}
