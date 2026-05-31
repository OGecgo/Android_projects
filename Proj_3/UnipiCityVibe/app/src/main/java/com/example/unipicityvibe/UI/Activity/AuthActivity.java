package com.example.unipicityvibe.UI.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.UI.Fragments.LoginFragment;
import com.example.unipicityvibe.UI.Fragments.RegisterFragment;
import com.example.unipicityvibe.UI.Fragments.TopViewMenu;

public class AuthActivity extends BaseActivity { //implements LoginFragment.LoginFragmentListener {

    private static final String TAG_LOGIN = "TAG_LOGIN";
    private static final String TAG_REGISTER = "TAG_REGISTER";

    // ----- Change Page -----
    private void goHomePage() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
        finish();
    }
    // ----- End Change Page -----

    // ----- Fragments -----
    private void showLoginFragment() {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(TAG_LOGIN);
        if (loginFragment == null) loginFragment = new LoginFragment();
        // set button register for movement
        loginFragment.setRegisterButton(this::showRegisterFragment);
        replaceFragment(loginFragment, TAG_LOGIN, false);
    }

    private void showRegisterFragment() {
        RegisterFragment registerFragment = (RegisterFragment) getSupportFragmentManager().findFragmentByTag(TAG_REGISTER);
        if (registerFragment == null) registerFragment = new RegisterFragment();
        replaceFragment(registerFragment, TAG_REGISTER, true);
    }
    // ----- End Fragments -----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            showLoginFragment();
        }

        // take fragment
        TopViewMenu tvm = (TopViewMenu) getSupportFragmentManager().findFragmentById(R.id.topViewMenuContainer);
        if (tvm != null){
            // set button home for movement
            tvm.setHomeButton(this::showLoginFragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // if user is authorized. change page to home
        IAuthService authService = AuthService.getInstance();
        UserAuthData user = authService.getUserAuth();
        if (!user.uID.isEmpty()) goHomePage();
    }

}
