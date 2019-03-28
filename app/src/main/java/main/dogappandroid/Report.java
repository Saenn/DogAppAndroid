package main.dogappandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Report extends AppCompatActivity {
    private Spinner province;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        province = (Spinner) findViewById(R.id.reportProvince);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.province_eng,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapter);
    }
}
