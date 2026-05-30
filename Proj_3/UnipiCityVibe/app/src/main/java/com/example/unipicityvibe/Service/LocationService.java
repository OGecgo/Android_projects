package com.example.unipicityvibe.Service;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.example.unipicityvibe.Enums.LocationTypeEnum;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Utils.PermissionHelper;
import com.google.android.gms.location.LocationRequest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationService implements ILocationService {

    private static final int INTERVAL_HIGH_SEC = 5;
    private static final int INTERVAL_BALANCE_SEC = 30;
    // configure for fine
    private static final LocationRequest locationRequestHigh = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
            .setIntervalMillis(1000 * INTERVAL_HIGH_SEC)
                .build();
    // configure for coarse
    private static final LocationRequest locationRequestBalance = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setIntervalMillis(1000 * INTERVAL_BALANCE_SEC)
                .build();
    // singleton
    private static LocationService service;

    private FusedLocationProviderClient fusedLocationClient;
    private Location mCurrentLocation;


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private LocationService(Context context){
        service.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        // take last location
        service.fusedLocationClient.getLastLocation().addOnSuccessListener(service::onListenerLocation);
    }

    // ----- Listeners -----
    private void onListenerLocation(Location location){
        mCurrentLocation = location;
    }
    // ----- End Listeners -----


    public static LocationService getInstance(Context context){
        // test permissions
        if (!PermissionHelper.isGrantedLocationPermission(context)){
            Log.w(TAG, "[LocationService] Location permission denied");
            return null;
        }
        // give context.getApplicationContext() to pretend memory leaks
        if (!context.equals(context.getApplicationContext())){
            Log.e(TAG, "[LocationService] context should be application context for pretend memory leaks. Use getApplicationContext");
            return null;
        }

        if (service == null) service = new LocationService(context);
        return service;
    }

    @Override
    public void stopLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(this::onListenerLocation);

    }

    @Override
    public void startLocationUpdate(@NonNull LocationTypeEnum e, Context context){
        switch (e){
            case FINE:
                if (!PermissionHelper.isGrantedFine(context)){
                    Log.w(TAG, "[LocationService] Location permission fine denied");
                    break;
                }
                fusedLocationClient.requestLocationUpdates(locationRequestHigh, this::onListenerLocation, Looper.getMainLooper());
                break;
            case COARSE:
                if (!PermissionHelper.isGrantedCoarse(context)){
                    Log.w(TAG, "[LocationService] Location permission coarse denied");
                    break;
                }
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
