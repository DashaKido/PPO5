package com.example.lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static public ArrayList<Trip> trips = new ArrayList<>();
    public static Trip selectedTrip;
    TripAdapter tripAdapter;
    private TimePicker timePicker;
    public static int sizeOfArray = 8;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    void fillData() {
        trips.add(new Trip("1001", BusType.OFFICIAL.name(), "Вокзал",
                10, 15, "10:15",
                10, 20, "10:20"));
        trips.add(new Trip("1001", BusType.OFFICIAL.name(), "Вокзал",
                10, 40, "10:40",
                10, 45, "10:45"));

        trips.add(new Trip("1006", BusType.BASIC.name(), "Чижовка",
                12, 55, "12:55",
                13, 0, "13:00"));
        trips.add(new Trip("1006", BusType.BASIC.name(), "Чижовка",
                15, 19, "15:19",
                15, 24, "15:24"));

        trips.add(new Trip("1053", BusType.HIGH_SPEED.name(), "Экспобел",
                1, 30, "01:30",
                1, 35, "01:35"));
        trips.add(new Trip("1053", BusType.HIGH_SPEED.name(), "Экспобел",
                2, 22, "02:22",
                2, 27, "02:27"));

        trips.add(new Trip("1212", BusType.BASIC.name(), "Сухарево-6",
                8, 0, "08:00",
                8, 5, "08:05"));
        trips.add(new Trip("1212", BusType.OFFICIAL.name(), "Сухарево-6",
                19, 38, "19:38",
                19, 43, "19:43"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void findTrip(View v) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        ArrayList<Trip> box = new ArrayList<>();
        for (Trip t : trips) {
            if (hour < t.getArrivalHour())
                box.add(t);
            else if (hour == t.getArrivalHour() && minute <= t.getArrivalMinute())
                box.add(t);
        }
        tripAdapter = new TripAdapter(box);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setAdapter(tripAdapter);
        Toast.makeText(this, "Показаны доступные рейсы", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.allTrips:
                tripAdapter = new TripAdapter(trips);
                RecyclerView recyclerView = findViewById(R.id.list);
                recyclerView.setAdapter(tripAdapter);
                Toast.makeText(this, "Показаны все рейсы", Toast.LENGTH_LONG).show();
                return true;
            case R.id.addTrip:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }
}
