package main.dogappandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "th"));
    }

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
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent i = new Intent(Report.this, ReportProvince.class);
                    startActivity(i);
                }else {
                    Toast.makeText(Report.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        regionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent i = new Intent(Report.this, ReportRegion.class);
                    startActivity(i);
                }else {
                    Toast.makeText(Report.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (mPreferences.getString("username", "").equals("admin")) {
            fullButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(Report.this);
                            LayoutInflater inflater = getLayoutInflater();

                            View myview = inflater.inflate(R.layout.custom_dialog_report_csv, null);
                            builder.setView(myview);

                            email = (EditText) myview.findViewById(R.id.report_csv_email);

                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String s = email.getText().toString();
                                            if (s.length() > 0) {
                                                new Report.onReportCsv().execute(s);
                                            } else {
                                                Toast.makeText(Report.this, R.string.email_error, Toast.LENGTH_SHORT).show();
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
                    }else {
                        Toast.makeText(Report.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            fullButton.setVisibility(View.GONE);
        }
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
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("Success")) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(Report.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Report.this, "This service is not available, please try again.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Report.this, "This service is not available, please try again.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
