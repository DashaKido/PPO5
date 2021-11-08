package com.example.lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class AddActivity extends AppCompatActivity {

    TimePicker timePicker;
    public String busType = "BASIC";
    RadioGroup radioGroup;
    RadioButton button;

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        timePicker = findViewById(R.id.timePicker2);
        timePicker.setIs24HourView(true);
        radioGroup = findViewById(R.id.radioGroup1);
        button = findViewById(R.id.radioBasic1);
        button.setChecked(true);
        radioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.radioBasic1:
                    busType = BusType.BASIC.name();
                    break;
                case R.id.radioHigh1:
                    busType = BusType.HIGH_SPEED.name();
                    break;
                case R.id.radioOfficial1:
                    busType = BusType.OFFICIAL.name();
                    break;
                default:
                    break;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addTripButton(View v) {
        EditText numberEdit = findViewById(R.id.numberTrip);
        EditText destinationEdit = findViewById(R.id.destinationTrip);

        String number = numberEdit.getText().toString();
        String destination = destinationEdit.getText().toString();
        int arrivalHour = timePicker.getHour();
        int arrivalMinute = timePicker.getMinute();
        String arrivalTime;
        int departureHour;
        int departureMinute;
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
        if (number.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Не все данные введены", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Добавлен новый рейс", Toast.LENGTH_LONG).show();
            MainActivity.trips.add(new Trip(number, busType, destination,
                    arrivalHour, arrivalMinute, arrivalTime,
                    departureHour, departureMinute, departureTime));
            MainActivity.sizeOfArray++;
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        }
    }
}