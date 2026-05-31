package com.example.unipicityvibe.UI.Fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.EventService;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.LocationService;
import com.example.unipicityvibe.UI.Activity.UserActivity;
import com.example.unipicityvibe.Utils.ExceptionToMessageHelper;
import com.example.unipicityvibe.Utils.PermissionHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private ILocationService locationService;
    private IEventService eventService;
    private EventData[] eventsData;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            // permission tested on onViewCreated
            if (!PermissionHelper.isGrantedLocationPermission(requireContext()))
                goHomePage();

            googleMap.clear(); // Clear old markers
            googleMap.setMyLocationEnabled(true);


            if (locationService != null && eventsData != null){
                // markers on maps around a user
                for (EventData event : eventsData){
                    double eventLat = Double.parseDouble(event.latitude);
                    double eventLon = Double.parseDouble(event.longitude);
                    LatLng pos = new LatLng(eventLat, eventLon);
                    googleMap.addMarker(new MarkerOptions().position(pos).title(event.title));
                }
            }
            else{
                Log.w(TAG, "[MapsFragment] data is null");
            }
        }
    };


    private void onCompleteListenerReceiveData(boolean success, String errorLog){
        if (success){
            eventsData = eventService.getRadiusEvents(locationService.getLatitude(), locationService.getLongitude());
        }
        else{
            int messageResId = ExceptionToMessageHelper.AuthExceptionToTextId(errorLog);
            Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show();
        }

        // only after try to receive data show the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

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
        eventService = EventService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TODO create a reload map button (reload only fragment. ot whole activity)
        //  or make maps update in locaitonService (locationService should a while update functions for that cases)
        //  maybe i do that with refFun

        super.onViewCreated(view, savedInstanceState);
        // test permissions
        if (!PermissionHelper.isGrantedLocationPermission(requireContext()))
            goHomePage();

        eventService.receiveEvents(this::onCompleteListenerReceiveData);
    }
}