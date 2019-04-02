package main.dogappandroid;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.dogappandroid.Utilities.NetworkUtils;

public class ReportProvince2 extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    private TextView provinceName,indoor , outdoor, stray, total;
    private int indoornum,outdoornum,straynum,totalnum;
    private DBHelper mHelper;
    SharedPreferences mPreferences;
    List<PieEntry> entries = new ArrayList<>();
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_province2);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        setTextview();

        pieChart = (PieChart) findViewById(R.id.chart);
        initData();
    }

    private void setTextview(){

        provinceName = (TextView) findViewById(R.id.reportTop);
        indoor = (TextView) findViewById(R.id.report_indoor);
        outdoor = (TextView) findViewById(R.id.report_outdoor);
        stray = (TextView) findViewById(R.id.report_stray);
        total = (TextView) findViewById(R.id.report_total);

        if(getIntent().getExtras().containsKey("province")){
            provinceName.setText(getIntent().getExtras().getString("province"));
            new ReportProvince2.onReportProvince().execute(getIntent().getExtras().getString("province"));
        }

        indoor.setText("Indoor : " + indoornum);
        outdoor.setText("Outdoor : " + outdoornum);
        stray.setText("Stray : " + straynum);
        total.setText("Total : " + totalnum);
    }

    private void initData() {

        entries.add(new PieEntry(indoornum, "Indoor"));
        entries.add(new PieEntry(outdoornum, "Outdoor"));
        entries.add(new PieEntry(straynum, "Stray"));

        PieDataSet set = new PieDataSet(entries,"");
        set.setValueTextSize(12f);
        if(indoornum == 0 && outdoornum == 0 && straynum == 0){
            pieChart.setCenterText("No Data");
        }
        else{
            pieChart.setCenterText("Doggy");
        }
        pieChart.setCenterTextSize(18f);
        set.setColors(new int[]{Color.parseColor("#8CEBFF"),
                Color.parseColor("#C5FF8C"),
                Color.parseColor("#FFD28C"),});
        PieData data = new PieData(set);
        data.setValueTextSize(14f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setData(data);
        pieChart.animateXY(2000, 2000);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(16f);



//        pieChart.invalidate(); // refresh

    }

    public class onReportProvince extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return NetworkUtils.getReportProvince(strs[0],
                    mPreferences.getString("token", ""),
                    mPreferences.getString("username", ""));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                totalnum = jsonObject.getInt("all");
                indoornum = jsonObject.getInt("indoor");
                outdoornum = jsonObject.getInt("outdoor");
                straynum = jsonObject.getInt("stray");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
