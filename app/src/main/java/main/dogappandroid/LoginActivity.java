package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import main.dogappandroid.Utilities.NetworkUtils;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView forgotPassword;
    private Button loginButton;
    private Button registerButton;

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent service = new Intent(this, ServiceRunning.class);
        startService(service);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        if (mPreferences.getString("token", "") != "") {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordActivity = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(forgotPasswordActivity);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                if (username.getText().toString() != "" && password.getText().toString() != "") {
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());
                    new onLogin().execute(params);
                } else {
                    Toast toast = Toast.makeText(LoginActivity.this, "Please input your username and password before proceeding.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public class onLogin extends AsyncTask<Map<String, String>, Void, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            return NetworkUtils.login(maps[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(s);
                try {
                    String token = jsonObject.getString("token");
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    String message = jsonObject.getString("message");
                    Toast toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);
                    toast.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}