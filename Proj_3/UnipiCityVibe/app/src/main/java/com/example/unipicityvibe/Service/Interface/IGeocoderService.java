package com.example.unipicityvibe.Service.Interface;

import android.location.Geocoder;

public interface IGeocoderService {
    void getAddress(double lat, double lon, Geocoder.GeocodeListener l);
}
