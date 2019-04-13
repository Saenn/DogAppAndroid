package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class ConsentForm extends AppCompatActivity {

    private CheckBox agreeCheckBox;
    private Button nextButton;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeCheckBox.isChecked()){
                    Intent intent = new Intent(ConsentForm.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ConsentForm.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
