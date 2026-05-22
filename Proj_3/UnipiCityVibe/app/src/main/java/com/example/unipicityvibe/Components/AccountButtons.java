package com.example.unipicityvibe.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.example.unipicityvibe.Class.AuthControl;
import com.example.unipicityvibe.Interface.IAuthControl;
import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.R;

import org.jetbrains.annotations.NotNull;

public class AccountButtons extends ConstraintLayout {

    private DialogDeleteUser deleteUser;

    private IAuthControl authControl;

    private OnCompleteListener logOutListener;
    private FragmentManager fragmentManager;

    private void onCompleteListenerEmpty(boolean success, String errorText){}

    private void logOutButton(View view){
        authControl.userLogOut(logOutListener);
    }

    private void deleteUserButton(View view){
        deleteUser.show(fragmentManager, "DELETE_USER_DIALOG");
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.dialog_acount, this);
        // proper work on graphic redactor
        if(isInEditMode()) return;

        Button buttonLogOut = findViewById(R.id.buttonLogOut);
        Button buttonDeleteUser = findViewById(R.id.buttonDeleteUser);
        deleteUser = new DialogDeleteUser();

        authControl = AuthControl.getInstance();
        logOutListener = this::onCompleteListenerEmpty;

        buttonDeleteUser.setOnClickListener(this::deleteUserButton);
        buttonLogOut.setOnClickListener(this::logOutButton);
    }

    public AccountButtons(@NotNull Context context){
        super(context);
        init(context);
    }
    public AccountButtons(@NotNull Context context, @NotNull AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public AccountButtons(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public AccountButtons(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void onClickListenerLogOutButton(@NonNull OnCompleteListener l){
        logOutListener = l;
    }

    public void onClickListenerDelete(View.OnClickListener l){
        deleteUser.onDeleteListener(l);
    }

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

}
