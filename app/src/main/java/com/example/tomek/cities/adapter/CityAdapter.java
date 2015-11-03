package com.example.tomek.cities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tomek.cities.R;
import com.example.tomek.cities.model.City;

import java.util.List;

/**
 * Created by Tomek on 2015-11-02.
 */
public class CityAdapter extends ArrayAdapter<String> {

    List<String> cities;

    public CityAdapter(Context context, int resourceId) {
        super(context, resourceId);
    }

    public CityAdapter(Context context, int resourceId, List<String> items) {
        super(context, resourceId, items);
        cities = items;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public String getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_city, null);
        }

        if (i%2 == 0) {
            view.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.light_gray));
        }

        String cityName = getItem(i);
        if (cityName != null) {
            TextView cityNameText = (TextView) view.findViewById(R.id.city_name_item);
            cityNameText.setText(cityName);
        }

        return view;
    }
}
