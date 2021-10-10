package com.example.lab2;

public class Trip {
    String number;
    String busType;
    String destination;
    int departureHour;//отправление
    int departureMinute;
    String departureTime;
    int arrivalHour;//прибытие
    int arrivalMinute;
    String arrivalTime;

    Trip(String _describe, String _busType, String _destination,
         int _arrivalHour, int _arrivalMinute, String _arrivalTime, int _departureHour, int _departureMinute, String _departureTime) {
        number = _describe;
        busType = _busType;
        destination = _destination;
        arrivalHour = _arrivalHour;
        arrivalMinute = _arrivalMinute;
        departureHour = _departureHour;
        departureMinute = _departureMinute;
        departureTime = _departureTime;
        arrivalTime = _arrivalTime;
    }

}
