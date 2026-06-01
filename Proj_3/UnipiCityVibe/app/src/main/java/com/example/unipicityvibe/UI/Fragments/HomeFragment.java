package com.example.unipicityvibe.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.Enums.LocationTypeEnum;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.UI.Activity.UserActivity;
import com.example.unipicityvibe.Utils.PermissionHelper;


public class HomeFragment extends Fragment {

    // ----- Buttons -----
    private void eventListButton(View view){
        if (PermissionHelper.isGrantedLocationPermission(requireContext()) && AppSettings.getLocationAccuracy(requireContext()) != LocationTypeEnum.OFF_LOCATION)
            ((UserActivity) requireActivity()).showEventListAroundFragment();
        else
            ((UserActivity) requireActivity()).showErrorFragment();
    }
    private void eventMapButton(View view){
        if (PermissionHelper.isGrantedLocationPermission(requireContext()) && AppSettings.getLocationAccuracy(requireContext()) != LocationTypeEnum.OFF_LOCATION)
            ((UserActivity) requireActivity()).showMapsFragment();
        else
            ((UserActivity) requireActivity()).showErrorFragment();
    }
    private void myTicketButton(View view){
        ((UserActivity) requireActivity()).showMyTicketsFragment();
    }
    // ----- End Buttons -----

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button b;
        b = view.findViewById(R.id.buttonEventList);
        b.setOnClickListener(this::eventListButton);
        b = view.findViewById(R.id.buttonEventMap);
        b.setOnClickListener(this::eventMapButton);
        b = view.findViewById(R.id.buttonMyTickets);
        b.setOnClickListener(this::myTicketButton);

    }

}
