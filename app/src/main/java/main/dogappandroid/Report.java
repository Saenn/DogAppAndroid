package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Report extends AppCompatActivity {

    private Button provinceButton, regionButton, fullButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        provinceButton = (Button) findViewById(R.id.report_province);
        regionButton = (Button) findViewById(R.id.report_region);
        fullButton = (Button) findViewById(R.id.report_full);

        provinceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Report.this, ReportProvince.class);
                startActivity(i);
            }
        });

        regionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Report.this, ReportRegion.class);
                startActivity(i);
            }
        });
    }
}
