package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button registerButton;

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        if (mPreferences.getString("token", "") != "") {
            Intent intent = new Intent(LoginActivity.this, NavigationBar.class);
            startActivity(intent);
        }

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                if (username.getText().toString() != "" && password.getText().toString() != "") {
                    params.put("email", username.getText().toString());
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
                if (s != null) {
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}