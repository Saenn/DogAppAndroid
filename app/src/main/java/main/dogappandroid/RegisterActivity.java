package main.dogappandroid;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstname,lastname, email, password, repassword;
    private Button nextButton;
    private Drawable originalStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = (EditText) findViewById(R.id.firstNameEditText);
        lastname = (EditText) findViewById(R.id.lastNameEditText);
        email = (EditText) findViewById(R.id.emailRegister);
        password = (EditText) findViewById(R.id.passwordRegister);
        repassword = (EditText) findViewById(R.id.repasswordRegister);
        nextButton = (Button) findViewById(R.id.nextButtonRegister1);
        originalStyle = firstname.getBackground();
        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]+";
                    if (!firstname.getText().toString().matches(regex))
                        firstname.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else firstname.setBackground(originalStyle);
                }
            }
        });


        lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]+";
                    if (!lastname.getText().toString().matches(regex))
                        lastname.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else lastname.setBackground(originalStyle);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z0-9.]+@[a-zA-Z0-9.]+";
                    if (!email.getText().toString().matches(regex))
                        email.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else email.setBackground(originalStyle);
                }
            }
        });

        repassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!repassword.getText().toString().equals(password.getText().toString())) {
                        repassword.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        repassword.setBackground(originalStyle);
                    }
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllInput()) {
                    Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                    intent.putExtra("firstname", firstname.getText().toString());
                    intent.putExtra("lastname", lastname.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(RegisterActivity.this, "Your inputs are incorrect", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });


    }

    protected boolean validateAllInput() {
        String fullnameRegex = "[a-zA-Z\\u0E00-\\u0E7F ]+";
        String emailRegex = "[a-zA-Z0-9.]+@[a-zA-Z0-9.]+";
        if (firstname.getText().toString().matches(fullnameRegex) && lastname.getText().toString().matches(fullnameRegex) && email.getText().toString().matches(emailRegex) && repassword.getText().toString().equals(password.getText().toString()))
            return true;
        return false;
    }

}
