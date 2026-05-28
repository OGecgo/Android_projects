package com.example.unipicityvibe.UI.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.Interface.RefFunction;

public class SettingsGeneralSettingsFragment extends Fragment {

    // activity reload for settings apply
    private void restartActivity() {
        getActivity().recreate();
    }

    // ----- RadioGroup -----
    private void textSizeRadioGroup(RadioGroup group, int checkedId){
        if (checkedId == R.id.radioButtonSmall) AppSettings.setFontScale(requireContext(), 0.75f);
        else if (checkedId == R.id.radioButtonNormal) AppSettings.setFontScale(requireContext(), 1f);
        else if (checkedId == R.id.radioButtonLarge) AppSettings.setFontScale(requireContext(), 1.5f);
        restartActivity();
    }
    // ----- End RadioGroup -----

    // ----- Switch -----
    private void darkModeSwitch(CompoundButton buttonView, boolean isChecked){
        AppSettings.setDarkMode(requireContext(), isChecked);
        restartActivity();
    }
    // ----- End Switch -----

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_general, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bring data
        float scale = AppSettings.getFontScale(requireContext());
        boolean darkMode = AppSettings.isDarkMode(requireContext());

        // text size
        RadioGroup rg = view.findViewById(R.id.radioGroupTextSize);
        if (scale < 1) rg.check(R.id.radioButtonSmall);
        else if (scale == 1) rg.check(R.id.radioButtonNormal);
        else rg.check(R.id.radioButtonLarge);

        rg.setOnCheckedChangeListener(this::textSizeRadioGroup);

        // dark mode
        @SuppressLint("UseSwitchCompatOrMaterialCode") 
        Switch s = view.findViewById(R.id.switchDarkMode);
        s.setChecked(darkMode);
        s.setOnCheckedChangeListener(this::darkModeSwitch);
    }

}
