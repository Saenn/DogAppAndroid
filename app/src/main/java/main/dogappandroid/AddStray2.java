package main.dogappandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddStray2 extends AppCompatActivity {

    private EditText addressEditText, subdistrictEditText, districtEditText, provinceEditText, phoneEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stray2);
    }
}
