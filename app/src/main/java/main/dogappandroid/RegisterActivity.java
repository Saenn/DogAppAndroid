package main.dogappandroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullname, username, password, repassword;
    private Button nextButton;
    private Drawable originalStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = (EditText) findViewById(R.id.fullnameEditText);
        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        repassword = (EditText) findViewById(R.id.repasswordEditText);
        nextButton = (Button) findViewById(R.id.nextButton);
        originalStyle = fullname.getBackground();

        fullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]+";
                    if (!fullname.getText().toString().matches(regex))
                        fullname.setBackgroundColor(getResources().getColor(R.color.redError));
                    else fullname.setBackground(originalStyle);
                }
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z0-9.]+";
                    if (!username.getText().toString().matches(regex))
                        username.setBackgroundColor(getResources().getColor(R.color.redError));
                    else username.setBackground(originalStyle);
                }
            }
        });

        repassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!repassword.getText().toString().equals(password.getText().toString())) {
                        repassword.setBackgroundColor(getResources().getColor(R.color.redError));
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
                    intent.putExtra("fullname", fullname.getText().toString());
                    intent.putExtra("username", username.getText().toString());
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
        String usernameRegex = "[a-zA-Z0-9.]+";
        if (fullname.getText().toString().matches(fullnameRegex) && username.getText().toString().matches(usernameRegex) && repassword.getText().toString().equals(password.getText().toString()))
            return true;
        return false;
    }

}
