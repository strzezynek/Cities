package com.example.tomek.cities;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomek on 2015-11-02.
 */
public class CitiesApp extends Application {

    private static final String TAG = CitiesApp.class.getSimpleName();

    private static CitiesApp mInstance;

    private RequestQueue mRequestQueue;

    private List<String> citiesNames;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        citiesNames = new ArrayList<String>();
    }

    public static synchronized CitiesApp getInstance() {
        return mInstance;
    }

    public List<String> getCitiesNames() {
        return citiesNames;
    }

    public void addCityName(String cityName) {
        Log.d(TAG, "Adding city");
        citiesNames.add(cityName);
    }

    public void clearCitiesNames() {
        citiesNames.clear();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
