package com.example.tomek.cities.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.tomek.cities.CitiesApp;
import com.example.tomek.cities.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Tomek on 2015-10-31.
 */
public class CitiesActivity extends SherlockFragmentActivity
        implements CitiesFragment.OnItemSelectedListener {

    private static final String TAG = CitiesActivity.class.getSimpleName();

    private static final String urlBegin = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    private static final String urlEnd = "&sensor=true&language=pl";

    private CitiesApp app;

    private FragmentManager fm;
    private Fragment fragment;

    private boolean showingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        app = CitiesApp.getInstance();

        ActionBar actionBar = getSupportActionBar();

        fm = getSupportFragmentManager();
        showCitiesFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            showCitiesFragment();
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (showingList){
            super.onBackPressed();
        } else {
            showCitiesFragment();
        }
    }

    @Override
    public void onItemSelected(String cityName) {
        makeApiRequest(cityName);
    }

    private void makeApiRequest(String cityName) {
        try {
            cityName = URLEncoder.encode(cityName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = urlBegin + cityName + urlEnd;
        Log.d(TAG, "Url: " + url);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] cityDetails = parseResponseToCityDetails(response);
                        if (cityDetails != null) {
                            showCityFragment(cityDetails);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        app.addToRequestQueue(jsonRequest);
    }

    private String[] parseResponseToCityDetails(String json) {
        String[] cityDetails = null;
        Log.d(TAG, json);
//        JsonElement jsonElement = new JsonParser().parse(json);
//        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(json);
        String success = jsonObject.get("status").getAsString();
        if (success.equals("OK")) {
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");
            JsonObject result = resultsArray.get(0).getAsJsonObject();
            JsonArray addressComponents = result.getAsJsonArray("address_components");
            cityDetails = new String[5];
            for (int i = 0; i < cityDetails.length; i++) {
                JsonObject detail = addressComponents.get(i).getAsJsonObject();
                cityDetails[i] = detail.get("long_name").getAsString();
                Log.d(TAG, cityDetails[i]);
            }
            return cityDetails;
        } else {
            Toast.makeText(this, getString(R.string.toast_wrong_city), Toast.LENGTH_SHORT).show();
            return cityDetails;
        }
    }

    private void showCitiesFragment() {
        showingList = true;
        fragment = CitiesFragment.newInstance();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.title_cities_fragment));
    }

    private void showCityFragment(String[] cityDetails) {
        showingList = false;
        fragment = CityFragment.newInstance(cityDetails[0], cityDetails[1], cityDetails[2],
                cityDetails[3], cityDetails[4]);
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_city_fragment));
    }

}
