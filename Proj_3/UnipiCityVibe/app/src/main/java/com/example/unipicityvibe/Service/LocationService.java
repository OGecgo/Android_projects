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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationService implements ILocationService {

    private static final int INTERVAL_HIGH_SEC = 2;
    private static final int INTERVAL_BALANCE_SEC = 6;
    // configure for fine
    private static final LocationRequest locationRequestHigh = new LocationRequest.Builder(1000 * INTERVAL_HIGH_SEC)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build();
    // configure for coarse
    private static final LocationRequest locationRequestBalance = new LocationRequest.Builder(1000 * INTERVAL_BALANCE_SEC)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build();
    // singleton
    private static LocationService service;

    private final FusedLocationProviderClient fusedLocationClient;
    private Location mCurrentLocation;

    // ----- Call Back -----
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                mCurrentLocation = location;
            }
            Log.d(TAG, "[LocationService] Position Updated");
        }
    };
    // ----- End Call Back -----

    // constractor
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private LocationService(Context context){
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        // take last location
        this.fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) mCurrentLocation = location;
        });
    }


    @Nullable
    public static LocationService getInstance(Context context){
        // test permissions
        if (!PermissionHelper.isGrantedLocationPermission(context)){
            Log.w(TAG, "[LocationService] Location permission denied");
            return null;
        }
        // give context.getApplicationContext() to prevent memory leaks
        if (!context.equals(context.getApplicationContext())){
            Log.e(TAG, "[LocationService] context should be application context to prevent memory leaks. Use getApplicationContext");
            return null;
        }

        if (service == null) service = new LocationService(context);
        return service;
    }

    @Override
    public void stopLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
        Log.d(TAG, "[LocationService] Stop location tracking");
    }

    @Override
    public void startLocationUpdate(@NonNull LocationTypeEnum e, Context context){
        switch (e){
            case FINE:
                if (!PermissionHelper.isGrantedFine(context)){
                    Log.w(TAG, "[LocationService] Location permission fine denied");
                    break;
                }
                fusedLocationClient.requestLocationUpdates(locationRequestHigh, locationCallback, Looper.getMainLooper());
                Log.d(TAG, "[LocationService] start location tracking fine");
                break;
            case COARSE:
                if (!PermissionHelper.isGrantedCoarse(context)){
                    Log.w(TAG, "[LocationService] Location permission coarse denied");
                    break;
                }
                fusedLocationClient.requestLocationUpdates(locationRequestBalance, locationCallback, Looper.getMainLooper());
                Log.d(TAG, "[LocationService] start location tracking coarse");
                break;
        }
    }



    // take last position from tracker
    @Override
    public double getLatitude(){
        return mCurrentLocation != null ? mCurrentLocation.getLatitude() : 0.0;
    }
    @Override
    public double getLongitude(){
        return mCurrentLocation != null ? mCurrentLocation.getLongitude() : 0.0;
    }
}
