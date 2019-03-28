package main.dogappandroid;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ReportProvince2 extends AppCompatActivity {

    private TextView provinceName, percent, percent2, domestic, stray, total;
    private ProgressBar progress, progress2;
    private int pStatus = 0;
    private int pStatus2 = 0;
    private Handler handler = new Handler();
    private int dom,str,tot;
    private DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_province2);
        mHelper = new DBHelper(this);
        setTextview();
    }

    private void setTextview(){
        provinceName = (TextView) findViewById(R.id.report_province2_name);
        percent = (TextView) findViewById(R.id.report_province_percent);
        percent2 = (TextView) findViewById(R.id.report_province_percent2);
        domestic = (TextView) findViewById(R.id.report_province_domestic);
        stray = (TextView) findViewById(R.id.report_province_stray);
        total = (TextView) findViewById(R.id.report_province_total);

        progress = (ProgressBar) findViewById(R.id.report_province_circular);
        progress2 = (ProgressBar) findViewById(R.id.report_province_circular2);

        if(getIntent().getExtras().containsKey("province")){
            provinceName.setText(getIntent().getExtras().getString("province"));
        }

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular_percentage);
        Drawable drawable2 = res.getDrawable(R.drawable.circular_percentage);

        progress.setProgress(0);   // Main Progress
        progress.setSecondaryProgress(100); // Secondary Progress
        progress.setMax(100); // Maximum Progress
        progress.setProgressDrawable(drawable);

        progress2.setProgress(0);   // Main Progress
        progress2.setSecondaryProgress(100); // Secondary Progress
        progress2.setMax(100); // Maximum Progress
        progress2.setProgressDrawable(drawable2);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 40) {
                    pStatus += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            progress.setProgress(pStatus);
                            percent.setText(pStatus + "%");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(20); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus2 < 60) {
                    pStatus2 += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            progress2.setProgress(pStatus2);
                            percent2.setText(pStatus2 + "%");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(20); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
