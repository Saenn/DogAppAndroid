package main.dogappandroid;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity2 extends AppCompatActivity {

    private EditText addressEditText, subdistrictEditText, districtEditText, provinceEditText, phoneEditText;
    private Button nextButton;
    private Drawable originalStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        subdistrictEditText = (EditText) findViewById(R.id.subdistrictEditText);
        districtEditText = (EditText) findViewById(R.id.districtEditText);
        provinceEditText = (EditText) findViewById(R.id.provinceEditText);
        nextButton = (Button) findViewById(R.id.nextButtonRegister2);
        originalStyle = addressEditText.getBackground();

        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[0-9]*";
                    if (!phoneEditText.getText().toString().matches(regex))
                        phoneEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else phoneEditText.setBackground(originalStyle);
                }
            }
        });

        addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F/., ]*";
                    if (!addressEditText.getText().toString().matches(regex))
                        addressEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else addressEditText.setBackground(originalStyle);
                }
            }
        });

        subdistrictEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!subdistrictEditText.getText().toString().matches(regex))
                        subdistrictEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else subdistrictEditText.setBackground(originalStyle);
                }
            }
        });

        districtEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!districtEditText.getText().toString().matches(regex))
                        districtEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else districtEditText.setBackground(originalStyle);
                }
            }
        });

        provinceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!provinceEditText.getText().toString().matches(regex))
                        provinceEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else provinceEditText.setBackground(originalStyle);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateAllInput()){
                    Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                    Intent prevIntent = getIntent();
                    intent.putExtra("firstname", prevIntent.getStringExtra("firstname"));
                    intent.putExtra("lastname", prevIntent.getStringExtra("lastname"));
                    intent.putExtra("email", prevIntent.getStringExtra("email"));
                    intent.putExtra("password", prevIntent.getStringExtra("password"));
                    intent.putExtra("address",addressEditText.getText().toString());
                    intent.putExtra("subdistrict",subdistrictEditText.getText().toString());
                    intent.putExtra("district",districtEditText.getText().toString());
                    intent.putExtra("province",provinceEditText.getText().toString());
                    intent.putExtra("phone",phoneEditText.getText().toString());
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(RegisterActivity2.this,"Your inputs are incorrect",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
    protected boolean validateAllInput() {
        String phoneRegex = "[0-9]*";
        String addressRegex = "[a-zA-Z\\u0E00-\\u0E7F/., ]*";
        String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
        if(phoneEditText.getText().toString().matches(phoneRegex) && addressEditText.getText().toString().matches(addressRegex) &&
                subdistrictEditText.getText().toString().matches(regex) && districtEditText.getText().toString().matches(regex) &&
                provinceEditText.getText().toString().matches(regex))
            return true;
        return false;
    }
}
