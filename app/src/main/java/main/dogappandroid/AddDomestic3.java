package main.dogappandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDomestic3 extends AppCompatActivity {

    private Button nextButton;
    private ImageButton takeFrontPhoto, loadFrontPhoto, takeSidePhoto, loadSidePhoto;
    private ImageView frontview, sideview;
    public static final int RESULT_LOAD_IMAGE_FRONT = 1;
    public static final int RESULT_LOAD_IMAGE_SIDE = 2;
    private static final int REQUEST_TAKE_PHOTO_FRONT = 3;
    private static final int REQUEST_TAKE_PHOTO_SIDE = 4;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION_FRONT = 1000;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION_SIDE = 1100;
    private String frontImagePath = "", sideImagePath = "";
    private TextView frontLabel, sideLabel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic3);

        nextButton = (Button) findViewById(R.id.nextDomestic3);
        frontview = (ImageView) findViewById(R.id.dogFaceDomestic);
        sideview = (ImageView) findViewById(R.id.dogSideDomestic);
        takeFrontPhoto = (ImageButton) findViewById(R.id.takePhotoDomesticFace);
        takeSidePhoto = (ImageButton) findViewById(R.id.takePhotoDomesticSide);
        loadFrontPhoto = (ImageButton) findViewById(R.id.loadPhotoDomesticFace);
        loadSidePhoto = (ImageButton) findViewById(R.id.loadPhotoDomesticSide);
        frontLabel = (TextView) findViewById(R.id.frontDomesticLabel);
        sideLabel = (TextView) findViewById(R.id.sideDomesticLabel);

        setAllButtonOnClick();
        setNextButton();

        SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        updateView(preferences.getString("lang","en"));
    }

    private void setNextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!frontImagePath.equals("") && !sideImagePath.equals("")) {
                    Bundle extras = getIntent().getExtras();
                    extras.putString("frontview", frontImagePath);
                    extras.putString("sideview", sideImagePath);
                    Intent intent = new Intent(AddDomestic3.this, AddDomestic4.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddDomestic3.this, "You have yet to take a photo of your puppy", Toast.LENGTH_LONG).show();
                }
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
                if (ContextCompat.checkSelfPermission(AddDomestic3.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddDomestic3.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE_PERMISSION_FRONT);
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
                if (ContextCompat.checkSelfPermission(AddDomestic3.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddDomestic3.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE_PERMISSION_SIDE);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_SIDE);
                }
            }
        });

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
                    Toast.makeText(AddDomestic3.this, R.string.requestPermissionDeny_EN, Toast.LENGTH_LONG).show();
                }
            }
            case REQUEST_EXTERNAL_STORAGE_PERMISSION_SIDE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/jpg");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_SIDE);
                } else {
                    Toast.makeText(AddDomestic3.this, R.string.requestPermissionDeny_EN, Toast.LENGTH_LONG).show();
                }
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

    private void updateView(String lang) {
        Context context = LocalHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        nextButton.setText(resources.getString(R.string.nextButton));
        frontLabel.setText(resources.getString(R.string.dogFront_label));
        sideLabel.setText(resources.getString(R.string.dogSide_label));


    }

}
