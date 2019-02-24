package main.dogappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Vaccine extends AppCompatActivity {

    private RecyclerView recyclerViewRabies,recyclerViewOthers;
    private RecyclerView.LayoutManager layoutManagerRabies,layoutManagerOther;
    private RecyclerView.Adapter mAdapterRabies,mAdapterOther;
    private List<DogVaccine> rabiesVaccine,othersVaccine,allVaccine;
    private Button addButton,doneButton;
    private DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);
        // set var //
        rabiesVaccine = new ArrayList<DogVaccine>();
        othersVaccine = new ArrayList<DogVaccine>();
        addButton = (Button) findViewById(R.id.vaccine_addbutton);
        doneButton = (Button) findViewById(R.id.vaccine_doneButton);

        mHelper = new DBHelper(this);
        rabiesVaccine = mHelper.getRabiesVaccineList();
        othersVaccine = mHelper.getOtherVaccineList();
//        allVaccine = mHelper.getVaccineList();
//        seperateData(allVaccine);

        // set recycle view //
        recyclerViewRabies = (RecyclerView) findViewById(R.id.vaccine_listview_rabies);
        recyclerViewOthers = (RecyclerView) findViewById(R.id.vaccine_listview_other);

        layoutManagerRabies = new LinearLayoutManager(this);
        recyclerViewRabies.setLayoutManager(layoutManagerRabies);
        mAdapterRabies = new RecyclerViewAdapter(rabiesVaccine);
        recyclerViewRabies.setAdapter(mAdapterRabies);

        layoutManagerOther = new LinearLayoutManager(this);
        recyclerViewOthers.setLayoutManager(layoutManagerOther);
        mAdapterOther = new RecyclerViewAdapter(othersVaccine);
        recyclerViewOthers.setAdapter(mAdapterOther);

        // set up button //
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vaccine.this, AddVaccineDropdown.class);
                startActivity(intent);
                finish();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vaccine.this, HomeActivityOld.class);
                startActivity(intent);
            }
        });

        Log.i("Vaccine",  String.valueOf(othersVaccine.size()));
    }

    @Override
    protected void onResume() {

        super.onResume();
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
        // remove
//        list.remove(position);
//        recycler.removeViewAt(position);
//        mAdapter.notifyItemRemoved(position);
//        mAdapter.notifyItemRangeChanged(position, list.size());
    }

    // Recycler class //
    protected class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        private List<DogVaccine> myDataset;

        public RecyclerViewAdapter(List<DogVaccine> mDataset) {
            myDataset = mDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vaccine_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DogVaccine v = myDataset.get(position);
            holder.vaccine.setText(v.getName());
            holder.vaccinatedDate.setText(v.getDate());
        }

        @Override
        public int getItemCount() {
            return myDataset.size();
        }

        public void swap(ArrayList<DogVaccine> datas)
        {
            myDataset.clear();
            myDataset.addAll(datas);
            notifyDataSetChanged();
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

    private void seperateData(List<DogVaccine> data){
        for(int i = 0 ; i < data.size() ; i++){
            DogVaccine d = data.get(i);
            if(d.getName().equals("Rabies")){
                rabiesVaccine.add(d);
            }
            else{
                othersVaccine.add(d);
            }
        }
    }

}


