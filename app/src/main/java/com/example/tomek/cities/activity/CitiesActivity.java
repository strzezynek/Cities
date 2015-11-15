package com.example.tomek.cities.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tomek.cities.CitiesApp;
import com.example.tomek.cities.R;
import com.example.tomek.cities.model.City;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Tomek on 2015-10-31.
 */
public class CitiesActivity extends SherlockFragmentActivity
        implements CitiesFragment.OnItemSelectedListener, CitiesFragment.OnItemLongClickListener {

    private static final String TAG = CitiesActivity.class.getSimpleName();

    private static final String ARG_SHOWING_LIST = "showing_list";

    private static final String urlBegin = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    private static final String urlEnd = "&sensor=true&language=pl";

    private CitiesApp app;

    private FragmentManager fm;
    private Fragment fragment;

    private boolean showingList = true;

    private ActionMode mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        app = CitiesApp.getInstance();

        ActionBar actionBar = getSupportActionBar();

        fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            showingList = savedInstanceState.getBoolean(ARG_SHOWING_LIST, true);
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "content");
        } else {
            showSuitableFragment();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_SHOWING_LIST, showingList);
        getSupportFragmentManager().putFragment(outState, "content", fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
        if (mode != null) {
            mode.finish();
        }
    }

    @Override
    public void onItemLongClick(int position) {
        mode = startActionMode(new MyActionMode());
    }

    private void makeApiRequest(String cityName) {
        try {
            cityName = URLEncoder.encode(cityName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = urlBegin + cityName + urlEnd;
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        String[] cityDetails = parseResponseToCityDetails(response);
                        parseResponseToCityDetails(response);
//                        if (cityDetails != null) {
                            showCityFragment();
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        app.addToRequestQueue(jsonRequest);
    }

    private void parseResponseToCityDetails(String json) {
        String[] cityDetails = null;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(json);
        String success = jsonObject.get("status").getAsString();
        if (success.equals("OK")) {
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");
            Gson gson = new Gson();
            City[] cities = gson.fromJson(resultsArray, City[].class);
            app.citiesFound = cities;
        } else {
            Toast.makeText(this, getString(R.string.toast_wrong_city), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuitableFragment() {
        if (showingList) {
            showCitiesFragment();
        } else {
            showCityFragment();
        }
    }

    private void showCitiesFragment() {
        showingList = true;
        fragment = CitiesFragment.newInstance();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void showCityFragment() {
        showingList = false;
        fragment = ResultFragment.newInstance();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void showCityFragment(String[] cityDetails) {
        showingList = false;
        fragment = ResultFragment.newInstance(cityDetails[0], cityDetails[1], cityDetails[2],
                cityDetails[3], cityDetails[4]);
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_city_fragment));
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && ((CitiesFragment)fragment).cityEdit.hasFocus()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private final class MyActionMode implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            menu.add(getString(R.string.action_delete))
                    .setIcon(R.mipmap.ic_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            String itemName = item.getTitle().toString();

            if (itemName.equals(getString(R.string.action_delete))){
                ((CitiesFragment)fragment).removeCity();
            }

            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }

}
