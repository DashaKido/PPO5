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

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private final String[] busTypeArray = {"BASIC", "HIGH_SPEED", "OFFICIAL"};
    Spinner spinner;
    TimePicker timePicker;
    ArrayAdapter<String> adapterSpinner;
    String busType = "";

    ArrayList<Trip> trips;

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
        trips = new ArrayList<Trip>();
        trips = (ArrayList<Trip>) getIntent().getSerializableExtra("ARRAYLIST");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addTripButton(View v) {
        EditText numberEdit = (EditText) findViewById(R.id.numberTrip);
        EditText destinationEdit = (EditText) findViewById(R.id.destinationTrip);

        String number = numberEdit.getText().toString();
        String destination = destinationEdit.getText().toString();
        int arrivalHour = timePicker.getHour();
        int arrivalMinute = timePicker.getMinute();
        String arrivalTime = arrivalHour + ":" + arrivalMinute;
        int departureHour = arrivalHour;
        int departureMinute = arrivalMinute + 5;
        String departureTime = departureHour + ":" + departureMinute;

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
        if (number.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Не все данные введены", Toast.LENGTH_LONG).show();
        } else if (!number.isEmpty() && !destination.isEmpty()) {
            trips.add(new Trip(number, busType, destination,
                    arrivalHour, arrivalMinute, arrivalTime,
                    departureHour, departureMinute, departureTime));
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            intent.putExtra("ARRAYLIST_with_add", trips);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

    }
}//off 02.14 02.19
//113 off  03.03