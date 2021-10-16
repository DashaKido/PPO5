package com.example.lab2;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private ArrayList<Trip> trips;
    private Context context;

    TripAdapter(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView number, busType, destination, arrivalTime, departureTime;

        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.Number);
            busType = view.findViewById(R.id.BusType);
            destination = view.findViewById(R.id.Destination);
            departureTime = view.findViewById(R.id.DepartureTime);
            arrivalTime = view.findViewById(R.id.ArrivalTime);
            view.setOnLongClickListener(v -> {
                showPopupMenu(v);
                return true;
            });
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            MainActivity.selectedTrip = trips.get(this.getAdapterPosition());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menuEdit) {
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                    return true;
                } else if (itemId == R.id.menuDelete) {
                    new AlertDialog.Builder(context)
                            .setTitle("Удаление")
                            .setMessage("Вы уверены, что хотите удалить рейс с номером " + MainActivity.selectedTrip.getNumber())
                            .setPositiveButton("Удалить", (dialogInterface, i) -> {
                                int position = getAdapterPosition();
                                MainActivity.trips.remove(position);
                                notifyItemRemoved(position);
                                MainActivity.sizeOfArray--;
                                Toast.makeText(context, "Удаление прошло успешно", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Отмена", null)
                            .show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}

