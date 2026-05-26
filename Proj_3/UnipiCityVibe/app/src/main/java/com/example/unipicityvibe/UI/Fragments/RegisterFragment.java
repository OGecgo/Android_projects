package com.example.unipicityvibe.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.UI.Activity.UserActivity;
import com.example.unipicityvibe.UI.CustomView.EditTextView;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;

public class RegisterFragment extends Fragment {

    private EditTextView editTextViewName;
    private EditTextView editTextViewLastName;
    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private EditTextView editTextViewPassword2;
    private TextView errorText;

    private IAuthService authService;



    // ------ Page Change ------
    // change activity
    private void goHomePage() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
    // ------ End Page Change ------

    // ----- Call Back ------
    private void onComposeListenerButton(boolean success, String errorLog) {
        if (!success) {
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
        } else {
            goHomePage();
        }
    }
    // ----- End Call Back ------


    // returns true if the input is invalid and set errorText text
    private boolean validateRegister(String name, String lastName, String email, String password, String password2) {
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            errorText.setText(R.string.error_all_fields_required);
            return true;
        }
        if (!email.contains("@") || !email.contains(".")) {
            errorText.setText(R.string.error_email_invalid);
            return true;
        }
        if (!password.equals(password2)) {
            errorText.setText(R.string.error_passwords_not_match);
            return true;
        }
        if (password.length() <= 6) {
            errorText.setText(R.string.error_password_too_short);
            return true;
        }
        return false;
    }

    // ------ Buttons ------
    private void registerButton(View view) {
        String password = editTextViewPassword.getEditText().toString();
        String password2 = editTextViewPassword2.getEditText().toString();
        String name = editTextViewName.getEditText().toString();
        String lastName = editTextViewLastName.getEditText().toString();
        String email = editTextViewEmail.getEditText().toString();

        if (validateRegister(name, lastName, email, password, password2)) return;

        authService.userRegister(email, password, name, lastName, this::onComposeListenerButton);
    }
    // ------ End Buttons ------


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authService = AuthService.getInstance();

        editTextViewName = view.findViewById(R.id.editTextViewName);
        editTextViewLastName = view.findViewById(R.id.editTextViewLastName);
        editTextViewEmail = view.findViewById(R.id.editTextViewEmail);
        editTextViewPassword = view.findViewById(R.id.editTextViewPassword);
        editTextViewPassword2 = view.findViewById(R.id.editTextViewPassword2);
        errorText = view.findViewById(R.id.textViewError);

        Button regBtn = view.findViewById(R.id.buttonRegister);
        regBtn.setOnClickListener(this::registerButton);
    }
}
