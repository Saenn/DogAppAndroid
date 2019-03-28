package main.dogappandroid;


import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

public class ReportProvince2 extends AppCompatActivity {

    private TextView provinceName,indoor , outdoor, stray, total;
    private int dom,str,tot;
    private DBHelper mHelper;
    List<PieEntry> entries = new ArrayList<>();
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_province2);
        mHelper = new DBHelper(this);
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
        }

        indoor.setText("Indoor : 4,000");
        outdoor.setText("Outdoor : 2,500");
        stray.setText("Stray : 3,500");
        total.setText("Total : 10,000");
    }

    private void initData() {

        entries.add(new PieEntry(40.0f, "Indoor"));
        entries.add(new PieEntry(25.0f, "Outdoor"));
        entries.add(new PieEntry(35.0f, "Stray"));

        PieDataSet set = new PieDataSet(entries,"");
        set.setValueTextSize(12f);
        pieChart.setCenterText("Doggy");
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


}
