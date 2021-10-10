package com.example.lab2;

public class Trip {
    String number;
    String busType;
    String destination;
    String  departureTime;//отправление
    int arrivalTime;//прибытие
    boolean box;

    Trip(String _describe, String _busType, String _destination,
         int _arrivalTime,String _departureTime, boolean _box) {
        number = _describe;
        busType = _busType;
        destination =_destination;
        arrivalTime=_arrivalTime;
        departureTime=_departureTime;
        box=_box;
    }

}
