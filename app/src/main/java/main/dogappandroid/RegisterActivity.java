package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, firstname, lastname, email, password, repassword, securityAnswer;
    private Button nextButton;
    private Spinner securityQuestionSpinner;
    private Drawable originalStyle;
    private int securityQuestionSelect;

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        firstname = (EditText) findViewById(R.id.firstNameEditText);
        lastname = (EditText) findViewById(R.id.lastNameEditText);
        username = (EditText) findViewById(R.id.usernameRegister);
        email = (EditText) findViewById(R.id.emailRegister);
        password = (EditText) findViewById(R.id.passwordRegister);
        repassword = (EditText) findViewById(R.id.repasswordRegister);
        securityAnswer = (EditText) findViewById(R.id.securityAnswer);
        nextButton = (Button) findViewById(R.id.nextButtonRegister1);
        Spinner securityQuestionSpinner = (Spinner) findViewById(R.id.securityQuestion);
        originalStyle = firstname.getBackground();

        ArrayAdapter<String> securityQuestionSet = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.securityQuestionArray));
        securityQuestionSpinner.setAdapter(securityQuestionSet);

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

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z0-9.]+";
                    if (!username.getText().toString().matches(regex))
                        username.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else {
                        username.setBackground(originalStyle);
                        username.setHint("should only be 0-9 A-Z a-z .");
                    }
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (email.getText().toString().length() != 0) {
                        String regex = "[a-zA-Z0-9.]+@[a-zA-Z0-9.]+";
                        if (!email.getText().toString().matches(regex))
                            email.setBackgroundColor(getResources().getColor(R.color.pink100));
                        else email.setBackground(originalStyle);
                    } else {
                        email.setBackground(originalStyle);
                    }
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

        securityAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (securityAnswer.getText().toString().length() == 0) {
                        securityAnswer.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        securityAnswer.setBackground(originalStyle);
                    }
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllInput()) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("firstName", firstname.getText().toString());
                    editor.putString("lastName", lastname.getText().toString());
                    editor.putString("email", email.getText().toString());
                    editor.putString("username", username.getText().toString());
                    editor.putString("forgotQuestion", securityQuestionSelect + "");
                    editor.putString("forgotAnswer", securityAnswer.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                    intent.putExtra("firstname", firstname.getText().toString());
                    intent.putExtra("lastname", lastname.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("username", username.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    intent.putExtra("forgotQuestion", securityQuestionSelect + "");
                    intent.putExtra("forgotAnswer", securityAnswer.getText().toString());
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(RegisterActivity.this, "Your inputs are incorrect", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        securityQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                securityQuestionSelect = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                securityQuestionSelect = 400;
            }
        });

    }

    protected boolean validateAllInput() {
        String usenameRegex = "[a-zA-Z0-9.]+";
        String fullnameRegex = "[a-zA-Z\\u0E00-\\u0E7F ]+";
        String emailRegex = "[a-zA-Z0-9.]+@[a-zA-Z0-9.]+";
        if (username.getText().toString().matches(usenameRegex)
                && firstname.getText().toString().matches(fullnameRegex)
                && lastname.getText().toString().matches(fullnameRegex)
                && (email.getText().toString().length() == 0 || email.getText().toString().matches(emailRegex))
                && repassword.getText().toString().equals(password.getText().toString())
                && securityQuestionSelect != 400
                && !securityAnswer.getText().toString().equals(""))
            return true;
        return false;
    }

}
