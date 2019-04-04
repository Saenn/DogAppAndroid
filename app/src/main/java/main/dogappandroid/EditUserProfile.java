package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EditUserProfile extends AppCompatActivity {
    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    ImageView profileImage;
    TextView firstname, lastname, address, subdistrict, district, province, phone, email, question, answer;
    Button doneBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userprofile);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        bindAndShowUserImage();
        bindAndShowInformation();
//        bindAndShowSecurity();
        bindButton();
    }

    private void bindAndShowUserImage(){
        profileImage = (ImageView) findViewById(R.id.userProfilePicture);
        if (mPreferences.getString("profilePictureInternalPath", "") != "") {
            Bitmap userPicture = BitmapFactory.decodeFile(mPreferences.getString("profilePictureInternalPath", ""));
            profileImage.setImageBitmap(userPicture);
        }
    }

    private void bindAndShowInformation(){
        firstname = (TextView) findViewById(R.id.firstNameEditText);
        lastname = (TextView) findViewById(R.id.lastNameEditText);
        address = (TextView) findViewById(R.id.addressEditText);
        subdistrict = (TextView) findViewById(R.id.subdistrictEditText);
        district = (TextView) findViewById(R.id.districtEditText);
        province = (TextView) findViewById(R.id.provinceEditText);
        phone = (TextView) findViewById(R.id.phoneEditText);
        email = (TextView) findViewById(R.id.emailRegister);

        firstname.setText(mPreferences.getString("firstName", ""));
        lastname.setText(mPreferences.getString("lastName", ""));
        address.setText(mPreferences.getString("address", ""));
        subdistrict.setText(mPreferences.getString("subdistrict", ""));
        district.setText(mPreferences.getString("district", ""));
        province.setText(mPreferences.getString("province", ""));
        phone.setText(mPreferences.getString("phone", ""));
        email.setText(mPreferences.getString("email", ""));
    }

    private void bindAndShowSecurity(){
        question = (TextView) findViewById(R.id.userProfile_forgotQuestion);
        answer = (TextView) findViewById(R.id.securityAnswer);

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

    private void bindButton(){
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserProfile.this, UserProfile.class);
                startActivity(intent);
            }
        });
    }
}
