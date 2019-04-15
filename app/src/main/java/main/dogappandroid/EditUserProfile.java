package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditUserProfile extends AppCompatActivity {
    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int RESULT_LOAD_IMAGE = 2;

    private Drawable originalStyle;

    private ImageView profileImage;
    private TextView firstname, lastname, address, subdistrict, district, province, phone, email, answer;
    private Button doneBtn;
    private ImageButton takePhotoButton, loadPhotoButton;
    private String userImagePath;
    private Spinner question;
    private int securityQuestionSelect;

    private Spinner provinceSpinner;
    private String[] provinceList;
    private String provinceValue;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userprofile);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        bindAndShowUserImage();
        bindAndShowInformation();
        bindAndShowSecurity();
        bindButton();
        bindAndSetSpinner();
    }

    private void bindAndShowUserImage() {
        profileImage = (ImageView) findViewById(R.id.userImage);
        if (mPreferences.getString("pictureProfilePath", "") != "") {
            Bitmap userPicture = BitmapFactory.decodeFile(mPreferences.getString("pictureProfilePath", ""));
            profileImage.setImageBitmap(userPicture);
        }
    }

    private void bindAndShowInformation() {
        firstname = (TextView) findViewById(R.id.firstNameEditText);
        lastname = (TextView) findViewById(R.id.lastNameEditText);
        address = (TextView) findViewById(R.id.addressEditText);
        subdistrict = (TextView) findViewById(R.id.subdistrictEditText);
        district = (TextView) findViewById(R.id.districtEditText);
        phone = (TextView) findViewById(R.id.phoneEditText);
        email = (TextView) findViewById(R.id.emailRegister);

        firstname.setText(mPreferences.getString("firstName", ""));
        lastname.setText(mPreferences.getString("lastName", ""));
        address.setText(mPreferences.getString("address", ""));
        subdistrict.setText(mPreferences.getString("subdistrict", ""));
        district.setText(mPreferences.getString("district", ""));
        phone.setText(mPreferences.getString("phone", ""));
        email.setText(mPreferences.getString("email", ""));
    }

    private void bindAndShowSecurity() {
        question = (Spinner) findViewById(R.id.securityQuestion);
        ArrayAdapter<String> securityQuestionSet = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.securityQuestionArray));
        answer = (TextView) findViewById(R.id.securityAnswer);
        Log.d("No. of Question : ", securityQuestionSet.getCount() + "");
        for (int i = 0; i < securityQuestionSet.getCount(); i++) {
            Log.d("secureQuestion is : ", securityQuestionSet.getItem(i));
        }
        question.setAdapter(securityQuestionSet);
        String questionNumber = mPreferences.getString("forgotQuestion", "");
        question.setSelection(Integer.parseInt(questionNumber));

        answer.setText(mPreferences.getString("forgotAnswer", ""));
    }

    private void bindButton() {
        originalStyle = address.getBackground();

        takePhotoButton = (ImageButton) findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(EditUserProfile.this,
                                "main.dogappandroid.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });

        loadPhotoButton = (ImageButton) findViewById(R.id.loadPhotoButton);
        loadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/jpg");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[0-9]*";
                    if (!phone.getText().toString().matches(regex))
                        phone.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else phone.setBackground(originalStyle);
                }
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F/., ]*";
                    if (!address.getText().toString().matches(regex))
                        address.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else address.setBackground(originalStyle);
                }
            }
        });

        subdistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!subdistrict.getText().toString().matches(regex))
                        subdistrict.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else subdistrict.setBackground(originalStyle);
                }
            }
        });

        district.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!district.getText().toString().matches(regex))
                        district.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else district.setBackground(originalStyle);
                }
            }
        });

        doneBtn = (Button) findViewById(R.id.doneButton);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllInput()) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("address", address.getText().toString());
                    editor.putString("subdistrict", subdistrict.getText().toString());
                    editor.putString("district", district.getText().toString());
                    editor.putString("province", provinceValue);
                    editor.putString("phone", phone.getText().toString());
                    editor.putString("pictureProfilePath", userImagePath);
                    editor.putString("email", email.getText().toString());
                    editor.putString("firstName", firstname.getText().toString());
                    editor.putString("lastName", lastname.getText().toString());
                    editor.putString("forgotQuestion", securityQuestionSelect + "");
                    editor.putString("forgotAnswer", answer.getText().toString());
                    editor.putBoolean("isSubmit", false);
                    editor.apply();
                    finish();
                } else {
                    Toast toast = Toast.makeText(EditUserProfile.this, "Your inputs are incorrect", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        question.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                securityQuestionSelect = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                securityQuestionSelect = 400;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File imgFile = new File(userImagePath);
            if (imgFile.exists()) {
                userImagePath = imgFile.getPath();
//              add image into gallery
                profileImage.setImageURI(Uri.fromFile(imgFile));
                galleryAddPic();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            userImagePath = picturePath;
            profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(userImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "pupify_user_image_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        userImagePath = image.getAbsolutePath();
        return image;
    }

    protected boolean validateAllInput() {
        String phoneRegex = "[0-9]*";
        String addressRegex = "[a-zA-Z\\u0E00-\\u0E7F/., ]*";
        String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
        if (phone.getText().toString().matches(phoneRegex) && address.getText().toString().matches(addressRegex) &&
                subdistrict.getText().toString().matches(regex) && district.getText().toString().matches(regex))
            return true;
        return false;
    }

    private void bindAndSetSpinner() {
        //Set Language
        SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        getListInfo(preferences.getString("lang", "th"));


        // Setup Spinner //
        provinceValue = "";
        provinceSpinner = (Spinner) findViewById(R.id.provinceEditUserSpinner);
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                provinceList);
        provinceSpinner.setAdapter(adapterProvince);


        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EditUserProfile.this,
                        "Select : " + provinceList[position],
                        Toast.LENGTH_SHORT).show();
                provinceValue = provinceList[position];
                Log.i("selectedvale : ", provinceValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListInfo(String lang) {
        Context context = LocalHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        provinceList = resources.getStringArray(R.array.provinceList);
    }

}
