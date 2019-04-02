package main.dogappandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import main.dogappandroid.Utilities.NetworkUtils;

public class Report extends AppCompatActivity {

    private Button provinceButton, regionButton, fullButton;
    private EditText email;
    private String usermail;
    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

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

        fullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Report.this);
                LayoutInflater inflater = getLayoutInflater();

                View myview = inflater.inflate(R.layout.custom_dialog_report_csv, null);
                builder.setView(myview);

                email = (EditText) myview.findViewById(R.id.report_csv_email);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check username password

                        String s = email.getText().toString();
                        usermail = email.getText().toString().trim();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if (usermail.matches(emailPattern) && s.length() > 0)
                        {
                            Toast.makeText(getApplicationContext(),"Email are sent!, please check your mailbox",Toast.LENGTH_SHORT).show();
                            new Report.onReportCsv().execute(s);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                );
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });
    }

    public class onReportCsv extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return NetworkUtils.getReportCsv(strs[0],
                    mPreferences.getString("token", ""),
                    mPreferences.getString("username", ""));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
