package com.example.tomek.cities.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.tomek.cities.CitiesApp;
import com.example.tomek.cities.R;
import com.example.tomek.cities.model.AddressComponent;
import com.example.tomek.cities.model.City;

/**
 * Created by Tomek on 2015-10-31.
 */
public class ResultFragment extends SherlockFragment {

    private static final String TAG = com.example.tomek.cities.activity.ResultFragment.class.getSimpleName();

    public static final String ARG_FRAGMENT_NUM = "fragment_number";
    public static final String ARG_CITY = "city_name";
    public static final String ARG_COMMUNE = "commune_name";
    public static final String ARG_COUNTY = "county_name";
    public static final String ARG_VOIVODESHIP = "voivodeship_name";
    public static final String ARG_COUNTRY = "country_name";

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private static CitiesApp app;

    public ResultFragment() {

    }

    public static ResultFragment newInstance() {
        ResultFragment fragment = new com.example.tomek.cities.activity.ResultFragment();
        app = CitiesApp.getInstance();
        return fragment;
    }

    public static ResultFragment newInstance(String city, String commune, String county, String voivodeship,
                                    String country) {
        ResultFragment fragment = new com.example.tomek.cities.activity.ResultFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_FRAGMENT_NUM, viewPager.getCurrentItem());
        Log.d(TAG, outState == null ? "out state null" : "out state not null");

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, savedInstanceState == null ? "out state null" : "out state not null");
        if (savedInstanceState != null) {
            Log.d(TAG, "Frag num: " + savedInstanceState.getInt(ARG_FRAGMENT_NUM));
            viewPager.setCurrentItem(savedInstanceState.getInt(ARG_FRAGMENT_NUM, 0));
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new CityFragment();
            Bundle args = new Bundle();
            args.putInt(CityFragment.ARG_CITY_NUM, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return CitiesApp.getInstance().citiesFound.length;
        }
    }

    public static class CityFragment extends Fragment {
        public static final String TAG = CityFragment.class.getSimpleName();
        public static final String ARG_CITY_NUM = "city_num";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(
                    R.layout.fragment_city, container, false);
            Bundle args = getArguments();
            int cityNum = args.getInt(ARG_CITY_NUM);
            City city = CitiesApp.getInstance().citiesFound[cityNum];
            return fillCityData(rootView, city);
        }
    }

    private static View fillCityData(View rootView, City city) {
        for (int i = 0; i < city.address_components.length; i++){
            AddressComponent ac = city.address_components[i];
            String type = ac.types[0];
            if (type.equals("locality")) {
                ((TextView) rootView.findViewById(R.id.city_name_text)).setText(ac.long_name);
            } else if (type.equals("administrative_area_level_3")) {
                ((TextView) rootView.findViewById(R.id.commune_name_text)).setText(ac.long_name);
            } else if (type.equals("administrative_area_level_2")) {
                ((TextView) rootView.findViewById(R.id.county_name_text)).setText(ac.long_name);
            } else if (type.equals("administrative_area_level_1")) {
                ((TextView) rootView.findViewById(R.id.voivodeship_name_text)).setText(ac.long_name);
            } else if (type.equals("country")) {
                ((TextView) rootView.findViewById(R.id.country_name_text)).setText(ac.long_name);
            }
        }
        return rootView;
    }

}
