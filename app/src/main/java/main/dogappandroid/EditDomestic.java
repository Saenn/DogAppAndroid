package main.dogappandroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditDomestic extends AppCompatActivity {
    private EditText dogname, dogage, dogbreed, dogcolor, dogaddress, dogsubdistrict, dogdistrict, dogprovince;
    private TextView ageView, genderView, nameView, colorage, colorgender;
    private RadioButton maleBtn, femaleBtn;
    private RadioGroup gender;
    private Button nextBtn;
    private DBHelper dbHelper;
    private Dog dog;
    private DogInformation info;
    private ImageView frontview,sideview;
    private ImageButton takeFrontPhoto,loadFrontPhoto,takeSidePhoto,loadSidePhoto;
    public static final int RESULT_LOAD_IMAGE_FRONT = 1;
    public static final int RESULT_LOAD_IMAGE_SIDE = 2;
    private static final int REQUEST_TAKE_PHOTO_FRONT = 3;
    private static final int REQUEST_TAKE_PHOTO_SIDE = 4;
    private String frontImagePath = "", sideImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_domestic);
        dbHelper = new DBHelper(this);

        dogname = (EditText) findViewById(R.id.nameDomestic);
        dogage = (EditText) findViewById(R.id.ageDomestic);
        dogbreed = (EditText) findViewById(R.id.breedDomestic);
        dogcolor = (EditText) findViewById(R.id.colorDomestic);
        dogaddress = (EditText) findViewById(R.id.addressDomestic);
        dogsubdistrict = (EditText) findViewById(R.id.subdistrictDomestic);
        dogdistrict = (EditText) findViewById(R.id.districtDomestic);
        dogprovince = (EditText) findViewById(R.id.provinceDomestic);
        maleBtn = (RadioButton) findViewById(R.id.maleDomesticButton);
        femaleBtn = (RadioButton) findViewById(R.id.femaleDomesticButton);
        gender = (RadioGroup) findViewById(R.id.genderDomestic);
        nextBtn = (Button) findViewById(R.id.nextDomesticButton);
        genderView = (TextView) findViewById(R.id.genderDomesticLabel);
        ageView = (TextView) findViewById(R.id.ageDomesticLabel);
        nameView = (TextView) findViewById(R.id.nameDomesticLabel);
        colorage = (TextView) findViewById(R.id.addDogRequired);
        colorgender = (TextView) findViewById(R.id.addDogRequired2);
        frontview = (ImageView) findViewById(R.id.dogFaceStray);
        sideview = (ImageView) findViewById(R.id.dogSideStray);
        takeFrontPhoto = (ImageButton) findViewById(R.id.takePhotoStrayFace);
        takeSidePhoto = (ImageButton) findViewById(R.id.takePhotoStraySide);
        loadFrontPhoto = (ImageButton) findViewById(R.id.loadPhotoStrayFace);
        loadSidePhoto = (ImageButton) findViewById(R.id.loadPhotoStraySide);

        getEditDogInfo();
        setAllButtonOnClick();


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
                        Uri photoURI = FileProvider.getUriForFile(EditDomestic.this,
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
                        Uri photoURI = FileProvider.getUriForFile(EditDomestic.this,
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
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/jpg");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_FRONT);
            }
        });

        loadSidePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/jpg");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_SIDE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dogage.getText().toString().equals("")) {
                    Toast.makeText(EditDomestic.this, "Please enter your puppy's age", Toast.LENGTH_LONG).show();
                } else if (gender.getCheckedRadioButtonId() == RadioButton.NO_ID) {
                    Toast.makeText(EditDomestic.this, "Please enter your puppy's gender", Toast.LENGTH_LONG).show();
                } else {
                    Bundle extras = new Bundle();
                    dog.setName(dogname.getText().toString());
                    if (maleBtn.isChecked()) {
                        dog.setGender("M");
                    } else if (femaleBtn.isChecked()) {
                        dog.setGender("F");
                    }
                    if (Integer.parseInt(dogage.getText().toString()) <= 3) {
                        dog.setAgeRange("1");
                    } else {
                        dog.setAgeRange("2");
                    }
                    dog.setAge(Integer.parseInt(dogage.getText().toString()));
                    dog.setBreed(dogbreed.getText().toString());
                    dog.setColor(dogcolor.getText().toString());
                    dog.setAddress(dogaddress.getText().toString());
                    dog.setSubdistrict(dogsubdistrict.getText().toString());
                    dog.setDistrict(dogdistrict.getText().toString());
                    dog.setProvince(dogprovince.getText().toString());
                    dog.setIsSubmit(0);
                    dbHelper.updateDog(dog);
                    //add Picture to Sqlite
                    addPicToSqlite(frontImagePath, 1, dog.getDogID());
                    addPicToSqlite(sideImagePath, 2, dog.getDogID());
                    Intent editVaccine = new Intent(EditDomestic.this, EditVaccine.class);
                    editVaccine.putExtra("internalDogID", dog.getId());
                    editVaccine.putExtras(extras);
                    overridePendingTransition(0, 0);
                    editVaccine.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(editVaccine);
                    finish();
                }
            }
        });


    }

    private void addPicToSqlite(String imagePath, int type, int index) {
        Bitmap src = BitmapFactory.decodeFile(imagePath);
        byte[] image = dbHelper.getBytes(src);
        DogImage dogImage = new DogImage();
        dogImage.setDog_internal_id(index);
        dogImage.setIsSubmit(0);

        if (type == 1) {
            dogImage.setType(1);
        } else {
            dogImage.setType(2);
        }
        dogImage.setKeyImage(image);
        dbHelper.addDogImage(dogImage);

    }

    private void getEditDogInfo() {
        Bundle prevBundle = getIntent().getExtras();
        if (prevBundle != null && prevBundle.containsKey("internalDogID")) {
            dog = dbHelper.getDogById(prevBundle.getInt("internalDogID"));
            info = dbHelper.getLastestDogInformationByDogID(prevBundle.getInt("internalDogID"));
            setAllField();
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
        dogprovince.setText(dog.getProvince());
        dogage.setText(String.valueOf(dog.getAge()));

        final DogImage imageFront = dbHelper.getDogFrontImageById(dog.getId());
        final DogImage imageSide = dbHelper.getDogSideImageById(dog.getId());

        frontview.setImageBitmap(dbHelper.getImage(imageFront.getKeyImage()));
        sideview.setImageBitmap(dbHelper.getImage(imageSide.getKeyImage()));

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
}

