package com.example.unipicityvibe.UI.Fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.LocationService;
import com.example.unipicityvibe.UI.Activity.UserActivity;
import com.example.unipicityvibe.Utils.PermissionHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private ILocationService locationService;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            // permission tested on onViewCreated
            if (!PermissionHelper.isGrantedLocationPermission(requireContext()))
                goHomePage();
            googleMap.setMyLocationEnabled(true);

            // markers
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

            // move camera
            if (locationService != null){
                LatLng pos = new LatLng(locationService.getLatitude(), locationService.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            }
            else{
                Log.w(TAG, "[MapsFragment] locationService is null");
            }
        }
    };


    private void goHomePage() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationService = LocationService.getInstance(requireContext().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // test permissions
        if (!PermissionHelper.isGrantedLocationPermission(requireContext()))
            goHomePage();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}