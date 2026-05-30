package com.example.unipicityvibe.Service.Interface;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Enums.LocationTypeEnum;

public interface ILocationService {

    void stopLocationUpdate();
    void startLocationUpdate(@NonNull LocationTypeEnum e, Context context);


    // data will do update every N time after startLocationUpdate
    double getLatitude();
    double getLongitude();
}
