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
import com.example.unipicityvibe.Utils.PermissionHelper;

public class SettingsPermissionsFragment extends Fragment {

    private TextView errorText;
    private LinearLayout layoutLocationDetails;
    
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchLocation, switchGPS, switchWIFICallTower;

    // activity reload for settings apply
    private void restartActivity() {
        requireActivity().recreate();
    }

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
        if (PermissionHelper.isGrantedFine(requireContext())){
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.FINE);
        }
        else if (PermissionHelper.isGrantedCoarse(requireContext())){
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.COARSE);
        } else {
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
        }
    }

    // ----- Switch -----
    private void locationSwitch(View view){
        boolean isChecked = ((CompoundButton) view).isChecked();
        errorText.setText("");

        if (isChecked){
            if(PermissionHelper.isGrantedLocationPermission(requireContext())){
                setDefaultAppSettingValue();
                layoutLocationDetails.setVisibility(VISIBLE);
                syncSwitches();
                // set new values
                restartActivity();
            }
            else{
                switchLocation.setChecked(false);
                errorText.setText(R.string.permission_denied);
            }
        }
        else{
            layoutLocationDetails.setVisibility(INVISIBLE);
            AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
            // set new values
            restartActivity();
        }
    }

    private void fineSwitch(View view) {
        boolean isChecked = ((CompoundButton) view).isChecked();
        errorText.setText("");

        if (isChecked) {
            if (PermissionHelper.isGrantedFine(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.FINE);
                syncSwitches();
                // set new values
                restartActivity();
            } else {
                errorText.setText(R.string.permission_denied);
                switchGPS.setChecked(false);
            }
        } else {
            // If we turn off fine, we either fall back to WI-FI/Call-tower or turn off location
            if (PermissionHelper.isGrantedCoarse(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.COARSE);
                syncSwitches();
                // set new values
                restartActivity();
            } else {
                switchLocation.setChecked(false);
                layoutLocationDetails.setVisibility(INVISIBLE);
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
                // set new values
                restartActivity();
            }
        }
    }

    private void coarseSwitch(View view) {
        boolean isChecked = ((CompoundButton) view).isChecked();
        errorText.setText("");

        if (isChecked) {
            if (PermissionHelper.isGrantedCoarse(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.COARSE);
                syncSwitches();
                // set new values
                restartActivity();
            } else {
                errorText.setText(R.string.permission_denied);
                switchWIFICallTower.setChecked(false);
            }
        } else {
            // If we turn off WI-FI/Call-tower, we either fall back to GPS or turn off location
            if (PermissionHelper.isGrantedFine(requireContext())) {
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.FINE);
                syncSwitches();
                // set new values
                restartActivity();
            } else {
                switchLocation.setChecked(false);
                layoutLocationDetails.setVisibility(INVISIBLE);
                AppSettings.setLocationAccuracy(requireContext(), LocationTypeEnum.OFF_LOCATION);
                // set new values
                restartActivity();
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
        // use onClickListener because in that case the function setChecked should not trigger my listeners
        switchLocation.setOnClickListener(this::locationSwitch);
        switchGPS.setOnClickListener(this::fineSwitch);
        switchWIFICallTower.setOnClickListener(this::coarseSwitch);
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
