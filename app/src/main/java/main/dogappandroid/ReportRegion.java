package main.dogappandroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ReportRegion extends AppCompatActivity {

    private BarChart chart, chart2;
    private List<BarEntry> barEntries, barEntries2, barEntries3, barEntries4, barEntries5, barEntries6, barTotal, barTotal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_region);
        chart = (BarChart) findViewById(R.id.report_region_chart);
        chart2 = (BarChart) findViewById(R.id.report_region_chart2);

        barEntries = new ArrayList<>();
        barEntries2 = new ArrayList<>();
        barEntries3 = new ArrayList<>();
        barEntries4 = new ArrayList<>();
        barEntries5 = new ArrayList<>();
        barEntries6 = new ArrayList<>();
        barTotal = new ArrayList<>();
        barTotal2 = new ArrayList<>();

        getBarentriesData();

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

    private void getBarentriesData() {
        barEntries.add(new BarEntry(1, 4000f));
        barEntries.add(new BarEntry(2, 9000f));

        barEntries2.add(new BarEntry(1, 4000f));
        barEntries2.add(new BarEntry(2, 5000f));

        barEntries3.add(new BarEntry(1, 3000f));
        barEntries3.add(new BarEntry(2, 1500f));

        barTotal.add(new BarEntry(1, 11000f));
        barTotal.add(new BarEntry(2, 15500f));

        barEntries4.add(new BarEntry(1, 30000f));
        barEntries4.add(new BarEntry(2, 15000f));

        barEntries5.add(new BarEntry(1, 4000f));
        barEntries5.add(new BarEntry(2, 11000f));

        barEntries6.add(new BarEntry(1, 8700f));
        barEntries6.add(new BarEntry(2, 9200f));

        barTotal2.add(new BarEntry(1, 42700f));
        barTotal2.add(new BarEntry(2, 35200f));
    }
}
