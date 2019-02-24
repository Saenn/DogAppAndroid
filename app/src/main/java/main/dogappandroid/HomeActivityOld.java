package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;


public class HomeActivityOld extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private Map<String, String> mDataset;
    private Button addDomesticBtn,addStrayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_old);

        // setup var //
        addDomesticBtn = (Button) findViewById(R.id.adddomestic);
        addStrayBtn = (Button) findViewById(R.id.addstray);
        // setup recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.doglistview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);


        // set onclick btn
        addDomesticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivityOld.this, AddDomestic.class);
                startActivity(intent);
            }
        });

        addStrayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivityOld.this, AddStray.class);
                startActivity(intent);
            }
        });
    }


    // Recycler class //
    protected class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        private Map<String, String> myDataset;

        public RecyclerViewAdapter(Map<String, String> mDataset) {
             myDataset = mDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dog_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.dogNameView.setText("NIKKY");
            holder.lastUpdatedView.setText("Kuy");
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView dogNameView,lastUpdatedView;

        public ViewHolder(View v) {
            super(v);
            dogNameView = (TextView) v.findViewById(R.id.dogName);
            lastUpdatedView = (TextView) v.findViewById(R.id.lastupdated);
        }
    }
}
