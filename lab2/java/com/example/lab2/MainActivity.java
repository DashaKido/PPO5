package com.example.lab2;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    static ArrayList<Trip> trips = new ArrayList<Trip>();
    BoxAdapter boxAdapter;
    static private TimePicker timePicker;
    static private int hour;
    static private int minute;

    /**
     * Called when the activity is first created.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    String getType() {
        BusType bus = null;
        int some = (int) (1 + Math.random() * (3 + 1));
        String type;
        if (some == 1) {
            type = bus.BASIC.name();
        } else if (some == 2) {
            type = bus.HIGH_SPEED.name();
        } else if (some == 3) {
            type = bus.OFFICIAL.name();
        } else {
            type = bus.BASIC.name();
        }

        return type;
    }

    int getArrivalHour() {
        int time = (int) (Math.random() * (24));
        return time;
    }

    int getArrivalMinute() {
        int time = (int) (Math.random() * (60));
        return time;
    }

    // генерируем данные для адаптера
    void fillData() {
        for (int i = 1; i <= 20; i++) {

            int arrivalHour = getArrivalHour();
            int arrivalMinute = getArrivalMinute();
            int departureHour = 0;
            int departureMinute = 0;
            String arrivalTime = "00:00";
            String departureTime = "00:00";

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
                    arrivalHour, arrivalMinute, arrivalTime, departureHour, departureMinute, departureTime));
        }
    }

    // вывод нужных рейсов
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void findTrip(View v) {
        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        ArrayList<Trip> box = new ArrayList<Trip>();
        for (Trip t : trips) {
            if (hour<t.arrivalHour)
                box.add(t);
            else if(hour==t.arrivalHour&&minute<=t.arrivalMinute)
                box.add(t);
        }
        boxAdapter = new BoxAdapter(this, box);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);
        Toast.makeText(this, "Показаны доступные рейсы", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.allTrips:
                boxAdapter = new BoxAdapter(this, trips);
                ListView lvMain = (ListView) findViewById(R.id.lvMain);
                lvMain.setAdapter(boxAdapter);
                Toast.makeText(this, "Показаны все рейсы", Toast.LENGTH_LONG).show();
                return true;
            case R.id.loadFile:
                Toast.makeText(this, "Загрузка файла", Toast.LENGTH_LONG).show();
                return true;
            case R.id.saveFile:
                Toast.makeText(this, "Сохранение файла", Toast.LENGTH_LONG).show();
                return true;
            case R.id.addTrip:
                Toast.makeText(this, "Добавление рейса", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
