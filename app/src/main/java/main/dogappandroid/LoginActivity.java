package main.dogappandroid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import main.dogappandroid.Utilities.NetworkUtils;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView forgotPassword;
    private Button loginButton;
    private Button registerButton;

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;
    DBHelper dbHelper;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent service = new Intent(this, ServiceRunning.class);
        startService(service);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        dbHelper = new DBHelper(this);

        if (mPreferences.getString("token", "") != "") {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        forgotPassword = findViewById(R.id.forgotPassword);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordActivity = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(forgotPasswordActivity);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                if (username.getText().toString() != "" && password.getText().toString() != "") {
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());
                    new onLogin().execute(params);
                } else {
                    Toast toast = Toast.makeText(LoginActivity.this, "Please input your username and password before proceeding.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ConsentForm.class);
                startActivity(intent);
            }
        });

        //set App Language
        SharedPreferences preferences = getSharedPreferences("defaultLanguage",Context.MODE_PRIVATE);
        setAppLocale(preferences.getString("lang","th"));

    }

    private void setAppLocale(String appLocale) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(appLocale.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }

    public class onLogin extends AsyncTask<Map<String, String>, Void, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            return NetworkUtils.login(maps[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(s);
                saveDataFromsServerToLocal(jsonObject);
                if (jsonObject.has("token")) {
                    new fetchDogData().execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void saveDataFromsServerToLocal(JSONObject jsonObject) {
            try {
                try {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("token", jsonObject.getString("token"));
                    editor.putString("userID", String.valueOf(jsonObject.getInt("userID")));
                    editor.putString("username", jsonObject.getString("username"));
                    editor.putString("firstName", jsonObject.getString("firstName"));
                    editor.putString("lastName", jsonObject.getString("lastName"));
                    editor.putString("forgotQuestion", jsonObject.getString("forgotQuestion"));
                    editor.putString("forgotAnswer", jsonObject.getString("forgotAnswer"));
                    editor.putString("registerDate", jsonObject.getString("registerDate"));
                    editor.putString("latestUpdate", jsonObject.getString("latestUpdate"));
                    if (!jsonObject.getString("address").equals("null"))
                        editor.putString("address", jsonObject.getString("address"));
                    if (!jsonObject.getString("subdistrict").equals("null"))
                        editor.putString("subdistrict", jsonObject.getString("subdistrict"));
                    if (!jsonObject.getString("district").equals("null"))
                        editor.putString("district", jsonObject.getString("district"));
                    if (!jsonObject.getString("province").equals("null"))
                        editor.putString("province", jsonObject.getString("province"));
                    if (!jsonObject.getString("phone").equals("null"))
                        editor.putString("phone", jsonObject.getString("phone"));
                    if (!jsonObject.getString("email").equals("null"))
                        editor.putString("email", jsonObject.getString("email"));
                    if (!jsonObject.getString("profilePicture").equals("null")) {
                        new onGetUserProfileImage().execute(jsonObject.getString("profilePicture"));
                    }
                    editor.apply();
                } catch (JSONException e) {
                    String message = jsonObject.getString("message");
                    Toast toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                Toast toast = Toast.makeText(LoginActivity.this, "There are some errors, please try again.", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public class onGetUserProfileImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            return NetworkUtils.getBitmapFromUrl(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            SharedPreferences.Editor editor = mPreferences.edit();
            String imagePath = saveToInternalStorage(bitmap);
            editor.putString("profilePicturePath", imagePath);
            editor.apply();
        }
    }

    public class fetchDogData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("Success")) {
                        String data = jsonObject.getString("data");
                        JSONObject dataJSON = new JSONObject(data);
                        JSONArray dogEntity = dataJSON.getJSONArray("dogs");
                        for (int i = 0; i < dogEntity.length(); i++) {
                            JSONObject tmp = dogEntity.getJSONObject(i);
                            Dog dog = new Dog();
                            dog.setDogID(tmp.getInt("dogID"));
                            dog.setDogType(tmp.getString("dogType"));
                            dog.setGender(tmp.getString("gender"));
                            if (!tmp.optString("color", "null").equals("null"))
                                dog.setColor(tmp.getString("color"));
                            if (!tmp.optString("name", "null").equals("null"))
                                dog.setName(tmp.getString("name"));
                            if (!tmp.optString("breed", "null").equals("null"))
                                dog.setBreed(tmp.getString("breed"));
                            if (tmp.optInt("age", -1) != -1) dog.setAge(tmp.getInt("age"));
                            dog.setAgeRange(tmp.getString("ageRange"));
                            dog.setAddress(tmp.getString("address"));
                            dog.setSubdistrict(tmp.getString("subdistrict"));
                            dog.setDistrict(tmp.getString("district"));
                            dog.setProvince(tmp.getString("province"));
                            dog.setLatitude(tmp.getDouble("latitude"));
                            dog.setLongitude(tmp.getDouble("longitude"));
                            dog.setIsSubmit(1);
                            dbHelper.addDog(dog);
                        }
                        JSONArray dogInformationEntity = dataJSON.getJSONArray("dogInformations");
                        for (int i = 0; i < dogInformationEntity.length(); i++) {
                            JSONObject tmp = dogInformationEntity.getJSONObject(i);
                            DogInformation dogInformation = new DogInformation();
                            dogInformation.setDogID(dbHelper.getInternalDogIDbyExternalID(tmp.getInt("dogID")));
                            dogInformation.setDogStatus(tmp.getString("dogStatus"));
                            if (tmp.optInt("age", -1) != -1)
                                dogInformation.setAge(tmp.optInt("age"));
                            dogInformation.setAgeRange(tmp.getString("ageRange"));
                            if (tmp.optInt("pregnant", -1) != -1)
                                dogInformation.setPregnant(tmp.optInt("pregnant"));
                            if (tmp.optInt("childNumber", -1) != -1)
                                dogInformation.setChildNumber(tmp.optInt("childNumber"));
                            if (!tmp.optString("deathRemark", "null").equals("null"))
                                dogInformation.setDeathRemark(tmp.optString("deathRemark"));
                            try {
                                SimpleDateFormat fromRDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                Date rdsDate = fromRDS.parse(tmp.optString("missingDate"));
                                SimpleDateFormat toLocalDB = new SimpleDateFormat("dd/MM/yyyy");
                                dogInformation.setMissingDate(toLocalDB.format(rdsDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (!tmp.optString("sterilized", "null").equals("null"))
                                dogInformation.setSterilized(tmp.optInt("sterilized"));
                            try {
                                SimpleDateFormat fromRDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                Date rdsDate = fromRDS.parse(tmp.optString("sterilizedDate"));
                                SimpleDateFormat toLocalDB = new SimpleDateFormat("dd/MM/yyyy");
                                dogInformation.setSterilizedDate(toLocalDB.format(rdsDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            dogInformation.setIsSubmit(1);
                            dbHelper.addDogInformation(dogInformation);
                        }
                        JSONArray vaccineEntity = dataJSON.getJSONArray("vaccines");
                        for (int i = 0; i < vaccineEntity.length(); i++) {
                            JSONObject tmp = vaccineEntity.getJSONObject(i);
                            DogVaccine dogVaccine = new DogVaccine();
                            dogVaccine.setDogID(dbHelper.getInternalDogIDbyExternalID(tmp.getInt("dogID")));
                            dogVaccine.setName(tmp.getString("vaccineName"));
                            try {
                                SimpleDateFormat fromRDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                Date rdsDate = fromRDS.parse(tmp.getString("injectedDate"));
                                SimpleDateFormat toLocalDB = new SimpleDateFormat("dd/MM/yyyy");
                                dogVaccine.setDate(toLocalDB.format(rdsDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            dogVaccine.setIsSubmit(1);
                            dbHelper.addVaccine(dogVaccine);
                        }
                        JSONArray imageEntity = dataJSON.getJSONArray("pictures");
                        new onPutImageDataIntoSQLite().execute(imageEntity);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.retrieveDogData(mPreferences.getString("userID", ""),
                    mPreferences.getString("username", ""),
                    mPreferences.getString("token", ""));
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mypath = new File(directory, "userProfile_" + timeStamp);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public class onPutImageDataIntoSQLite extends AsyncTask<JSONArray, Void, List<DogImage>> {
        @Override
        protected void onPostExecute(List<DogImage> dogImages) {
            super.onPostExecute(dogImages);
            for (DogImage di : dogImages) {
                dbHelper.addDogImage(di);
            }
            Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(homeActivity);
            finish();
        }

        @Override
        protected List<DogImage> doInBackground(JSONArray... jsonArrays) {
            JSONArray pictureArray = jsonArrays[0];
            List<DogImage> dogImageList = new ArrayList<>();
            for (int i = 0; i < pictureArray.length(); i++) {
                try {
                    JSONObject dogImageData = pictureArray.getJSONObject(i);
                    DogImage dogImage = new DogImage();
                    dogImage.setIsSubmit(1);
                    dogImage.setDogInternalId(dbHelper.getInternalDogIDbyExternalID(dogImageData.getInt("dogID")));
                    dogImage.setType(Integer.parseInt(dogImageData.getString("side")));
                    Bitmap imageBitmap = NetworkUtils.getBitmapFromUrl(dogImageData.getString("picture"));
                    String pathToImage = saveToInternalStorage(imageBitmap);
                    dogImage.setImagePath(pathToImage);
                    dogImageList.add(dogImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return dogImageList;
        }
    }

}