package com.example.tomek.cities.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Tomek on 2015-11-03.
 */
public class CityProvider extends ContentProvider{

    public static final String AUTHORITY = "com.example.tomek.cities.provider";
    public static final String PROVIDER_NAME = AUTHORITY + ".CityProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/city";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final int ALL_CITIES = 10;
    public static final int SINGLE_CITY = 20;
    public static final String CITY_TABLE = "city";
    public static final String[] ALL_COLUMNS = new String[]{};
    public static final String DATABASE_NAME = "CityProvider";
    public static final int DATABASE_VERSION = 1;
    public static final String TAG = "CityProvider";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
