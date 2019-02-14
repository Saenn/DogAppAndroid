package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity2 extends AppCompatActivity {

    private EditText addressEditText, subdistrictEditText, districtEditText, provinceEditText, phoneEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Intent intent = getIntent();

        addressEditText = (EditText) findViewById(R.id.addressEditText);
        subdistrictEditText = (EditText) findViewById(R.id.subdistrictEditText);
        districtEditText = (EditText) findViewById(R.id.districtEditText);
        provinceEditText = (EditText) findViewById(R.id.provinceEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        nextButton = (Button) findViewById(R.id.nextButtonRegister2);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                intent.putExtra("firstname", intent.getStringExtra("firstname"));
                intent.putExtra("lastname", intent.getStringExtra("lastname"));
                intent.putExtra("email", intent.getStringExtra("email"));
                intent.putExtra("password", intent.getStringExtra("password"));
                intent.putExtra("address",addressEditText.getText().toString());
                intent.putExtra("subdistrict",subdistrictEditText.getText().toString());
                intent.putExtra("district",districtEditText.getText().toString());
                intent.putExtra("province",provinceEditText.getText().toString());
                intent.putExtra("phone",phoneEditText.getText().toString());
                startActivity(intent);
            }
        });

    }
}
