package com.project.a_star_fitness.record;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.project.a_star_fitness.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    private LocationRequest locationRequest;
    final WeatherDataService weatherDataService = new WeatherDataService(AddActivity.this);
    private Spinner spinnerSelection;
    private Spinner spinnerDuration;
    private EditText editTextReocrd;
    private Button buttonSubmit;
    private Button buttonLocation;
    private Button buttonWeather;
    private ProgressBar progressBar;
    private TextView textLatLong;
    private String cityName;
    ListView lv_weatherReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        buttonLocation = findViewById(R.id.button_location);
        editTextReocrd = findViewById(R.id.editTextRecord);
        progressBar = findViewById(R.id.progress_bar);
        spinnerSelection = findViewById(R.id.spinner_selection);
        spinnerDuration = findViewById(R.id.spinner_duration);
        buttonSubmit = findViewById(R.id.button_submit);
        textLatLong = findViewById(R.id.textLatLong);
        buttonWeather = findViewById(R.id.button_weather);
        lv_weatherReport = findViewById(R.id.lv_weatherReports);

        String[] types = new String[]{"cardio", "strength"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        //set the spinners adapter to the previously created one.
        spinnerSelection.setAdapter(adapter);


        String[] times = new String[]{"0.5", "1","1.5","2"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times);
        //set the spinners adapter to the previously created one.
        spinnerDuration.setAdapter(adapter2);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = editTextReocrd.getText().toString();
                String type = spinnerSelection.getSelectedItem().toString();
                String duration = spinnerDuration.getSelectedItem().toString();
                Log.d("des",description);
                Log.d("typ",type);
                Log.d("dur",duration);
                Record record = new Record(type,duration,description);

                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("Records")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                        .setValue(record).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddActivity.this, "Record has been created successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            // if write to firebase fails
                        } else {
                            Toast.makeText(AddActivity.this, "Failed to create record! Try again!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            AddActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    getCurrentLocation();
                }
            }
        });

        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cityName = "Boston";
                weatherDataService.getCityForecastByName("Boston", new WeatherDataService.GetCityForecastByNameCallBack() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(AddActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }



                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        // put the entire list into the listview model

                        ArrayAdapter arrayAdapter = new ArrayAdapter(AddActivity.this, android.R.layout.simple_list_item_1,weatherReportModels);
                        lv_weatherReport.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            } else {
                Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        progressBar.setVisibility(View.VISIBLE);
        locationRequest = new LocationRequest();

        LocationServices.getFusedLocationProviderClient(AddActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(AddActivity.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size()>0){
                            int latestLocationIndex = locationResult.getLocations().size()-1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            Locale locale = new Locale("en"); //OR Locale.getDefault()
                            String updatedCity;
                            String updatedCountry;
                            Geocoder geocoder = new Geocoder(AddActivity.this, locale);
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                updatedCity = addresses.get(0).getLocality();

                                if (updatedCity == null) {
                                    updatedCity = addresses.get(0).getAdminArea();
                                }

                                updatedCountry = addresses.get(0).getCountryName();
                                textLatLong.setText(
                                        String.format(
                                                "city: %s\ncountry: %s",
                                                updatedCity,
                                                updatedCountry
                                        )
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, Looper.getMainLooper());
    }

}