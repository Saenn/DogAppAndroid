package main.dogappandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Vaccine extends AppCompatActivity {

    private RecyclerView recyclerViewRabies,recyclerViewOthers;
    private RecyclerView.LayoutManager layoutManagerRabies,layoutManagerOther;
    private RecyclerView.Adapter mAdapterRabies,mAdapterOther;
    private List<DogVaccine> rabiesVaccine,othersVaccine;
    private Button addButton,doneButton;
    private DBHelper mHelper;
    private ClickListener rabiesListener, othersListener;
    private ProgressBar bar;
    private int isAdding = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);
        Intent service = new Intent(this, ServiceRunning.class);
        startService(service);
        Bundle prevBundle = getIntent().getExtras();

        // set var //
        mHelper = new DBHelper(this);
        rabiesVaccine = new ArrayList<DogVaccine>();
        othersVaccine = new ArrayList<DogVaccine>();
        addButton = (Button) findViewById(R.id.vaccine_addbutton);
        doneButton = (Button) findViewById(R.id.vaccine_doneButton);
        bar = (ProgressBar) findViewById(R.id.vaccine_progressbar);

        if(prevBundle != null && !prevBundle.containsKey("addingdog")){
            bar.setVisibility(View.GONE);
            if(prevBundle.containsKey("internal_dog_id")){
                rabiesVaccine = mHelper.getRabiesVaccineListById(prevBundle.getInt("internal_dog_id"));
                othersVaccine = mHelper.getOtherVaccineListById(prevBundle.getInt("internal_dog_id"));
            }
        }
        else{
            // มาจากหน้า add dog //
            isAdding = 1;
            rabiesVaccine = mHelper.getRabiesVaccineList();
            othersVaccine = mHelper.getOtherVaccineList();
        }


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
                intent.putExtra("isAdding", isAdding);
                startActivity(intent);
                finish();
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
            final DogVaccine v = myDataset.get(position);
            Log.i("VID : " , String.valueOf(v.getId()));
            holder.vaccine.setText(v.getName());
            holder.vaccinatedDate.setText(v.getDate());

            holder.setOnClickListener(new ClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (isLongClick) {
                        //LongClick//
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(Vaccine.this);
                        builder.setMessage("Are you sure to delete this vaccine?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // DB Helper Delete //

                                mHelper.deleteVaccine(String.valueOf(v.getId()));
                                reload();

                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                    } else {
                        //not LongClick
                        Intent I = new Intent(Vaccine.this, AddVaccineDropdown.class);
                        I.putExtra("isAdding", isAdding);
                        I.putExtra("vid",v.getId());
                        I.putExtra("vname",v.getName());
                        I.putExtra("vdate",v.getDate());
                        startActivity(I);
                        finish();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return myDataset.size();
        }


    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener, View.OnLongClickListener{
        // each data item is just a string in this case
        TextView vaccine,vaccinatedDate;
        private ClickListener myListener;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            vaccine = (TextView) v.findViewById(R.id.vaccine_name);
            vaccinatedDate = (TextView) v.findViewById(R.id.vaccine_date);
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

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


}


