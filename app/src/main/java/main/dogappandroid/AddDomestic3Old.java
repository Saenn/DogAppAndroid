package main.dogappandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static main.dogappandroid.RegisterActivity2.REQUEST_TAKE_PHOTO;

public class AddDomestic3Old extends AppCompatActivity {

    private Button nextButton,addfront,addside;
    private ImageView frontview,sideview;
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int RESULT_LOAD_IMAGE2 = 2;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_TAKE_PHOTO2 = 2;
    private String ImagePath1,ImagePath2;
    private Bundle prevExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic3);

        nextButton = (Button) findViewById(R.id.domestic3_nextButton);
        frontview = (ImageView) findViewById(R.id.domestic3_frontpic);
        sideview = (ImageView) findViewById(R.id.domestic3_sidepic);
        addfront = (Button) findViewById(R.id.domestic3_addfrontpic);
        addside = (Button) findViewById(R.id.domestic3_addsidepic);

        Intent prevAdd = getIntent();
        prevExtras = prevAdd.getExtras();

        Log.d("AddDog3","name :" + prevAdd.getStringExtra("name"));
        Log.d("AddDog3","age :" + prevAdd.getIntExtra("age",0)+"");
        Log.d("AddDog3","ageRange :" + prevAdd.getStringExtra("ageRange"));
        Log.d("AddDog3","breed :" + prevAdd.getStringExtra("breed"));
        Log.d("AddDog3","color :" + prevAdd.getStringExtra("color"));

        Log.d("AddDog3","sterilized :" + prevAdd.getBooleanExtra("sterilized",false));
        Log.d("AddDog3","sterilizedDate :" + prevAdd.getStringExtra("sterilizedDate"));
        Log.d("AddDog3","address :" + prevAdd.getStringExtra("address"));
        Log.d("AddDog3","subdistrict :" + prevAdd.getStringExtra("subdistrict"));
        Log.d("AddDog3","district :" + prevAdd.getStringExtra("district"));
        Log.d("AddDog3","province :" + prevAdd.getStringExtra("province"));
        Log.d("AddDog3","dogType :" + prevAdd.getStringExtra("dogType"));


        setAllButtonOnClick();
    }

    public void setAllButtonOnClick(){
        addfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });

        addside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE2);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddDomestic3Old.this, Vaccine.class);
                if(!ImagePath1.equals("") && !!ImagePath2.equals("")){
                    prevExtras.putString("frontview",ImagePath1);
                    prevExtras.putString("sideview",ImagePath2);
                    intent.putExtras(prevExtras);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddDomestic3Old.this);
                    builder.setTitle(R.string.picmissing);
                    builder.setMessage(R.string.picmissingmessage);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File imgFile = new File(ImagePath1);
            if (imgFile.exists()) {
                ImagePath1 = imgFile.getPath();
//              add image into gallery
                frontview.setImageURI(Uri.fromFile(imgFile));
                galleryAddPic(ImagePath1);
            }
        }

        else if (requestCode == REQUEST_TAKE_PHOTO2 && resultCode == RESULT_OK) {
            File imgFile = new File(ImagePath2);
            if (imgFile.exists()) {
                ImagePath2 = imgFile.getPath();
//              add image into gallery
                sideview.setImageURI(Uri.fromFile(imgFile));
                galleryAddPic(ImagePath2);
            }
        }

        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImagePath1 = picturePath;
            frontview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }

        else if(requestCode == RESULT_LOAD_IMAGE2 && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImagePath2 = picturePath;
            sideview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private void galleryAddPic(String ImagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(ImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



}
