package com.example.unipicityvibe.Components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.unipicityvibe.Class.AuthControl;
import com.example.unipicityvibe.Constants.ExceptionToMessage;
import com.example.unipicityvibe.Interface.IAuthControl;
import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.R;

import org.jetbrains.annotations.NotNull;

public class DialogDeleteUser extends DialogFragment {

    private EditTextView editTextViewPassword;
    private TextView errorText;
    private Button buttonSubmit;

    private IAuthControl authControl;
    private View.OnClickListener deleteListener;

    private void onCompleteListenerLogOut(boolean success, String errorLog){
        if(!success){
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
            return;
        }


    }

    private void onCompleteListenerSubmit(boolean success, String errorLog) {
        if (!success) {
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
            return;
        }
        dismiss();
        if (deleteListener != null) deleteListener.onClick(buttonSubmit);    }

    private void submitButton(View view) {
        String password = editTextViewPassword.getEditText();
        authControl.userDelete(password, this::onCompleteListenerSubmit);
    }

    private void cancelButton(View view) {
        dismiss();
    }


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_delete_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextViewPassword = view.findViewById(R.id.editTextViewPassword);
        errorText = view.findViewById(R.id.textViewError);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);

        authControl = AuthControl.getInstance();

        buttonSubmit.setOnClickListener(this::submitButton);
        buttonCancel.setOnClickListener(this::cancelButton);
    }

    public void onDeleteListener(View.OnClickListener l){
        deleteListener = l;
    }
}
