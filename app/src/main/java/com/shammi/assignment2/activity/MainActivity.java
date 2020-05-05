package com.shammi.assignment2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.shammi.assignment2.R;
import com.shammi.assignment2.adapter.RaffleAdapter;
import com.shammi.assignment2.db.DBHelper;
import com.shammi.assignment2.model.RaffleModel;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private RecyclerView rvRaffle;
    private TextView tvEmptyList;
    private ArrayList<RaffleModel> raffleList;
    private RaffleAdapter adapter;
    private Bundle extras;
    private FloatingActionButton fab;
    private String status ="running";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Raffle Draw");

        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_viw);
        navigationView.setNavigationItemSelectedListener(this);

        rvRaffle = findViewById(R.id.rvRaffle);
        fab= findViewById(R.id.fab);
        raffleList = new ArrayList<>();

        tvEmptyList=findViewById(R.id.tvEmptyList);
        rvRaffle = findViewById(R.id.rvRaffle);
        rvRaffle.setHasFixedSize(true);
        rvRaffle.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            status = extras.getString("status");

            Log.d("main", "onCreate: "+status);
        }

        //check if the raffle selected status is running or ended
        if (status!=null&&status.equals("ended")){
            getSupportActionBar().setTitle("Ended Raffle");
            raffleList = dbHelper.getEndedRaffleList();
            fab.setVisibility(View.INVISIBLE);
        }
        else {
            raffleList = dbHelper.getRaffleList();
        }

        Log.d("check", "raffleList: " + raffleList.size());


        if (raffleList.size() > 0) {
            adapter = new RaffleAdapter(this, raffleList, status);
            rvRaffle.setAdapter(adapter);
        }
        else{
            rvRaffle.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }

    }

    //Action for add raffle
    public void gtAddRaffle(View view) {
        Intent intent = new Intent(this, AddRaffle.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_running) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        if (id == R.id.menu_ended) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("status", "ended");
            startActivity(intent);
            finish();
        }


        return false;
    }
}
