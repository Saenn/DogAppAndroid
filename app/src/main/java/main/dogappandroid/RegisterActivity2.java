package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity2 extends AppCompatActivity {

    private EditText addressEditText, subdistrictEditText, districtEditText, provinceEditText, postalEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        addressEditText = (EditText) findViewById(R.id.addressEditText);
        subdistrictEditText = (EditText) findViewById(R.id.subdistrictEditText);
        districtEditText = (EditText) findViewById(R.id.districtEditText);
        provinceEditText = (EditText) findViewById(R.id.provinceEditText);
        postalEditText = (EditText) findViewById(R.id.postalEditText);
        nextButton = (Button) findViewById(R.id.nextButtonRegister2);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                String fullAddress = addressEditText.getText().toString() + ", ";
                fullAddress += subdistrictEditText.getText().toString() + ", ";
                fullAddress += districtEditText.getText().toString() + ", ";
                fullAddress += provinceEditText.getText().toString() + " ";
                fullAddress += postalEditText.getText().toString();
                intent.putExtra("fullAddress",fullAddress);
                startActivity(intent);
//              intent.putExtra("address",addressEditText.getText().toString());
//              intent.putExtra("subdistrict",subdistrictEditText.getText().toString());
//              intent.putExtra("district",districtEditText.getText().toString());
//              intent.putExtra("province",provinceEditText.getText().toString());
//              intent.putExtra("postal",postalEditText.getText().toString());

            }
        });

    }
}
