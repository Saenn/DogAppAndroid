package main.dogappandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.dogappandroid.Utilities.BitmapUtils;
import main.dogappandroid.Utilities.ProvinceUtils;

public class EditStray extends AppCompatActivity {

    private Spinner ageSpinner, provinceSpinner;
    private ArrayList<String> ageList = new ArrayList<String>();
    private String selectedValue, selectedValue2;
    private EditText dogname, dogage, dogbreed, dogcolor, dogaddress, dogsubdistrict, dogdistrict, dogprovince;
    private TextView ageView, genderView, nameView, colorage, colorgender;
    private RadioButton maleBtn, femaleBtn;
    private RadioGroup gender;
    private Button nextBtn;
    private DBHelper dbHelper;
    private Dog dog;
    private DogInformation info;
    private ImageView frontview, sideview;
    private ImageButton takeFrontPhoto, loadFrontPhoto, takeSidePhoto, loadSidePhoto;
    public static final int RESULT_LOAD_IMAGE_FRONT = 1;
    public static final int RESULT_LOAD_IMAGE_SIDE = 2;
    private static final int REQUEST_TAKE_PHOTO_FRONT = 3;
    private static final int REQUEST_TAKE_PHOTO_SIDE = 4;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION_FRONT = 1000;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION_SIDE = 2000;
    private String frontImagePath = "", sideImagePath = "";
    private String[] provinceList;
    private Drawable originalStyle;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stray);
        dbHelper = new DBHelper(this);

        dogname = (EditText) findViewById(R.id.nameStray);
        dogbreed = (EditText) findViewById(R.id.breedStray);
        dogcolor = (EditText) findViewById(R.id.colorStray);
        dogaddress = (EditText) findViewById(R.id.addressStray);
        dogsubdistrict = (EditText) findViewById(R.id.subdistrictStray);
        dogdistrict = (EditText) findViewById(R.id.districtStray);
        maleBtn = (RadioButton) findViewById(R.id.maleStrayButton);
        femaleBtn = (RadioButton) findViewById(R.id.femaleStrayButton);
        gender = (RadioGroup) findViewById(R.id.genderStray);
        nextBtn = (Button) findViewById(R.id.nextStrayButton);
        genderView = (TextView) findViewById(R.id.genderStrayLabel);
        ageView = (TextView) findViewById(R.id.ageStrayLabel);
        nameView = (TextView) findViewById(R.id.nameStrayLabel);
        colorage = (TextView) findViewById(R.id.addDogRequired);
        colorgender = (TextView) findViewById(R.id.addDogRequired2);
        frontview = (ImageView) findViewById(R.id.dogFaceStray);
        sideview = (ImageView) findViewById(R.id.dogSideStray);
        takeFrontPhoto = (ImageButton) findViewById(R.id.takePhotoStrayFace);
        takeSidePhoto = (ImageButton) findViewById(R.id.takePhotoStraySide);
        loadFrontPhoto = (ImageButton) findViewById(R.id.loadPhotoStrayFace);
        loadSidePhoto = (ImageButton) findViewById(R.id.loadPhotoStraySide);
        originalStyle = dogaddress.getBackground();

        getEditDogInfo();
        setAllButtonOnClick();

        dogaddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    if (!dogaddress.getText().toString().matches("[0-9a-zA-Z\\u0E00-\\u0E7F/.,_ ]+")) {
                        dogaddress.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        dogaddress.setBackground(originalStyle);
                    }
                }
            }
        });

        dogsubdistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    if (!dogsubdistrict.getText().toString().matches("[a-zA-Z\\u0E00-\\u0E7F ]+")) {
                        dogsubdistrict.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        dogsubdistrict.setBackground(originalStyle);
                    }
                }
            }
        });

        dogdistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    if (!dogdistrict.getText().toString().matches("[a-zA-Z\\u0E00-\\u0E7F ]+")) {
                        dogdistrict.setBackgroundColor(getResources().getColor(R.color.pink100));
                    } else {
                        dogdistrict.setBackground(originalStyle);
                    }
                }
            }
        });

        // Setup Spinner //
        selectedValue = "";
        ageSpinner = (Spinner) findViewById(R.id.ageStray);
        ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ageList);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapterAge);

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedValue = "";
                } else {
                    Toast.makeText(EditStray.this,
                            "Select : " + ageList.get(position),
                            Toast.LENGTH_SHORT).show();
                    selectedValue = ageList.get(position);
                    Log.i("selectedvale : ", selectedValue);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Set Language
        SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        getListInfo(preferences.getString("lang", "th"));

        // Setup Spinner //
        selectedValue2 = dog.getProvince();
        provinceSpinner = (Spinner) findViewById(R.id.provinceSpinner);
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                provinceList);
        provinceSpinner.setAdapter(adapterProvince);
        provinceSpinner.setSelection(ProvinceUtils.calculateProvincePosition(selectedValue2));

        if (preferences.getString("lang", "th").equals("th")) {
            provinceList = getResources().getStringArray(R.array.provinceListTHEN);
        }
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue2 = provinceList[position];
                Log.i("selectedvale : ", selectedValue2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void setAllButtonOnClick() {
        takeFrontPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(EditStray.this,
                                "main.dogappandroid.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_FRONT);
                    }
                }
            }
        });

        takeSidePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile(2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(EditStray.this,
                                "main.dogappandroid.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_SIDE);
                    }
                }
            }
        });

        loadFrontPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditStray.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditStray.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE_PERMISSION_SIDE);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_FRONT);
                }
            }
        });

        loadSidePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditStray.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditStray.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE_PERMISSION_SIDE);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_SIDE);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(EditStray.this, R.string.dog_gender_error, Toast.LENGTH_LONG).show();
                } else {
                    int checkInput = validateAddressInput();
                    if (checkInput == 0) {
                        Bundle extras = new Bundle();
                        extras.putString("name", dogname.getText().toString());
                        if (maleBtn.isChecked()) {
                            extras.putString("gender", "M");
                        } else if (femaleBtn.isChecked()) {
                            extras.putString("gender", "F");
                        }
                        if (selectedValue.equals("Not exceed 3 years") || selectedValue.equals("ไม่เกิน 3 ปี")) {
                            extras.putString("ageRange", "1");
                        } else {
                            extras.putString("ageRange", "2");
                        }
                        extras.putString("breed", dogbreed.getText().toString());
                        extras.putString("color", dogcolor.getText().toString());
                        extras.putString("address", dogaddress.getText().toString());
                        extras.putString("subdistrict", dogsubdistrict.getText().toString());
                        extras.putString("district", dogdistrict.getText().toString());
                        extras.putString("province", selectedValue2);
                        if (!frontImagePath.equals(""))
                            extras.putString("frontImagePath", frontImagePath);
                        if (!sideImagePath.equals(""))
                            extras.putString("sideImagePath", sideImagePath);

                        Intent editVaccine = new Intent(EditStray.this, EditVaccine.class);
                        editVaccine.putExtra("internal_dog_id", dog.getId());
                        editVaccine.putExtras(extras);
                        overridePendingTransition(0, 0);
                        editVaccine.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(editVaccine);
                    } else if (checkInput == 1) {
                        Toast.makeText(EditStray.this, R.string.address_error, Toast.LENGTH_LONG).show();
                    } else if (checkInput == 2) {
                        Toast.makeText(EditStray.this, R.string.subdistrict_error, Toast.LENGTH_LONG).show();
                    } else if (checkInput == 3) {
                        Toast.makeText(EditStray.this, R.string.district_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private int validateAddressInput() {
        String addressRegex = "[0-9a-zA-Z\\u0E00-\\u0E7F/.,_ ]+";
        String regex = "[a-zA-Z\\u0E00-\\u0E7F ]+";
        if (!dogaddress.getText().toString().matches(addressRegex)) {
            return 1;
        } else if (!dogsubdistrict.getText().toString().matches(regex)) {
            return 2;
        } else if (!dogdistrict.getText().toString().matches(regex)) {
            return 3;
        } else {
            return 0;
        }
    }

    private void getEditDogInfo() {
        Bundle prevBundle = getIntent().getExtras();
        if (prevBundle != null && prevBundle.containsKey("internalDogID")) {
            dog = dbHelper.getDogById(prevBundle.getInt("internalDogID"));
            info = dbHelper.getLastestDogInformationByDogID(prevBundle.getInt("internalDogID"));
            setAllField();
            addAgeDataToList();
        }
    }

    private void setAllField() {

        // need to set name and image //
        dogname.setText(dog.getName());
        if (dog.getGender().equals("M")) {
            maleBtn.setChecked(true);
            femaleBtn.setChecked(false);
        } else if (dog.getGender().equals("F")) {
            maleBtn.setChecked(false);
            femaleBtn.setChecked(true);
        }
        dogcolor.setText(dog.getColor());
        dogbreed.setText(dog.getBreed());
        dogaddress.setText(dog.getAddress());
        dogsubdistrict.setText(dog.getSubdistrict());
        dogdistrict.setText(dog.getDistrict());

        final DogImage imageFront = dbHelper.getDogFrontImageById(dog.getId());
        final DogImage imageSide = dbHelper.getDogSideImageById(dog.getId());

        if (!imageFront.getImagePath().equals("")) {
            frontview.setImageBitmap(BitmapUtils.decodeSampledBitmapFromImagePath(imageFront.getImagePath(), 200, 200));
        }
        if (!imageSide.getImagePath().equals("")) {
            sideview.setImageBitmap(BitmapUtils.decodeSampledBitmapFromImagePath(imageSide.getImagePath(), 200, 200));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSION_FRONT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_FRONT);
                } else {
                    Toast.makeText(EditStray.this, R.string.requestPermissionDeny_EN, Toast.LENGTH_LONG).show();
                }
                break;
            }
            case REQUEST_EXTERNAL_STORAGE_PERMISSION_SIDE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_SIDE);
                } else {
                    Toast.makeText(EditStray.this, R.string.requestPermissionDeny_EN, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO_FRONT && resultCode == RESULT_OK) {
            File imgFile = new File(frontImagePath);
            if (imgFile.exists()) {
                frontImagePath = imgFile.getPath();
                frontview.setImageURI(Uri.fromFile(imgFile));
                galleryAddPic(frontImagePath);
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO_SIDE && resultCode == RESULT_OK) {
            File imgFile = new File(sideImagePath);
            if (imgFile.exists()) {
                sideImagePath = imgFile.getPath();
                sideview.setImageURI(Uri.fromFile(imgFile));
                galleryAddPic(sideImagePath);
            }
        } else if (requestCode == RESULT_LOAD_IMAGE_FRONT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            frontImagePath = picturePath;
            frontview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (requestCode == RESULT_LOAD_IMAGE_SIDE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            sideImagePath = picturePath;
            sideview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private File createImageFile(int side) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "dog_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        if (side == 1) frontImagePath = image.getAbsolutePath();
        else if (side == 2) sideImagePath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic(String ImagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(ImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void addAgeDataToList() {
        SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);

        if (dog.getAgeRange().equals("1")) {
            if (preferences.getString("lang", "th").equals("en")) {
                ageList.add(0, "Not exceed 3 years");
                ageList.add(1, "More than 3 years");
            } else {
                ageList.add(0, "ไม่เกิน 3 ปี");
                ageList.add(1, "มากกว่า 3 ปี");
            }
        } else {
            if (preferences.getString("lang", "th").equals("en")) {
                ageList.add(0, "More than 3 years");
                ageList.add(1, "Not exceed 3 years");

            } else {
                ageList.add(0, "มากกว่า 3 ปี");
                ageList.add(1, "ไม่เกิน 3 ปี");
            }
        }

    }

    private void getListInfo(String lang) {
        Context context = LocalHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        provinceList = resources.getStringArray(R.array.provinceList);
    }
}
