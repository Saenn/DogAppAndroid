package main.dogappandroid;

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
    private List<String> mDataset = new ArrayList<>();
    private DBHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_province);
        mHelper = new DBHelper(this);
        AddProvinceData();

        //        handle recycler view
        recyclerView = (RecyclerView) findViewById(R.id.reportprovince_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<String> mDataset;

        public RecyclerViewAdapter(List<String> mDataset) {
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
            final String provinceName = mDataset.get(position);
            holder.name.setText(provinceName);
            if(position % 2 != 0){
                holder.layout.setBackgroundColor(getResources().getColor(R.color.pink100));
            }
            else{
                holder.layout.setBackgroundColor(getResources().getColor(R.color.pink50));
            }
            holder.setOnClickListener(new ClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

                    Intent I = new Intent(ReportProvince.this, ReportProvince2.class);
                    I.putExtra("province", provinceName);
                    startActivity(I);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
        // each data item is just a string in this case
        public TextView name;
        private LinearLayout layout;
        private ClickListener myListener;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            name = (TextView) v.findViewById(R.id.province_name);
            layout = (LinearLayout) v.findViewById(R.id.province_list_linear);
        }

        @Override
        public void onClick(View v) {
            myListener.onClick(v, getAdapterPosition(), false, null);
        }

        @Override
        public boolean onLongClick(View view) {
            myListener.onClick(view, getAdapterPosition(), true, null);
            return true;
        }

        public void setOnClickListener(ClickListener listener) {
            this.myListener = listener;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            myListener.onClick(view, getAdapterPosition(), false, motionEvent);
            return true;
        }
    }

    private void AddProvinceData(){
        mDataset.add(0,"Bangkok");
        mDataset.add("Pattaya");
        mDataset.add("Krabi");
        mDataset.add("Kanchanaburi");
        mDataset.add("Kalasin");
        mDataset.add("Kamphaeng Phet");
        mDataset.add("Khon Kaen");
        mDataset.add("Chanthaburi");
        mDataset.add("Chachoengsao");
        mDataset.add("Chon Buri");
        mDataset.add("Chai Nat");
        mDataset.add("Chaiyaphum");
        mDataset.add("Chumphon");
        mDataset.add("Trang");
        mDataset.add("Trat");
        mDataset.add("Tak");
        mDataset.add("Nakhon Nayok");
        mDataset.add("Nakhon Pathom");
        mDataset.add("Nakhon Phanom");
        mDataset.add("Nakhon Ratchasima");
        mDataset.add("Nakhon Si Thammarat");
        mDataset.add("Nakhon Sawan");
        mDataset.add("Nonthaburi");
        mDataset.add("Narathiwat");
        mDataset.add("Nan");
        mDataset.add("Bueng Kan");
        mDataset.add("Buri Ram");
        mDataset.add("Pathum Thani");
        mDataset.add("Prachuap Khiri Khan");
        mDataset.add("Prachin Buri");
        mDataset.add("Pattani");
        mDataset.add("Phra Nakhon Si Ayutthaya");
        mDataset.add("Phayao");
        mDataset.add("Phangnga");
        mDataset.add("Phatthalung");
        mDataset.add("Phichit");
        mDataset.add("Phitsanulok");
        mDataset.add("Phuket");
        mDataset.add("Maha Sarakham");
        mDataset.add("Mukdahan");
        mDataset.add("Yala");
        mDataset.add("Yasothon");
        mDataset.add("Ranong");
        mDataset.add("Rayong");
        mDataset.add("Ratchaburi");
        mDataset.add("Roi Et");
        mDataset.add("Lop Buri");
        mDataset.add("Lampang");
        mDataset.add("Lamphun");
        mDataset.add("Si Sa Ket");
        mDataset.add("Sakon Nakhon");
        mDataset.add("Songkhla");
        mDataset.add("Satun");
        mDataset.add("Samut Prakan");
        mDataset.add("Samut Songkhram");
        mDataset.add("Samut Sakhon");
        mDataset.add("Saraburi");
        mDataset.add("Sa Kaeo");
        mDataset.add("Sing Buri");
        mDataset.add("Suphan Buri");
        mDataset.add("Surat Thani");
        mDataset.add("Surin");
        mDataset.add("Sukhothai");
        mDataset.add("Nong Khai");
        mDataset.add("Nong Bua Lam Phu");
        mDataset.add("Amnat Charoen");
        mDataset.add("Udon Thani");
        mDataset.add("Uttaradit");
        mDataset.add("Uthai Thani");
        mDataset.add("Ubon Ratchathani");
        mDataset.add("Ang Thong");
        mDataset.add("Chiang Rai");
        mDataset.add("Chiang Mai");
        mDataset.add("Phetchaburi");
        mDataset.add("Phetchabun");
        mDataset.add("Loei");
        mDataset.add("Phrae");
        mDataset.add("Mae Hong Son");
    }
}
