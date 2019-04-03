package main.dogappandroid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            finish();
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
                saveDataFromsServerToLocal(jsonObject);
                if(jsonObject.has("token")){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void saveDataFromsServerToLocal(JSONObject jsonObject) {
            try {
                try {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("token", jsonObject.getString("token"));
                    editor.putString("userID", String.valueOf(jsonObject.getInt("userID")));
                    editor.putString("username", jsonObject.getString("username"));
                    editor.putString("firstName", jsonObject.getString("firstName"));
                    editor.putString("lastName", jsonObject.getString("lastName"));
                    editor.putString("forgotQuestion", jsonObject.getString("forgotQuestion"));
                    editor.putString("forgotAnswer", jsonObject.getString("forgotAnswer"));
                    editor.putString("registerDate", jsonObject.getString("registerDate"));
                    editor.putString("latestUpdate", jsonObject.getString("latestUpdate"));
                    if (!jsonObject.getString("address").equals("null"))
                        editor.putString("address", jsonObject.getString("address"));
                    if (!jsonObject.getString("subdistrict").equals("null"))
                        editor.putString("subdistrict", jsonObject.getString("subdistrict"));
                    if (!jsonObject.getString("district").equals("null"))
                        editor.putString("district", jsonObject.getString("district"));
                    if (!jsonObject.getString("province").equals("null"))
                        editor.putString("province", jsonObject.getString("province"));
                    if (!jsonObject.getString("phone").equals("null"))
                        editor.putString("phone", jsonObject.getString("phone"));
                    if (!jsonObject.getString("lineID").equals("null"))
                        editor.putString("lineID", jsonObject.getString("lineID"));
                    if (!jsonObject.getString("facebookID").equals("null"))
                        editor.putString("facebookID", jsonObject.getString("facebookID"));
                    if (!jsonObject.getString("googleID").equals("null"))
                        editor.putString("googleID", jsonObject.getString("googleID"));
                    if (!jsonObject.getString("email").equals("null"))
                        editor.putString("email", jsonObject.getString("email"));
                    if (!jsonObject.getString("profilePicture").equals("null")) {
                        new onGetBitmapFromUrl().execute(jsonObject.getString("profilePicture"));
                    }
                    editor.apply();
                } catch (JSONException e) {
                    String message = jsonObject.getString("message");
                    Toast toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                Toast toast = Toast.makeText(LoginActivity.this, "There are some errors, please try again.", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public class onGetBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            return NetworkUtils.getBitmapFromUrl(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            SharedPreferences.Editor editor = mPreferences.edit();
            String imagePath = saveToInternalStorage(bitmap);
            editor.putString("profilePictureInternalPath", imagePath);
            editor.apply();
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mypath = new File(directory, "userProfile_" + timeStamp);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

}