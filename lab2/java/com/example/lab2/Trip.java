package com.example.lab2;

public class Trip {
    String number;
    String busType;
    String destination;
    int departureHour;
    int departureMinute;
    String departureTime;
    int arrivalHour;
    int arrivalMinute;
    String arrivalTime;

    Trip(String _number, String _busType, String _destination,
         int _arrivalHour, int _arrivalMinute, String _arrivalTime,
         int _departureHour, int _departureMinute, String _departureTime) {
        number = _number;
        busType = _busType;
        destination = _destination;
        arrivalHour = _arrivalHour;
        arrivalMinute = _arrivalMinute;
        departureHour = _departureHour;
        departureMinute = _departureMinute;
        departureTime = _departureTime;
        arrivalTime = _arrivalTime;
    }

    public String getNumber() {
        return number;
    }

    public String getBusType() {
        return busType;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
}
