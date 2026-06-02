package com.example.unipicityvibe.Utils;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public final class PermissionHelper {

    public static final int PERMISSION_REQUEST_CODE_LOCATION = 0;
    public static final int PERMISSION_REQUEST_CODE_NOTIFICATION = 1;



    // ----- Location Permissions -----
    public static void requestFinePermission(Activity activity) {
        boolean fine = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!fine) {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST_CODE_LOCATION);
        }
    }
    public static void requestCoarsePermission(Activity activity) {
        boolean coarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!coarse) {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_REQUEST_CODE_LOCATION);
        }
    }
    public static void requestLocationPermission(Activity activity) {
        boolean fine = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!fine || !coarse) {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_REQUEST_CODE_LOCATION);
        }
    }
    public static boolean isGrantedLocationPermission(Context context){
        boolean fine = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return fine || coarse;
    }
    public static boolean isGrantedFine(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean isGrantedCoarse(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    // ----- End Location Permissions -----

    // ----- Notification Permission -----
    public static boolean isGrantedNotification(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestNotificationPermission(Activity activity){
        boolean not = ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        if (!not)
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE_NOTIFICATION);
    }
    // ----- End Notification Permission -----

}


