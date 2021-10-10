package com.example.lab2;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class MainActivity extends Activity {

    static ArrayList<Trip> trips = new ArrayList<Trip>();
    BoxAdapter boxAdapter;
    static private TimePicker timePicker;
    static private int hour;

    /**
     * Called when the activity is first created.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        // создаем адаптер
        fillData();
        boxAdapter = new BoxAdapter(this, trips);

        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);
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

    int getArrivalTime() {
        int time = (int) (Math.random() * (24));
        return time;
    }


    // генерируем данные для адаптера
    void fillData() {
        for (int i = 1; i <= 20; i++) {
            int arrivedTime = getArrivalTime();
            String departureTime = arrivedTime + ":05";
            trips.add(new Trip("" + i, getType(), "some",
                    arrivedTime, departureTime, false));
        }
    }

    // выводим информацию о рейсах
    public void showResult(View v) {
        String result = "Выбраны рейсы:";
        for (Trip t : boxAdapter.getBox()) {
            if (t.box)
                result += "\n" + t.number;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    // вывод нужных рейсов
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void findTrip(View v) {
        hour = timePicker.getHour();
        ArrayList<Trip> box = new ArrayList<Trip>();
        for (Trip t : trips) {
            if(hour<=t.arrivalTime)
                box.add(t);
        }
        boxAdapter = new BoxAdapter(this, box);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);
    }

    // вывод всех рейсов
    public void showAll(View v) {
        boxAdapter = new BoxAdapter(this, trips);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);
    }
}