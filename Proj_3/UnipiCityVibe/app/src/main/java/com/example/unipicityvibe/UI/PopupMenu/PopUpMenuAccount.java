package com.example.unipicityvibe.UI.PopupMenu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Listeners.RefFunctionListener;
import com.example.unipicityvibe.UI.Activity.AuthActivity;
import com.example.unipicityvibe.R;

public class PopUpMenuAccount extends PopupWindow {

    private final IAuthService authService;
    private final Context context;
    private RefFunctionListener settingsListener;
    private RefFunctionListener languageListener;

    // ------ Call Back ------
    private void onCompleteListenerLogOut(boolean success, String errorText){
        if (success){
            dismiss();
            // if user log out. Move user to logIn page
            Intent intent = new Intent(context, AuthActivity.class);
            context.startActivity(intent);
        }
    }
    // ------ End Call Back ------

    // ------ Buttons ------
    private void logOutButton(View view){
        authService.userLogOut(this::onCompleteListenerLogOut);
    }
        private void languageButton(View view){
        dismiss();
        if (languageListener != null) {
            languageListener.execute();
        }
    }
    private void settingsButton(View view){
        dismiss();
        if (settingsListener != null) {
            settingsListener.execute();
        }
    }
    // ------ End Buttons ------

    public PopUpMenuAccount(Context context){
        super(context);
        this.context = context;
        this.authService = AuthService.getInstance();

        // set the view of popup menu
        View view = LayoutInflater.from(context).inflate(R.layout.popup_menu_account, null);
        setContentView(view);

        // close window if press out of window
        setFocusable(true);
    
        // set buttons onClickListener
        Button b;
        b = view.findViewById(R.id.buttonLogOut);
        b.setOnClickListener(this::logOutButton);
        b = view.findViewById(R.id.buttonLanguage);
        b.setOnClickListener(this::languageButton);
        b = view.findViewById(R.id.buttonSettings);
        b.setOnClickListener(this::settingsButton);

    }

    public void setSettingsListener(@NonNull RefFunctionListener l) {
        this.settingsListener = l;
    }
    public void setLanguageListener(@NonNull RefFunctionListener l) {
        this.languageListener = l;
    }
}
