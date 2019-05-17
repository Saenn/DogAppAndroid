package main.dogappandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReportProvince extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] provinceList;
    private DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_province);
        mHelper = new DBHelper(this);
        SharedPreferences preferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);

        if (preferences.getString("lang", "th").equals("th")) {
            provinceList = getResources().getStringArray(R.array.provinceList);
        }

        //        handle recycler view
        recyclerView = (RecyclerView) findViewById(R.id.reportprovince_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapter(provinceList);
        recyclerView.setAdapter(mAdapter);
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        private String[] mDataset;

        public RecyclerViewAdapter(String[] mDataset) {
            this.mDataset = mDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.province_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String provinceName = mDataset[position];
            holder.name.setText(provinceName);
            if (position % 2 != 0) {
                holder.layout.setBackgroundColor(getResources().getColor(R.color.pink100));
            } else {
                holder.layout.setBackgroundColor(getResources().getColor(R.color.pink50));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent I = new Intent(ReportProvince.this, ReportProvince2.class);
                    I.putExtra("province", provinceName);
                    startActivity(I);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        private LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.province_name);
            layout = (LinearLayout) v.findViewById(R.id.province_list_linear);
        }
    }
}
