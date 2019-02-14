package main.dogappandroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity3 extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        // declare variables //
        Button doneButton = (Button) findViewById(R.id.doneButton);
        Button addPhotoButton = (Button) findViewById(R.id.addprofilepic);
        imageView = (ImageView) findViewById(R.id.photo);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> params = new HashMap<>();
                Intent intent = getIntent();
                params.put("email",intent.getStringExtra("email"));
                params.put("password",intent.getStringExtra("password"));
                params.put("firstName",intent.getStringExtra("firstname"));
                params.put("lastName",intent.getStringExtra("lastname"));
                params.put("address",intent.getStringExtra("address"));
                params.put("subdistrict",intent.getStringExtra("subdistrict"));
                params.put("district",intent.getStringExtra("district"));
                params.put("province",intent.getStringExtra("province"));
                params.put("profilePicture","1");
                params.put("phone",intent.getStringExtra("phone"));
                params.put("forgotQuestion","1");
                params.put("forgotAnswer","1");
                new onRegister().execute(params);
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });
    }

    // get image back //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
    public class onRegister extends AsyncTask<Map<String,String>,Void,String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            return NetworkUtils.registerNewUser(maps[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String message = jsonObject.getString("message");
                Toast toast = Toast.makeText(RegisterActivity3.this,message,Toast.LENGTH_LONG);
                toast.show();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
