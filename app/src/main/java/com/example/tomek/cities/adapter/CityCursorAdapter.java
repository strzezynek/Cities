package com.example.tomek.cities.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.tomek.cities.R;
import com.example.tomek.cities.model.City;

/**
 * Created by Tomek on 2015-11-03.
 */
public class CityCursorAdapter extends CursorAdapter {

    public CityCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_city, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int i = cursor.getPosition();
        if (i%2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
        }

        String cityName = cursor.getString(cursor.getColumnIndexOrThrow(City.CityColumns.NAME));
        if (cityName != null) {
            TextView cityNameText = (TextView) view.findViewById(R.id.city_name_item);
            cityNameText.setText(cityName);
        }
    }
}
