package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import main.dogappandroid.Utilities.BitmapUtils;
import main.dogappandroid.Utilities.ProvinceUtils;

public class UserProfile extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    ImageView profileImage;
    TextView userID, fullname, address, subdistrict, district, province, phone, email, registered, latestUpdate, question, answer;
    Button editProfileBtn;
    String[] provinceListFromResource;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        provinceListFromResource = getResources().getStringArray(R.array.provinceList);
        bindAllData();
        showUserImage();
        showInformation();
        showSecurity();
        setButtonEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserImage();
        showInformation();
        showSecurity();
    }

    private void bindAllData() {
        profileImage = findViewById(R.id.userProfilePicture);
        userID = findViewById(R.id.userProfile_userID);
        fullname = findViewById(R.id.userProfile_fullName);
        address = findViewById(R.id.userProfile_address);
        subdistrict = findViewById(R.id.userProfile_subdistrict);
        district = findViewById(R.id.userProfile_district);
        province = findViewById(R.id.userProfile_province);
        phone = findViewById(R.id.userProfile_phone);
        email = findViewById(R.id.userProfile_email);
        registered = findViewById(R.id.userProfile_register);
        latestUpdate = findViewById(R.id.userProfile_latestUpdate);
        question = findViewById(R.id.userProfile_forgotQuestion);
        answer = findViewById(R.id.userProfile_forgotAnswer);
        editProfileBtn = findViewById(R.id.profileEditInformationButton);
    }

    private void showUserImage() {
        if (mPreferences.getString("profilePicturePath", "") != "") {
            Log.i("Image", "Loading");
            Bitmap userPicture = BitmapUtils.decodeSampledBitmapFromImagePath(mPreferences.getString("profilePicturePath", ""),150,150);
            profileImage.setImageBitmap(userPicture);
        }
    }

    private void showInformation() {
        userID.setText(mPreferences.getString("userID", ""));
        fullname.setText(mPreferences.getString("firstName", "") + " " + mPreferences.getString("lastName", ""));
        address.setText(mPreferences.getString("address", ""));
        subdistrict.setText(mPreferences.getString("subdistrict", ""));
        district.setText(mPreferences.getString("district", ""));
        province.setText(provinceListFromResource[ProvinceUtils.calculateProvincePosition(mPreferences.getString("province", ""))]);
        phone.setText(mPreferences.getString("phone", ""));
        email.setText(mPreferences.getString("email", ""));
        registered.setText(mPreferences.getString("registerDate", ""));
        latestUpdate.setText(mPreferences.getString("latestUpdate", ""));
    }

    private void showSecurity() {
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

    private void setButtonEvent() {
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, EditUserProfile.class);
                startActivity(intent);
            }
        });
    }
}
