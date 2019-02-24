package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VaccineConsentForm extends AppCompatActivity {

    private Button nextButton,declineButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_consent_form);

        nextButton = (Button) findViewById(R.id.consentform_nextButton);
        declineButton = (Button) findViewById(R.id.consentform_declinenextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VaccineConsentForm.this, AddVaccineWithDog.class);
                startActivity(intent);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VaccineConsentForm.this, HomeActivityOld.class);
                startActivity(intent);
                addDog();
            }
        });
    }

    protected void addDog(){

        // Do something //

    }
}
