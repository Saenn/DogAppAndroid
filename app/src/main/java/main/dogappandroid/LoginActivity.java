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

    private static final String MY_PREFS = "my_prefs";

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button registerButton;

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        shared = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        editor = shared.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                if(username.getText().toString() != "" && password.getText().toString() != ""){
                    params.put("email",username.getText().toString());
                    params.put("password",password.getText().toString());
                    new onLogin().execute(params);
//                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(LoginActivity.this,"Please input your username and password before proceeding.",Toast.LENGTH_LONG);
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
            try {
                JSONObject jsonObject = new JSONObject(s);
                try {
                    String token = jsonObject.getString("token");
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("token",token);
                    editor.apply();
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