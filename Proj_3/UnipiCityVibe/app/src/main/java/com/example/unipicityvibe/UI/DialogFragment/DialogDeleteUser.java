package com.example.unipicityvibe.UI.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.unipicityvibe.UI.Activity.AuthActivity;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.UI.CustomView.EditTextView;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.R;

public class DialogDeleteUser extends DialogFragment {

    private EditTextView editTextViewPassword;
    private TextView errorText;

    private IAuthService authService;

    // ------ Call Back ------
    private void onCompleteListenerSubmit(boolean success, String errorLog) {
        if (!success) {
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
            return;
        }
        dismiss();
        Intent intent = new Intent(getContext(), AuthActivity.class);
        startActivity(intent);
    }
    // ------ End Call Back ------

    // ------ Buttons ------
    private void submitButton(View view) {
        String password = editTextViewPassword.getEditText().toString();
        authService.userDelete(password, this::onCompleteListenerSubmit);
    }

    private void cancelButton(View view) {
        dismiss();
    }
    // ------ End Buttons ------


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_delete_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initializations
        editTextViewPassword = view.findViewById(R.id.editTextViewPassword);
        errorText = view.findViewById(R.id.textViewError);

        authService = AuthService.getInstance();

        // set to buttons onClickListener
        Button b;
        b = view.findViewById(R.id.buttonSubmit);
        b.setOnClickListener(this::submitButton);
        b = view.findViewById(R.id.buttonCancel);
        b.setOnClickListener(this::cancelButton);
    }

}
