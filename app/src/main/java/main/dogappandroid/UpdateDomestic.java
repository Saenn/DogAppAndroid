package main.dogappandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class UpdateDomestic extends AppCompatActivity {

    private Spinner dogStatus;
    private LinearLayout missingLayout, deathLayout, pregnantLayout, childrenLayout, sterilizedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_domestic);

        bindAllParameters();
        hideLayout();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dogStatusArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dogStatus.setAdapter(adapter);
        dogStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void bindAllParameters() {
        dogStatus = (Spinner) findViewById(R.id.dogStatus);
        missingLayout = (LinearLayout) findViewById(R.id.missingLayout);
        deathLayout = (LinearLayout) findViewById(R.id.deathLayout);
        pregnantLayout = (LinearLayout) findViewById(R.id.pregnantLayout);
        childrenLayout = (LinearLayout) findViewById(R.id.childrenLayout);
        sterilizedLayout = (LinearLayout) findViewById(R.id.sterilizedLayout);
    }

    private void hideLayout() {
        missingLayout.setVisibility(View.GONE);
        deathLayout.setVisibility(View.GONE);
        pregnantLayout.setVisibility(View.GONE);
        childrenLayout.setVisibility(View.GONE);
        sterilizedLayout.setVisibility(View.GONE);
    }
}
