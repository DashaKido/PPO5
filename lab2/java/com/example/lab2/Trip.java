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

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDepartureHour() {
        return departureHour;
    }

    public void setDepartureHour(int departureHour) {
        this.departureHour = departureHour;
    }

    public int getDepartureMinute() {
        return departureMinute;
    }

    public void setDepartureMinute(int departureMinute) {
        this.departureMinute = departureMinute;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(int arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public int getArrivalMinute() {
        return arrivalMinute;
    }

    public void setArrivalMinute(int arrivalMinute) {
        this.arrivalMinute = arrivalMinute;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
