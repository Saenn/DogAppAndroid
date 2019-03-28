package main.dogappandroid;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    ImageView profileImage;
    TextView userID, fullname, address, subdistrict, district, province, phone, email, registered, latestUpdate, question, answer;
    ImageButton editProfileBtn, editSecurityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        bindAndShowUserImage();
        bindAndShowInformation();
        bindAndShowSecurity();
        bindButton();
    }

    private void bindAndShowUserImage() {
        profileImage = (ImageView) findViewById(R.id.userProfilePicture);
        if (mPreferences.getString("profilePictureInternalPath", "") != "") {
            Bitmap userPicture = BitmapFactory.decodeFile(mPreferences.getString("profilePictureInternalPath", ""));
            profileImage.setImageBitmap(userPicture);
        }
    }

    private void bindAndShowInformation() {
        userID = (TextView) findViewById(R.id.userProfile_userID);
        fullname = (TextView) findViewById(R.id.userProfile_fullName);
        address = (TextView) findViewById(R.id.userProfile_address);
        subdistrict = (TextView) findViewById(R.id.userProfile_subdistrict);
        district = (TextView) findViewById(R.id.userProfile_district);
        province = (TextView) findViewById(R.id.userProfile_province);
        phone = (TextView) findViewById(R.id.userProfile_phone);
        email = (TextView) findViewById(R.id.userProfile_email);
        registered = (TextView) findViewById(R.id.userProfile_register);
        latestUpdate = (TextView) findViewById(R.id.userProfile_latestUpdate);

        userID.setText(mPreferences.getString("userID", ""));
        fullname.setText(mPreferences.getString("firstName", "") + " " + mPreferences.getString("lastName", ""));
        address.setText(mPreferences.getString("address", ""));
        subdistrict.setText(mPreferences.getString("subdistrict", ""));
        district.setText(mPreferences.getString("district", ""));
        province.setText(mPreferences.getString("province", ""));
        phone.setText(mPreferences.getString("phone", ""));
        email.setText(mPreferences.getString("email", ""));
        registered.setText(mPreferences.getString("registerDate", ""));
        latestUpdate.setText(mPreferences.getString("latestUpdate", ""));

    }

    private void bindAndShowSecurity() {
        question = (TextView) findViewById(R.id.userProfile_forgotQuestion);
        answer = (TextView) findViewById(R.id.userProfile_forgotAnswer);

        switch (mPreferences.getString("forgotQuestion", "")) {
            case "1":
                question.setText("What is the name of the road you grew up on?");
                break;
            case "2":
                question.setText("What is your mother’s maiden name?");
                break;
            case "3":
                question.setText("What was the name of your first/current/favorite pet?");
                break;
            case "4":
                question.setText("What was the first company that you worked for?");
                break;
            case "5":
                question.setText("Where did you meet your spouse?");
                break;
            case "6":
                question.setText("Where did you go to high school/college?");
                break;
            case "7":
                question.setText("What is your favorite food?");
                break;
            case "8":
                question.setText("What city were you born in?");
                break;
            case "9":
                question.setText("Where is your favorite place to vacation?");
                break;
            case "0":
                question.setText("What Is your favorite book?");
                break;
            default:
                question.setText("");
        }

        answer.setText(mPreferences.getString("forgotAnswer", ""));
    }

    private void bindButton() {
        editProfileBtn = (ImageButton) findViewById(R.id.profileEditInformationButton);
        editSecurityBtn = (ImageButton) findViewById(R.id.profileEditSecurityButton);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, "Hello there!!", Toast.LENGTH_LONG).show();
            }
        });
        editSecurityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, "Hello there!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
