package com.example.tomek.cities.activity;

import android.app.Activity;
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
import com.example.tomek.cities.adapter.CityAdapter;

/**
 * Created by Tomek on 2015-10-31.
 */
public class CitiesFragment extends SherlockFragment {

    private static final String TAG = CitiesFragment.class.getSimpleName();

    private CitiesApp app;

    private EditText cityEdit;
    private ImageButton addCityBtn;
    private ListView citiesList;
    private CityAdapter cityAdapter;

    private OnItemSelectedListener listener;

    public CitiesFragment() {

    }

    public static CitiesFragment newInstance() {
        CitiesFragment fragment = new CitiesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = CitiesApp.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cities, container, false);

        cityEdit = (EditText) rootView.findViewById(R.id.city_name_edit);
        addCityBtn = (ImageButton) rootView.findViewById(R.id.add_city_btn);
        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCityEditFilled()) {
                    addCity(cityEdit.getText().toString());
                    cityEdit.setText("");
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_enter_city),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        citiesList = (ListView) rootView.findViewById(R.id.cities_list);
        app = (CitiesApp) getActivity().getApplication();
        if (getActivity() == null) {
            Log.d(TAG, "activity null");
        }
        if (app.getCitiesNames() == null) {
            Log.d(TAG, "cities names null");
        }
        cityAdapter = new CityAdapter(getActivity(), R.layout.item_city, app.getCitiesNames());
        citiesList.setAdapter(cityAdapter);
        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cityName = (String) adapterView.getItemAtPosition(i);
                listener.onItemSelected(cityName);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void addCity(String cityName) {
        Log.d(TAG, "Adding city");
        app.addCityName(cityName);
        cityAdapter.notifyDataSetChanged();
    }

    private boolean isCityEditFilled() {
        return !cityEdit.getText().toString().equals("");
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(String cityName);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(int position);
    }

}
