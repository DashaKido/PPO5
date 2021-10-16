package com.example.lab2;

public class Trip {
    private String number;
    private String busType;
    private String destination;
    private int departureHour;
    private int departureMinute;
    private String departureTime;
    private int arrivalHour;
    private int arrivalMinute;
    private String arrivalTime;

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

    public int getArrivalHour() {
        return arrivalHour;
    }

    public int getArrivalMinute() {
        return arrivalMinute;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureHour(int departureHour) {
        this.departureHour = departureHour;
    }

    public void setDepartureMinute(int departureMinute) {
        this.departureMinute = departureMinute;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalHour(int arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public void setArrivalMinute(int arrivalMinute) {
        this.arrivalMinute = arrivalMinute;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
