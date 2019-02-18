package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegisterActivity2 extends AppCompatActivity {

    private EditText addressEditText, subdistrictEditText, districtEditText, provinceEditText, phoneEditText;
    private Button nextButton;
    private Drawable originalStyle;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView userImage;
    private ImageButton takePhotoButton, loadPhotoButton;
    private String userImagePath;

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        subdistrictEditText = (EditText) findViewById(R.id.subdistrictEditText);
        districtEditText = (EditText) findViewById(R.id.districtEditText);
        provinceEditText = (EditText) findViewById(R.id.provinceEditText);
        nextButton = (Button) findViewById(R.id.nextButtonRegister2);
        userImage = (ImageView) findViewById(R.id.userImage);
        takePhotoButton = (ImageButton) findViewById(R.id.takePhotoButton);
        loadPhotoButton = (ImageButton) findViewById(R.id.loadPhotoButton);
        originalStyle = addressEditText.getBackground();
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

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
                        Uri photoURI = FileProvider.getUriForFile(RegisterActivity2.this,
                                "main.dogappandroid.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });

        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[0-9]*";
                    if (!phoneEditText.getText().toString().matches(regex))
                        phoneEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else phoneEditText.setBackground(originalStyle);
                }
            }
        });

        addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F/., ]*";
                    if (!addressEditText.getText().toString().matches(regex))
                        addressEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else addressEditText.setBackground(originalStyle);
                }
            }
        });

        subdistrictEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!subdistrictEditText.getText().toString().matches(regex))
                        subdistrictEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else subdistrictEditText.setBackground(originalStyle);
                }
            }
        });

        districtEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!districtEditText.getText().toString().matches(regex))
                        districtEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else districtEditText.setBackground(originalStyle);
                }
            }
        });

        provinceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
                    if (!provinceEditText.getText().toString().matches(regex))
                        provinceEditText.setBackgroundColor(getResources().getColor(R.color.pink100));
                    else provinceEditText.setBackground(originalStyle);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllInput()) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("address", addressEditText.getText().toString());
                    editor.putString("subdistrict", subdistrictEditText.getText().toString());
                    editor.putString("district", districtEditText.getText().toString());
                    editor.putString("province", provinceEditText.getText().toString());
                    editor.putString("phone", phoneEditText.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                    Intent prevIntent = getIntent();
                    intent.putExtra("firstname", prevIntent.getStringExtra("firstname"));
                    intent.putExtra("lastname", prevIntent.getStringExtra("lastname"));
                    intent.putExtra("email", prevIntent.getStringExtra("email"));
                    intent.putExtra("password", prevIntent.getStringExtra("password"));
                    intent.putExtra("forgotQuestion", prevIntent.getStringExtra("forgotQuestion"));
                    intent.putExtra("forgotAnswer", prevIntent.getStringExtra("forgotAnswer"));
                    intent.putExtra("address", addressEditText.getText().toString());
                    intent.putExtra("subdistrict", subdistrictEditText.getText().toString());
                    intent.putExtra("district", districtEditText.getText().toString());
                    intent.putExtra("province", provinceEditText.getText().toString());
                    intent.putExtra("phone", phoneEditText.getText().toString());
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(RegisterActivity2.this, "Your inputs are incorrect", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            System.out.println(userImagePath);
            File imgFile = new File(userImagePath);
            if (imgFile.exists()) {
                userImage.setImageURI(Uri.fromFile(imgFile));
            }
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
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "userImage_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        userImagePath = image.getAbsolutePath();
        return image;
    }

    protected boolean validateAllInput() {
        String phoneRegex = "[0-9]*";
        String addressRegex = "[a-zA-Z\\u0E00-\\u0E7F/., ]*";
        String regex = "[a-zA-Z\\u0E00-\\u0E7F ]*";
        if (phoneEditText.getText().toString().matches(phoneRegex) && addressEditText.getText().toString().matches(addressRegex) &&
                subdistrictEditText.getText().toString().matches(regex) && districtEditText.getText().toString().matches(regex) &&
                provinceEditText.getText().toString().matches(regex))
            return true;
        return false;
    }
}
