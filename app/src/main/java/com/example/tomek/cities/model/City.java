package com.example.tomek.cities.model;

import android.provider.BaseColumns;

/**
 * Created by Tomek on 2015-11-01.
 */
public class City {

    public static final String KEY_NAME = "locality";
    public static final String KEY_COMMUNE = "administrative_area_level_3";
    public static final String KEY_COUNTY = "administrative_area_level_2";
    public static final String KEY_VOIVODESHIP = "administrative_area_level_1";
    public static final String KEY_COUNTRY = "country";

    private String mName;
    private String mCommune;
    private String mCounty;
    private String mVoivodeship;
    private String mCountry;

    public AddressComponent[] address_components;

    public City(String name, String commune, String county, String voivodeship, String country) {
        mName = name;
        mCommune = commune;
        mCounty = county;
        mVoivodeship = voivodeship;
        mCountry = country;
    }

    public String getName() {
        return mName;
    }

    public String getCommune() {
        return mCommune;
    }

    public String getCounty() {
        return mCounty;
    }

    public String getVoivodeship() {
        return mVoivodeship;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setCommune(String commune) {
        mCommune = commune;
    }

    public void setCounty(String county) {
        mCounty = county;
    }

    public void setVoivodeship(String voivodeship) {
        mVoivodeship = voivodeship;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public class CityColumns implements BaseColumns {
        public static final String NAME = "name";
    }

}
