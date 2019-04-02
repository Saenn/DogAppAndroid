package main.dogappandroid;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import main.dogappandroid.Utilities.NetworkUtils;

public class ReportRegion extends AppCompatActivity {

    private static final String sharedPrefFile = "main.dogappandroid.sharedpref";
    SharedPreferences mPreferences;
    private BarChart chart, chart2;
    private List<BarEntry> barEntries, barEntries2, barEntries3, barEntries4, barEntries5, barEntries6, barTotal, barTotal2;
    private int indoor_central, indoor_neast, indoor_north, indoor_south;
    private int outdoor_central, outdoor_north, outdoor_neast, outdoor_south;
    private int stray_central, stray_north, stray_neast, stray_south;
    private int all_central, all_north, all_south, all_neast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_region);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        indoor_central=0; indoor_north=0; indoor_neast=0; indoor_south=0;
        outdoor_central=0; outdoor_neast=0; outdoor_north=0; outdoor_south=0;
        stray_central=0; stray_neast=0; stray_north=0; stray_south=0;
        all_central =0; all_north=0; all_neast =0; all_south=0;
        new ReportRegion.onReportRegion().execute("eiei");

        chart = (BarChart) findViewById(R.id.report_region_chart);
        chart2 = (BarChart) findViewById(R.id.report_region_chart2);

    }

    public class onReportRegion extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return NetworkUtils.getReportRegion(strs[0],
                    mPreferences.getString("token", ""),
                    mPreferences.getString("username", ""));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.d("JSONOBJECT", jsonObject.toString());
                indoor_central = jsonObject.getInt("indoor_central");
                outdoor_central = jsonObject.getInt("outdoor_central");
                stray_central = jsonObject.getInt("stray_central");
                indoor_north = jsonObject.getInt("indoor_north");
                outdoor_north = jsonObject.getInt("outdoor_north");
                stray_north = jsonObject.getInt("stray_north");
                indoor_south = jsonObject.getInt("indoor_south");
                outdoor_south = jsonObject.getInt("outdoor_south");
                stray_south = jsonObject.getInt("stray_south");
                indoor_neast = jsonObject.getInt("indoor_neast");
                outdoor_neast = jsonObject.getInt("outdoor_neast");
                stray_neast = jsonObject.getInt("stray_neast");

                all_central = jsonObject.getInt("all_central");
                all_north = jsonObject.getInt("all_north");
                all_south = jsonObject.getInt("all_south");
                all_neast = jsonObject.getInt("all_neast");

                getBarentriesData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getBarentriesData() {

        barEntries = new ArrayList<>();
        barEntries2 = new ArrayList<>();
        barEntries3 = new ArrayList<>();
        barEntries4 = new ArrayList<>();
        barEntries5 = new ArrayList<>();
        barEntries6 = new ArrayList<>();
        barTotal = new ArrayList<>();
        barTotal2 = new ArrayList<>();

        barEntries.add(new BarEntry(1, indoor_north));
        barEntries.add(new BarEntry(2, indoor_neast));

        barEntries2.add(new BarEntry(1, outdoor_north));
        barEntries2.add(new BarEntry(2, outdoor_neast));

        barEntries3.add(new BarEntry(1, stray_north));
        barEntries3.add(new BarEntry(2, stray_neast));

        barTotal.add(new BarEntry(1, all_north));
        barTotal.add(new BarEntry(2, all_neast));

        barEntries4.add(new BarEntry(1, indoor_central));
        barEntries4.add(new BarEntry(2, indoor_south));

        barEntries5.add(new BarEntry(1, outdoor_central));
        barEntries5.add(new BarEntry(2,  outdoor_south));

        barEntries6.add(new BarEntry(1, stray_central));
        barEntries6.add(new BarEntry(2, stray_south));

        barTotal2.add(new BarEntry(1, all_central));
        barTotal2.add(new BarEntry(2, all_south));

        List<String> xValues = new ArrayList<String>();
        List<String> xValues2 = new ArrayList<String>();
        xValues.add("Northern");
        xValues.add("Northeastern");
        xValues2.add("Central");
        xValues2.add("Southern");


        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);

        // create 2 datasets
        BarDataSet set1 = new BarDataSet(barEntries, "Indoor");
        set1.setColor(Color.parseColor("#8CEBFF"));
        BarDataSet set2 = new BarDataSet(barEntries2, "Outdoor");
        set2.setColor(Color.parseColor("#C5FF8C"));
        BarDataSet set3 = new BarDataSet(barEntries3, "Stray");
        set3.setColor(Color.parseColor("#FFD28C"));
        BarDataSet setTotal = new BarDataSet(barTotal, "Total");
        setTotal.setColor(Color.parseColor("#FF8E9C"));


        set1.setValueTextSize(9f);
        set2.setValueTextSize(9f);
        set3.setValueTextSize(9f);
        setTotal.setValueTextSize(9f);

        BarData data = new BarData(set1, set2, set3, setTotal);
        float groupSpace = 0.08f;
        float barSpace = 0.1f; // x4 DataSet
        float barWidth = 0.13f; // x4 DataSet
        int groupCount = 2;
        data.setBarWidth(barWidth);

        chart.getDescription().setEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setData(data);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.invalidate();

        XAxis xAxis2 = chart2.getXAxis();
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(xValues2));
        xAxis2.setCenterAxisLabels(true);
        xAxis2.setAxisMinimum(0);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setGranularityEnabled(true);
        xAxis2.setGranularity(1f);

        BarDataSet set4 = new BarDataSet(barEntries4, "Indoor");
        set4.setColor(Color.parseColor("#8CEBFF"));
        BarDataSet set5 = new BarDataSet(barEntries5, "Outdoor");
        set5.setColor(Color.parseColor("#C5FF8C"));
        BarDataSet set6 = new BarDataSet(barEntries6, "Stray");
        set6.setColor(Color.parseColor("#FFD28C"));
        BarDataSet setTotal2 = new BarDataSet(barTotal2, "Total");
        setTotal2.setColor(Color.parseColor("#FF8E9C"));
        set4.setValueTextSize(9f);
        set5.setValueTextSize(9f);
        set6.setValueTextSize(9f);
        setTotal2.setValueTextSize(9f);

        BarData data2 = new BarData(set4, set5, set6, setTotal2);

        data2.setBarWidth(barWidth);

        chart2.getDescription().setEnabled(false);
        chart2.setDrawBarShadow(false);
        chart2.setDrawValueAboveBar(true);
        chart2.setPinchZoom(false);
        chart2.setDrawGridBackground(false);
        chart2.setData(data2);
        chart2.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart2.groupBars(0, groupSpace, barSpace);
        chart2.invalidate();

    }
}
