package main.dogappandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Timer timer = new Timer();
        int begin = 0;
        int timeInterval = 500;
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                //call the method
                splash();
                counter++;
                if (counter >= 500){
                    timer.cancel();
                }
            }
        }, begin, timeInterval);
    }

    private void splash(){

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        if(mPreferences.contains("finished")){
            counter += 500;
            Intent ii = new Intent(SplashActivity.this,
                    HomeActivity.class);
            startActivity(ii);
            finish();
        }
    }
}

