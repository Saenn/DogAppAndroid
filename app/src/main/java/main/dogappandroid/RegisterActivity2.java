package main.dogappandroid;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import main.dogappandroid.Utilities.NetworkUtils;


public class RegisterActivity2 extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1000;

    private EditText addressEditText, subdistrictEditText, districtEditText, provinceEditText, phoneEditText;
    private Button nextButton;
    private Drawable originalStyle;
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

        loadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterActivity2.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity2.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
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
                    editor.putString("pictureProfilePath", userImagePath);
                    editor.apply();

                    Map<String, String> params = new HashMap<>();
                    Intent intent = getIntent();
                    params.put("username", intent.getStringExtra("username"));
                    params.put("email", intent.getStringExtra("email"));
                    params.put("password", intent.getStringExtra("password"));
                    params.put("firstName", intent.getStringExtra("firstname"));
                    params.put("lastName", intent.getStringExtra("lastname"));
                    params.put("address", addressEditText.getText().toString());
                    params.put("subdistrict", subdistrictEditText.getText().toString());
                    params.put("district", districtEditText.getText().toString());
                    params.put("province", provinceEditText.getText().toString());
                    params.put("phone", phoneEditText.getText().toString());
                    params.put("forgotQuestion", intent.getStringExtra("forgotQuestion"));
                    params.put("forgotAnswer", intent.getStringExtra("forgotAnswer"));
                    params.put("profilePicturePath", userImagePath);
                    new RegisterActivity2.onRegister().execute(params);

                    Intent loginActivity = new Intent(RegisterActivity2.this, LoginActivity.class);
                    startActivity(loginActivity);
                } else {
                    Toast toast = Toast.makeText(RegisterActivity2.this, "Your inputs are incorrect", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
                } else {
                    Snackbar.make(findViewById(R.id.registerActivity2), R.string.requestPermissionDeny_EN, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File imgFile = new File(userImagePath);
            if (imgFile.exists()) {
                userImagePath = imgFile.getPath();
//              add image into gallery
                userImage.setImageURI(Uri.fromFile(imgFile));
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
            userImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
        if (phoneEditText.getText().toString().matches(phoneRegex) && addressEditText.getText().toString().matches(addressRegex) &&
                subdistrictEditText.getText().toString().matches(regex) && districtEditText.getText().toString().matches(regex) &&
                provinceEditText.getText().toString().matches(regex))
            return true;
        return false;
    }

    public class onRegister extends AsyncTask<Map<String, String>, Void, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            return NetworkUtils.register(maps[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String message = jsonObject.getString("message");
                Toast toast = Toast.makeText(RegisterActivity2.this, message, Toast.LENGTH_LONG);
                toast.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
