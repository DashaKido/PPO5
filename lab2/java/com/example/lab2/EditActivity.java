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

public class EditActivity extends AppCompatActivity {

    TimePicker timePicker;
    EditText numberEdit, destinationEdit;
    Trip selectedTrip;
    String number, destination;
    int arrivalHour, arrivalMinute;
    String arrivalTime;
    int departureHour;
    int departureMinute;
    String departureTime;
    RadioGroup radioGroup;
    RadioButton button;
    public String busType = "";

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        timePicker = findViewById(R.id.timePicker3);
        timePicker.setIs24HourView(true);
        numberEdit = findViewById(R.id.numberTrip2);
        destinationEdit = findViewById(R.id.destinationTrip2);
        selectedTrip = MainActivity.selectedTrip;

        number = selectedTrip.getNumber();
        numberEdit.setText(number);

        destination = selectedTrip.getDestination();
        destinationEdit.setText(destination);

        arrivalHour = selectedTrip.getArrivalHour();
        timePicker.setHour(arrivalHour);

        arrivalMinute = selectedTrip.getArrivalMinute();
        timePicker.setMinute(arrivalMinute);
        radioGroup = findViewById(R.id.radioGroup2);
        busType = selectedTrip.getBusType();
        if (busType.equals(BusType.OFFICIAL.name())) {
            button = findViewById(R.id.radioOfficial2);
            button.setChecked(true);
        }
        if (busType.equals(BusType.BASIC.name())) {
            button = findViewById(R.id.radioBasic2);
            button.setChecked(true);
        }
        if (busType.equals(BusType.HIGH_SPEED.name())) {
            button = findViewById(R.id.radioHigh2);
            button.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.radioBasic2:
                    busType = BusType.BASIC.name();
                    break;
                case R.id.radioHigh2:
                    busType = BusType.HIGH_SPEED.name();
                    break;
                case R.id.radioOfficial2:
                    busType = BusType.OFFICIAL.name();
                    break;
                default:
                    break;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void editTripButton(View v) {
        number = numberEdit.getText().toString();
        destination = destinationEdit.getText().toString();
        arrivalHour = timePicker.getHour();
        arrivalMinute = timePicker.getMinute();
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
        } else if (number.length() > 4 || number.length() > 4) {
            Toast.makeText(this, "Номер должен содержать 4 цифры", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Выбранный рейс изменен", Toast.LENGTH_LONG).show();
            selectedTrip.setNumber(number);
            selectedTrip.setDestination(destination);
            selectedTrip.setArrivalHour(arrivalHour);
            selectedTrip.setArrivalMinute(arrivalMinute);
            selectedTrip.setArrivalTime(arrivalTime);
            selectedTrip.setBusType(busType);
            selectedTrip.setDepartureHour(departureHour);
            selectedTrip.setDepartureMinute(departureMinute);
            selectedTrip.setDepartureTime(departureTime);
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
}