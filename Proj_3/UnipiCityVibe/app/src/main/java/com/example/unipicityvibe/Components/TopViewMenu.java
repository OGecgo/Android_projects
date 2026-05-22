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
import com.example.unipicityvibe.Struct.UserAuthStruct;

import org.jetbrains.annotations.NotNull;

public class TopViewMenu extends ConstraintLayout {

    private Button buttonLogo;
    private AccountButtons accountButtons;


    private void nameButton(View view){
        if (accountButtons.getVisibility() == VISIBLE) accountButtons.setVisibility(INVISIBLE);
        else accountButtons.setVisibility(VISIBLE);
    }


    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.top_view_menu, this);

        // proper work on graphic redactor
        if(isInEditMode()) return;

        buttonLogo = findViewById(R.id.buttonLogo);
        Button buttonName = findViewById(R.id.buttonName);
        accountButtons = findViewById(R.id.dialogAccount);

        IAuthControl authControl = AuthControl.getInstance();
        UserAuthStruct user = authControl.getUserAuth();

        if (!user.uID.isEmpty()){
            buttonName.setText(user.email);
            buttonName.setOnClickListener(this::nameButton);
        }
    }

    public TopViewMenu(@NotNull Context context){
        super(context);
        init(context);
    }
    public TopViewMenu(@NotNull Context context, @NotNull AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public TopViewMenu(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public TopViewMenu(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnClickListenerButtonLogo(View.OnClickListener l){
        buttonLogo.setOnClickListener(l);
    }
    public void setOnClickListenerButtonLogOut(@NonNull OnCompleteListener l){
        accountButtons.onClickListenerLogOutButton(l);
    }

    public void setOnClickListenerDeleteAccount(View.OnClickListener l){
        accountButtons.onClickListenerDelete(l);
    }

    public void setFragmentManager(FragmentManager fragmentManager){
        accountButtons.setFragmentManager(fragmentManager);
    }
}
