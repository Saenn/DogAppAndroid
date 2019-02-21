package main.dogappandroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AddDomestic3 extends AppCompatActivity {

    private Button nextButton,addfront,addback,addside;
    private ImageView frontview,sideview,backview;
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int RESULT_LOAD_IMAGE2 = 2;
    public static final int RESULT_LOAD_IMAGE3 = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domestic3);

        nextButton = (Button) findViewById(R.id.domestic3_nextButton);
        frontview = (ImageView) findViewById(R.id.domestic3_frontpic);
        backview = (ImageView) findViewById(R.id.domestic3_backpic);
        sideview = (ImageView) findViewById(R.id.domestic3_sidepic);
        addfront = (Button) findViewById(R.id.domestic3_addfrontpic);
        addback = (Button) findViewById(R.id.domestic3_addbackpic);
        addside = (Button) findViewById(R.id.domestic3_addsidepic);

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

        addback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE2);
            }
        });

        addside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE3);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddDomestic3.this, VaccineConsentForm.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
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
            backview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        else if(requestCode == RESULT_LOAD_IMAGE3 && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            sideview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }


}