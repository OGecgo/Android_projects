package com.example.unipicityvibe.UI.Activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Enums.LocationTypeEnum;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.LocationService;
import com.example.unipicityvibe.Service.PermissionService;
import com.example.unipicityvibe.UI.Fragments.ErrorFragment;
import com.example.unipicityvibe.UI.Fragments.EventListFragment;
import com.example.unipicityvibe.UI.Fragments.HomeFragment;
import com.example.unipicityvibe.UI.Fragments.SettingsFragment;
import com.example.unipicityvibe.UI.Fragments.TopViewMenu;

public class UserActivity extends BaseActivity {

    // Fragments
    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private EventListFragment eventListFragment;
    private ErrorFragment errorFragment;

    // Location
    private ILocationService locationService;


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
        if (!PermissionService.isGrantedLocationPermission(this)){
            homeFragment.setEventListButton(this::showErrorFragment);
            homeFragment.setEventMapButton(this::showErrorFragment);
        }
        else{
            homeFragment.setEventListButton(this::showEventListFragment);
            // homeFragment.set(this::showEventMapFragment);
        }
        // homeFragment.set(this::showMyTicketsFragment);
        replaceFragment(homeFragment, false);

    }

    private void showSettingsFragment() {
        if (settingsFragment == null) settingsFragment = new SettingsFragment();
        replaceFragment(settingsFragment, false);
    }

    private void showEventListFragment() {
        if (eventListFragment == null) eventListFragment = new EventListFragment();
        replaceFragment(eventListFragment, true);
    }

    private void showErrorFragment() {
        if (errorFragment == null) errorFragment = new ErrorFragment();
        replaceFragment(errorFragment, false);
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
            showHomeFragment();
        }

        // take fragment
        TopViewMenu tvm = (TopViewMenu) getSupportFragmentManager().findFragmentById(R.id.topViewMenuContainer);
        // set button home and settings for movement
        tvm.setHomeButton(this::showHomeFragment);
        tvm.setSettingsButton(this::showSettingsFragment);

        // request permissions from user if not required
        if (!PermissionService.isGrantedLocationPermission(this)) {
            AppSettings.setLocationAccuracy(getBaseContext(), LocationTypeEnum.OFF_LOCATION);
            PermissionService.requestLocationPermission(this);
        }


        // permission notification

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionService.PERMISSION_REQUEST_CODE_GPS){
            if (PermissionService.isGrantedLocationPermission(this)) {
                if (PermissionService.isGrantedFine(this)){
                    AppSettings.setLocationAccuracy(this, LocationTypeEnum.FINE);
                }
                else if (PermissionService.isGrantedCoarse(this)){
                    AppSettings.setLocationAccuracy(this, LocationTypeEnum.COARSE);
                }
                // i do recreate activity for syncronize data
                Intent starterIntent = getIntent();
                finish();
                startActivity(starterIntent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // test if user exist. if not. send to Log In page
        IAuthService service = AuthService.getInstance();
        UserAuthData userAuthData = service.getUserAuth();
        if (userAuthData.uID.isEmpty()) goLogInPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationTypeEnum accuracy;
        // is permissions changed and not location is not available
        if (PermissionService.isGrantedLocationPermission(this)) {
            // after reload have chance the locationService be null
            if (locationService == null) locationService = new LocationService(this);
            // if permission changed and fine now is not available
            accuracy = AppSettings.getLocationAccuracy(this);
            if (!PermissionService.isGrantedFine(this) && accuracy == LocationTypeEnum.FINE){
                AppSettings.setLocationAccuracy(this, LocationTypeEnum.COARSE);
            }

            accuracy = AppSettings.getLocationAccuracy(this);
            if (accuracy == LocationTypeEnum.FINE){
                locationService.startLocationUpdate(LocationTypeEnum.FINE);
            }
            else if (accuracy == LocationTypeEnum.COARSE){
                locationService.startLocationUpdate(LocationTypeEnum.COARSE);
            }
        }
        else{
            AppSettings.setLocationAccuracy(getBaseContext(), LocationTypeEnum.OFF_LOCATION);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop location tracking
        if (locationService != null && PermissionService.isGrantedLocationPermission(this))
            locationService.stopLocationUpdate();
    }


}
