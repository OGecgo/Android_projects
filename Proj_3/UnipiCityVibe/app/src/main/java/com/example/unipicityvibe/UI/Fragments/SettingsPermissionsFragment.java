package com.example.unipicityvibe.UI.Fragments;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.Enums.LocationTypeEnum;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.PermissionService;

public class SettingsPermissionsFragment extends Fragment {

    private TextView errorText;
    private LinearLayout layoutLocationDetails;
    
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchLocation, switchGPS, switchWIFICallTower;


    private void syncSwitches() {
        LocationTypeEnum accuracy = AppSettings.getLocationAccuracy(requireContext());

        if (accuracy == LocationTypeEnum.FINE) {
            switchGPS.setChecked(true);
            switchWIFICallTower.setChecked(false);
        } else if (accuracy == LocationTypeEnum.COARSE) {
            switchGPS.setChecked(false);
            switchWIFICallTower.setChecked(true);
        } else {
            switchGPS.setChecked(false);
            switchWIFICallTower.setChecked(false);
            layoutLocationDetails.setVisibility(INVISIBLE);
        }
    }

    private void setDefaultAppSettingValue(){
        if (PermissionService.isGrantedFine(requireContext())){
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.FINE);
        }
        else if (PermissionService.isGrantedCoarse(requireContext())){
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.COARSE);
        } else {
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
        }
    }

    // ----- Switch -----
    private void locationSwitch(@NonNull CompoundButton buttonView, boolean isChecked){
        errorText.setText("");

        if (isChecked){
            if(PermissionService.isGrantedLocationPermission(requireContext())){
                setDefaultAppSettingValue();
                layoutLocationDetails.setVisibility(VISIBLE);
                syncSwitches();
            }
            else{
                switchLocation.setChecked(false);
                errorText.setText(R.string.permission_denied);
            }
        }
        else{
            layoutLocationDetails.setVisibility(INVISIBLE);
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
        }
    }

    private void fineSwitch(CompoundButton buttonView, boolean isChecked) {
        errorText.setText("");

        if (isChecked) {
            if (PermissionService.isGrantedFine(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.FINE);
                syncSwitches();
            } else {
                errorText.setText(R.string.permission_denied);
                switchGPS.setChecked(false);
            }
        } else {
            // If we turn off fine, we either fall back to WI-FI/Call-tower or turn off location
            if (PermissionService.isGrantedCoarse(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.COARSE);
                syncSwitches();
            } else {
                switchLocation.setChecked(false);
                layoutLocationDetails.setVisibility(INVISIBLE);
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
            }
        }
    }

    private void coarseSwitch(CompoundButton buttonView, boolean isChecked) {
        errorText.setText("");

        if (isChecked) {
            if (PermissionService.isGrantedCoarse(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.COARSE);
                syncSwitches();
            } else {
                errorText.setText(R.string.permission_denied);
                switchWIFICallTower.setChecked(false);
            }
        } else {
            // If we turn off WI-FI/Call-tower, we either fall back to GPS or turn off location
            if (PermissionService.isGrantedFine(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.FINE);
                syncSwitches();
            } else {
                switchLocation.setChecked(false);
                layoutLocationDetails.setVisibility(INVISIBLE);
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
            }
        }
    }
    // ----- End Switch

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorText = view.findViewById(R.id.textViewError);
        layoutLocationDetails = view.findViewById(R.id.layoutLocationDetails);
        switchLocation = view.findViewById(R.id.switchLocation);
        switchGPS = view.findViewById(R.id.switchGPS);
        switchWIFICallTower = view.findViewById(R.id.switchWIFICallTower);



        // Set listeners
        switchLocation.setOnCheckedChangeListener(this::locationSwitch);
        switchGPS.setOnCheckedChangeListener(this::fineSwitch);
        switchWIFICallTower.setOnCheckedChangeListener(this::coarseSwitch);
    }

    @Override
    public void onResume() {
        super.onResume();
        // do synchronize switches on resume for situations like change permission from setting and go back to app
        if (AppSettings.getLocationAccuracy(requireContext()) != LocationTypeEnum.OFF_LOCATION) {
            switchLocation.setChecked(true);
            layoutLocationDetails.setVisibility(VISIBLE);
        }
        syncSwitches();
    }
}
