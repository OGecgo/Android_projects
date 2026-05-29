package com.example.unipicityvibe.Service.Interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unipicityvibe.Enums.LocationTypeEnum;

public interface ILocationService {

    void stopLocationUpdate();
    void startLocationUpdate(@NonNull LocationTypeEnum e);


    // data will do update every N time after startLocationUpdate
    double getLatitude();
    double getLongitude();
}
