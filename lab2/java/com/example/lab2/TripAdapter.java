package com.example.lab2;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    interface OnTripClickListener {
        void onTripClick(Trip trip, int position);
    }

    private final OnTripClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Trip> trips;

    TripAdapter(Context context, List<Trip> trips, OnTripClickListener onClickListener) {
        this.trips = trips;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TripAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Trip trip = trips.get(position);
        holder.number.setText(trip.getNumber());
        holder.busType.setText("Тип: " + trip.getBusType());
        holder.destination.setText("Пункт назначения: " + trip.getDestination());
        holder.departureTime.setText("Время прибытия: " + trip.getDepartureTime());
        holder.arrivalTime.setText("Время отправления: " + trip.getArrivalTime());
        holder.itemView.setOnClickListener(v -> onClickListener.onTripClick(trip, position));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView number, busType, destination, arrivalTime, departureTime;

        ViewHolder(View view) {
            super(view);
            number = (TextView) view.findViewById(R.id.Number);
            busType = (TextView) view.findViewById(R.id.BusType);
            destination = (TextView) view.findViewById(R.id.Destination);
            departureTime = (TextView) view.findViewById(R.id.DepartureTime);
            arrivalTime = (TextView) view.findViewById(R.id.ArrivalTime);
        }
    }
}