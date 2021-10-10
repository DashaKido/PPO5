package com.example.lab2;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Trip> objects;

    BoxAdapter(Context context, ArrayList<Trip> trips) {
        ctx = context;
        objects = trips;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Trip t = getTrip(position);

        // заполняем View в пункте списка данными из рейсов
        ((TextView) view.findViewById(R.id.Number)).setText(t.number);
        ((TextView) view.findViewById(R.id.BusType)).setText("Тип: " + t.busType);
        ((TextView) view.findViewById(R.id.Destination)).setText("Пункт назначения: " + t.destination);
        ((TextView) view.findViewById(R.id.DepartureTime)).setText("Время прибытия: " + t.departureTime);
        ((TextView) view.findViewById(R.id.ArrivalTime)).setText("Время отправления: " + t.arrivalTime);

        return view;
    }

    // товар по позиции
    Trip getTrip(int position) {
        return ((Trip) getItem(position));
    }


}