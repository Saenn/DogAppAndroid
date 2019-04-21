package main.dogappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Setting extends AppCompatActivity {

    private TextView headerLabel, appLanguageLabel;
    private RadioGroup languageGroup;
    private RadioButton thBtn, enBtn;
    private Button doneBtn;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"th"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        headerLabel = (TextView) findViewById(R.id.settingHeader);
        appLanguageLabel = (TextView) findViewById(R.id.primaryLanguageLabel);
        languageGroup = (RadioGroup) findViewById(R.id.languageChoice);
        thBtn = (RadioButton) findViewById(R.id.thBtn);
        enBtn = (RadioButton) findViewById(R.id.enBtn);
        doneBtn = (Button) findViewById(R.id.settingDoneButton);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thBtn.isChecked()){
                    setAppLocale("th");
                    SharedPreferences preferences = getSharedPreferences("defaultLanguage",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("lang","th");
                    editor.commit();
                }else{
                    setAppLocale("en");
                    SharedPreferences preferences = getSharedPreferences("defaultLanguage",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("lang","en");
                    editor.commit();
                }
                Intent home = new Intent(Setting.this, HomeActivity.class);
                startActivity(home);
                finish();
            }
        });

    }

    private void setAppLocale(String appLocale) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        Locale locale = new Locale(appLocale.toLowerCase());
        Locale.setDefault(locale);
        conf.setLocale(locale);
        conf.setLayoutDirection(locale);
        res.updateConfiguration(conf,dm);
    }


}
