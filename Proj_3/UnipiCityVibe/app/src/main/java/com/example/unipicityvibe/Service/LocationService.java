package com.example.unipicityvibe.Service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.example.unipicityvibe.Enums.LocationTypeEnum;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.google.android.gms.location.LocationRequest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationService implements ILocationService {

    private static final int INTERVAL_HIGH_SEC = 5;
    private static final int INTERVAL_BALANCE_SEC = 30;

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    // use GPS
    private final LocationRequest locationRequestHigh;
    // use Wi-Fi and Cell towers
    private final LocationRequest locationRequestBalance;
    private Location mCurrentLocation;

    // ----- Listeners -----
    private void onListenerLocation(Location location){
        mCurrentLocation = location;
    }
    // ----- End Listeners -----



    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public LocationService(Context context){
        this.context = context;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // location settings
        locationRequestHigh = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
                .setIntervalMillis(1000 * INTERVAL_HIGH_SEC)
                .build();
        locationRequestBalance = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setIntervalMillis(1000 * INTERVAL_BALANCE_SEC)
                .build();

        // take last location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this::onListenerLocation);
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void stopLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(this::onListenerLocation);

    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void startLocationUpdate(@NonNull LocationTypeEnum e){
        if (context == null || fusedLocationClient == null) return;
        switch (e){
            case FINE:
                fusedLocationClient.requestLocationUpdates(locationRequestHigh, this::onListenerLocation, Looper.getMainLooper());
                break;
            case COARSE:
                fusedLocationClient.requestLocationUpdates(locationRequestBalance, this::onListenerLocation, Looper.getMainLooper());
                break;
        }
    }



    // take last position data
    @Override
    public double getLatitude(){
        return mCurrentLocation.getLatitude();
    }
    @Override
    public double getLongitude(){
        return mCurrentLocation.getLongitude();
    }
}
