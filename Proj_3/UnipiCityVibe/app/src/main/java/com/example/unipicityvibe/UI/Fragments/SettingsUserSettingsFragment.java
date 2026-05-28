package com.example.unipicityvibe.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Models.UserData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.UI.DialogFragment.DialogDeleteUser;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;

public class SettingsUserSettingsFragment extends Fragment {

    // ----- Call Back -----
    private void onCompleteListenerUserData(boolean success, String errorLog, View view, UserData userData){
        if (success) {
            TextView textViewEmail = view.findViewById(R.id.textViewEmail);
            TextView textViewName = view.findViewById(R.id.textViewName);
            TextView textViewLastName = view.findViewById(R.id.textViewLastName);
            textViewEmail.setText(userData.email);
            textViewName.setText(userData.name);
            textViewLastName.setText(userData.last_name);
        } else {
            TextView errorText = view.findViewById(R.id.textViewError);
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
        }
    }
    // ----- End Call Back -----

    // ----- Button -----
    private void deleteButton(View view){
        DialogDeleteUser ddu = new DialogDeleteUser();
        ddu.show(getParentFragmentManager(), "DIALOG_DELETE_USER");
    }
    // ----- End Button -----

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // take data for user from database
        IAuthService service = AuthService.getInstance();
        UserData userData = new UserData();
        service.getUserData(userData, ((success, errorLog) -> onCompleteListenerUserData(success, errorLog, view, userData)));

        Button btnDelete = view.findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this::deleteButton);

    }


}
