package main.dogappandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddDomestic3 extends AppCompatActivity {

    private Button nextButton;
    private ImageButton takeFrontPhoto, loadFrontPhoto, takeSidePhoto, loadSidePhoto;
    private ImageView frontview, sideview;
    public static final int RESULT_LOAD_IMAGE_FRONT = 1;
    public static final int RESULT_LOAD_IMAGE_SIDE = 2;
    private static final int REQUEST_TAKE_PHOTO_FRONT = 3;
    private static final int REQUEST_TAKE_PHOTO_SIDE = 4;
    private Bundle prevExtras;
    private String frontImagePath = "", sideImagePath = "";
    private int edit, editFront, editSide;
    private List<DogImage> imageList;
    private DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic3);

        editFront = 0;
        editSide = 0;
        imageList = new ArrayList<>();
        mHelper = new DBHelper(this);
        nextButton = (Button) findViewById(R.id.nextDomestic3);
        frontview = (ImageView) findViewById(R.id.dogFaceDomestic);
        sideview = (ImageView) findViewById(R.id.dogSideDomestic);
        takeFrontPhoto = (ImageButton) findViewById(R.id.takePhotoDomesticFace);
        takeSidePhoto = (ImageButton) findViewById(R.id.takePhotoDomesticSide);
        loadFrontPhoto = (ImageButton) findViewById(R.id.loadPhotoDomesticFace);
        loadSidePhoto = (ImageButton) findViewById(R.id.loadPhotoDomesticSide);

        Intent prevAdd = getIntent();
        prevExtras = prevAdd.getExtras();
        edit = prevExtras.getInt("edit");

        setAllButtonOnClick();
        setNextButton();
        getDogImage();
    }

    private void setNextButton(){

        if(edit == 0) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(AddDomestic3.this, Vaccine.class);
                    if (!frontImagePath.equals("") && !sideImagePath.equals("")) {
                        prevExtras.putString("frontview", frontImagePath);
                        prevExtras.putString("sideview", sideImagePath);
                        prevExtras.putString("addingdog", "yes");
                        intent.putExtras(prevExtras);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddDomestic3.this, "You have yet to take a photo of your puppy", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            nextButton.setText(R.string.done);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(AddDomestic3.this, DogProfileActivity.class);
                    if (!frontImagePath.equals("") && !sideImagePath.equals("")) {
                        prevExtras.remove("edit");
                        Dog dog = mHelper.getDogById(prevExtras.getInt("internal_dog_id"));
                        dog.setColor(prevExtras.getString("color"));
                        dog.setBreed(prevExtras.getString("breed"));
                        dog.setName(prevExtras.getString("name"));
                        mHelper.updateDog(dog);

                        // add picture to sqlite //
                        if(editFront == 1){
                            addPicToSqlite(frontImagePath, 1);
                        }
                        if(editSide == 1){
                            addPicToSqlite(sideImagePath, 2);
                        }

                        startActivity(intent);
                        intent.putExtras(prevExtras);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddDomestic3.this, "You have yet to take a photo of your puppy", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
                        Uri photoURI = FileProvider.getUriForFile(AddDomestic3.this,
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
                        Uri photoURI = FileProvider.getUriForFile(AddDomestic3.this,
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
            editFront = 1;
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
            editSide = 1;
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

    private void addPicToSqlite(String imagePath, int type){
        Bitmap src = BitmapFactory.decodeFile(imagePath);
        byte[] image = mHelper.getBytes(src);
        DogImage dogImage;

        if(prevExtras.containsKey("internal_dog_id")){
            if(type == 1){
                dogImage = mHelper.getDogFrontImageById(prevExtras.getInt("internal_dog_id"));
            }
            else{
                dogImage = mHelper.getDogSizeImageById(prevExtras.getInt("internal_dog_id"));
            }
            if(type == 1){
                dogImage.setType(1);
            }
            else{
                dogImage.setType(2);
            }
            dogImage.setKeyImage(image);
            mHelper.updateDogImage(dogImage, type);
        }

    }

    private void getDogImage(){
        if(prevExtras.containsKey("internal_dog_id")){
            imageList =  mHelper.getDogImageById(prevExtras.getInt("internal_dog_id"));
            byte[] b1 = imageList.get(0).getKeyImage();
            byte[] b2 = imageList.get(1).getKeyImage();

            if(imageList.size() >0){
                frontview.setImageBitmap(mHelper.getImage(b1));
                sideview.setImageBitmap(mHelper.getImage(b2));

                frontImagePath = new String(b1);
                sideImagePath = new String(b2);
            }
        }
    }


}
