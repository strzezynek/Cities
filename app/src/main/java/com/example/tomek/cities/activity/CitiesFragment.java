package com.example.tomek.cities.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.tomek.cities.CitiesApp;
import com.example.tomek.cities.R;
import com.example.tomek.cities.adapter.CityCursorAdapter;
import com.example.tomek.cities.model.City;
import com.example.tomek.cities.provider.CityProvider;

/**
 * Created by Tomek on 2015-10-31.
 */
public class CitiesFragment extends SherlockFragment {

    private static final String TAG = CitiesFragment.class.getSimpleName();

    private static final String ARG_CITY_EDIT_FOCUS = "city_edit_focus";

    private CitiesApp app;

    public EditText cityEdit;
    private ImageButton addCityBtn;
    private ListView citiesList;
    private CityCursorAdapter cityAdapter;
    private Cursor cityCursor;

    private OnItemSelectedListener selectedListener;
    private OnItemLongClickListener longClickListener;

    public CitiesFragment() {

    }

    public static CitiesFragment newInstance() {
        CitiesFragment fragment = new CitiesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cities, container, false);

        ((CitiesActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((CitiesActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.title_cities_fragment));

        cityEdit = (EditText) rootView.findViewById(R.id.city_name_edit);
        addCityBtn = (ImageButton) rootView.findViewById(R.id.add_city_btn);
        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCityEditFilled()) {
                    String cityName = cityEdit.getText().toString().trim();
                    Log.d(TAG, cityName);
                    if (!isCityOnList(cityName)) {
                        addCity(cityName);
                        showCityList();
                        cityEdit.clearFocus();
                        ((CitiesActivity)getActivity()).hideKeyboard();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.toast_already_on_list),
                                Toast.LENGTH_SHORT).show();
                    }
                    cityEdit.setText("");
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_enter_city),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        showCityList();
        citiesList = (ListView) rootView.findViewById(R.id.cities_list);
        cityAdapter = new CityCursorAdapter(getActivity(), cityCursor);
        citiesList.setAdapter(cityAdapter);
        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ((CitiesActivity)getActivity()).hideKeyboard();
                cityCursor.moveToPosition(pos);
                String cityName = cityCursor.getString(
                        cityCursor.getColumnIndexOrThrow(City.CityColumns.NAME));
                selectedListener.onItemSelected(cityName);

            }
        });
        citiesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                citiesList.setItemChecked(pos, true);
                view.setSelected(true);
                longClickListener.onItemLongClick(pos);
                return true;
            }
        });
        citiesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            selectedListener = (OnItemSelectedListener) activity;
            longClickListener = (OnItemLongClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener and OnItemLongClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        selectedListener = null;
        longClickListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_CITY_EDIT_FOCUS, cityEdit.hasFocus());
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            boolean hasFocus = savedInstanceState.getBoolean(ARG_CITY_EDIT_FOCUS, true);
            if (hasFocus) {
                cityEdit.requestFocus();
            } else {
                cityEdit.clearFocus();
                ((CitiesActivity)getActivity()).hideKeyboard();
            }
        }
    }

    private void addCity(String cityName) {
        getActivity().getContentResolver().insert(CityProvider.CONTENT_URI, getContentValues(cityName));
    }

    private ContentValues getContentValues(String cityName) {
        ContentValues values = new ContentValues();
        values.put(City.CityColumns.NAME, cityName);
        return values;
    }

    public void removeCity() {
        int position = citiesList.getCheckedItemPosition();
        citiesList.setItemChecked(position, false);
        citiesList.setSelected(false);

        cityCursor.moveToPosition(position);
        int cityId = cityCursor.getInt(cityCursor.getColumnIndexOrThrow(City.CityColumns._ID));

        String selection = City.CityColumns._ID + " = ?";
        String[] selectionArgs = {String.valueOf(cityId)};
        getActivity().getContentResolver().delete(CityProvider.CONTENT_URI, selection,
                selectionArgs);

        showCityList();
    }

    public boolean isCityOnList(String cityName) {
        String[] projection = {City.CityColumns._ID};
        String selection = City.CityColumns.NAME + " = ?";
        String[] selectionArgs = {cityName};
        cityCursor = getActivity().getContentResolver().query(CityProvider.CONTENT_URI, projection,
                selection, selectionArgs, null);
        if (cityCursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCityEditFilled() {
        return !cityEdit.getText().toString().equals("") && cityEdit.getText().toString().trim().length() > 0;
    }

    public void showCityList(){
        String[] projection = {};
        cityCursor = getActivity().getContentResolver().query(CityProvider.CONTENT_URI, projection,
                null, null, null);
        if (cityAdapter != null) {
            cityAdapter.swapCursor(cityCursor);
            cityAdapter.notifyDataSetChanged();
        }
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(String cityName);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(int position);
    }

}
