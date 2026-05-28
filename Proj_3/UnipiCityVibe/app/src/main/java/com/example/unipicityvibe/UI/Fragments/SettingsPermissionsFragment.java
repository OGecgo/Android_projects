package com.example.unipicityvibe.UI.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.R;

public class SettingsPermissionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        @SuppressLint("UseSwitchCompatOrMaterialCode") 
        Switch switchLocation = view.findViewById(R.id.switchLocation);
        RadioGroup radioGroupLocation = view.findViewById(R.id.radioGroupLocation);

        // for now nothing
    }
}
