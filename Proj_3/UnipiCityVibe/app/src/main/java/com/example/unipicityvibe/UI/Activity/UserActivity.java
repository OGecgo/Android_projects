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
import com.example.unipicityvibe.UI.Fragments.EventListFragment;
import com.example.unipicityvibe.UI.Fragments.HomeFragment;
import com.example.unipicityvibe.UI.Fragments.SettingsFragment;
import com.example.unipicityvibe.UI.Fragments.TopViewMenu;

public class UserActivity extends BaseActivity {

    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private EventListFragment eventListFragment;

    // ------ Change Page ------
    private void goLogInPage() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
    // ------ End Change Page ------


    // ----- Fragments -----
    private void showHomeFragment() {
        if (homeFragment == null) homeFragment = new HomeFragment();
        homeFragment.setEventListButton(this::showEventListFragment);
//        homeFragment.setEventListButton(this::showEventMapFragment);
//        homeFragment.setEventListButton(this::showMyTicketsFragment);
        replaceFragment(homeFragment, false);

    }

    private void showSettingsFragment() {
        if (settingsFragment == null) settingsFragment = new SettingsFragment();
        settingsFragment.setRestartListener(this::restartAndShowSettings);
        replaceFragment(settingsFragment, true);
    }
    // set up reload page for settings
    private void restartAndShowSettings() {
        Intent intent = getIntent();
        intent.putExtra("SHOW_SETTINGS", true);
        finish();
        startActivity(intent);
    }

    private void showEventListFragment() {
        if (eventListFragment == null) eventListFragment = new EventListFragment();
        replaceFragment(eventListFragment, true);
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
            // if settings do restart. move to settings. no to home page
            if (getIntent().getBooleanExtra("SHOW_SETTINGS", false)) {
                showSettingsFragment();
            } else {
                showHomeFragment();
            }
        }

        // take fragment
        TopViewMenu tvm = (TopViewMenu) getSupportFragmentManager().findFragmentById(R.id.topViewMenuContainer);
        // set button home and settings for movement
        tvm.setHomeButton(this::showHomeFragment);
        tvm.setSettingsButton(this::showSettingsFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // test if user exist. if not. send to Log In page
        IAuthService service = AuthService.getInstance();
        UserAuthData userAuthData = service.getUserAuth();
        if (userAuthData.uID.isEmpty()) goLogInPage();
    }
}
