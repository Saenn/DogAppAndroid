package main.dogappandroid;

import android.content.Context;
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

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;
    private EditText username, password, repassword;
    private Button nextBtn;
    private Drawable originalStyle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        username = (EditText) findViewById(R.id.usernameForgotPassword);
        password = (EditText) findViewById(R.id.passwordForgot);
        repassword = (EditText) findViewById(R.id.repasswordForgot);
        nextBtn = (Button) findViewById(R.id.nextButtonForgot);
        originalStyle = username.getBackground();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                int checkInput = validateInputs();
                if (checkInput == 4) {
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());
                    new onRequestForgot().execute(params);
                } else if (checkInput == 1) {
                    Toast.makeText(ForgotPassword.this, R.string.usernameError, Toast.LENGTH_LONG).show();
                } else if (checkInput == 2) {
                    Toast.makeText(ForgotPassword.this, R.string.passwordIsNotMatchError, Toast.LENGTH_LONG).show();
                } else if (checkInput == 3) {
                    Toast.makeText(ForgotPassword.this, R.string.emptyPasswordError, Toast.LENGTH_LONG).show();
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
                        username.setHint("should only be 0-9 A-Z a-z .");
                    }
                }
            }
        });
    }


    public void logout() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
        Intent login = new Intent(ForgotPassword.this, HomeActivity.class);
        startActivity(login);
    }

    private int validateInputs() {
        String usernameRegex = "[a-zA-Z0-9._-]+";
        if (!username.getText().toString().matches(usernameRegex)) return 1;
        else if (!password.getText().toString().equals(repassword.getText().toString())) return 2;
        else if (password.getText().toString().equals("")) return 3;
        else return 4;
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
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    try {
                        String message = jsonObject.getString("message");
                        Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_LONG).show();
                        logout();
                        Intent loginIntent = new Intent(ForgotPassword.this, LoginActivity.class);
                        finish();
                        startActivity(loginIntent);
                    } catch (JSONException e2) {
                        String error = jsonObject.getString("error");
                        Toast.makeText(ForgotPassword.this, error, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, R.string.internet_disconnect_error, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
