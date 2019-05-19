package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import main.dogappandroid.Utilities.NetworkUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, firstname, lastname, email, password, repassword, securityAnswer;
    private Button nextButton;
    private Drawable originalStyle;
    private int securityQuestionSelect;

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

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
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F. ]+";
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
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F. ]+";
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
                    String regex = "[a-zA-Z0-9._-]+";
                    if (!username.getText().toString().matches(regex))
                        username.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else {
                        username.setBackground(originalStyle);
                        username.setHint("should only be 0-9 A-Z a-z . _");
                    }
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (email.getText().toString().length() != 0) {
                        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
                        if (!email.getText().toString().matches(regex))
                            email.setBackgroundColor(getResources().getColor(R.color.pink100));
                        else email.setBackground(originalStyle);
                    } else {
                        email.setBackground(originalStyle);
                    }
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (password.getText().toString().equals("")) {
                        password.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        password.setBackground(originalStyle);
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
                    new onCheckUsername().execute(username.getText().toString());
                } else {
                    String errorTxt = "";
                    Log.d("errorType", getWrongType() + "");
                    if (getWrongType() == 0) {
                        errorTxt = getResources().getString(R.string.firstname_error);

                    } else if (getWrongType() == 1) {
                        errorTxt = getResources().getString(R.string.lastname_error);

                    } else if (getWrongType() == 2) {
                        errorTxt = getResources().getString(R.string.usernameError);

                    } else if (getWrongType() == 3) {
                        errorTxt = getResources().getString(R.string.password_error);

                    } else if (getWrongType() == 4) {
                        errorTxt = getResources().getString(R.string.password_not_match_error);

                    } else if (getWrongType() == 5) {
                        errorTxt = getResources().getString(R.string.emails_error);

                    } else if (getWrongType() == 6) {
                        errorTxt = getResources().getString(R.string.securityquestion_error);

                    } else if (getWrongType() == 7) {
                        errorTxt = getResources().getString(R.string.securityanswer_error);

                    } else {
                        errorTxt = "None";
                    }
                    Toast toast = Toast.makeText(RegisterActivity.this, errorTxt, Toast.LENGTH_LONG);
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

    private boolean validateAllInput() {
        String usenameRegex = "[a-zA-Z0-9._-]+";
        String fullnameRegex = "[a-zA-Z\\u0E00-\\u0E7F. ]+";
        String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        if (username.getText().toString().matches(usenameRegex)
                && firstname.getText().toString().matches(fullnameRegex)
                && lastname.getText().toString().matches(fullnameRegex)
                && !password.getText().toString().equals("")
                && repassword.getText().toString().equals(password.getText().toString())
                && (email.getText().toString().length() == 0 || email.getText().toString().matches(emailRegex))
                && securityQuestionSelect != 400
                && !securityAnswer.getText().toString().equals(""))
            return true;
        return false;

    }

    private int getWrongType() {
        String usenameRegex = "[a-zA-Z0-9._-]+";
        String fullnameRegex = "[a-zA-Z\\u0E00-\\u0E7F. ]+";
        String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        if (!firstname.getText().toString().matches(fullnameRegex)) {
            return 0;
        }
        if (!lastname.getText().toString().matches(fullnameRegex)) {
            return 1;
        }
        if (!username.getText().toString().matches(usenameRegex)) {
            return 2;
        }
        if (password.getText().toString().equals("")) {
            return 3;
        }
        if (!repassword.getText().toString().equals(password.getText().toString())) {
            return 4;
        }
        if (email.getText().toString().length() != 0 && !email.getText().toString().matches(emailRegex)) {
            return 5;
        }
        if (securityQuestionSelect == 400) {
            return 6;
        }
        if (securityAnswer.getText().toString().equals("")) {
            return 7;
        }
        return -1;
    }

    class onCheckUsername extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                if (status.equals("Success")) {
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
                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(RegisterActivity.this, R.string.internet_disconnect_error, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return NetworkUtils.checkUsername(strings[0]);
        }
    }
}