package com.example.unipicityvibe.UI.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.UI.Fragments.LoginFragment;
import com.example.unipicityvibe.UI.Fragments.RegisterFragment;
import com.example.unipicityvibe.UI.Fragments.TopViewMenu;

public class AuthActivity extends BaseActivity { //implements LoginFragment.LoginFragmentListener {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    // ----- Change Page -----
    private void goHomePage() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
        finish();
    }
    // ----- End Change Page -----

    // ----- Fragments -----
    private void showLoginFragment() {
        if (loginFragment == null) loginFragment = new LoginFragment();
        // set button register for movement
        loginFragment.setRegisterButton(this::showRegisterFragment);
        replaceFragment(loginFragment, false);
    }

    private void showRegisterFragment() {
        if (registerFragment == null) registerFragment = new RegisterFragment();
        replaceFragment(registerFragment, true);
    }
    // fragment manager transaction pages
    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
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
        // set button home for movement
        tvm.setHomeButton(this::showLoginFragment);
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
