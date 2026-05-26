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

import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Service.Interface.RefFunction;
import com.example.unipicityvibe.UI.Activity.UserActivity;
import com.example.unipicityvibe.UI.CustomView.EditTextView;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;

public class LoginFragment extends Fragment {

    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private TextView errorText;

    private IAuthService authService;
    private RefFunction transferListener;


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
    private void onCompleteListenerLogIn(boolean success, String errorLog) {
        if (!success) {
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
            return;
        }
        goHomePage();
    }
    // ------ End Call Back ------


    // returns true if the input is invalid and set errorText text
    private boolean validateLogIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            errorText.setText(R.string.error_all_fields_required);
            return true;
        }
        if (!email.contains("@") || !email.contains(".")) {
            errorText.setText(R.string.error_email_invalid);
            return true;
        }
        return false;
    }

    // ------ Buttons ------
    private void loginButton(View view) {
        String email = editTextViewEmail.getEditText().toString();
        String password = editTextViewPassword.getEditText().toString();
        if (validateLogIn(email, password)) return;
        authService.userLogIn(email, password, this::onCompleteListenerLogIn);
    }

    private  void registerButton(View view) {
        if (transferListener != null) transferListener.execute();
    }
    // ------ End Buttons ------


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authService = AuthService.getInstance();

        editTextViewEmail = view.findViewById(R.id.editTextViewEmail);
        editTextViewPassword = view.findViewById(R.id.editTextViewPassword);
        errorText = view.findViewById(R.id.textViewError);

        Button loginBtn = view.findViewById(R.id.buttonLogin);
        loginBtn.setOnClickListener(this::loginButton);

        Button registerBtn = view.findViewById(R.id.buttonRegister);
        registerBtn.setOnClickListener(this::registerButton);

    }

    public void setRegisterButton(@NonNull RefFunction l){
        transferListener = l;
    }
}
