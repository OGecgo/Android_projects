package com.example.unipicityvibe.UI.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Data.Models.UserData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Service.Interface.RefFunction;
import com.example.unipicityvibe.UI.Activity.AuthActivity;
import com.example.unipicityvibe.UI.DialogFragment.DialogDeleteUser;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;

public class SettingsFragment extends Fragment {
    private TextView errorText;
    private RefFunction restartListener;

    public void setRestartListener(@NonNull RefFunction l) {
        this.restartListener = l;
    }

    private void restartActivity() {
        if (restartListener != null) {
            restartListener.execute();
        } else if (getActivity() != null) {
            getActivity().recreate();
        }
    }

    // ------ Call Back ------
    private void onCompleteListenerGetData(boolean success, String errorLog, UserData userData) {
        if (success) {
            TextView tv;
            tv = getView().findViewById(R.id.textViewEmail);
            tv.setText(userData.email);
            tv = getView().findViewById(R.id.textViewName);
            tv.setText(userData.name);
            tv = getView().findViewById(R.id.textViewLastName);
            tv.setText(userData.last_name);
            return;
        }
        errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
    }
    // ------ End Call Back ------

    // ------ Radio Group ------
    private void setSmallText() {
        AppSettings.setFontScale(requireContext(), 0.75f);
        restartActivity();
    }

    private void setNormalText() {
        AppSettings.setFontScale(requireContext(), 1f);
        restartActivity();
    }

    private void setLargeText() {
        AppSettings.setFontScale(requireContext(), 1.5f);
        restartActivity();
    }

    private void sizeTextRadioGroupTextSize(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.radioButtonSmall) setSmallText();
        else if (checkedId == R.id.radioButtonNormal) setNormalText();
        else if (checkedId == R.id.radioButtonLarge) setLargeText();
    }
    // ------ End Radio Group ------

    // ------ Switch ------
    private void darkModeSwitch(CompoundButton compoundButton, boolean b) {
        AppSettings.setDarkMode(requireContext(), b);
        restartActivity();
    }
    // ------ End Switch ------

    // ------ Buttons ------
    private void deleteButton(View view) {
        DialogDeleteUser ddu = new DialogDeleteUser();
        ddu.show(getParentFragmentManager(), "DIALOG_DELETE_USER");
    }
    // ------ End Buttons ------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        errorText = view.findViewById(R.id.textViewError);

        // get user data and set it
        IAuthService service = AuthService.getInstance();
        UserData userData = new UserData();
        service.getUserData(userData, (success, errorLog) -> this.onCompleteListenerGetData(success, errorLog, userData));

        // get last settings. After set to radio group and switch
        float scale = AppSettings.getFontScale(requireContext());
        boolean darkMode = AppSettings.isDarkMode(requireContext());

        RadioGroup rg = view.findViewById(R.id.radioGroupTextSize);
        if (scale < 1) rg.check(R.id.radioButtonSmall);
        else if (scale == 1) rg.check(R.id.radioButtonNormal);
        else rg.check(R.id.radioButtonLarge);
        rg.setOnCheckedChangeListener(this::sizeTextRadioGroupTextSize);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = view.findViewById(R.id.switchDarkMode);
        s.setChecked(darkMode);
        s.setOnCheckedChangeListener(this::darkModeSwitch);

        // set delete button
        Button btnDelete = view.findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this::deleteButton);
    }
}
