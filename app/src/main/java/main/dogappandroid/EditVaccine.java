package main.dogappandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class EditVaccine extends AppCompatActivity {

    private RecyclerView recyclerViewRabies, recyclerViewOthers;
    private RecyclerView.LayoutManager layoutManagerRabies, layoutManagerOther;
    private RecyclerView.Adapter mAdapterRabies, mAdapterOther;
    private List<DogVaccine> rabiesVaccine, othersVaccine;
    private Button addButton, doneButton;
    private DBHelper dbHelper;
    private ClickListener rabiesListener, othersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vaccine);
        Intent service = new Intent(this, ServiceRunning.class);
        startService(service);

        // set var //
        dbHelper = new DBHelper(this);
        queryFromDB();
        addButton = (Button) findViewById(R.id.vaccine_addbutton);
        doneButton = (Button) findViewById(R.id.vaccine_doneButton);

        rabiesVaccine = dbHelper.getRabiesVaccineList();
        othersVaccine = dbHelper.getOtherVaccineList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                Intent intent = new Intent(EditVaccine.this, AddVaccineDropdown.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle extras = getIntent().getExtras();
                for (DogVaccine v : rabiesVaccine) {
                    v.setDogID(extras.getInt("internalDogID"));
                    dbHelper.updateVaccine(v);
                }
                for (DogVaccine v : othersVaccine) {
                    v.setDogID(extras.getInt("internalDogID"));
                    dbHelper.updateVaccine(v);
                }
                List<DogVaccine> dvr = dbHelper.getRabiesVaccineListById(extras.getInt("internalDogID"));
                List<DogVaccine> dvo = dbHelper.getOtherVaccineListById(extras.getInt("internalDogID"));
                for (DogVaccine v : dvr) {
                    Log.i("DogVaccineRabies", v.getId() + " " + v.getDate());
                }
                for (DogVaccine v : dvo) {
                    Log.i("DogVaccineOthers", v.getId() + " " + v.getDate());
                }

                Intent intent = new Intent(EditVaccine.this, DogProfileActivity2.class);
                startActivity(intent);
                dbHelper.deleteNull();
                finish();
            }
        });

        // set recycle view //
        recyclerViewRabies = (RecyclerView) findViewById(R.id.vaccine_listview_rabies);
        recyclerViewOthers = (RecyclerView) findViewById(R.id.vaccine_listview_other);
        layoutManagerRabies = new LinearLayoutManager(this);
        recyclerViewRabies.setLayoutManager(layoutManagerRabies);
//        mAdapterRabies = new EditVaccine.RecyclerViewAdapter(rabiesVaccine);
//        recyclerViewRabies.setAdapter(mAdapterRabies);
        layoutManagerOther = new LinearLayoutManager(this);
        recyclerViewOthers.setLayoutManager(layoutManagerOther);
//        mAdapterOther = new EditVaccine.RecyclerViewAdapter(othersVaccine);
//        recyclerViewOthers.setAdapter(mAdapterOther);
    }

    // Recycler class //
    protected class RecyclerViewAdapter extends RecyclerView.Adapter<EditVaccine.ViewHolder> {

        private List<DogVaccine> myDataset;

        public RecyclerViewAdapter(List<DogVaccine> mDataset) {
            myDataset = mDataset;
        }

        @Override
        public EditVaccine.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vaccine_list_item, parent, false);
            EditVaccine.ViewHolder vh = new EditVaccine.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(EditVaccine.ViewHolder holder, int position) {
            final DogVaccine v = myDataset.get(position);
            holder.vaccine.setText(v.getName());
            holder.vaccinatedDate.setText(v.getDate());

            holder.setOnClickListener(new ClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (isLongClick) {
                        //LongClick//
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(EditVaccine.this);
                        builder.setMessage("Are you sure to delete this vaccine?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // DB Helper Delete //

                                dbHelper.deleteVaccine(String.valueOf(v.getId()));
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
                        Intent I = new Intent(EditVaccine.this, AddVaccineDropdown.class);
                        Bundle extras = getIntent().getExtras();
                        I.putExtras(extras);
                        I.putExtra("isAdding", 1);
                        I.putExtra("vid", v.getId());
                        I.putExtra("vname", v.getName());
                        I.putExtra("vdate", v.getDate());
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

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
        // each data item is just a string in this case
        TextView vaccine, vaccinatedDate;
        private ClickListener myListener;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            vaccine = (TextView) v.findViewById(R.id.vaccine_name_label);
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

    private void queryFromDB() {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("internalDogID")) {
            rabiesVaccine = dbHelper.getRabiesVaccineListById(extras.getInt("internalDogID"));
            othersVaccine = dbHelper.getOtherVaccineListById(extras.getInt("internalDogID"));
            setAllFields();
        }
    }

    private void setAllFields(){
        mAdapterRabies = new RecyclerViewAdapter(rabiesVaccine);
        recyclerViewRabies.setAdapter(mAdapterRabies);
        mAdapterOther = new RecyclerViewAdapter(othersVaccine);
        recyclerViewOthers.setAdapter(mAdapterOther);

    }

}
