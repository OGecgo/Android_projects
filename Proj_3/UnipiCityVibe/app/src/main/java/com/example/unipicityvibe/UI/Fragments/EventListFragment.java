package com.example.unipicityvibe.UI.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.EventService;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.LocationService;
import com.example.unipicityvibe.UI.Activity.UserActivity;
import com.example.unipicityvibe.Utils.PermissionHelper;

import com.example.unipicityvibe.UI.CustomView.EventInfoView;

public class EventListFragment extends Fragment {

    private ILocationService locationService;
    private IEventService eventService;

    private void goHomePage() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }


    private void showEvents(EventData[] eventsData) {
        View view = getView();
        if (view == null) return;
        LinearLayout container = view.findViewById(R.id.event_list_container);
        if (container == null) return;

        // clean page from old view
        container.removeAllViews();

        // show a message if no events are found
        if (eventsData.length == 0) {
            Toast.makeText(requireContext(), R.string.error_no_events, Toast.LENGTH_SHORT).show();
            return;
        }

        for (EventData event : eventsData) {
            EventInfoView eventView = new EventInfoView(requireContext());
            eventView.setValues(event);
            container.addView(eventView);
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
        return inflater.inflate(R.layout.fragment_event_list_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!PermissionHelper.isGrantedLocationPermission(requireContext())) {
            goHomePage();
            return;
        }

        EventData[] eventsData = eventService.getRadiusEvents(locationService.getLatitude(), locationService.getLongitude());
        showEvents(eventsData);
    }



}
