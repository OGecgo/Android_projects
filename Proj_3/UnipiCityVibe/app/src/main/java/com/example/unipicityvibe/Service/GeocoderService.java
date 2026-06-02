package com.example.unipicityvibe.Service;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipicityvibe.Service.Interface.IGeocoderService;

public class GeocoderService implements IGeocoderService {
    private static GeocoderService service;
    private final Geocoder geocoder;

    private GeocoderService(Context context){
        // give context.getApplicationContext() to prevent memory leaks
        if (!context.equals(context.getApplicationContext())){
            throw new RuntimeException("[GeocoderService] CRITICAL ERROR: context should be application context to prevent memory leaks. Use getApplicationContext");
        }
        geocoder = new Geocoder(context);
    }

    public static GeocoderService getInstance(@NonNull Context context) {
        if (service == null) service = new GeocoderService(context);
        return service;
    }

    @Override
    public void getAddress(double lat, double lon, Geocoder.GeocodeListener l) {
        geocoder.getFromLocation(lat, lon, 1, l);
    }
}
