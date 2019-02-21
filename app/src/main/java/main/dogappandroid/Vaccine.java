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

public class Vaccine extends AppCompatActivity {

    private RecyclerView recyclerViewRabies,recyclerViewOthers;
    private RecyclerView.LayoutManager layoutManagerRabies,layoutManagerOther;
    private RecyclerView.Adapter mAdapterRabies,mAdapterOther;
    private Map<String, String> mDatasetRabies,mDatasetOther;
    private Button addButton,doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);

        // set var //
        addButton = (Button) findViewById(R.id.vaccine_addbutton);
        doneButton = (Button) findViewById(R.id.vaccine_doneButton);

        // set recycle view //
        recyclerViewRabies = (RecyclerView) findViewById(R.id.vaccine_listview_rabies);
        recyclerViewOthers = (RecyclerView) findViewById(R.id.vaccine_listview_other);

        layoutManagerRabies = new LinearLayoutManager(this);
        recyclerViewRabies.setLayoutManager(layoutManagerRabies);
        mAdapterRabies = new RecyclerViewAdapter(mDatasetRabies);
        recyclerViewRabies.setAdapter(mAdapterRabies);

        layoutManagerOther = new LinearLayoutManager(this);
        recyclerViewOthers.setLayoutManager(layoutManagerOther);
        mAdapterOther = new RecyclerViewAdapter(mDatasetOther);
        recyclerViewOthers.setAdapter(mAdapterOther);

        // set up button //
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vaccine.this, AddVaccineDropdown.class);
                startActivity(intent);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vaccine.this, HomeActivity.class);
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
                    .inflate(R.layout.vaccinelist, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.vaccine.setText("DHPP");
            holder.vaccinatedDate.setText("16-02-2009");
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView vaccine,vaccinatedDate;

        public ViewHolder(View v) {
            super(v);
            vaccine = (TextView) v.findViewById(R.id.vaccine_name);
            vaccinatedDate = (TextView) v.findViewById(R.id.vaccine_date);
        }
    }
//
//    protected void addMdataset(){
//        mDatasetRabies.put("Rabies","16-08-2018");
//        mDatasetOther.put("DHPP","16-08-2018");
//        mDatasetOther.put("DHPP","02-02-2019");
//    }

}


