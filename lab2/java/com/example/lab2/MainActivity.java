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
    TripAdapter tripAdapter;
    private TimePicker timePicker;
    static public int sizeOfArray = 5;
    TripAdapter.OnTripClickListener tripClickListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        fillData();

        tripClickListener = (trip, position) -> Toast.makeText(getApplicationContext(), "Был выбран пункт " + trip.getNumber(),
                Toast.LENGTH_SHORT).show();
        tripAdapter = new TripAdapter(this, trips, tripClickListener);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setAdapter(tripAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    String getType() {
        int some = (int) (1 + Math.random() * (3 + 1));
        String type;
        if (some == 1) {
            type = BusType.BASIC.name();
        } else if (some == 2) {
            type = BusType.HIGH_SPEED.name();
        } else if (some == 3) {
            type = BusType.OFFICIAL.name();
        } else {
            type = BusType.BASIC.name();
        }
        return type;
    }

    int getArrivalHour() {
        return (int) (Math.random() * (24));
    }

    int getArrivalMinute() {
        return (int) (Math.random() * (60));
    }

    void fillData() {
        for (int i = 1; i <= sizeOfArray; i++) {

            int arrivalHour = getArrivalHour();
            int arrivalMinute = getArrivalMinute();
            int departureHour;
            int departureMinute;
            String arrivalTime;
            String departureTime;

            if (arrivalHour == 23 && arrivalMinute >= 55) {
                arrivalTime = arrivalHour + ":" + arrivalMinute;
                departureHour = 0;
                departureMinute = arrivalMinute + 5 - 60;
                departureTime = "0" + departureHour + ":0" + departureMinute;
            } else if (arrivalHour <= 9 && arrivalMinute < 55 && arrivalMinute > 9) {
                arrivalTime = "0" + arrivalHour + ":" + arrivalMinute;
                departureHour = arrivalHour;
                departureMinute = arrivalMinute + 5;
                departureTime = "0" + departureHour + ":" + departureMinute;
            } else if (arrivalHour <= 9 && arrivalMinute < 10 && arrivalMinute > 4) {
                arrivalTime = "0" + arrivalHour + ":0" + arrivalMinute;
                departureHour = arrivalHour;
                departureMinute = arrivalMinute + 5;
                departureTime = "0" + departureHour + ":" + departureMinute;
            } else if (arrivalHour <= 9 && arrivalMinute <= 4) {
                arrivalTime = "0" + arrivalHour + ":0" + arrivalMinute;
                departureHour = arrivalHour;
                departureMinute = arrivalMinute + 5;
                departureTime = "0" + departureHour + ":0" + departureMinute;
            } else if (arrivalHour <= 23 && arrivalHour > 9 && arrivalMinute <= 4) {
                arrivalTime = arrivalHour + ":0" + arrivalMinute;
                departureHour = arrivalHour;
                departureMinute = arrivalMinute + 5;
                departureTime = departureHour + ":0" + departureMinute;
            } else if (arrivalHour <= 23 && arrivalHour > 9 && arrivalMinute < 55 && arrivalMinute > 9) {
                arrivalTime = arrivalHour + ":" + arrivalMinute;
                departureHour = arrivalHour;
                departureMinute = arrivalMinute + 5;
                departureTime = departureHour + ":" + departureMinute;
            } else if (arrivalHour <= 23 && arrivalHour > 9 && arrivalMinute < 10 && arrivalMinute > 4) {
                arrivalTime = arrivalHour + ":0" + arrivalMinute;
                departureHour = arrivalHour;
                departureMinute = arrivalMinute + 5;
                departureTime = departureHour + ":" + departureMinute;
            } else if (arrivalHour <= 23 && arrivalHour > 9 && arrivalMinute >= 55) {
                arrivalTime = arrivalHour + ":" + arrivalMinute;
                departureHour = arrivalHour + 1;
                departureMinute = arrivalMinute + 5 - 60;
                departureTime = departureHour + ":0" + departureMinute;
            } else if (arrivalHour < 9 && arrivalMinute >= 55) {
                arrivalTime = "0" + arrivalHour + ":" + arrivalMinute;
                departureHour = arrivalHour + 1;
                departureMinute = arrivalMinute + 5 - 60;
                departureTime = "0" + departureHour + ":0" + departureMinute;
            } else if (arrivalHour == 9 && arrivalMinute >= 55) {
                arrivalTime = arrivalHour + ":" + arrivalMinute;
                departureHour = arrivalHour + 1;
                departureMinute = arrivalMinute + 5 - 60;
                departureTime = departureHour + ":0" + departureMinute;
            } else {
                departureHour = arrivalHour;
                departureMinute = arrivalMinute + 5;
                arrivalTime = arrivalHour + ":" + arrivalMinute;
                departureTime = departureHour + ":" + departureMinute;
            }

            trips.add(new Trip("" + i, getType(), "some",
                    arrivalHour, arrivalMinute, arrivalTime,
                    departureHour, departureMinute, departureTime));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void findTrip(View v) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        ArrayList<Trip> box = new ArrayList<>();
        for (Trip t : trips) {
            if (hour < t.arrivalHour)
                box.add(t);
            else if (hour == t.arrivalHour && minute <= t.arrivalMinute)
                box.add(t);
        }
        tripAdapter = new TripAdapter(this, box, tripClickListener);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setAdapter(tripAdapter);
        Toast.makeText(this, "Показаны доступные рейсы", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.allTrips:
                tripAdapter = new TripAdapter(this, trips, tripClickListener);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
                recyclerView.setAdapter(tripAdapter);
                Toast.makeText(this, "Показаны все рейсы", Toast.LENGTH_LONG).show();
                return true;
            case R.id.addTrip:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
