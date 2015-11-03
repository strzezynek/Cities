package com.example.tomek.cities.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.tomek.cities.R;

/**
 * Created by Tomek on 2015-10-31.
 */
public class CityFragment extends SherlockFragment {

    private static final String TAG = CityFragment.class.getSimpleName();

    public static final String ARG_CITY = "city_name";
    public static final String ARG_COMMUNE = "commune_name";
    public static final String ARG_COUNTY = "county_name";
    public static final String ARG_VOIVODESHIP = "voivodeship_name";
    public static final String ARG_COUNTRY = "country_name";

    private TextView cityText;
    private TextView communeText;
    private TextView countyText;
    private TextView voivodeshipText;
    private TextView countryText;

    public CityFragment() {

    }

    public static CityFragment newInstance(String city, String commune, String county, String voivodeship,
                                    String country) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY, city);
        args.putString(ARG_COMMUNE, commune);
        args.putString(ARG_COUNTY, county);
        args.putString(ARG_VOIVODESHIP, voivodeship);
        args.putString(ARG_COUNTRY, country);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city, container, false);

        cityText = (TextView) rootView.findViewById(R.id.city_name_text);
        communeText = (TextView) rootView.findViewById(R.id.commune_name_text);
        countyText = (TextView) rootView.findViewById(R.id.county_name_text);
        voivodeshipText = (TextView) rootView.findViewById(R.id.voivodeship_name_text);
        countryText = (TextView) rootView.findViewById(R.id.country_name_text);

        Bundle args = getArguments();
        cityText.setText(args.getString(ARG_CITY));
        communeText.setText(args.getString(ARG_COMMUNE));
        countyText.setText(args.getString(ARG_COUNTY));
        voivodeshipText.setText(args.getString(ARG_VOIVODESHIP));
        countryText.setText(args.getString(ARG_COUNTRY));

        return rootView;
    }
}
