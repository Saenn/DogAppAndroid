package main.dogappandroid;

import android.content.DialogInterface;
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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Dog> mDataset;
    private DBHelper mHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHelper = new DBHelper(this);
        mDataset = mHelper.getDog();

//        handle recycler view
        recyclerView = (RecyclerView) findViewById(R.id.dogListView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DogListAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);

//        handle navigation bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout navigationHeader = (LinearLayout) navigationView.getHeaderView(0);
        navigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfile = new Intent(HomeActivity.this, UserProfile.class);
                startActivity(userProfile);
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_domestic:
                        Intent addDomestic = new Intent(HomeActivity.this, AddDomestic.class);
                        startActivity(addDomestic);
                        return true;
                    case R.id.add_stray:
                        Intent addStray = new Intent(HomeActivity.this, AddStray.class);
                        startActivity(addStray);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class DogListAdapter extends RecyclerView.Adapter<DogListViewHolder> {
        private List<Dog> mDataset;

        public DogListAdapter(List<Dog> mDataset) {
            this.mDataset = mDataset;
        }

        @Override
        public DogListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dog_list_item, parent, false);
            DogListViewHolder vh = new DogListViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(DogListViewHolder holder, int position) {
            final Dog dog = mDataset.get(position);
            final DogInformation info = mHelper.getAllDogInformationByDogID(dog.getId());
            final DogImage image = mHelper.getDogFrontImageById(dog.getId());

            // need to set image //
            if (dog.getName().equals("")) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setText(dog.getName().toUpperCase());
            }
            if (info.getAge() == 0) {
                if (info.getAgeRange().equals("1")) {
                    holder.age.setText("Age : " + "0-3");
                } else {
                    holder.age.setText("Age : " + "Older than 3");
                }
            } else {
                holder.age.setText("Age : " + info.getAge());
            }
            holder.color.setText("Color : " + dog.getColor());
            holder.gender.setText("Gender : " + dog.getGender());
            holder.breed.setText("Breed :" + dog.getBreed());
            holder.pic.setImageBitmap(mHelper.getImage(image.getKeyImage()));


            holder.setOnClickListener(new ClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

                    if (isLongClick) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage("Are you sure to delete this vaccine?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // DB Helper Delete //

                                mHelper.deleteDog(String.valueOf(dog.getId()));
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
                        Intent I = new Intent(HomeActivity.this, DogProfileActivity2.class);
                        I.putExtra("internalDogID", dog.getId());
                        startActivity(I);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    public class DogListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
        // each data item is just a string in this case
        public TextView age, gender, breed, color, name;
        public ImageView pic;
        private ClickListener myListener;

        public DogListViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);
            name = (TextView) v.findViewById(R.id.dog_list_name);
            age = (TextView) v.findViewById(R.id.dog_list_age);
            gender = (TextView) v.findViewById(R.id.dog_list_gender);
            breed = (TextView) v.findViewById(R.id.dog_list_breed);
            color = (TextView) v.findViewById(R.id.dog_list_color);
            pic = (ImageView) v.findViewById(R.id.dog_list_image);
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
