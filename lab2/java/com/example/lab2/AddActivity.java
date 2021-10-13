package com.example.lab2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class AddActivity extends AppCompatActivity {

    private final String[] busTypeArray = {"BASIC", "HIGH_SPEED", "OFFICIAL"};
    Spinner spinner;
    TimePicker timePicker;
    ArrayAdapter<String> adapterSpinner;
    String busType = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        timePicker = (TimePicker) findViewById(R.id.timePicker2);
        timePicker.setIs24HourView(true);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, busTypeArray);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addTripButton(View v) {
        EditText numberEdit = (EditText) findViewById(R.id.numberTrip);
        EditText destinationEdit = (EditText) findViewById(R.id.destinationTrip);

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

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                busType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        Trip tripForAdd;
        String numberAdd;
        if (number.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Не все данные введены", Toast.LENGTH_LONG).show();
        } else {
            boolean add = true;
            for (int i = 0; i < MainActivity.sizeOfArray; i++) {
                tripForAdd = MainActivity.trips.get(i);
                numberAdd = tripForAdd.number;
                if (numberAdd.equals(number)) {
                    Toast.makeText(this, "Рейс с таким номером уже существует", Toast.LENGTH_LONG).show();
                    add = false;
                }
            }
            if (add) {
                Toast.makeText(this, "Добавлен новый рейс", Toast.LENGTH_LONG).show();
                MainActivity.trips.add(new Trip(number, busType, destination,
                        arrivalHour, arrivalMinute, arrivalTime,
                        departureHour, departureMinute, departureTime));
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }
    }
}