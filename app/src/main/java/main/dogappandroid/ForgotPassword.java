package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import main.dogappandroid.Utilities.NetworkUtils;

public class ForgotPassword extends AppCompatActivity {

    private EditText username, securityAnswer, password, repassword;
    private Spinner securityQuestion;
    private Button nextBtn;
    private Drawable originalStyle;
    private int securityQuestionSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        username = (EditText) findViewById(R.id.usernameForgotPassword);
        password = (EditText) findViewById(R.id.passwordForgot);
        repassword = (EditText) findViewById(R.id.repasswordForgot);
        securityAnswer = (EditText) findViewById(R.id.securityAnswer2);
        securityQuestion = (Spinner) findViewById(R.id.securityQuestion2);
        nextBtn = (Button) findViewById(R.id.nextButtonForgot);
        originalStyle = username.getBackground();
//        Initiate adapter for spinner
        ArrayAdapter<String> securityQuestionSet = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.securityQuestionArray));
//        set adapter for spinner
        securityQuestion.setAdapter(securityQuestionSet);
        securityQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                securityQuestionSelect = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                securityQuestionSelect = 400;
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                if (validateInputs()) {
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());
                    params.put("forgotQuestion", securityQuestionSelect + "");
                    params.put("forgotAnswer", securityAnswer.getText().toString());
                    new onRequestForgot().execute(params);
                } else {
                    Toast.makeText(ForgotPassword.this, "Your inputs are wrong, please try again.", Toast.LENGTH_LONG).show();
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
    }

    private boolean validateInputs() {
        String usernameRegex = "[a-zA-Z0-9.]+";
        if (username.getText().toString().matches(usernameRegex)
                && password.getText().toString().equals(repassword.getText().toString())
                && !password.getText().toString().equals("")
                && !securityAnswer.getText().toString().equals(""))
            return true;
        return false;
    }

    public class onRequestForgot extends AsyncTask<Map<String, String>, Void, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            return NetworkUtils.forgotPassword(maps[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                try {
                    String message = jsonObject.getString("message");
                    Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_LONG).show();
                    Intent loginIntent = new Intent(ForgotPassword.this, LoginActivity.class);
                    startActivity(loginIntent);
                } catch (JSONException e2) {
                    String error = jsonObject.getString("error");
                    Toast.makeText(ForgotPassword.this, error, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}