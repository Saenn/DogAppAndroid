package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DogProfileActivity extends AppCompatActivity {

    private ImageView dogProfilePic;
    private TextView dogName;
    private Button editProfileButton, editVaccineButton;
    private DogDB dog;
    private DogDB.DogDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);

        // setup var //
            dogProfilePic = (ImageView) findViewById(R.id.dog_profile_imageview);
            dogName = (TextView) findViewById(R.id.dog_profile_name);
            editProfileButton = (Button) findViewById(R.id.dog_profile_editprofile_button);
            editVaccineButton = (Button) findViewById(R.id.dog_profile_editvaccine_button);
            getDogInfo();
            setAllButton();
        // finished setup var //


    }

    private void getDogInfo(){
        Bundle prevBundle = getIntent().getExtras();
        if(prevBundle != null && prevBundle.containsKey("id")){
            dog = helper.getDogDBById(prevBundle.getString("id"));
        }
    }

    private void setAllButton(){
        editVaccineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogProfileActivity.this, Vaccine.class);
                intent.putExtra("id",dog.getId());
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogProfileActivity.this, AddDomestic.class);
                intent.putExtra("id",dog.getId());
                startActivity(intent);
            }
        });
    }
}
